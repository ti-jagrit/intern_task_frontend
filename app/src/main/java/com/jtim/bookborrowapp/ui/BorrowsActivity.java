package com.jtim.bookborrowapp.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.adapter.BorrowRecordAdapter;
import com.jtim.bookborrowapp.api.ApiClient;
import com.jtim.bookborrowapp.api.BookApi;
import com.jtim.bookborrowapp.api.BorrowApi;
import com.jtim.bookborrowapp.api.BorrowerApi;
import com.jtim.bookborrowapp.models.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowsActivity extends AppCompatActivity {

    private Spinner borrowerSpinner;
    private Button btnView, btnViewAll;
    private RecyclerView recyclerView;
    private FloatingActionButton btnBorrow;

    private BorrowRecordAdapter adapter;

    private List<Borrower> borrowerList = new ArrayList<>();
    private List<BorrowRecord> borrowList = new ArrayList<>();

    private BorrowApi borrowApi;
    private BorrowerApi borrowerApi;
    private BookApi bookApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_filter);

        borrowerSpinner = findViewById(R.id.spinnerBorrowers);
        btnView = findViewById(R.id.btnViewBorrows);
        btnViewAll = findViewById(R.id.btnViewAllBorrows);
        btnBorrow = findViewById(R.id.fabAddBorrow);
        recyclerView = findViewById(R.id.recycler_view_borrows);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BorrowRecordAdapter(borrowList);
        recyclerView.setAdapter(adapter);

        borrowApi = ApiClient.getClient(this).create(BorrowApi.class);
        borrowerApi = ApiClient.getClient(this).create(BorrowerApi.class);
        bookApi = ApiClient.getClient(this).create(BookApi.class);

        loadBorrowers();

        btnView.setOnClickListener(v -> {
            int pos = borrowerSpinner.getSelectedItemPosition();
            if (pos >= 0 && !borrowerList.isEmpty()) {
                int borrowerId = borrowerList.get(pos).getId();
                loadBorrowsByBorrower(borrowerId);
            }
        });

        btnViewAll.setOnClickListener(v -> loadAllBorrows());

        btnBorrow.setOnClickListener(v -> fetchBooksAndOpenDialog());
    }

    private void loadBorrowers() {
        borrowerApi.getBorrowers().enqueue(new Callback<BorrowerResponseWrapper>() {
            @Override
            public void onResponse(Call<BorrowerResponseWrapper> call, Response<BorrowerResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borrowerList = response.body().getData().getContent();
                    List<String> names = new ArrayList<>();
                    for (Borrower b : borrowerList) names.add(b.getBorrowerName());
                    borrowerSpinner.setAdapter(new ArrayAdapter<>(BorrowsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, names));
                } else {
                    Toast.makeText(BorrowsActivity.this, "Failed to load borrowers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BorrowerResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBorrowsByBorrower(int borrowerId) {
        borrowApi.getBorrowsByBorrower(borrowerId).enqueue(new Callback<BorrowResponseWrapper>() {
            @Override
            public void onResponse(Call<BorrowResponseWrapper> call, Response<BorrowResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borrowList.clear();
                    borrowList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BorrowsActivity.this, "Failed to load borrow records", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BorrowResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllBorrows() {
        borrowApi.getAllBorrows().enqueue(new Callback<BorrowResponseWrapper>() {
            @Override
            public void onResponse(Call<BorrowResponseWrapper> call, Response<BorrowResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borrowList.clear();
                    borrowList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BorrowsActivity.this, "Failed to load all borrows", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BorrowResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBooksAndOpenDialog() {
        bookApi.getBooks().enqueue(new Callback<BookResponseWrapper>() {
            @Override
            public void onResponse(Call<BookResponseWrapper> call, Response<BookResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> books = response.body().getData().getContent();
                    new DialogBorrow(BorrowsActivity.this, borrowerList, books, (borrowerId, bookIds) -> {
                        borrowApi.createBorrow(new BorrowRequest(borrowerId, bookIds))
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(BorrowsActivity.this, "Borrowed successfully", Toast.LENGTH_SHORT).show();
                                        loadAllBorrows();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(BorrowsActivity.this, "Borrow failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowsActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
