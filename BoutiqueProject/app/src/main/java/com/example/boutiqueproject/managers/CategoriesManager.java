package com.example.boutiqueproject.managers;
import android.content.Context;
import android.database.Cursor;

import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.services.ConnectionDb;

import java.util.ArrayList;
public class CategoriesManager {
    /**
     * Gets the Categories list
     * @param context : context from method is called
     * @return A list of Categories
     */
    public static ArrayList<Categories> getAll(Context context) {
        ArrayList<Categories> listData = null;
        try {
            String query = "SELECT * FROM Categories";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                listData = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Categories e = new Categories(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name"))
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
    /***
     * Gets the category id by the name
     * @param context context from method is called
     * @param name category's name to search
     * @return Category's Id
     */
    public static int  getIdCategoryByName(Context context, String name) {
        int idCategory = 0 ;
        try {
            String query = "SELECT * FROM Categories WHERE name = '" + name + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    idCategory =cursor.getInt(cursor.getColumnIndex("id"));
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idCategory;
    }



}
