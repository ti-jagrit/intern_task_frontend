package com.jtim.bookborrowapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Book;
import com.jtim.bookborrowapp.models.Borrower;

import java.util.ArrayList;
import java.util.List;

public class DialogBorrow extends Dialog {

    public interface OnBorrowConfirm {
        void onConfirm(int borrowerId, List<Integer> bookIds);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_borrow);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Spinner spinnerBorrower = findViewById(R.id.spinnerSelectBorrower);
        ListView listBooks = findViewById(R.id.listBooks);
        Button btnCancel = findViewById(R.id.btnCancelBorrow);
        Button btnConfirm = findViewById(R.id.btnConfirmBorrow);

        List<String> borrowerNames = new ArrayList<>();
        for (Borrower b : borrowers) borrowerNames.add(b.getBorrowerName());
        ArrayAdapter<String> borrowerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, borrowerNames);
        spinnerBorrower.setAdapter(borrowerAdapter);

        List<String> bookTitles = new ArrayList<>();
        for (Book b : books) bookTitles.add(b.getBookName());
        ArrayAdapter<String> bookAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, bookTitles);
        listBooks.setAdapter(bookAdapter);
        listBooks.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            int borrowerPos = spinnerBorrower.getSelectedItemPosition();
            if (borrowerPos == -1) {
                Toast.makeText(getContext(), "Please select a borrower", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Integer> selectedBookIds = new ArrayList<>();
            for (int i = 0; i < books.size(); i++) {
                if (listBooks.isItemChecked(i)) {
                    selectedBookIds.add(books.get(i).getId());
                }
            }

            if (selectedBookIds.isEmpty()) {
                Toast.makeText(getContext(), "Select at least one book", Toast.LENGTH_SHORT).show();
                return;
            }

            listener.onConfirm(borrowers.get(borrowerPos).getId(), selectedBookIds);
            dismiss();
        });
    }
}
