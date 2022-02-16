package com.example.boutiqueproject;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.boutiqueproject.adapters.ShoppingCartItemAdapter;
import com.example.boutiqueproject.entities.Orders;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.entities.ShoppingCartItem;
import com.example.boutiqueproject.managers.CheckoutManager;
import com.example.boutiqueproject.managers.ProductsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
public class ShoppingCartActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tvNoItems, tvTotal;
    LinearLayout llProducts;
    ListView lvShoppingCartItems;
    double total;
    ArrayList<ShoppingCartItem> shoppingCart;
    ShoppingCartItemAdapter shoppingCartItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        tvNoItems = findViewById(R.id.tvNoItems);
        tvTotal = findViewById(R.id.tvTotal);
        llProducts = findViewById(R.id.llProducts);
        lvShoppingCartItems = findViewById(R.id.lvShoppingCartItems);
        editor.remove("cartTotal");
        editor.commit();
        total = 0;

        String shoppingCartJson = preferences.getString("shopping", "NA");
        // CHECK IF SHOPPING CART EXISTS
        if (shoppingCartJson.equals("NA")) {
            tvNoItems.setVisibility(View.VISIBLE);
        } else {
            llProducts.setVisibility(View.VISIBLE);
            Type type = new TypeToken<ArrayList<ShoppingCartItem>>(){}.getType();
            shoppingCart = new Gson().fromJson(shoppingCartJson, type);
            shoppingCartItemAdapter = new ShoppingCartItemAdapter(ctx, R.layout.cart_product_layout, shoppingCart);
            lvShoppingCartItems.setAdapter(shoppingCartItemAdapter);
            // GET ORDER TOTAL
            for (ShoppingCartItem i : shoppingCart) {
                Products p = ProductsManager.getById(ctx, i.getIdProduit());
                total += i.getQuantity() * p.getPrice();
            }
            setTvTotalText(total);
        }
    }

    public void handleCheckoutBtnClick(View view) {
        int userId = preferences.getInt("ShopAppUserId", -1);
        if (userId != -1) {
            Orders newOrder = CheckoutManager.createNewOrder(ctx, userId, total, shoppingCart);
            // GO TO CHECKOUT
            Intent intent = new Intent(ctx, CheckoutActivity.class);
            intent.putExtra("order", new Gson().toJson(newOrder));
            startActivity(intent);
        }
    }

    public void handleClearCartBtnClick(View view) {
        editor.remove("shopping");
        editor.commit();
        setVisibilityNoItems();
    }

    public void setTvTotalText(double total) {
        tvTotal.setText("$" + ((double) Math.round(total * 100) / 100));
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setVisibilityNoItems() {
        llProducts.setVisibility(View.GONE);
        tvNoItems.setVisibility(View.VISIBLE);
    }
}