package com.vendorpro.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.model.MenuItem;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.MenuViewModel;

public class AddEditMenuItemActivity extends BaseActivity {

    private EditText etName, etDescription, etPrice;
    private CheckBox cbAvailable;
    private Button btnSave;
    private TextView tvTitle;
    private MenuViewModel viewModel;
    private MenuItem menuItem;
    private boolean isEditMode = false;

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

        btnSave.setOnClickListener(v -> saveMenuItem());
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        cbAvailable = findViewById(R.id.cbAvailable);
        btnSave = findViewById(R.id.btnSave);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void populateData() {
        tvTitle.setText("Edit Menu Item");
        etName.setText(menuItem.getName());
        etDescription.setText(menuItem.getDescription());
        etPrice.setText(String.valueOf(menuItem.getPrice()));
        cbAvailable.setChecked(menuItem.isAvailable());
    }

    private void saveMenuItem() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        boolean isAvailable = cbAvailable.isChecked();

        if (name.isEmpty() || priceStr.isEmpty()) {
            showMessage("Please fill required fields");
            return;
        }

        double price = Double.parseDouble(priceStr);
        String vendorId = TokenManager.getInstance(this).getUserId();

        if (isEditMode) {
            menuItem.setName(name);
            menuItem.setDescription(description);
            menuItem.setPrice(price);
            menuItem.setAvailable(isAvailable);
            
            viewModel.updateMenuItem(menuItem.getId(), menuItem).observe(this, resource -> {
                handleResource(resource, "Item updated");
            });
        } else {
            MenuItem newItem = new MenuItem(name, description, price, isAvailable);
            viewModel.addMenuItem(vendorId, newItem).observe(this, resource -> {
                handleResource(resource, "Item added");
            });
        }
    }

    private void handleResource(com.vendorpro.network.Resource<?> resource, String successMessage) {
        if (resource != null) {
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
}
