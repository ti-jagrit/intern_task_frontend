package com.jtim.bookborrowapp.api;

import com.jtim.bookborrowapp.models.BorrowRequest;
import com.jtim.bookborrowapp.models.BorrowResponseWrapper;
import retrofit2.Call;
import retrofit2.http.*;

public interface BorrowApi {
    @GET("borrows")
    Call<BorrowResponseWrapper> getAllBorrows();

    @GET("borrows/borrower/{id}")
    Call<BorrowResponseWrapper> getBorrowsByBorrower(@Path("id") int id);

    @POST("borrows")
    Call<Void> createBorrow(@Body BorrowRequest request);
}


