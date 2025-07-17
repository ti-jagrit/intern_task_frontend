package com.jtim.bookborrowapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jtim.bookborrowapp.R;
import com.jtim.bookborrowapp.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }

    private final List<Book> books;
    private final OnBookActionListener listener;

    public BookAdapter(List<Book> books, OnBookActionListener listener) {
        this.books = books;
        this.listener = listener;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, price, quantity;
        ImageButton btnEdit, btnDelete;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_book_title);
            author = itemView.findViewById(R.id.text_book_author);
            price = itemView.findViewById(R.id.text_book_price);
            quantity = itemView.findViewById(R.id.text_book_quantity);
            btnEdit = itemView.findViewById(R.id.btn_edit_book);
            btnDelete = itemView.findViewById(R.id.btn_delete_book);
        }

        public void bind(Book book, OnBookActionListener listener) {
            title.setText(book.getBookName());
            author.setText("Author: " + book.getAuthor());
            price.setText("Price: Rs. " + book.getPrice());
            quantity.setText("Quantity: " + book.getQuantity());

            btnEdit.setOnClickListener(v -> listener.onEdit(book));
            btnDelete.setOnClickListener(v -> listener.onDelete(book));
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int pos) {
        holder.bind(books.get(pos), listener);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateList(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
        notifyDataSetChanged();
    }
}
