package com.example.boutiqueproject;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.boutiqueproject.adapters.ProductAdapter;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.managers.ProductsManager;
import java.util.ArrayList;
public class SearchProductActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    EditText etSearch;
    TextView tvInvalidSearch, tvNoItemsFound;
    GridView gvProducts;
    ImageView imgSearchIcon;
    ArrayList<Products> products;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etSearch = findViewById(R.id.etSearch);
        tvInvalidSearch = findViewById(R.id.tvInvalidSearch);
        tvNoItemsFound = findViewById(R.id.tvNoItemsFound);
        gvProducts = findViewById(R.id.gvProducts);
        imgSearchIcon = findViewById(R.id.imgSearchIcon);
    }

    public void handleBtnSearchClick(View view) {
        String searchString = etSearch.getText().toString();
        // CHECK IF INPUT IS EMPTY
        if (searchString.isEmpty()) {
            tvInvalidSearch.setVisibility(View.VISIBLE);
        } else {
            tvInvalidSearch.setVisibility(View.GONE);
            products = ProductsManager.getByName(ctx, searchString);
            // CHECK IF PRODUCT LIST IS EMPTY
            if (products == null || products.size() == 0) {
                imgSearchIcon.setVisibility(View.GONE);
                tvNoItemsFound.setVisibility(View.VISIBLE);
            } else {
                imgSearchIcon.setVisibility(View.GONE);
                tvNoItemsFound.setVisibility(View.GONE);
                productAdapter = new ProductAdapter(ctx, R.layout.product_view_layout, products);
                gvProducts.setAdapter(productAdapter);
                gvProducts.setVisibility(View.VISIBLE);
            }
        }
    }
}