package com.jtim.bookborrowapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Borrow;

import java.util.List;
import java.util.stream.Collectors;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.BorrowViewHolder> {

    private List<Borrow> borrowList;

    public BorrowAdapter(List<Borrow> list) {
        this.borrowList = list;
    }

    public void updateList(List<Borrow> list) {
        this.borrowList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrow, parent, false);
        return new BorrowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowViewHolder holder, int position) {
        Borrow borrow = borrowList.get(position);
        holder.txtBorrower.setText("Borrower: " + borrow.getBorrowerName());
        holder.txtMobile.setText("Mobile: " + borrow.getMobileNo());
        holder.txtDate.setText("Date: " + borrow.getBorrowDate());

        String bookDetails = borrow.getBooks().stream()
                .map(book -> book.getBookName() + " by " + book.getAuthor())
                .collect(Collectors.joining(",\n"));

        holder.txtBooks.setText("Books:\n" + bookDetails);
    }

    @Override
    public int getItemCount() {
        return borrowList.size();
    }

    static class BorrowViewHolder extends RecyclerView.ViewHolder {
        TextView txtBorrower, txtMobile, txtDate, txtBooks;

        public BorrowViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBorrower = itemView.findViewById(R.id.text_borrower_name);
            txtMobile = itemView.findViewById(R.id.text_mobile_no);
            txtDate = itemView.findViewById(R.id.text_borrow_date);
            txtBooks = itemView.findViewById(R.id.text_books);
        }
    }
}
