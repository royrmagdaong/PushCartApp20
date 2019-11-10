package com.example.pushcartapp20;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;
import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context){
        this.openHelper = new SQLiteHelper(context);
    }

    public static DatabaseAccess getInstance(Context context){
        if (instance==null){
            instance = new DatabaseAccess(context);

        }
        return instance;
    }

    public void open(){
        this.db = openHelper.getWritableDatabase();
    }

    public void close(){
        if (db!=null){
            this.db.close();
        }
    }

    public ArrayList<GroceryItem> getData(){
        ArrayList<GroceryItem> groceryItems = new ArrayList<>();
        c = db.rawQuery("SELECT * FROM Product ORDER BY Id ASC",new String[]{});

        while (c.moveToNext()){
            groceryItems.add(new GroceryItem(Integer.parseInt(c.getString(0)),c.getString(1),Float.parseFloat(c.getString(2)),
                    Integer.parseInt(c.getString(3)),c.getString(4),c.getString(5),c.getBlob(6),Float.parseFloat(c.getString(7)),Float.parseFloat(c.getString(8))));
        }

        return groceryItems;
    }

    public ArrayList<String> getProductNames(){
        ArrayList<String> names = new ArrayList<>();
        c = db.rawQuery("SELECT * FROM Product ORDER BY product_name ASC",new String[]{});

        while (c.moveToNext()){
            names.add(c.getString(1));
        }

        return names;
    }

    public void insertCartItems(int prodId,String name, String price, int quantity, String category,float discount,float promo){
        String query = String.format("INSERT INTO cart_items (id,product_name,price,quantity,category,discount,promo) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                prodId,
                name,
                price,
                quantity,
                category,
                discount,
                promo
                );
        db.execSQL(query);
    }

    public ArrayList<CartItem> getCartItems(){
        ArrayList<CartItem> items = new ArrayList<>();
        c = db.rawQuery("SELECT * FROM cart_items ORDER BY item_no asc",new String[]{});

        while (c.moveToNext()){
            items.add(new CartItem(c.getString(1),Integer.parseInt(c.getString(0)),Integer.parseInt(c.getString(3)),
                    Float.parseFloat(c.getString(2)),c.getString(4),Float.parseFloat(c.getString(6)),Float.parseFloat(c.getString(7))));
        }

        return items;
    }

    public void deleteCartItems(){
        db.execSQL("DELETE FROM cart_items");
    }

    public void removeItemFromCart(int id){
        db.execSQL("DELETE FROM cart_items WHERE id = "+id);
    }

    public void updateQuantityFromCartItem(int id, int quantity){
        db.execSQL("UPDATE cart_items SET quantity = "+quantity+" WHERE id = "+id);
    }
}
