package com.example.icecreamshopsignin;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MakeIceCream extends AppCompatActivity {

    private RadioGroup sizeRadioGroup;
    private CheckBox checkChocolate, checkVanilla, checkStrawberry, checkOreo, checkLemon, checkMint;
    private CheckBox checkSprinkles, checkChocoChips, checkCaramel, checkNuts;
    private Button addToCartButton;
    private ArrayList<CheckBox> selectedFlavors = new ArrayList<>();
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_ice_cream);
        // Initialize Views
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

        // Replace it with Ftoom Cart interface
//        cartButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MakeIceCream.this, CartActivity.class);
//            startActivity(intent);
//        });


        setFlavorRestrictions();
        setRealTimeListeners();

        // Add to Cart Functionality
        addToCartButton.setOnClickListener(v -> {
            saveDataToDatabase();
            Toast.makeText(this, "Order added to cart!", Toast.LENGTH_SHORT).show();
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
        // Listener for Size Selection
        sizeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> updateTotalPrice());

        // Listener for Toppings
        checkSprinkles.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkChocoChips.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkCaramel.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
        checkNuts.setOnCheckedChangeListener((buttonView, isChecked) -> updateTotalPrice());
    }

    private void updateTotalPrice() {
        totalPrice = 0;

        // Get Size Price
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

        // Get Flavors Price
        totalPrice += selectedFlavors.size() * 5;

        // Get Toppings Price
        if (checkSprinkles.isChecked()) totalPrice += 2;
        if (checkChocoChips.isChecked()) totalPrice += 2;
        if (checkCaramel.isChecked()) totalPrice += 3;
        if (checkNuts.isChecked()) totalPrice += 3;

        // Update Button Text with Total Price
        addToCartButton.setText("Add to Cart - Total: " + totalPrice + " SAR");
    }

    private void saveDataToDatabase() {
        SQLiteOpenHelper dbHelper = new com.example.icecreamapp.DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        StringBuilder flavors = new StringBuilder();
        for (CheckBox flavor : selectedFlavors) {
            flavors.append(flavor.getText().toString()).append(", ");
        }

        String toppings = "";
        if (checkSprinkles.isChecked()) toppings += "Sprinkles, ";
        if (checkChocoChips.isChecked()) toppings += "Chocolate Chips, ";
        if (checkCaramel.isChecked()) toppings += "Caramel Syrup, ";
        if (checkNuts.isChecked()) toppings += "Nuts, ";

        db.execSQL("INSERT INTO Orders (size, flavors, toppings, total_price) VALUES (?, ?, ?, ?)",
                new Object[]{
                        ((RadioButton) findViewById(sizeRadioGroup.getCheckedRadioButtonId())).getText().toString(),
                        flavors.toString(),
                        toppings,
                        totalPrice
                });


    }
}


