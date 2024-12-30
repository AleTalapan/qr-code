package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.register);

        Button backToLoginButton = findViewById(R.id.back_to_login);

        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, com.example.myapplication.ui.login.LoginActivity.class);
            startActivity(intent);
            finish(); // Închide RegisterActivity pentru a preveni revenirea
        });


        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Introduceți un username și o parolă!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertUser(username, password);
                Toast.makeText(RegisterActivity.this, "Cont creat cu succes!", Toast.LENGTH_SHORT).show();
                finish(); // Înapoi la LoginActivity
            }
        });
    }
}