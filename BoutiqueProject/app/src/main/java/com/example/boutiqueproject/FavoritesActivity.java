package com.example.boutiqueproject;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.example.boutiqueproject.adapters.ProductAdapter;
import com.example.boutiqueproject.entities.Favorites;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.managers.FavoritesManager;
import com.example.boutiqueproject.managers.ProductsManager;
import java.util.ArrayList;
public class FavoritesActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    GridView gvProducts;
    LinearLayout llNoFavorites;
    ProductAdapter productAdapter;
    SharedPreferences preferences;
    ArrayList<Favorites> favorites;
    ArrayList<Products> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        gvProducts = findViewById(R.id.gvProducts);
        llNoFavorites = findViewById(R.id.llNoFavorites);
        favorites = FavoritesManager.getByUserId(ctx, preferences.getInt("ShopAppUserId", -1));
        products = new ArrayList<>();

        // GET PRODUCTS FROM FAVORITES LIST
        for (Favorites f : favorites) {
            products.add(ProductsManager.getById(ctx, f.getProductId()));
        }
        productAdapter = new ProductAdapter(ctx, R.layout.product_view_layout, products);
        gvProducts.setAdapter(productAdapter);

        // CHECK IF FAVORITES LIST IS EMPTY
        if (favorites == null || favorites.size() == 0) {
            llNoFavorites.setVisibility(View.VISIBLE);
            gvProducts.setVisibility(View.GONE);
        } else {
            gvProducts.setVisibility(View.VISIBLE);
            llNoFavorites.setVisibility(View.GONE);
        }
    }
}