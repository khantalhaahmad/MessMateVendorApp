package com.vendorpro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vendorpro.R;
import com.vendorpro.model.MenuItem;
import com.vendorpro.network.Resource;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.MenuViewModel;

public class MenuActivity extends BaseActivity
        implements MenuAdapter.OnItemClickListener {

    private static final String TAG = "MENU_DEBUG";

    private MenuViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "MenuActivity started");

        initializeViews();
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        fabAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditMenuItemActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MenuActivity resumed → loading menu");
        loadMenu();
    }

    private void initializeViews() {

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMenu() {

        // 🔹 Fetch Mess ID
        String messId = TokenManager
                .getInstance(this)
                .getMessId();

        Log.d(TAG, "Fetched Mess ID: " + messId);

        if (messId == null || messId.isEmpty()) {

            Log.e(TAG, "Mess ID is NULL or empty");

            tvEmpty.setVisibility(View.VISIBLE);
            showError("Mess ID not found");

            return;
        }

        Log.d(TAG, "Calling API → getMenu(" + messId + ")");

        viewModel.getMenu(messId)
                .observe(this, resource -> {

                    if (resource == null) {
                        Log.e(TAG, "Resource is NULL");
                        return;
                    }

                    switch (resource.status) {

                        case LOADING:

                            Log.d(TAG, "Menu loading...");
                            showLoading();

                            break;

                        case SUCCESS:

                            hideLoading();

                            Log.d(TAG, "Menu API success");

                            if (resource.data != null) {

                                Log.d(TAG, "Menu items count: " + resource.data.size());

                                if (!resource.data.isEmpty()) {

                                    recyclerView.setAdapter(
                                            new MenuAdapter(
                                                    this,
                                                    resource.data,
                                                    this
                                            )
                                    );

                                    tvEmpty.setVisibility(View.GONE);

                                } else {

                                    Log.w(TAG, "Menu list empty");

                                    recyclerView.setAdapter(null);
                                    tvEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {

                                Log.e(TAG, "Menu response data NULL");
                            }

                            break;

                        case ERROR:

                            hideLoading();

                            Log.e(TAG, "Menu API error: " + resource.message);

                            showError(
                                    resource.message != null
                                            ? resource.message
                                            : "Failed to load menu"
                            );

                            break;
                    }
                });
    }

    @Override
    public void onEditClick(MenuItem item) {

        Log.d(TAG, "Edit item clicked: " + item.getName());

        Intent intent = new Intent(this, AddEditMenuItemActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(MenuItem item) {

        Log.d(TAG, "Delete item clicked: " + item.getName());

        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> deleteItem(item))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(MenuItem item) {

        Log.d(TAG, "Deleting item ID: " + item.getId());

        showLoading();

        viewModel.deleteMenuItem(item.getId())
                .observe(this, resource -> {

                    hideLoading();

                    if (resource != null
                            && resource.status == Resource.Status.SUCCESS
                            && Boolean.TRUE.equals(resource.data)) {

                        Log.d(TAG, "Item deleted successfully");

                        Toast.makeText(
                                this,
                                "Item deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadMenu();

                    } else {

                        Log.e(TAG, "Delete failed: "
                                + (resource != null ? resource.message : "unknown"));

                        Toast.makeText(
                                this,
                                resource != null && resource.message != null
                                        ? resource.message
                                        : "Failed to delete item",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}