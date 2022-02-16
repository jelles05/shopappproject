package com.example.boutiqueproject;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.boutiqueproject.entities.Favorites;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.entities.ShoppingCartItem;
import com.example.boutiqueproject.managers.FavoritesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
public class ProductDetailActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    Intent intent;
    Products product;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout llProductDetail, llSpinner;
    RelativeLayout rlProduct;
    ImageView imgProduct, imgFavorite;
    TextView tvProductName, tvProductPrice, tvDescription;
    Spinner spinnerSize;
    ImageButton btnAdd;
    int quantityProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        product = new Gson().fromJson(intent.getStringExtra("product"), Products.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        quantityProduct = 1;
        llProductDetail = findViewById(R.id.llProductDetail);
        rlProduct = (RelativeLayout) getLayoutInflater().inflate(R.layout.product_view_layout, null);
        imgProduct = rlProduct.findViewById(R.id.imgProduct);
        imgFavorite = rlProduct.findViewById(R.id.imgFavorite);
        tvProductName = rlProduct.findViewById(R.id.tvProductName);
        tvProductPrice = rlProduct.findViewById(R.id.tvProductPrice);
        tvDescription = rlProduct.findViewById(R.id.tvDescription);
        llSpinner = rlProduct.findViewById(R.id.llSpinner);
        spinnerSize = rlProduct.findViewById(R.id.spinnerSize);
        btnAdd = rlProduct.findViewById(R.id.btnAdd);

        // SET PRODUCT IMAGE
        imgProduct.getLayoutParams().height = 980;
        try {
            imgProduct.setImageDrawable(Drawable.createFromStream(ctx.getAssets().open(product.getImage()), null));
        } catch (IOException e) {
            e.printStackTrace();
            // Is an exception is thrown , validates if the product has a image path
            if (product.getImage().length() > 0){
                // Loads image from the path
                imgProduct.setImageBitmap(BitmapFactory.decodeFile(product.getImage()));
            }
        }

        // GET USER ID
        int userId = preferences.getInt("ShopAppUserId", -1);
        // CHECK IF PRODUCT IS IN USER'S FAVORITES
        if (FavoritesManager.isProductInFavorites(ctx, userId, product.getId())) {
            imgFavorite.setTag("added");
            imgFavorite.setImageResource(R.drawable.added_to_favorites);
        }

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("add")) {
                    imgFavorite.setImageResource(R.drawable.added_to_favorites);
                    view.setTag("added");
                    // ADD PRODUCT TO FAVORITES
                    if (!FavoritesManager.isProductInFavorites(ctx, userId, product.getId())) {
                        FavoritesManager.addToFavorites(ctx, new Favorites(1, product.getId(), userId));
                    }
                } else {
                    imgFavorite.setImageResource(R.drawable.add_to_favorites);
                    view.setTag("add");
                    // REMOVE PRODUCT FROM FAVORITES
                    int favoriteId = -1;
                    ArrayList<Favorites> favoritesList = FavoritesManager.getByUserId(ctx, userId);
                    for (Favorites f : favoritesList) {
                        if (f.getProductId() == product.getId()) {
                            favoriteId = f.getId();
                        }
                    }
                    if (FavoritesManager.isProductInFavorites(ctx, userId, product.getId())) {
                        FavoritesManager.deleteFromFavorites(ctx, favoriteId);
                    }
                }
            }
        });

        // SET COLORS
        tvProductName.setBackgroundColor(Color.rgb(103, 161, 248));
        tvProductName.setTextColor(Color.WHITE);
        tvProductPrice.setBackgroundColor(Color.rgb(193, 237, 248));
        tvDescription.setBackgroundColor(Color.rgb(193, 237, 248));

        // SET TEXTS
        tvProductName.setText(product.getName());
        tvProductPrice.setText("$" + product.getPrice());
        tvDescription.setText(product.getDescription());
        tvDescription.setVisibility(View.VISIBLE);
        llSpinner.setVisibility(View.VISIBLE);
        spinnerSize.setAdapter(new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, new String[]{"XS", "S", "M", "L", "XL"}));
        btnAdd.setVisibility(View.VISIBLE);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder.setTitle("Select quantity:");
                LinearLayout llAlertDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.product_quantity_dialog_layout, null);
                Button btnMinus = llAlertDialog.findViewById(R.id.btnMinus);
                Button btnPlus = llAlertDialog.findViewById(R.id.btnPlus);
                TextView tvQuantity = llAlertDialog.findViewById(R.id.tvQuantity);

                // GET SHOPPING CART
                String shoppingCartJson = preferences.getString("shopping", "NA");
                ArrayList<ShoppingCartItem> shoppingCart;
                // CHECK IF SHOPPING CART ALREADY EXISTS
                if (shoppingCartJson.equals("NA")) {
                    shoppingCart = new ArrayList<>();
                } else {
                    Type type = new TypeToken<ArrayList<ShoppingCartItem>>(){}.getType();
                    shoppingCart = new Gson().fromJson(shoppingCartJson, type);
                }
                // CHECK IF PRODUCT WAS ALREADY ADDED
                for (ShoppingCartItem item : shoppingCart) {
                    if (item.getIdProduit() == product.getId()) {
                        quantityProduct = item.getQuantity();
                    }
                }
                tvQuantity.setText("" + quantityProduct);

                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (quantityProduct > 1) {
                            quantityProduct--;
                            tvQuantity.setText("" + quantityProduct);
                        }
                    }
                });

                btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        quantityProduct++;
                        tvQuantity.setText("" + quantityProduct);
                    }
                });

                alertDialogBuilder.setView(llAlertDialog);
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // GET SELECTED SIZE
                        TextView tv = (TextView) spinnerSize.getSelectedView();
                        String size = tv.getText().toString();

                        // CHECK IF PRODUCT IS ALREADY IN SHOPPING CART
                        int itemIndex = -1;
                        for (ShoppingCartItem item : shoppingCart) {
                            if (item.getIdProduit() == product.getId()) {
                                itemIndex = shoppingCart.indexOf(item);
                            }
                        }
                        if (itemIndex != -1) {
                            shoppingCart.set(itemIndex, new ShoppingCartItem(product.getId(), quantityProduct, size));
                        } else {
                            shoppingCart.add(new ShoppingCartItem(product.getId(), quantityProduct, size));
                        }

                        editor.putString("shopping", new Gson().toJson(shoppingCart));
                        editor.commit();
                        Toast.makeText(ctx, "Item(s) added to cart!", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNegativeButton("Back", null);

                AlertDialog alertDialog = alertDialogBuilder.show();
            }
        });
        llProductDetail.addView(rlProduct);
    }
}