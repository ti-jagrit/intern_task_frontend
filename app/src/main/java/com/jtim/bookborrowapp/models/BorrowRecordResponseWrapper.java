package com.jtim.bookborrowapp.models;

import java.util.List;

public class BorrowRecordResponseWrapper {
    private int status;
    private String message;
    private List<BorrowRecord> data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<BorrowRecord> getData() { return data; }
}
