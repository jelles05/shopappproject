package com.example.boutiqueproject.managers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.services.ConnectionDb;

import java.util.ArrayList;
public class UsersManager {
    /**
     * Check is an user exists in the table
     * @param context : context from method is called
     * @param email : Email to validate
     * @param password : Password to validate
     * @return : An User object with data if is an valid user. Otherwise return an null User Object.
     */
    public static Users checkUser(Context context, String email, String password) {
        Users userRetour = null;
        try {
            String query = "select * FROM Users WHERE email = '" + email + "' ";
            query += " AND password = '" + password + "' ";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    userRetour = new Users(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("firstName")),
                            cursor.getString(cursor.getColumnIndex("lastName")),
                            cursor.getString(cursor.getColumnIndex("email")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("city")),
                            cursor.getString(cursor.getColumnIndex("isAdmin")),
                            cursor.getString(cursor.getColumnIndex("postalCode")),
                            cursor.getString(cursor.getColumnIndex("province"))
                    );
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userRetour;
    }
    /**
     * Crea an user in the table
     * @param context : context from method is called
     * @param data : User data
     * @return : !0 id users was created
     */
    public static boolean addUser(Context context, Users data) {
        long retour = 0;
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(context);
            //inserer
            ContentValues cv = new ContentValues();
            //

            cv.put("password", data.getPassword());
            cv.put("firstName", data.getFirstName());
            cv.put("lastName", data.getLastName());
            cv.put("email", data.getEmail());
            cv.put("address", data.getAddress());
            cv.put("city", data.getCity());
            cv.put("isAdmin", data.getIsAdmin());
            cv.put("postalCode", data.getPostalCode());
            cv.put("province", data.getProvince());
            //
            retour = bd.insert("Users", null, cv);
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != -1;
    }
    /**
     * Update an user in the table
     * @param context : context from method is called
     * @param data : User Data
     */
    public static void updateUser(Context context, Users data) {
        try {
            ContentValues value = new ContentValues();
            //

            value.put("password", data.getPassword());
            value.put("firstName", data.getFirstName());
            value.put("lastName", data.getLastName());
            value.put("email", data.getEmail());
            value.put("address", data.getAddress());
            value.put("city", data.getCity());
            value.put("isAdmin", data.getIsAdmin());
            value.put("postalCode", data.getPostalCode());
            value.put("province", data.getProvince());
            //
            SQLiteDatabase bd = ConnectionDb.getBd(context);
            //
            bd.update("Users", value, "id = ?", new String[]{String.valueOf(data.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Validate by the user Id, if the user exists in the table
     * @param context : context from method is called
     * @param id : Id of the user to validate
     * @return : An Users Object with the users data
     */
    public static Users getUserById(Context context, int id) {
       Users data = new Users();
        try {
            String query = "SELECT * FROM Users WHERE id = '" + id + "' ";
            //
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    data = new Users(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("firstName")),
                            cursor.getString(cursor.getColumnIndex("lastName")),
                            cursor.getString(cursor.getColumnIndex("email")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("city")),
                            cursor.getString(cursor.getColumnIndex("isAdmin")),
                            cursor.getString(cursor.getColumnIndex("postalCode")),
                            cursor.getString(cursor.getColumnIndex("province"))
                    );
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Validate by the user email, if the user exists in the table
     * @param context : context from method is called
     * @param id : Id of the user to validate
     * @return : An Users Object with the users data
     */
    public static Users getUserByEmail(Context context, String email) {
        Users data = new Users();
        try {
            String query = "SELECT * FROM Users WHERE email = '" + email + "' ";
            //
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    data = new Users(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("firstName")),
                            cursor.getString(cursor.getColumnIndex("lastName")),
                            cursor.getString(cursor.getColumnIndex("email")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("city")),
                            cursor.getString(cursor.getColumnIndex("isAdmin")),
                            cursor.getString(cursor.getColumnIndex("postalCode")),
                            cursor.getString(cursor.getColumnIndex("province"))
                    );
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<Users> getAll(Context context) {
        ArrayList<Users> listData = null;
        try {
            String query = "SELECT * FROM Users";
            Cursor cursor = ConnectionDb.getBd(context).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                listData = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Users e = new Users(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("firstName")),
                            cursor.getString(cursor.getColumnIndex("lastName")),
                            cursor.getString(cursor.getColumnIndex("email")),
                            cursor.getString(cursor.getColumnIndex("address")),
                            cursor.getString(cursor.getColumnIndex("city")),
                            cursor.getString(cursor.getColumnIndex("isAdmin")),
                            cursor.getString(cursor.getColumnIndex("postalCode")),
                            cursor.getString(cursor.getColumnIndex("province"))
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






}
