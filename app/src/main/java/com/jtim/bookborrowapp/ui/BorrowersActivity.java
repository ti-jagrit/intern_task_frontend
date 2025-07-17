package com.jtim.bookborrowapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.adapter.BorrowerAdapter;
import com.jtim.bookborrowapp.api.ApiClient;
import com.jtim.bookborrowapp.api.BorrowerApi;
import com.jtim.bookborrowapp.models.Borrower;
import com.jtim.bookborrowapp.models.BorrowerResponseWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowerAdapter adapter;
    private BorrowerApi api;
    private List<Borrower> borrowerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowers);

        recyclerView = findViewById(R.id.recycler_view_borrowers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BorrowerAdapter(borrowerList, new BorrowerAdapter.OnBorrowerActionListener() {
            @Override
            public void onEdit(Borrower borrower) {
                openDialog(borrower);
            }

            @Override
            public void onDelete(Borrower borrower) {
                new AlertDialog.Builder(BorrowersActivity.this)
                        .setTitle("Delete")
                        .setMessage("Delete \"" + borrower.getBorrowerName() + "\"?")
                        .setPositiveButton("Yes", (dialog, idx) -> api.deleteBorrower(borrower.getId()).enqueue(deleteCallback(borrower)))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        api = ApiClient.getClient(this).create(BorrowerApi.class);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> openDialog(null));

        loadBorrowers();
    }

    private void openDialog(Borrower borrower) {
        new DialogBorrower(this, borrower, updated -> {
            if (borrower != null && borrower.getId() != 0) {
                api.updateBorrower(borrower.getId(), updated).enqueue(updateCallback());
            } else {
                api.createBorrower(updated).enqueue(addCallback());
            }
        }).show();
    }

    private Callback<Borrower> updateCallback() {
        return new Callback<Borrower>() {
            @Override
            public void onResponse(Call<Borrower> call, Response<Borrower> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BorrowersActivity.this, "Borrower Updated", Toast.LENGTH_SHORT).show();
                    loadBorrowers();
                } else {
                    Toast.makeText(BorrowersActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Borrower> call, Throwable t) {
                Toast.makeText(BorrowersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Callback<Borrower> addCallback() {
        return new Callback<Borrower>() {
            @Override
            public void onResponse(Call<Borrower> call, Response<Borrower> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BorrowersActivity.this, "Borrower Added", Toast.LENGTH_SHORT).show();
                    loadBorrowers();
                } else {
                    Toast.makeText(BorrowersActivity.this, "Add failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Borrower> call, Throwable t) {
                Toast.makeText(BorrowersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Callback<Void> deleteCallback(Borrower borrower) {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BorrowersActivity.this, "Borrower \"" + borrower.getBorrowerName() + "\" Deleted", Toast.LENGTH_SHORT).show();
                    loadBorrowers();
                } else {
                    Toast.makeText(BorrowersActivity.this, "Delete failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BorrowersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void loadBorrowers() {
        api.getBorrowers().enqueue(new Callback<BorrowerResponseWrapper>() {
            @Override
            public void onResponse(Call<BorrowerResponseWrapper> call, Response<BorrowerResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body().getData().getContent());
                }
            }

            @Override
            public void onFailure(Call<BorrowerResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowersActivity.this, "Load failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
