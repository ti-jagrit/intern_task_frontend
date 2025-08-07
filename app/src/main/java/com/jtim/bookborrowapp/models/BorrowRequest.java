package com.jtim.bookborrowapp.models;

import com.google.gson.annotations.SerializedName;
import com.jtim.bookborrowapp.ui.DialogBorrow.BookQuantity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorrowRequest {

    @SerializedName("borrowerId")
    private int borrowerId;

    @SerializedName("bookQuantities")
    private Map<Integer, Integer> bookQuantities;

    public BorrowRequest(int borrowerId, List<BookQuantity> quantities) {
        this.borrowerId = borrowerId;
        this.bookQuantities = new HashMap<>();
        for (BookQuantity bq : quantities) {
            this.bookQuantities.put(bq.getBookId(), bq.getQuantity());
        }
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public Map<Integer, Integer> getBookQuantities() {
        return bookQuantities;
    }
}
