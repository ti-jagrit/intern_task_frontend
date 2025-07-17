package com.jtim.bookborrowapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Borrower;

public class DialogBorrower extends Dialog {

    public interface OnBorrowerSaved {
        void onSaved(Borrower borrower);
    }

    private final Borrower borrower;
    private final OnBorrowerSaved listener;

    public DialogBorrower(Context context, Borrower borrower, OnBorrowerSaved listener) {
        super(context);
        this.borrower = borrower;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_borrower);

        // Optional: Make dialog bigger
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        EditText nameInput = findViewById(R.id.edit_borrower_name);
        EditText addressInput = findViewById(R.id.edit_address);
        EditText mobileInput = findViewById(R.id.edit_mobile_no);
        EditText emailInput = findViewById(R.id.edit_email);
        Button saveBtn = findViewById(R.id.btn_save_borrower);

        if (borrower != null) {
            nameInput.setText(borrower.getBorrowerName());
            addressInput.setText(borrower.getAddress());
            mobileInput.setText(borrower.getMobileNo());
            emailInput.setText(borrower.getEmail());
        }

//        saveBtn.setOnClickListener(v -> {
//            Borrower updated = new Borrower();
//            updated.setBorrowerName(nameInput.getText().toString().trim());
//            updated.setAddress(addressInput.getText().toString().trim());
//            updated.setMobileNo(mobileInput.getText().toString().trim());
//            updated.setEmail(emailInput.getText().toString().trim());
//
//            if (borrower != null && borrower.getId() != 0) {
//                updated.setId(borrower.getId()); // Preserve ID for update
//            }
//
//            listener.onSaved(updated);
//            dismiss();
//        });
        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();
            String mobile = mobileInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            // === Input Validation ===
            if (name.isEmpty()) {
                nameInput.setError("Name is required");
                nameInput.requestFocus();
                return;
            }

            if (address.isEmpty()) {
                addressInput.setError("Address is required");
                addressInput.requestFocus();
                return;
            }

            if (mobile.isEmpty() || mobile.length() != 10) {
                mobileInput.setError("Enter valid 10-digit mobile number");
                mobileInput.requestFocus();
                return;
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.setError("Enter valid email address");
                emailInput.requestFocus();
                return;
            }

            Borrower updated = new Borrower();
            updated.setBorrowerName(name);
            updated.setAddress(address);
            updated.setMobileNo(mobile);
            updated.setEmail(email);

            if (borrower != null && borrower.getId() != 0) {
                updated.setId(borrower.getId());
            }

            listener.onSaved(updated);
            dismiss();
        });

    }
}
