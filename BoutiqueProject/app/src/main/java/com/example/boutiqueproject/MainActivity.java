package com.example.boutiqueproject;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.CategoriesManager;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
public class MainActivity extends OptionsMenuActivity {
    Toolbar toolbar;
    Context ctx;
    Intent infIntent;
    ImageView imgWomen, imgMen, imgKids, imgPromo;
    TextView tvWomen, tvKids, tvMen;
    //carousel
    CarouselView carouselView;
    int[] sampleImages;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        infIntent = getIntent();
        user = new Users();
        sampleImages = new int[]{R.drawable.p5, R.drawable.p9, R.drawable.p15, R.drawable.p18, R.drawable.p23, R.drawable.p27};

        // GET USER FROM INTENT EXTRAS
        String stuser = infIntent.getStringExtra("user");
        user = new Gson().fromJson(stuser, Users.class);

        // GET FROM VIEW
        imgPromo = findViewById(R.id.imgPromo);
        imgWomen = findViewById(R.id.imgWomen);
        tvWomen = findViewById(R.id.tvWomen);
        imgMen = findViewById(R.id.imgMen);
        tvMen = findViewById(R.id.tvMen);
        imgKids = findViewById(R.id.imgKids);
        tvKids = findViewById(R.id.tvKids);

        // GET CATEGORIES
        ArrayList<Categories> listCategories = CategoriesManager.getAll(ctx);
        for (Categories item : listCategories) {
            switch (item.getId()) {
                case 1:
                    try {
                        // get input stream
                        InputStream ims = getAssets().open("women_icon.png");
                        // load image as Drawable
                        Drawable d = Drawable.createFromStream(ims, null);
                        // set image to ImageView
                        imgWomen.setImageDrawable(d);
                        ims.close();
                    } catch (IOException ex) {
                        String aux = ex.getMessage();
                    }
                    tvWomen.setText(item.getName());
                    break;
                case 2:
                    try {
                        // get input stream
                        InputStream ims = getAssets().open("men_icon.png");
                        // load image as Drawable
                        Drawable d = Drawable.createFromStream(ims, null);
                        // set image to ImageView
                        imgMen.setImageDrawable(d);
                        ims.close();
                    } catch (IOException ex) {
                        String aux = ex.getMessage();
                    }
                    tvMen.setText(item.getName());
                    break;
                case 3:
                    try {
                        // get input stream
                        InputStream ims = getAssets().open("kids_icon.png");
                        // load image as Drawable
                        Drawable d = Drawable.createFromStream(ims, null);
                        // set image to ImageView
                        imgKids.setImageDrawable(d);
                        ims.close();
                    } catch (IOException ex) {
                        String aux = ex.getMessage();
                    }
                    tvKids.setText(item.getName());
                    break;
            }
        }

        imgWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SubCategories Activity
                Intent intent = new Intent(ctx, SubCategoriesActivity.class);
                Gson gson = new Gson();
                // PUT USER IN EXTRA
                String JSON = gson.toJson(user);
                intent.putExtra("user", JSON);
                // PUT CATEGORY IN EXTRA
                Categories category = new Categories(1, String.valueOf(tvWomen.getText()));
                JSON = gson.toJson(category);
                intent.putExtra("category", JSON);
                // REDIRECT
                startActivity(intent);
            }
        });

        imgMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SubCategories Activity
                Intent intent = new Intent(ctx, SubCategoriesActivity.class);
                Gson gson = new Gson();
                // PUT USER IN EXTRA
                String JSON = gson.toJson(user);
                intent.putExtra("user", JSON);
                // PUT CATEGORY IN EXTRA
                Categories category = new Categories(2, String.valueOf(tvMen.getText()));
                JSON = gson.toJson(category);
                intent.putExtra("category", JSON);
                // REDIRECT
                startActivity(intent);
            }
        });

        imgKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SubCategories Activity
                Intent intent = new Intent(ctx, SubCategoriesActivity.class);
                Gson gson = new Gson();
                // PUT USER IN EXTRA
                String JSON = gson.toJson(user);
                intent.putExtra("user", JSON);
                // PUT CATEGORY IN EXTRA
                Categories category = new Categories(3, String.valueOf(tvKids.getText()));
                JSON = gson.toJson(category);
                intent.putExtra("category", JSON);
                // REDIRECT
                startActivity(intent);
            }
        });

        imgPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to Products Activity
                Intent intent = new Intent(ctx, ProductsActivity.class);
                intent.putExtra("searchId", 6);
                intent.putExtra("searchType", "subcategory");
                startActivity(intent);
            }
        });

        //carrousel
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        //end carrousel
    }

    //carrousel
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}