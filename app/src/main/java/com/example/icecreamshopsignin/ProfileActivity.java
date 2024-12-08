package com.example.icecreamshopsignin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextInputEditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        databaseHelper = new DatabaseHelper(this);

        // Initialize views
        ImageButton backButton = findViewById(R.id.backButton);
        emailField = findViewById(R.id.emailField);

        // Set user email
        String userEmail = UserManager.getInstance(this).getUserEmail();
        emailField.setText(userEmail);

        // Set up back button
        backButton.setOnClickListener(v -> finish());

        // Set up change password button
        findViewById(R.id.changePasswordButton).setOnClickListener(v -> showChangePasswordDialog());
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        EditText currentPasswordField = dialogView.findViewById(R.id.currentPasswordField);
        EditText newPasswordField = dialogView.findViewById(R.id.newPasswordField);
        EditText confirmPasswordField = dialogView.findViewById(R.id.confirmPasswordField);

        new AlertDialog.Builder(this)
                .setTitle("Change Password")
                .setView(dialogView)
                .setPositiveButton("Change", (dialog, which) -> {
                    String currentPassword = currentPasswordField.getText().toString();
                    String newPassword = newPasswordField.getText().toString();
                    String confirmPassword = confirmPasswordField.getText().toString();

                    if (validatePasswordChange(currentPassword, newPassword, confirmPassword)) {
                        updatePassword(newPassword);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private boolean validatePasswordChange(String currentPassword, String newPassword, String confirmPassword) {
        String userEmail = UserManager.getInstance(this).getUserEmail();

        if (!databaseHelper.checkEmailPassword(userEmail, currentPassword)) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void updatePassword(String newPassword) {
        // Add updatePassword method to DatabaseHelper
        if (databaseHelper.updatePassword(UserManager.getInstance(this).getUserEmail(), newPassword)) {
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
        }
    }
}