package com.jtim.bookborrowapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Book;

public class DialogBook extends Dialog {

    private EditText bookNameEt, authorEt, priceEt, qtyEt;
    private Button saveBtn, cancelBtn;
    private Book book;
    private OnSaveListener listener;

    public DialogBook(Context context, Book book, OnSaveListener listener) {
        super(context);
        this.book = book;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_book);

        Window win = getWindow();
        if (win != null) {
            win.setLayout(
                    (int)(getContext().getResources().getDisplayMetrics().widthPixels * 0.95),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        bookNameEt = findViewById(R.id.edit_book_name);
        authorEt = findViewById(R.id.edit_author);
        priceEt = findViewById(R.id.edit_price);
        qtyEt = findViewById(R.id.edit_quantity);
        saveBtn = findViewById(R.id.btn_save);
        cancelBtn = findViewById(R.id.btn_cancel);

        if (book != null) {
            bookNameEt.setText(book.getBookName());
            authorEt.setText(book.getAuthor());
            priceEt.setText(String.valueOf(book.getPrice()));
            qtyEt.setText(String.valueOf(book.getQuantity()));
        }

//        saveBtn.setOnClickListener(v -> {
//            String name = bookNameEt.getText().toString().trim();
//            String auth = authorEt.getText().toString().trim();
//            double price = priceEt.getText().toString().isEmpty()
//                    ? 0 : Double.parseDouble(priceEt.getText().toString().trim());
//            int qty = qtyEt.getText().toString().isEmpty()
//                    ? 0 : Integer.parseInt(qtyEt.getText().toString().trim());
//
//            Book updated = new Book(
//                    (book != null) ? book.getId() : 0,
//                    name, auth, price, qty
//            );
//
//            listener.onSave(updated);
//            dismiss();
//        });

        saveBtn.setOnClickListener(v -> {
            String name = bookNameEt.getText().toString().trim();
            String author = authorEt.getText().toString().trim();
            String priceStr = priceEt.getText().toString().trim();
            String qtyStr = qtyEt.getText().toString().trim();

            //  Input Validation
            if (name.isEmpty()) {
                bookNameEt.setError("Book name is required");
                bookNameEt.requestFocus();
                return;
            }

            if (author.isEmpty()) {
                authorEt.setError("Author is required");
                authorEt.requestFocus();
                return;
            }

            if (priceStr.isEmpty()) {
                priceEt.setError("Price is required");
                priceEt.requestFocus();
                return;
            }

            if (qtyStr.isEmpty()) {
                qtyEt.setError("Quantity is required");
                qtyEt.requestFocus();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    priceEt.setError("Price must be positive");
                    priceEt.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                priceEt.setError("Invalid price");
                priceEt.requestFocus();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(qtyStr);
                if (quantity < 0) {
                    qtyEt.setError("Quantity must be positive");
                    qtyEt.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                qtyEt.setError("Invalid quantity");
                qtyEt.requestFocus();
                return;
            }

            Book updated = new Book(
                    (book != null) ? book.getId() : 0,
                    name, author, price, quantity
            );

            listener.onSave(updated);
            dismiss();
        });


        cancelBtn.setOnClickListener(v -> dismiss());
    }

    public interface OnSaveListener {
        void onSave(Book book);
    }
}
