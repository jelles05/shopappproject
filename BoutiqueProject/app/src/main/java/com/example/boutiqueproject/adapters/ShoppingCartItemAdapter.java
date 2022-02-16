package com.example.boutiqueproject.adapters;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.boutiqueproject.R;
import com.example.boutiqueproject.ShoppingCartActivity;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.entities.ShoppingCartItem;
import com.example.boutiqueproject.managers.ProductsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class ShoppingCartItemAdapter extends ArrayAdapter<ShoppingCartItem> {
    int idLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public ShoppingCartItemAdapter(@NonNull Context context, int resource, @NonNull List<ShoppingCartItem> objects) {
        super(context, resource, objects);
        idLayout = resource;
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShoppingCartItem shoppingCartItem = getItem(position);
        Products product = ProductsManager.getById(getContext(), shoppingCartItem.getIdProduit());

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(idLayout, null);
        }

        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvSize = convertView.findViewById(R.id.tvSize);
        TextView tvQty = convertView.findViewById(R.id.tvQty);
        Button btnMinus = convertView.findViewById(R.id.btnMinus);
        Button btnPlus = convertView.findViewById(R.id.btnPlus);

        try {
            imgProduct.setImageDrawable(Drawable.createFromStream(getContext().getAssets().open(product.getImage()), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tvName.setText(product.getName());
        tvPrice.setText("$" + product.getPrice());
        tvSize.setText(shoppingCartItem.getSize());
        tvQty.setText("" + shoppingCartItem.getQuantity());

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = shoppingCartItem.getQuantity();
                quantity++;
                shoppingCartItem.setQuantity(quantity);
                saveProductQuantity(shoppingCartItem);
                notifyDataSetChanged();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = shoppingCartItem.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    shoppingCartItem.setQuantity(quantity);
                    saveProductQuantity(shoppingCartItem);
                } else {
                    shoppingCartItem.setQuantity(0);
                    saveProductQuantity(shoppingCartItem);
                    remove(shoppingCartItem);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void saveProductQuantity(ShoppingCartItem item) {
        String shoppingCartJson = preferences.getString("shopping", "NA");
        Type type = new TypeToken<ArrayList<ShoppingCartItem>>(){}.getType();
        ArrayList<ShoppingCartItem> shoppingCart = new Gson().fromJson(shoppingCartJson, type);

        // GET PRODUCT ID IN SHOPPING CART ARRAY
        int itemIndex = -1;
        for (ShoppingCartItem i : shoppingCart) {
            if (i.getIdProduit() == item.getIdProduit()) {
                itemIndex = shoppingCart.indexOf(i);
            }
        }

        // CHECK PRODUCT'S QUANTITY
        if (item.getQuantity() > 0) {
            shoppingCart.set(itemIndex, item);
        } else {
            shoppingCart.remove(itemIndex);
        }

        // CHECK IF SHOPPING CART IS EMPTY AND GET TOTAL
        double total = 0;
        if (shoppingCart.size() == 0) {
            editor.remove("shopping");
            ((ShoppingCartActivity) getContext()).setVisibilityNoItems();
        } else {
            editor.putString("shopping", new Gson().toJson(shoppingCart));
            for (ShoppingCartItem i : shoppingCart) {
                Products p = ProductsManager.getById(getContext(), i.getIdProduit());
                total += i.getQuantity() * p.getPrice();
            }
        }
        editor.commit();
        ((ShoppingCartActivity) getContext()).setTotal(total);
        ((ShoppingCartActivity) getContext()).setTvTotalText(total);
    }
}