package com.jtim.bookborrowapp.models;

import java.util.List;

public class Borrow {
    private int borrowId;
    private String borrowDate;
    private int borrowerId;
    private String borrowerName;
    private String mobileNo;
    private List<BookItem> books;

    public int getBorrowId() { return borrowId; }
    public String getBorrowDate() { return borrowDate; }
    public String getBorrowerName() { return borrowerName; }
    public String getMobileNo() { return mobileNo; }
    public List<BookItem> getBooks() { return books; }

    public static class BookItem {
        private int bookId;
        private String bookName;
        private String author;

        public int getBookId() { return bookId; }
        public String getBookName() { return bookName; }
        public String getAuthor() { return author; }
    }
}
