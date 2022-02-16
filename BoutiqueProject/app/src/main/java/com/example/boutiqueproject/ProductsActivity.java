package com.example.boutiqueproject;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import com.example.boutiqueproject.adapters.ProductAdapter;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.managers.ProductsManager;
import java.util.ArrayList;
public class ProductsActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    GridView gvProducts;
    Intent intent;
    ArrayList<Products> products;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gvProducts = findViewById(R.id.gvProducts);
        intent = getIntent();
        int searchId = intent.getIntExtra("searchId", -1);
        String searchType = intent.getStringExtra("searchType");
        products = ProductsManager.getAll(ctx, searchId, searchType);

        productAdapter = new ProductAdapter(ctx, R.layout.product_view_layout, products);
        gvProducts.setAdapter(productAdapter);
    }
}