package com.jtim.bookborrowapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowsActivity extends AppCompatActivity {

    private Spinner borrowerSpinner;
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
        btnBorrow = findViewById(R.id.fabAddBorrow);
        recyclerView = findViewById(R.id.recycler_view_borrows);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BorrowRecordAdapter(borrowList);
        recyclerView.setAdapter(adapter);

        borrowApi = ApiClient.getClient(this).create(BorrowApi.class);
        borrowerApi = ApiClient.getClient(this).create(BorrowerApi.class);
        bookApi = ApiClient.getClient(this).create(BookApi.class);

        loadAllBorrows();    // Load all borrows by default
        loadBorrowers();     // Then set up spinner to filter

        btnBorrow.setOnClickListener(v -> fetchBooksAndOpenDialog());
    }

    private void loadBorrowers() {
        borrowerApi.getBorrowers().enqueue(new Callback<BorrowerResponseWrapper>() {
            @Override
            public void onResponse(Call<BorrowerResponseWrapper> call, Response<BorrowerResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borrowerList = response.body().getData().getContent();

                    List<String> names = new ArrayList<>();
                    names.add("All Borrowers"); // First item
                    for (Borrower b : borrowerList) names.add(b.getBorrowerName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(BorrowsActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, names);
                    borrowerSpinner.setAdapter(adapter);

                    borrowerSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                loadAllBorrows(); // All
                            } else {
                                int borrowerId = borrowerList.get(position - 1).getId();
                                loadBorrowsByBorrower(borrowerId);
                            }
                        }

                        @Override
                        public void onNothingSelected(android.widget.AdapterView<?> parent) { }
                    });

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

                    new DialogBorrow(BorrowsActivity.this, borrowerList, books, (borrowerId, bookQuantities) -> {

                        BorrowRequest borrowRequest = new BorrowRequest(borrowerId, bookQuantities);

                        borrowApi.createBorrow(borrowRequest).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(BorrowsActivity.this, "Borrowed successfully", Toast.LENGTH_SHORT).show();
                                    loadAllBorrows();
                                } else {
                                    try {
                                        String errorMsg = "Borrow failed";
                                        if (response.errorBody() != null) {
                                            String errorBody = response.errorBody().string();
                                            JSONObject json = new JSONObject(errorBody);
                                            errorMsg = json.optString("message", errorMsg);
                                        }
                                        Toast.makeText(BorrowsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(BorrowsActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(BorrowsActivity.this, "Borrow failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }).show();
                } else {
                    Toast.makeText(BorrowsActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponseWrapper> call, Throwable t) {
                Toast.makeText(BorrowsActivity.this, "Error fetching books: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

