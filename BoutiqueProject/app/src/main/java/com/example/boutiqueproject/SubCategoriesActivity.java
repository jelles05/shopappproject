package com.example.boutiqueproject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.entities.SubCategories;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.SubCategoriesManager;
import com.google.gson.Gson;
import java.util.ArrayList;
public class SubCategoriesActivity extends OptionsMenuActivity {
    Context ctx;
    Toolbar toolbar;
    LinearLayout llSubCategories;
    ImageView imgCategory;
    TextView tvCategorie;
    Intent infIntent;
    ArrayList<SubCategories> subCategories;
    Users user;
    Categories category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);

        ctx = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        llSubCategories = findViewById(R.id.llSubCategories);
        imgCategory = findViewById(R.id.imgCategory);
        tvCategorie =  findViewById(R.id.tvCategorie);
        user = new Users();
        category = new Categories();
        infIntent = getIntent();

        // RETRIEVE USER AND CATEGORY FROM INTENT EXTRAS
        String stUser = infIntent.getStringExtra("user");
        user = new Gson().fromJson(stUser, Users.class);
        String stCategory = infIntent.getStringExtra("category");
        category = new Gson().fromJson(stCategory, Categories.class);

        // SET CATEGORY TITLE
        tvCategorie.setText(category.getName());
        switch (category.getName()) {
            case "Women":
                imgCategory.setImageResource(R.drawable.women);
                break;
            case "Men":
                imgCategory.setImageResource(R.drawable.men);
                break;
            case "Kids":
                imgCategory.setImageResource(R.drawable.kids);
                break;
        }

        // GET SUBCATEGORIES
        subCategories = SubCategoriesManager.getAll(ctx, category.getId());

        // ADD EACH CATEGORY TO THE VIEW
        for (SubCategories subCategory : subCategories) {
            createSubcategoryTextView(subCategory.getName(), subCategory.getId(), "subcategory");
        }
        createSubcategoryTextView("View All", category.getId(), "category");
    }

    private void createSubcategoryTextView(String txt, int searchId, String searchType) {
        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.subcategory_layout, null);
        tv.setText(txt);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ProductsActivity.class);
                intent.putExtra("searchId", searchId);
                intent.putExtra("searchType", searchType);
                startActivity(intent);
            }
        });
        llSubCategories.addView(tv);
    }
}