package com.example.boutiqueproject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.UsersManager;
public class OptionsMenuActivity extends AppCompatActivity {
    MenuItem addProduct;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);

        // GET USER
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("ShopAppUserId", -1);
        Users user = UsersManager.getUserById(this, userId);

        // SET ADD PRODUCT ITEM VISIBLE FOR ADMIN ONLY
        MenuItem item = menu.findItem(R.id.addProduct);
        if (item != null) {
            if (user.getIsAdmin().equals("0")) {
                item.setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        Context ctx = this;
        switch (item.getItemId()) {
            case R.id.searchItem:
                intent.setClass(ctx, SearchProductActivity.class);
                startActivity(intent);
                break;
            case R.id.favoritesItem:
                intent.setClass(ctx, FavoritesActivity.class);
                startActivity(intent);
                break;
            case R.id.cartItem:
                intent.setClass(ctx, ShoppingCartActivity.class);
                startActivity(intent);
                break;
            case R.id.logOut:
                // REMOVE USER ID
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
                editor.remove("ShopAppUserId");
                editor.commit();
                // GO BACK TO LOGIN
                intent.setClass(ctx, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.addProduct:
                intent.setClass(ctx, AddProductActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}