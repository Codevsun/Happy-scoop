package com.example.icecreamshopsignin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "happyscoop";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE allusers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL);";

    private static final String CREATE_MENU_TABLE =
            "CREATE TABLE Menu (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "description TEXT, " +
                    "image_resource TEXT" +
                    ");";

    private static final String CREATE_CART_TABLE =
            "CREATE TABLE Cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "menu_item_id INTEGER NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "customizations TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES allusers (id), " +
                    "FOREIGN KEY (menu_item_id) REFERENCES Menu (id));";

    private static final String CREATE_ORDERS_TABLE =
            "CREATE TABLE Orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "total_price REAL NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES allusers (id));";

    private static final String CREATE_ORDER_ITEMS_TABLE =
            "CREATE TABLE OrderItems (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "order_id INTEGER NOT NULL, " +
                    "menu_item_id INTEGER NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "customizations TEXT, " +
                    "FOREIGN KEY (order_id) REFERENCES Orders (id), " +
                    "FOREIGN KEY (menu_item_id) REFERENCES Menu (id));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MENU_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS OrderItems");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Menu");
        db.execSQL("DROP TABLE IF EXISTS allusers");
        onCreate(db);
    }

    // User Management
    public boolean insertData(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("allusers", null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select id from allusers where email = ?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public long addMenuItem(String name, double price, String description,String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", description);
        values.put("image", image);
        return db.insert("Menu", null, values);
    }

    public Cursor getAllMenuItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Menu WHERE name != 'Custom Ice Cream' ORDER BY id";
        return db.rawQuery(query, null);
    }
    public long getCustomIceCreamId() {
        SQLiteDatabase db = this.getReadableDatabase();
        long id = 1; // Default ID if not found

        Cursor cursor = db.rawQuery("SELECT id FROM Menu WHERE name = 'Custom Ice Cream' LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }
        return id;
    }


    public long addToCart(int userId, int menuItemId, int quantity, String customizations) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("menu_item_id", menuItemId);
        values.put("quantity", quantity);
        values.put("customizations", customizations);
        return db.insert("Cart", null, values);
    }

    public Cursor getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Cart.*, Menu.name, Menu.price FROM Cart " +
                "INNER JOIN Menu ON Cart.menu_item_id = Menu.id " +
                "WHERE Cart.user_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "user_id = ?", new String[]{String.valueOf(userId)});
    }

    public long createOrder(int userId, String date, double totalPrice, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("date", date);
        values.put("total_price", totalPrice);
        values.put("status", status);
        return db.insert("Orders", null, values);
    }

    public long addOrderItem(int orderId, int menuItemId, int quantity, String customizations) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", orderId);
        values.put("menu_item_id", menuItemId);
        values.put("quantity", quantity);
        values.put("customizations", customizations);
        return db.insert("OrderItems", null, values);
    }

    public Cursor getUserOrders(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("Orders", null, "user_id = ?",
                new String[]{String.valueOf(userId)}, null, null, "date DESC");
    }

    public Cursor getOrderItems(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT OrderItems.id, OrderItems.menu_item_id, OrderItems.quantity, " +
                "OrderItems.customizations, Menu.name, Menu.price " +
                "FROM OrderItems " +
                "INNER JOIN Menu ON OrderItems.menu_item_id = Menu.id " +
                "WHERE OrderItems.order_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId)});
    }

    public void updateCartItemQuantity(int userId, int menuItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        db.update("Cart", values, "user_id = ? AND menu_item_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(menuItemId)});
    }
    public boolean isCartEmpty(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Cart WHERE user_id = ?",
                new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        cursor.close();
        return true;
    }
    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;

        Cursor cursor = db.query(
                "allusers",  // table name
                new String[]{"id"},  // columns
                "email = ?",  // selection
                new String[]{email},  // selection args
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
        }

        return userId;
    }
    public void removeCartItem(int userId, int menuItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart",
                "user_id = ? AND menu_item_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(menuItemId)});
    }

    public double getCartTotal(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0.0;

        String query = "SELECT SUM(Menu.price * Cart.quantity) as total FROM Cart " +
                "INNER JOIN Menu ON Cart.menu_item_id = Menu.id " +
                "WHERE Cart.user_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            cursor.close();
        }

        return total;
    }

}