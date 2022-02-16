package com.example.boutiqueproject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boutiqueproject.entities.Categories;
import com.example.boutiqueproject.entities.Products;
import com.example.boutiqueproject.entities.SubCategories;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.CategoriesManager;
import com.example.boutiqueproject.managers.ProductsManager;
import com.example.boutiqueproject.managers.SubCategoriesManager;
import com.example.boutiqueproject.managers.UsersManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class AddProductActivity extends AppCompatActivity {
    Button addPhoto;
    Button btnSave;
    EditText ed_name, ed_description;
    EditText ed_price;
    ImageView imgProduct;
    Intent i;
    final static int cons = 0;
    Bitmap bmp;
    Context ctx;
    Spinner categorySpinner;
    Spinner subCategorySpinner;
    String categoryName = "";
    String subCategoryName = "";
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        //
        ctx = this;
        categorySpinner = findViewById(R.id.categorySpinner);
        subCategorySpinner = findViewById(R.id.subCategorySpinner);
        addPhoto = findViewById(R.id.addPhoto);
        btnSave = findViewById(R.id.btnSave);
        ed_name = findViewById(R.id.ed_name);
        ed_price = findViewById(R.id.ed_price);
        ed_description = findViewById(R.id.ed_description);
        imgProduct = findViewById(R.id.imgProduct);
        //
        loadCategorySpinnerData();
        //
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //
                categoryName = (String) adapterView.getItemAtPosition(i);
                loadSubCategorySpinnerData(categoryName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subCategoryName = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Checks for permissions for the camera
        checkCameraPermission();
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take a Photo
                // Calls an activity for the photo capture
                i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cons);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save de image in the device
                FileOutputStream fileOutputStream = null;
                // Gets the path for the file
                File file = getdisc();
                if (!file.exists() && !file.mkdirs()) {
                    Toast.makeText(getApplicationContext(), "sorry can not make dir", Toast.LENGTH_LONG).show();
                    return;
                }
                // Gets the current date and time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
                String date = simpleDateFormat.format(new Date());
                // this is the image file name
                String name = "productimage" + date + ".jpeg";
                // this is the full image file name
                String file_name = file.getAbsolutePath() + "/" + name;
                File new_file = new File(file_name);
                try {
                    // Writes into the file de image view content
                    fileOutputStream = new FileOutputStream(new_file);
                    //
                    Bitmap bitmap = viewToBitmap(imgProduct, imgProduct.getWidth(), imgProduct.getHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    Toast.makeText(getApplicationContext(), "Succes!", Toast.LENGTH_LONG).show();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch
                (FileNotFoundException e) {
                } catch (IOException e) {
                }
                // Save the Product in the DataBase
                Products product = new Products();
                int idCategory = CategoriesManager.getIdCategoryByName(ctx, categoryName);
                product.setCategoryId(idCategory);
                int idSubCategory = SubCategoriesManager.getIdSubCategoryByName(ctx, idCategory, subCategoryName);
                product.setSubCategoryId(idSubCategory);
                product.setDescription(String.valueOf(ed_description.getText()));
                product.setName(String.valueOf(ed_name.getText()));
                //
                product.setPrice(Double.parseDouble(String.valueOf(ed_price.getText())));
                //
                product.setQuantity(10);
                // This is the full path image file name
                product.setImage(file_name);
                ProductsManager.addProduct(ctx, product);
                //
                // READ USER ID FROM SHARED PREFERENCES
                int userId = 0;
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                userId = preferences.getInt("ShopAppUserId", 0);
                // redirect to MainActivity
                Users user = new Users();
                user = UsersManager.getUserById(ctx, userId);
                Intent intent = new Intent(ctx, MainActivity.class);
                Gson gson = new Gson();
                String JSON = gson.toJson(user);
                intent.putExtra("user", JSON);
                startActivity(intent);
                finish();
            }
        });
    }
    private void checkCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Message", "You don't have permission for the camera!.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 225);
        } else {
            Log.i("Message", "You have permission to use the camera!.");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Gets the result of the camera activity
        if (resultCode == Activity.RESULT_OK) {
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            // Assings the bitmap to the image view
            imgProduct.setImageBitmap(bmp);
        }
    }
    private File getdisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "My Image");
    }
    private static Bitmap viewToBitmap(View view, int widh, int hight) {
        Bitmap bitmap = Bitmap.createBitmap(widh, hight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void loadCategorySpinnerData() {
        String categoryName = "";
        ArrayList<Categories> listCategories = CategoriesManager.getAll(ctx);
        List<String> categoriesList = new ArrayList<String>();
        for (Categories item : listCategories) {
            categoriesList.add(item.getName());
            if (categoryName.length() == 0) {
                categoryName = item.getName();
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_spinner_item, categoriesList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        categorySpinner.setAdapter(dataAdapter);
        //
        loadSubCategorySpinnerData(categoryName);
    }
    private void loadSubCategorySpinnerData(String categoryName) {
        //
        ArrayList<SubCategories> listSubCategories = SubCategoriesManager.getAllByCategoryName(ctx, categoryName);
        List<String> subCategoriesList = new ArrayList<String>();
        for (SubCategories item : listSubCategories) {
            subCategoriesList.add(item.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_spinner_item, subCategoriesList);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        subCategorySpinner.setAdapter(dataAdapter);
    }
}