package com.example.boutiqueproject.managers;
import android.content.Context;
import android.database.Cursor;

import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.entities.SubCategories;
import com.example.boutiqueproject.services.ConnectionDb;

import java.util.ArrayList;
public class SubCategoriesManager {

    /**
     * Gets the SubCategories list
     * @param context : context from method is called
     * @param categoryId : Id of the category to search
     * @return A list of SubCategories
     */
    public static ArrayList<SubCategories> getAll(Context context, int categoryId) {
        ArrayList<SubCategories> listData = null;
        try {
            String query = "SELECT * FROM SubCategories WHERE categoryId = '" + categoryId + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                listData = new ArrayList<>();
                while (cursor.moveToNext()) {
                    SubCategories e = new SubCategories(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("categoryId")),
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



    /**
     * Gets the SubCategories list by the category Name
     * @param context : context from method is called
     * @param categoryName :The category's name to search
     * @return A list of SubCategories
     */
    public static ArrayList<SubCategories> getAllByCategoryName(Context context, String categoryName) {
        ArrayList<SubCategories> listData = null;
        try {
            String query = "SELECT SubCategories.* FROM SubCategories ";
            query += " INNER JOIN Categories ON SubCategories.categoryId = Categories.Id ";
            query += " WHERE Categories.name = '" + categoryName + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                listData = new ArrayList<>();
                while (cursor.moveToNext()) {
                    SubCategories e = new SubCategories(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("categoryId")),
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
    /**
     * Gets the SubCategory's Id with the name on the subcategory
     * @param context : context from method is called
     * @param categoryId : Category's Id
     * @param name SubCategory's name
     * @return SubCategory's Id
     */
    public static int getIdSubCategoryByName(Context context, int categoryId, String name) {
        int subCategoryId = 0;
        try {
            String query = "SELECT * FROM SubCategories WHERE categoryId = '" + categoryId + "' ";
            query += " AND name = '" + name + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    subCategoryId = cursor.getInt(cursor.getColumnIndex("id"));
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subCategoryId;
    }


}
