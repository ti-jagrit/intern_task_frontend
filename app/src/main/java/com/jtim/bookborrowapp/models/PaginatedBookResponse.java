package com.jtim.bookborrowapp.models;

import java.util.List;

public class PaginatedBookResponse {
    private int pageNumber, totalPages, pageSize, totalElements;
    private boolean last;
    private List<Book> content;

    public List<Book> getContent() { return content; }
}
