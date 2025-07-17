package com.jtim.bookborrowapp.api;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    private static Retrofit retrofit;

    // This method will create and return the Retrofit instance
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            // Create an OkHttpClient with an interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            // Get the original request
                            Request original = chain.request();

                            // Get the token from SharedPreferences
                            SharedPreferences prefs = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
                            String token = prefs.getString("JWT_TOKEN", null);

                            // If a token exists, add it to the header of the request
                            Request.Builder requestBuilder = original.newBuilder()
                                    .header("Content-Type", "application/json");

                            if (token != null) {
                                requestBuilder.header("Authorization", token); // Ensure "Bearer " prefix is included
                            }

                            // Proceed with the request
                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .build();

            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:6060/api/") // Use emulator-friendly IP for localhost
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)  // Set the OkHttpClient
                    .build();
        }
        return retrofit;  // Return the created Retrofit instance
    }
}
