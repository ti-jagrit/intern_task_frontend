package com.jtim.bookborrowapp.models;

public class BookResponse {
    private int status;
    private String message;
    private BookWrapper data;

    public BookWrapper getData() {
        return data;
    }
}
