package com.vendorpro.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.model.MenuItem;
import com.vendorpro.network.Resource;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.MenuViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddEditMenuItemActivity extends BaseActivity {

    private static final int PICK_IMAGE = 101;
    private static final int PERMISSION_CODE = 102;

    private EditText etName, etDescription, etPrice;
    private CheckBox cbAvailable;
    private Button btnSave, btnSelectImage;
    private TextView tvTitle;
    private ImageView imagePreview;

    private MenuViewModel viewModel;
    private MenuItem menuItem;

    private boolean isEditMode = false;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_menu_item);

        initializeViews();

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        if (getIntent().hasExtra("item")) {
            menuItem = (MenuItem) getIntent().getSerializableExtra("item");
            isEditMode = true;
            populateData();
        }

        btnSelectImage.setOnClickListener(v -> checkPermissionAndOpenGallery());

        btnSave.setOnClickListener(v -> saveMenuItem());
    }

    private void initializeViews() {

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);

        cbAvailable = findViewById(R.id.cbAvailable);

        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        imagePreview = findViewById(R.id.imagePreview);

        tvTitle = findViewById(R.id.tvTitle);
    }

    /* =====================================
       Permission check
    ===================================== */

    private void checkPermissionAndOpenGallery() {

        String permission;

        if (Build.VERSION.SDK_INT >= 33) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    PERMISSION_CODE
            );

        } else {
            openGallery();
        }
    }

    /* =====================================
       Open gallery
    ===================================== */

    private void openGallery() {

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        startActivityForResult(intent, PICK_IMAGE);
    }

    /* =====================================
       Permission result
    ===================================== */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openGallery();
            } else {
                showError("Permission required to select image");
            }
        }
    }

    /* =====================================
       Image result
    ===================================== */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            imageUri = data.getData();

            if (imagePreview != null && imageUri != null) {
                imagePreview.setImageURI(imageUri);
            }
        }
    }

    /* =====================================
       Populate edit data
    ===================================== */

    private void populateData() {

        if (menuItem == null) return;

        tvTitle.setText("Edit Menu Item");

        etName.setText(menuItem.getName());
        etDescription.setText(menuItem.getDescription());
        etPrice.setText(String.valueOf(menuItem.getPrice()));

        cbAvailable.setChecked(menuItem.isAvailable());
    }

    /* =====================================
       Save menu item
    ===================================== */

    private void saveMenuItem() {

        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            showMessage("Please fill required fields");
            return;
        }

        double price = Double.parseDouble(priceStr);

        String messId = TokenManager
                .getInstance(this)
                .getMessId();

        if (messId == null || messId.isEmpty()) {
            showError("Mess ID not found");
            return;
        }

        RequestBody nameBody =
                RequestBody.create(MediaType.parse("text/plain"), name);

        RequestBody descBody =
                RequestBody.create(MediaType.parse("text/plain"), description);

        RequestBody priceBody =
                RequestBody.create(MediaType.parse("text/plain"),
                        String.valueOf(price));

        RequestBody vegBody =
                RequestBody.create(MediaType.parse("text/plain"), "true");

        RequestBody categoryBody =
                RequestBody.create(MediaType.parse("text/plain"), "main");

        MultipartBody.Part imagePart = null;

        if (imageUri != null) {

            String path = getRealPathFromURI(imageUri);

            if (path != null) {

                File file = new File(path);

                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("image/*"), file);

                imagePart = MultipartBody.Part.createFormData(
                        "image",
                        file.getName(),
                        requestFile
                );
            }
        }

        if (isEditMode && menuItem != null) {

            viewModel.updateMenuItem(
                    menuItem.getId(),
                    nameBody,
                    descBody,
                    priceBody,
                    vegBody,
                    categoryBody,
                    imagePart
            ).observe(this,
                    resource -> handleResource(resource, "Item updated"));

        } else {

            viewModel.addMenuItem(
                    messId,
                    nameBody,
                    descBody,
                    priceBody,
                    vegBody,
                    categoryBody,
                    imagePart
            ).observe(this,
                    resource -> handleResource(resource, "Item added"));
        }
    }

    /* =====================================
       Convert URI → file path
    ===================================== */

    private String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor == null) return uri.getPath();

        cursor.moveToFirst();

        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        String path = cursor.getString(idx);

        cursor.close();

        return path;
    }

    /* =====================================
       Handle API response
    ===================================== */

    private void handleResource(Resource<?> resource, String successMessage) {

        if (resource == null) return;

        switch (resource.status) {

            case LOADING:
                showLoading();
                break;

            case SUCCESS:

                hideLoading();
                showMessage(successMessage);
                finish();

                break;

            case ERROR:

                hideLoading();
                showError(resource.message);

                break;
        }
    }
}