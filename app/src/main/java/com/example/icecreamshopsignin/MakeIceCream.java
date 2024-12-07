package com.example.icecreamshopsignin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MakeIceCream extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RadioGroup sizeRadioGroup;
    private CheckBox checkChocolate, checkVanilla, checkStrawberry, checkOreo, checkLemon, checkMint;
    private CheckBox checkSprinkles, checkChocoChips, checkCaramel, checkNuts;
    private Button addToCartButton;
    private ArrayList<CheckBox> selectedFlavors = new ArrayList<>();
    private int totalPrice = 0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_ice_cream);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("email", "");
        userId = dbHelper.getUserId(userEmail);

        // Initialize Views
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        checkChocolate = findViewById(R.id.checkChocolate);
        checkVanilla = findViewById(R.id.checkVanilla);
        checkStrawberry = findViewById(R.id.checkStrawberry);
        checkOreo = findViewById(R.id.checkOreo);
        checkLemon = findViewById(R.id.checkLemon);
        checkMint = findViewById(R.id.checkMint);

        checkSprinkles = findViewById(R.id.checkSprinkles);
        checkChocoChips = findViewById(R.id.checkChocoChips);
        checkCaramel = findViewById(R.id.checkCaramel);
        checkNuts = findViewById(R.id.checkNuts);

        addToCartButton = findViewById(R.id.addToCartButton);
        ImageView backButton = findViewById(R.id.backButton);
        ImageView cartButton = findViewById(R.id.cartButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MakeIceCream.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        cartButton.setOnClickListener(v -> {
            // Navigate to cart activity
            Intent intent = new Intent(MakeIceCream.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupListeners() {
        setFlavorRestrictions();
        setRealTimeListeners();

        addToCartButton.setOnClickListener(v -> {
            if (userId != -1) {
                saveOrderToDatabase();
            } else {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }

    private void setFlavorRestrictions() {
        ArrayList<CheckBox> flavorCheckBoxes = new ArrayList<>();
        flavorCheckBoxes.add(checkChocolate);
        flavorCheckBoxes.add(checkVanilla);
        flavorCheckBoxes.add(checkStrawberry);
        flavorCheckBoxes.add(checkOreo);
        flavorCheckBoxes.add(checkLemon);
        flavorCheckBoxes.add(checkMint);

        for (CheckBox checkBox : flavorCheckBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedFlavors.size() >= 3) {
                        checkBox.setChecked(false);
                        Toast.makeText(this, "You can only select up to 3 flavors.", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedFlavors.add(checkBox);
                    }
                } else {
                    selectedFlavors.remove(checkBox);
                }
                updateTotalPrice();
            });
        }
    }

    private void setRealTimeListeners() {
        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateTotalPrice());
        checkSprinkles.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkChocoChips.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkCaramel.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkNuts.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
    }

    private void updateTotalPrice() {
        totalPrice = 0;

        // Calculate size price
        int selectedSizeId = sizeRadioGroup.getCheckedRadioButtonId();
        if (selectedSizeId != -1) {
            RadioButton selectedSize = findViewById(selectedSizeId);
            if (selectedSize.getText().toString().contains("10")) {
                totalPrice += 10;
            } else if (selectedSize.getText().toString().contains("15")) {
                totalPrice += 15;
            } else if (selectedSize.getText().toString().contains("20")) {
                totalPrice += 20;
            }
        }

        // Add flavors price
        totalPrice += selectedFlavors.size() * 5;

        // Add toppings price
        if (checkSprinkles.isChecked()) totalPrice += 2;
        if (checkChocoChips.isChecked()) totalPrice += 2;
        if (checkCaramel.isChecked()) totalPrice += 3;
        if (checkNuts.isChecked()) totalPrice += 3;

        addToCartButton.setText("Add to Cart - Total: " + totalPrice + " SAR");
    }

    private void saveOrderToDatabase() {
        // Create customizations string
        StringBuilder customizations = new StringBuilder();

        // Add selected size
        RadioButton selectedSize = findViewById(sizeRadioGroup.getCheckedRadioButtonId());
        customizations.append("Size: ").append(selectedSize.getText()).append("; ");

        // Add selected flavors
        customizations.append("Flavors: ");
        for (CheckBox flavor : selectedFlavors) {
            customizations.append(flavor.getText()).append(", ");
        }

        // Add selected toppings
        customizations.append("; Toppings: ");
        if (checkSprinkles.isChecked()) customizations.append("Sprinkles, ");
        if (checkChocoChips.isChecked()) customizations.append("Chocolate Chips, ");
        if (checkCaramel.isChecked()) customizations.append("Caramel Syrup, ");
        if (checkNuts.isChecked()) customizations.append("Nuts, ");

        // First add item to Menu if it doesn't exist
        long menuItemId = dbHelper.addMenuItem("Custom Ice Cream", totalPrice,
                customizations.toString(), null);

        // Add to cart
        dbHelper.addToCart(userId, (int)menuItemId, 1, customizations.toString());

        Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();

        // Optionally clear selections
        clearSelections();
    }

    private void clearSelections() {
        sizeRadioGroup.clearCheck();
        checkChocolate.setChecked(false);
        checkVanilla.setChecked(false);
        checkStrawberry.setChecked(false);
        checkOreo.setChecked(false);
        checkLemon.setChecked(false);
        checkMint.setChecked(false);
        checkSprinkles.setChecked(false);
        checkChocoChips.setChecked(false);
        checkCaramel.setChecked(false);
        checkNuts.setChecked(false);
        selectedFlavors.clear();
        updateTotalPrice();
    }
}