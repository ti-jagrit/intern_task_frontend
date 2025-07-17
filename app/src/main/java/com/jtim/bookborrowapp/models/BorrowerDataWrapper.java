package com.jtim.bookborrowapp.models;

import java.util.List;

public class BorrowerDataWrapper {
    private int pageNumber;
    private boolean last;
    private int totalPages;
    private int pageSize;
    private List<Borrower> content;
    private int totalElements;

    // Getters and setters
    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public List<Borrower> getContent() { return content; }
    public void setContent(List<Borrower> content) { this.content = content; }

    public int getTotalElements() { return totalElements; }
    public void setTotalElements(int totalElements) { this.totalElements = totalElements; }
}
