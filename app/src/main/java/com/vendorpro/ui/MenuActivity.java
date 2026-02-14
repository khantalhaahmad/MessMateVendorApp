package com.vendorpro.ui;

import android.content.Intent;
import android.os.Bundle;
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

    private MenuViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initializeViews();
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        fabAdd.setOnClickListener(v ->
                startActivity(
                        new Intent(this, AddEditMenuItemActivity.class)
                )
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        tvEmpty = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMenu() {
        String vendorId = TokenManager
                .getInstance(this)
                .getUserId();

        if (vendorId == null || vendorId.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        viewModel.getMenu(vendorId)
                .observe(this, resource -> {

                    if (resource == null) return;

                    switch (resource.status) {

                        case LOADING:
                            showLoading();
                            break;

                        case SUCCESS:
                            hideLoading();

                            if (resource.data != null
                                    && !resource.data.isEmpty()) {

                                recyclerView.setAdapter(
                                        new MenuAdapter(
                                                this,
                                                resource.data,
                                                this
                                        )
                                );
                                tvEmpty.setVisibility(View.GONE);

                            } else {
                                recyclerView.setAdapter(null);
                                tvEmpty.setVisibility(View.VISIBLE);
                            }
                            break;

                        case ERROR:
                            hideLoading();
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
        Intent intent = new Intent(this, AddEditMenuItemActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes",
                        (dialog, which) -> deleteItem(item))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteItem(MenuItem item) {
        showLoading();

        viewModel.deleteMenuItem(item.getId())
                .observe(this, resource -> {

                    hideLoading();

                    if (resource != null
                            && resource.status == Resource.Status.SUCCESS
                            && Boolean.TRUE.equals(resource.data)) {

                        Toast.makeText(
                                this,
                                "Item deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadMenu();

                    } else {
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
