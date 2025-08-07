package com.jtim.bookborrowapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.*;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Book;
import com.jtim.bookborrowapp.models.Borrower;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogBorrow extends Dialog {

    public interface OnBorrowConfirm {
        void onConfirm(int borrowerId, List<BookQuantity> bookQuantities);
    }

    private final List<Borrower> borrowers;
    private final List<Book> books;
    private final OnBorrowConfirm listener;

    public DialogBorrow(Context context, List<Borrower> borrowers, List<Book> books, OnBorrowConfirm listener) {
        super(context);
        this.borrowers = borrowers;
        this.books = books;
        this.listener = listener;
    }

    public static class BookQuantity {
        private final int bookId;
        private final int quantity;

        public BookQuantity(int bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public int getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_borrow);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Spinner spinnerBorrower = findViewById(R.id.spinnerSelectBorrower);
        LinearLayout bookContainer = findViewById(R.id.containerBooks);
        Button btnCancel = findViewById(R.id.btnCancelBorrow);
        Button btnConfirm = findViewById(R.id.btnConfirmBorrow);

        // Load borrowers in spinner
        List<String> borrowerNames = new ArrayList<>();
        for (Borrower b : borrowers) borrowerNames.add(b.getBorrowerName());
        ArrayAdapter<String> borrowerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, borrowerNames);
        spinnerBorrower.setAdapter(borrowerAdapter);

        // Dynamically inflate book items with qty
        LayoutInflater inflater = LayoutInflater.from(getContext());
        List<CheckBox> checkBoxes = new ArrayList<>();
        List<EditText> qtyFields = new ArrayList<>();

        for (Book book : books) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);

            CheckBox cb = new CheckBox(getContext());
            cb.setText(book.getBookName());
            cb.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));

            EditText qty = new EditText(getContext());
            qty.setHint("Qty");
            qty.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            qty.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            row.addView(cb);
            row.addView(qty);
            bookContainer.addView(row);

            checkBoxes.add(cb);
            qtyFields.add(qty);
        }

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            int borrowerPos = spinnerBorrower.getSelectedItemPosition();
            if (borrowerPos == -1) {
                Toast.makeText(getContext(), "Please select a borrower", Toast.LENGTH_SHORT).show();
                return;
            }

            List<BookQuantity> selected = new ArrayList<>();
            for (int i = 0; i < books.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    String qtyStr = qtyFields.get(i).getText().toString().trim();
                    if (qtyStr.isEmpty()) {
                        Toast.makeText(getContext(), "Enter quantity for selected book", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int qty = Integer.parseInt(qtyStr);
                    if (qty <= 0) {
                        Toast.makeText(getContext(), "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    selected.add(new BookQuantity(books.get(i).getId(), qty));
                }
            }

            if (selected.isEmpty()) {
                Toast.makeText(getContext(), "Select at least one book", Toast.LENGTH_SHORT).show();
                return;
            }

            int borrowerId = borrowers.get(borrowerPos).getId();
            listener.onConfirm(borrowerId, selected);
            dismiss();
        });
    }
}
