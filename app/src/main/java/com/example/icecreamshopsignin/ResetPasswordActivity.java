package com.example.icecreamshopsignin;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icecreamshopsignin.DatabaseHelper;
import com.example.icecreamshopsignin.LoginActivity;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText resetEmail, newPassword, confirmPassword;
    private Button resetPasswordButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // Initialize views
        resetEmail = findViewById(R.id.reset_email);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        ImageView backButton = findViewById(R.id.btnBack);

        databaseHelper = new DatabaseHelper(this);

        // Set up back button
        backButton.setOnClickListener(view -> {
            navigateToLogin();
        });

        resetPasswordButton.setOnClickListener(view -> {
            String email = resetEmail.getText().toString().trim();
            String password = newPassword.getText().toString();
            String confirmPwd = confirmPassword.getText().toString();

            if (validateInputs(email, password, confirmPwd)) {
                if (databaseHelper.updatePassword(email, password)) {
                    showSuccess("Password Reset Successfully");
                    navigateToLogin();
                } else {
                    showError("Password Reset Failed");
                }
            }
        });
    }

    private boolean validateInputs(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format");
            return false;
        }

        if (!databaseHelper.checkEmail(email)) {
            showError("Email not registered");
            return false;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords don't match");
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

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}