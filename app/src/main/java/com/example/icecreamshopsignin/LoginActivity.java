package com.example.icecreamshopsignin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icecreamshopsignin.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(view -> {
            String email = binding.loginEmail.getText().toString().trim();
            String password = binding.loginPassword.getText().toString();

            if (validateInputs(email, password)) {
                if (databaseHelper.checkEmailPassword(email, password)) {
                    UserManager.getInstance(this).saveUserEmail(email);
                    showSuccess("Login successful");
                    navigateToMain();
                } else {
                    showError("Invalid email or password");
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(view -> navigateToSignUp());

        binding.forgotPasswordText.setOnClickListener(view -> {
            startActivity(new Intent(this, ResetPasswordActivity.class));
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showError("All fields are required");
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
}