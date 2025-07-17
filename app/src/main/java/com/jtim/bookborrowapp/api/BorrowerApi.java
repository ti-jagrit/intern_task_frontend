package com.jtim.bookborrowapp.api;

import com.jtim.bookborrowapp.models.Borrower;
import com.jtim.bookborrowapp.models.BorrowerResponseWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BorrowerApi {

    @GET("borrowers")
    Call<BorrowerResponseWrapper> getBorrowers();

    @POST("borrowers")
    Call<Borrower> createBorrower(@Body Borrower borrower);

    @PUT("borrowers/{id}")
    Call<Borrower> updateBorrower(@Path("id") int id, @Body Borrower borrower);

    @DELETE("borrowers/{id}")
    Call<Void> deleteBorrower(@Path("id") int id);
}
