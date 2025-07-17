package com.jtim.bookborrowapp.api;

import com.jtim.bookborrowapp.models.Book;
import com.jtim.bookborrowapp.models.BookResponseWrapper;

import retrofit2.Call;
import retrofit2.http.*;

public interface BookApi {

    @GET("books")
    Call<BookResponseWrapper> getBooks();

    @POST("books")
    Call<Book> createBook(@Body Book book);

    @PUT("books/{id}")
    Call<Book> updateBook(@Path("id") int id, @Body Book book);

    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") int id);
}
