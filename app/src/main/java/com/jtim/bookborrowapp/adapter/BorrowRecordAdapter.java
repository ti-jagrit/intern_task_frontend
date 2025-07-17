package com.jtim.bookborrowapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Book;
import com.jtim.bookborrowapp.models.BorrowRecord;

import java.util.List;

public class BorrowRecordAdapter extends RecyclerView.Adapter<BorrowRecordAdapter.ViewHolder> {

    private final List<BorrowRecord> borrowList;

    public BorrowRecordAdapter(List<BorrowRecord> borrowList) {
        this.borrowList = borrowList;
    }

    public void updateList(List<BorrowRecord> newList) {
        borrowList.clear();
        borrowList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BorrowRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BorrowRecordAdapter.ViewHolder holder, int position) {
        BorrowRecord record = borrowList.get(position);
        holder.textBorrowerName.setText("Borrower: " + record.getBorrowerName());
        holder.textBorrowDate.setText("Date: " + record.getBorrowDate());
        holder.textMobile.setText("Mobile: " + record.getMobileNo());

        StringBuilder bookList = new StringBuilder("Books: ");
        for (Book b : record.getBooks()) {
            bookList.append("\n- ").append(b.getBookName()).append(" (").append(b.getAuthor()).append(")");
        }
        holder.textBooks.setText(bookList.toString());
    }

    @Override
    public int getItemCount() {
        return borrowList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textBorrowerName, textBorrowDate, textMobile, textBooks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBorrowerName = itemView.findViewById(R.id.text_borrower_name);
            textBorrowDate = itemView.findViewById(R.id.text_borrow_date);
            textMobile = itemView.findViewById(R.id.text_mobile_no);
            textBooks = itemView.findViewById(R.id.text_books_list);
        }
    }
}
