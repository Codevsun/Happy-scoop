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

        try {
            // Initialize DatabaseHelper
            dbHelper = new DatabaseHelper(this);

            // Get userId from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String userEmail = prefs.getString("email", "");
            if (userEmail.isEmpty()) {
                Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            userId = dbHelper.getUserId(userEmail);
            if (userId == -1) {
                Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            // Initialize Views
            initializeViews();
            setupListeners();

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
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

            if (backButton == null || cartButton == null || addToCartButton == null) {
                throw new IllegalStateException("Required views not found");
            }

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

        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupListeners() {
        try {
            setFlavorRestrictions();
            setRealTimeListeners();

            addToCartButton.setOnClickListener(v -> {
                if (userId != -1) {
                    if (validateSelections()) {
                        saveOrderToDatabase();
                    }
                } else {
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateSelections() {
        if (sizeRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedFlavors.isEmpty()) {
            Toast.makeText(this, "Please select at least one flavor", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
            if (checkBox == null) continue;

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                try {
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
                } catch (Exception e) {
                    Toast.makeText(this, "Error handling flavor selection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setRealTimeListeners() {
        try {
            sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateTotalPrice());
            checkSprinkles.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
            checkChocoChips.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
            checkCaramel.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
            checkNuts.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up real-time listeners", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPrice() {
        try {
            totalPrice = 0;

            // Calculate size price
            int selectedSizeId = sizeRadioGroup.getCheckedRadioButtonId();
            if (selectedSizeId != -1) {
                RadioButton selectedSize = findViewById(selectedSizeId);
                if (selectedSize != null) {
                    String sizeText = selectedSize.getText().toString();
                    if (sizeText.contains("10")) {
                        totalPrice += 10;
                    } else if (sizeText.contains("15")) {
                        totalPrice += 15;
                    } else if (sizeText.contains("20")) {
                        totalPrice += 20;
                    }
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
        } catch (Exception e) {
            Toast.makeText(this, "Error updating price", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveOrderToDatabase() {
        try {
            // Create customizations string
            StringBuilder customizations = new StringBuilder();

            // Add selected size
            int selectedSizeId = sizeRadioGroup.getCheckedRadioButtonId();
            if (selectedSizeId != -1) {
                RadioButton selectedSize = findViewById(selectedSizeId);
                if (selectedSize != null) {
                    customizations.append("Size: ").append(selectedSize.getText()).append("\n");
                }
            }

            // Add selected flavors
            if (!selectedFlavors.isEmpty()) {
                customizations.append("Flavors: ");
                for (int i = 0; i < selectedFlavors.size(); i++) {
                    CheckBox flavor = selectedFlavors.get(i);
                    if (flavor != null) {
                        customizations.append(flavor.getText());
                        if (i < selectedFlavors.size() - 1) {
                            customizations.append(", ");
                        }
                    }
                }
                customizations.append("\n");
            }

            // Add selected toppings
            ArrayList<String> toppings = new ArrayList<>();
            if (checkSprinkles.isChecked()) toppings.add("Sprinkles");
            if (checkChocoChips.isChecked()) toppings.add("Chocolate Chips");
            if (checkCaramel.isChecked()) toppings.add("Caramel Syrup");
            if (checkNuts.isChecked()) toppings.add("Nuts");

            if (!toppings.isEmpty()) {
                customizations.append("Toppings: ");
                for (int i = 0; i < toppings.size(); i++) {
                    customizations.append(toppings.get(i));
                    if (i < toppings.size() - 1) {
                        customizations.append(", ");
                    }
                }
            }

            // First add item to Menu if it doesn't exist
            long menuItemId = dbHelper.getCustomIceCreamId();
            if (menuItemId == -1) {
                // Add custom ice cream to menu if it doesn't exist
                menuItemId = dbHelper.addMenuItem("Custom Ice Cream", totalPrice,
                        "Customized ice cream", null);
                if (menuItemId == -1) {
                    throw new Exception("Failed to create menu item");
                }
            }

            // Add to cart with the formatted customizations
            boolean success = dbHelper.addToCart(userId, (int)menuItemId, 1, customizations.toString()) != -1;
            if (!success) {
                throw new Exception("Failed to add item to cart");
            }

            Toast.makeText(this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
            clearSelections();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving order: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearSelections() {
        try {
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
        } catch (Exception e) {
            Toast.makeText(this, "Error clearing selections", Toast.LENGTH_SHORT).show();
        }
    }
}