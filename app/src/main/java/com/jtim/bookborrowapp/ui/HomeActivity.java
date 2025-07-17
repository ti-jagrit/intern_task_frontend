package com.jtim.bookborrowapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.jtim.bookborrowapp.R;

public class HomeActivity extends AppCompatActivity {

    private CardView cardBooks, cardBorrowers, cardBorrowBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardBooks = findViewById(R.id.cardBooks);
        cardBorrowers = findViewById(R.id.cardBorrowers);
        cardBorrowBook = findViewById(R.id.cardBorrowBook);

        cardBooks.setOnClickListener(v -> startActivity(new Intent(this, BooksActivity.class)));
        cardBorrowers.setOnClickListener(v -> startActivity(new Intent(this, BorrowersActivity.class)));
        cardBorrowBook.setOnClickListener(v -> startActivity(new Intent(this, BorrowsActivity.class)));
    }
}
