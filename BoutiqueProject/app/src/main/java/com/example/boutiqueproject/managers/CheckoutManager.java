package com.example.boutiqueproject.managers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.boutiqueproject.entities.OrderDetail;
import com.example.boutiqueproject.entities.Orders;
import com.example.boutiqueproject.entities.ShoppingCartItem;
import com.example.boutiqueproject.services.ConnectionDb;
import java.util.ArrayList;
public class CheckoutManager {

    public static Orders createNewOrder(Context ctx, int userId, double total, ArrayList<ShoppingCartItem> shoppingCartItems) {
        // CALCULATE TAX
        double tax = (total * 0.05) + (total * 0.09975);
        // CREATE ORDER
        addNewOrder(ctx, new Orders(1, userId, total, tax, "date"));
        Orders order = getOrder(ctx, userId, total);
        // GET SHOPPING CART ITEMS
        for (ShoppingCartItem i : shoppingCartItems) {
            // ADD PRODUCT TO ORDER DETAILS
            addOrderDetail(ctx, new OrderDetail(1, i.getIdProduit(), order.getId(), i.getQuantity(), i.getSize()));
        }
        return order;
    }

    // CREATE A NEW ORDER ON THE DB
    public static boolean addNewOrder(Context ctx, Orders order) {
        long retour = 0;
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            //inserer
            ContentValues cv = new ContentValues();
            //
            cv.put("userId", order.getUserId());
            cv.put("amount", order.getAmount());
            cv.put("tax", order.getTax());
            //
            retour = bd.insert("Orders", null, cv);
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != -1;
    }

    // GET AN ORDER BY USER ID AND ORDER TOTAL
    public static Orders getOrder(Context ctx, int userId, double total) {
        Orders retour = null;
        try {
            String query = "SELECT * FROM Orders WHERE userId = '" + userId + "' AND amount = '" + total + "'";
            Cursor cursor = ConnectionDb.getBd(ctx).rawQuery(query, null);
            if (cursor.isBeforeFirst()) {
                while (cursor.moveToNext()) {
                    retour = new Orders(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getInt(cursor.getColumnIndex("userId")),
                            cursor.getDouble(cursor.getColumnIndex("amount")),
                            cursor.getDouble(cursor.getColumnIndex("tax")),
                            cursor.getString(cursor.getColumnIndex("date"))
                    );
                }
            }
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour;
    }

    // ADD NEW ORDER DETAIL TO DB
    public static boolean addOrderDetail(Context ctx, OrderDetail orderDetail) {
        long retour = 0;
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            //inserer
            ContentValues cv = new ContentValues();
            //
            cv.put("productId", orderDetail.getProductId());
            cv.put("orderId", orderDetail.getOrderId());
            cv.put("quantity", orderDetail.getQuantity());
            cv.put("size", orderDetail.getSize());
            //
            retour = bd.insert("OrderDetails", null, cv);
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != -1;
    }

    public static boolean updateOrderDate(Context ctx, Orders order) {
        long retour = 0;
        try{
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            ContentValues cv = new ContentValues();
            cv.put("date", order.getDate());
            retour = bd.update("Orders", cv, "id = ?", new  String[]{String.valueOf(order.getId())});
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retour != -1;
    }

    public static void deleteOrder(Context ctx, int id) {
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            bd.delete("Orders", "id = ?", new String[]{String.valueOf(id)});
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderDetails(Context ctx, int idOrder) {
        try {
            SQLiteDatabase bd = ConnectionDb.getBd(ctx);
            bd.delete("OrderDetails", "orderId = ?", new String[]{String.valueOf(idOrder)});
            ConnectionDb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}