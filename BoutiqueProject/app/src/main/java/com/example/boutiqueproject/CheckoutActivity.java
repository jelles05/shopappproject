package com.example.boutiqueproject;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.boutiqueproject.entities.Orders;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.CheckoutManager;
import com.example.boutiqueproject.managers.UsersManager;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CheckoutActivity extends AppCompatActivity {
    Context ctx;
    Intent intent;
    Orders order;
    EditText edCardNumber, etdExpirationDate, edAddress;
    TextView tvNoInfo, tvOrderNumber, tvDate, tvAddress, tvSubtotal, tvTax, tvTotal;
    ScrollView svFinishOrder;
    LinearLayout llOrderConfirmation;
    String dateOrder;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ctx = this;
        intent = getIntent();
        order = new Gson().fromJson(intent.getStringExtra("order"), Orders.class);

        svFinishOrder = findViewById(R.id.svFinishOrder);
        edCardNumber = findViewById(R.id.edCardNumber);
        etdExpirationDate = findViewById(R.id.etdExpirationDate);
        edAddress = findViewById(R.id.edAddress);
        tvNoInfo = findViewById(R.id.tvNoInfo);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvDate = findViewById(R.id.tvDate);
        tvAddress = findViewById(R.id.tvAddress);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        llOrderConfirmation = findViewById(R.id.llOrderConfirmation);
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = preferences.edit();
        user = UsersManager.getUserById(ctx, preferences.getInt("ShopAppUserId", -1));
    }

    public void handleBtnCancelClick(View view) {
        finish();
        CheckoutManager.deleteOrderDetails(ctx, order.getId());
        CheckoutManager.deleteOrder(ctx, order.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handleBtnConfirmClick(View view) {
        String cardNumber = edCardNumber.getText().toString();
        String expirationDate = etdExpirationDate.getText().toString();
        String address = edAddress.getText().toString();
        if (cardNumber.isEmpty() || expirationDate.isEmpty() || address.isEmpty()) {
            tvNoInfo.setVisibility(View.VISIBLE);
        } else {
            tvOrderNumber.setText("Order #" + order.getId());
            tvAddress.setText("Shipping/Billing address: " + address);
            tvSubtotal.setText("Subtotal: $" + ((double) Math.round(order.getAmount() * 100) / 100));
            tvTax.setText("Tax: $" + ((double) Math.round(order.getTax() * 100) / 100));
            tvTotal.setText("Total: $" + ((double) Math.round((order.getAmount() + order.getTax()) * 100) / 100));
            dateOrder = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
            tvDate.setText("Date: " + dateOrder);
            order.setDate(dateOrder);
            showLlOrderConfirmation();
            CheckoutManager.updateOrderDate(ctx, order);
            // DELETE SHOPPING CART
            editor.remove("shopping");
            editor.commit();
        }
    }

    public void handleBtnEndOrderConfirmationClick(View view) {
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("user", new Gson().toJson(user));
        startActivity(intent);
        finish();
    }

    public void showLlOrderConfirmation() {
        svFinishOrder.setVisibility(View.GONE);
        llOrderConfirmation.setVisibility(View.VISIBLE);
    }
}