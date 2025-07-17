package com.jtim.bookborrowapp.models;

import java.util.List;

public class BorrowResponseWrapper {
    private int status;
    private String message;
    private List<BorrowRecord> data; // <-- Must match JSON array

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BorrowRecord> getData() {
        return data;
    }

    public void setData(List<BorrowRecord> data) {
        this.data = data;
    }
}
