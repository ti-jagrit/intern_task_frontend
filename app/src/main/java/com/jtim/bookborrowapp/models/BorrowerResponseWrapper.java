package com.jtim.bookborrowapp.models;

public class BorrowerResponseWrapper {
    private int status;
    private String message;
    private BorrowerDataWrapper data;  // ← this wraps the real list

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public BorrowerDataWrapper getData() { return data; }
    public void setData(BorrowerDataWrapper data) { this.data = data; }
}
