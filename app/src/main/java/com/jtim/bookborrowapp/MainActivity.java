package com.jtim.bookborrowapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jtim.bookborrowapp.api.ApiClient;
import com.jtim.bookborrowapp.api.AuthService;
import com.jtim.bookborrowapp.models.LoginRequest;
import com.jtim.bookborrowapp.models.LoginResponseWrapper;
import com.jtim.bookborrowapp.ui.BooksActivity;
import com.jtim.bookborrowapp.ui.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(user, pass);
        AuthService authService = ApiClient.getClient(this).create(AuthService.class);

        authService.login(loginRequest).enqueue(new Callback<LoginResponseWrapper>() {
            @Override
            public void onResponse(Call<LoginResponseWrapper> call, Response<LoginResponseWrapper> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getData();

                    if (token != null && !token.isEmpty()) {
                        SharedPreferences.Editor editor = getSharedPreferences("APP_PREF", Context.MODE_PRIVATE).edit();
                        editor.putString("JWT_TOKEN", "Bearer " + token); // Prefix with Bearer
                        editor.apply();

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed: No token received.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseWrapper> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
