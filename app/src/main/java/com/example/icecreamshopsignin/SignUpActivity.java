package com.example.icecreamshopsignin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icecreamshopsignin.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    DatabaseHelper databaseHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        // Setup date picker
        binding.signupDob.setOnClickListener(v -> showDatePicker());

        binding.signupButton.setOnClickListener(view -> {
            String username = binding.signupUsername.getText().toString().trim();
            String phone = binding.signupPhone.getText().toString().trim();
            String dob = binding.signupDob.getText().toString().trim();
            String email = binding.signupEmail.getText().toString().trim();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if (validateInputs(username, phone, dob, email, password, confirmPassword)) {
                if (databaseHelper.checkEmail(email)) {
                    showError("Email already registered");
                    return;
                }

                if (databaseHelper.insertData(email, password, username, phone)) {
                    showSuccess("Sign up successful!");
                    navigateToLogin();
                } else {
                    showError("Sign up failed");
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(view -> navigateToLogin());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateField();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set max date to current date (no future dates)
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        
        // Set min date (e.g., must be at least 13 years old)
        Calendar minAge = Calendar.getInstance();
        minAge.add(Calendar.YEAR, -13);
        datePickerDialog.getDatePicker().setMinDate(minAge.getTimeInMillis());
        
        datePickerDialog.show();
    }

    private void updateDateField() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        binding.signupDob.setText(sdf.format(calendar.getTime()));
    }

    private boolean validateInputs(String username, String phone, String dob, 
                                 String email, String password, String confirmPassword) {
        if (username.isEmpty() || phone.isEmpty() || dob.isEmpty() || 
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return false;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email format");
            return false;
        }

        if (!isValidPhone(phone)) {
            showError("Invalid phone number");
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

    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{10}$"); // Simple example for 10-digit number
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