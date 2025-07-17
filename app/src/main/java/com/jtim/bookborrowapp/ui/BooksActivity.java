package com.jtim.bookborrowapp.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.adapter.BookAdapter;
import com.jtim.bookborrowapp.api.ApiClient;
import com.jtim.bookborrowapp.api.BookApi;
import com.jtim.bookborrowapp.models.Book;
import com.jtim.bookborrowapp.models.BookResponseWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private BookApi api;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        recyclerView = findViewById(R.id.recycler_view_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(bookList, new BookAdapter.OnBookActionListener() {
            @Override
            public void onEdit(Book book) {
                openDialog(book);
            }

            @Override
            public void onDelete(Book book) {
                new AlertDialog.Builder(BooksActivity.this)
                        .setTitle("Delete")
                        .setMessage("Delete \"" + book.getBookName() + "\"?")
                        .setPositiveButton("Yes", (dialog, idx) -> api.deleteBook(book.getId()).enqueue(deleteCallback(book)))
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        api = ApiClient.getClient(this).create(BookApi.class);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> openDialog(null));

        loadBooks();
    }

    private void openDialog(Book book) {
        new DialogBook(this, book, updated -> {
            if (book != null && book.getId() != 0) {
                api.updateBook(book.getId(), updated).enqueue(updateCallback());
            } else {
                api.createBook(updated).enqueue(addCallback());
            }
        }).show();
    }

    // Callback for when a book is updated
    private Callback<Book> updateCallback() {
        return new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BooksActivity.this, "Book Updated", Toast.LENGTH_SHORT).show();
                    loadBooks(); // Refresh the book list
                } else {
                    Toast.makeText(BooksActivity.this, "Update failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    // Callback for when a book is added
    private Callback<Book> addCallback() {
        return new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BooksActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                    loadBooks(); // Refresh the book list
                } else {
                    Toast.makeText(BooksActivity.this, "Add failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    // Callback for when a book is deleted
    private Callback<Void> deleteCallback(Book book) {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BooksActivity.this, "Book \"" + book.getBookName() + "\" Deleted", Toast.LENGTH_SHORT).show();
                    loadBooks(); // Refresh the book list
                } else {
                    Toast.makeText(BooksActivity.this, "Delete failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void loadBooks() {
        api.getBooks().enqueue(new Callback<BookResponseWrapper>() {
            @Override
            public void onResponse(Call<BookResponseWrapper> call, Response<BookResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body().getData().getContent());
                }
            }

            @Override
            public void onFailure(Call<BookResponseWrapper> call, Throwable t) {
                Toast.makeText(BooksActivity.this, "Load failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
