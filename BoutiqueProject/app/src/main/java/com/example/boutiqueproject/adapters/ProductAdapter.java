package com.example.boutiqueproject.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.boutiqueproject.ProductDetailActivity;
import com.example.boutiqueproject.R;
import com.example.boutiqueproject.entities.Favorites;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.managers.FavoritesManager;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ProductAdapter extends ArrayAdapter<Products> {
    int idLayout;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Products> objects) {
        super(context, resource, objects);
        idLayout = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Products product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(idLayout, null);
        }

        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        ImageView imgFavorite = convertView.findViewById(R.id.imgFavorite);
        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);

        try {
            imgProduct.setImageDrawable(Drawable.createFromStream(getContext().getAssets().open(product.getImage()), null));
        } catch (IOException e) {
            e.printStackTrace();
            // Is an exception is thrown , validates if the product has a image path
            if (product.getImage().length() > 0 ){
                // Loads image from the path
                imgProduct.setImageBitmap(BitmapFactory.decodeFile(product.getImage()));
            }
        }
        tvProductName.setText(product.getName());
        tvProductPrice.setText("$" + product.getPrice());

        // GET USER ID
        int userId = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("ShopAppUserId", -1);
        // CHECK IF PRODUCT IS IN USER'S FAVORITES
        if (FavoritesManager.isProductInFavorites(getContext(), userId, product.getId())) {
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
                    if (!FavoritesManager.isProductInFavorites(getContext(), userId, product.getId())) {
                        FavoritesManager.addToFavorites(getContext(), new Favorites(1, product.getId(), userId));
                    }
                } else {
                    imgFavorite.setImageResource(R.drawable.add_to_favorites);
                    view.setTag("add");
                    // REMOVE PRODUCT FROM FAVORITES
                    int favoriteId = -1;
                    ArrayList<Favorites> favoritesList = FavoritesManager.getByUserId(getContext(), userId);
                    for (Favorites f : favoritesList) {
                        if (f.getProductId() == product.getId()) {
                            favoriteId = f.getId();
                        }
                    }
                    if (FavoritesManager.isProductInFavorites(getContext(), userId, product.getId())) {
                        FavoritesManager.deleteFromFavorites(getContext(), favoriteId);
                    }
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product", new Gson().toJson(product));
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
