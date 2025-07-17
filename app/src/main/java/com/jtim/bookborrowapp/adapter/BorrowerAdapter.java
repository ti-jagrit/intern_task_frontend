package com.jtim.bookborrowapp.adapter;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Borrower;

import java.util.List;

public class BorrowerAdapter extends RecyclerView.Adapter<BorrowerAdapter.BorrowerViewHolder> {

    private List<Borrower> borrowerList;
    private OnBorrowerActionListener onBorrowerActionListener;

    public interface OnBorrowerActionListener {
        void onEdit(Borrower borrower);
        void onDelete(Borrower borrower);
    }

    public BorrowerAdapter(List<Borrower> borrowerList, OnBorrowerActionListener onBorrowerActionListener) {
        this.borrowerList = borrowerList;
        this.onBorrowerActionListener = onBorrowerActionListener;
    }

    @Override
    public BorrowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrower, parent, false);
        return new BorrowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BorrowerViewHolder holder, int position) {
        Borrower borrower = borrowerList.get(position);
        holder.borrowerName.setText(borrower.getBorrowerName());
        holder.address.setText(borrower.getAddress());
        holder.mobileNo.setText(borrower.getMobileNo());
        holder.email.setText(borrower.getEmail());

//        holder.editButton.setOnClickListener(v->{
//            if (isValidBorrowerData(borrower)) {
//                onBorrowerActionListener.onEdit(borrower);
//            }else{
//                Toast.makeText(holder.itemView.getContext(),"Invalid Data",Toast.LENGTH_SHORT).show();
//            }
//        });

    holder.editButton.setOnClickListener(v -> onBorrowerActionListener.onEdit(borrower));
        holder.deleteButton.setOnClickListener(v -> onBorrowerActionListener.onDelete(borrower));
    }

    @Override
    public int getItemCount() {
        return borrowerList.size();
    }

    public void updateList(List<Borrower> newList) {
        borrowerList.clear();
        borrowerList.addAll(newList);
        notifyDataSetChanged();
    }
//    //defining the isValidBorrowerDate
//    private boolean isValidBorrowerData(Borrower borrower) {
//        if (borrower.getBorrowerName() == null || borrower.getBorrowerName().isEmpty()) {
//            return false;
//        }
//
//        if (borrower.getEmail() == null || !Patterns.EMAIL_ADDRESS.matcher(borrower.getEmail()).matches()) {
//            return false;
//        }
//
//        if (borrower.getMobileNo() == null || borrower.getMobileNo().length() != 10) {
//            return false;
//        }
//
//        return true;
//    }
    public static class BorrowerViewHolder extends RecyclerView.ViewHolder {
        TextView borrowerName, address, mobileNo, email;
        View editButton, deleteButton;

        public BorrowerViewHolder(View itemView) {
            super(itemView);
            borrowerName = itemView.findViewById(R.id.text_borrower_name);
            address = itemView.findViewById(R.id.text_address);
            mobileNo = itemView.findViewById(R.id.text_mobile_no);
            email = itemView.findViewById(R.id.text_email);
            editButton = itemView.findViewById(R.id.btn_edit_borrower);
            deleteButton = itemView.findViewById(R.id.btn_delete_borrower);
        }
    }
}
