package com.jtim.bookborrowapp.models;

import java.util.List;

public class BorrowRequest {
    private int borrowerId;
    private List<Integer> bookIds;

    public BorrowRequest(int bId, List<Integer> bIds) {
        this.borrowerId = bId;
        this.bookIds = bIds;
    }
}
