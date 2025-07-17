package com.jtim.bookborrowapp.models;

import java.util.List;

public class BookResponseWrapper {
    private int status;
    private String message;
    private BookPage data;

    public BookPage getData() {
        return data;
    }

    public static class BookPage {
        private int pageNumber;
        private boolean last;
        private int totalPages;
        private int pageSize;
        private List<Book> content;
        private int totalElements;

        public List<Book> getContent() {
            return content;
        }
    }
}
