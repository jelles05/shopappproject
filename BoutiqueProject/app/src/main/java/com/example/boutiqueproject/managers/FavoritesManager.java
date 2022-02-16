package com.example.boutiqueproject.managers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.boutiqueproject.entities.Favorites;
import com.example.boutiqueproject.services.ConnectionDb;
import java.util.ArrayList;
public class FavoritesManager {

    // GET FAVORITES BY USER ID
    public static ArrayList<Favorites> getByUserId(Context ctx, int userId) {
        ArrayList<Favorites> favorites = null;
        try {
            String query = "SELECT * FROM Favorites WHERE userId = ?";
            Cursor cursor = ConnectionDb.getBd(ctx).rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor.isBeforeFirst()) {
                favorites = new ArrayList<>();
                while (cursor.moveToNext()) {
                    Favorites f = new Favorites(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("productId")),
                            cursor.getInt(cursor.getColumnIndex("userId"))
                    );
                    favorites.add(f);
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favorites;
    }

    // ADD PRODUCT TO FAVORITES TABLE
    public static boolean addToFavorites(Context ctx, Favorites favorite) {
        long retour = -1;
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            ContentValues cv = new ContentValues();
            cv.put("productId", favorite.getProductId());
            cv.put("userId", favorite.getUserId());
            retour = bd.insert("Favorites", null, cv);
            ConnectionDb.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return retour != -1;
    }

    // ERASE PRODUCT FROM FAVORITES TABLE
    public static void deleteFromFavorites(Context ctx, int id) {
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            bd.delete("Favorites", "id = ?", new String[]{String.valueOf(id)});
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND IF PRODUCT IS IN USERS FAVORITES
    public static boolean isProductInFavorites(Context ctx, int userId, int productId) {
        boolean retour = false;
        ArrayList<Favorites> favorites = getByUserId(ctx, userId);
        for (Favorites f : favorites) {
            if (f.getProductId() == productId) {
                retour = true;
            }
        }
        return retour;
    }

}