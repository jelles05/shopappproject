package com.example.boutiqueproject.managers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.services.ConnectionDb;

import java.util.ArrayList;
public class ProductsManager {
    /**
     * Get a list of all products of a Subcategory
     * @param context : context from method is called
     * @param searchId : Id of the subcategory or category to filter
     * @param searchType : type of search (by category or subcategory)
     * @return : List of Products Object
     */
    public static ArrayList<Products> getAll(Context context, int searchId, String searchType) {
        ArrayList<Products> listData = null;
        try {
            String query = "SELECT * FROM Products WHERE " + (searchType.equals("category") ? "categoryId" : "subCategoryId") + " = '" + searchId + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                listData = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Products e = new Products(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("description")),
                            cursor.getInt(cursor.getColumnIndex("categoryId")),
                            cursor.getInt(cursor.getColumnIndex("subCategoryId")),
                            cursor.getString(cursor.getColumnIndex("image")),
                            cursor.getDouble(cursor.getColumnIndex("price")),
                            cursor.getInt(cursor.getColumnIndex("quantity")),
                            cursor.getString(cursor.getColumnIndex("mostSale"))
                    );
                    listData.add(e);
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    /**
     * Get a product by its id number
     * @param context : context from method is called
     * @param id : Id of the product to filter
     * @return : Products Object
     */
    public static Products getById(Context context, int id) {
        Products retour = null;
        try {
            String query = "SELECT * FROM Products WHERE id = '" + id + "'";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    retour = new Products(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("description")),
                            cursor.getInt(cursor.getColumnIndex("categoryId")),
                            cursor.getInt(cursor.getColumnIndex("subCategoryId")),
                            cursor.getString(cursor.getColumnIndex("image")),
                            cursor.getDouble(cursor.getColumnIndex("price")),
                            cursor.getInt(cursor.getColumnIndex("quantity")),
                            cursor.getString(cursor.getColumnIndex("mostSale"))
                    );
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    /**
     * Get a product by its id number
     * @param context : context from method is called
     * @param name : name to filter the products
     * @return : Products ArrayList
     */
    public static ArrayList<Products> getByName(Context context, String name) {
        ArrayList<Products> products = null;
        try {
            String query = "SELECT * FROM Products WHERE name LIKE ?";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, new String[]{"%" + name + "%"});
            if (cursor.isBeforeFirst()) {
                products = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Products p = new Products(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("description")),
                            cursor.getInt(cursor.getColumnIndex("categoryId")),
                            cursor.getInt(cursor.getColumnIndex("subCategoryId")),
                            cursor.getString(cursor.getColumnIndex("image")),
                            cursor.getDouble(cursor.getColumnIndex("price")),
                            cursor.getInt(cursor.getColumnIndex("quantity")),
                            cursor.getString(cursor.getColumnIndex("mostSale"))
                    );
                    products.add(p);
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Creates a new product
     * @param context : context from method is called
     * @param data : I
     * @return : !0 if the product was created
     */
    public static boolean addProduct(Context context, Products data) {
        long retour = 0;
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(context);
            //inserer
            ContentValues cv = new ContentValues();
            //
            cv.put("name", data.getName());
            cv.put("description", data.getDescription());
            cv.put("categoryId", data.getCategoryId());
            cv.put("subCategoryId", data.getSubCategoryId());
            cv.put("image", data.getImage());
            cv.put("price", data.getPrice());
            cv.put("quantity", data.getQuantity());
            cv.put("mostSale", data.getMostSale());
            //
            retour = bd.insert("Products", null, cv);
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != -1;
    }
    /**
     * Update the information of a product
     * @param context : context from method is called
     * @param data : Information os the product to update
     */
    public static void updateProduct(Context context, Products data) {
        try {
            ContentValues value = new ContentValues();
            //
            value.put("name", data.getName());
            value.put("description", data.getDescription());
            value.put("categoryId", data.getCategoryId());
            value.put("subCategoryId", data.getSubCategoryId());
            value.put("image", data.getImage());
            value.put("price", data.getPrice());
            value.put("quantity", data.getQuantity());
            value.put("mostSale", data.getMostSale());
            //
            SQLiteDatabase bd = ConnectionDb.getBd(context);
            //
            bd.update("Products", value, "id = ?", new String[]{String.valueOf(data.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Delete a product from the table
     * @param context : context from method is called
     * @param id : Id of the product to delete
     */
    public static void deleteProduct(Context context, int id) {
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(context);
            //
            if ( id == 0 ){
                bd.delete("Products", "id <> ?", new String[]{String.valueOf(id)});
            }
            else {
                bd.delete("Products", "id = ?", new String[]{String.valueOf(id)});
            }
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
