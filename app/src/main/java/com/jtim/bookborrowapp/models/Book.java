package com.jtim.bookborrowapp.models;

import org.jetbrains.annotations.NotNull;

public class Book {

    private int id;  // Use int instead of String
    private String bookName;
    private String author;
    private double price;
    private int quantity;

    // Constructor for Book object
    public Book(int id, String bookName, String author, double price, int quantity) {
        this.id = id;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters for all fields
    public int getId() {
        return id;  // Returning int instead of String
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
