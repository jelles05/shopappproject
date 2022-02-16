package com.example.boutiqueproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.UsersManager;
import com.example.boutiqueproject.services.ConnectionDb;
import com.google.gson.Gson;
public class LoginActivity extends AppCompatActivity {
    Button btnSingIn, btnSignUp;
    Context ctx;
    EditText inputEmail;
    EditText inputPassword;
    TextView messageText;

    @Override
    protected void onResume() {
        super.onResume();
        inputEmail.setText("");
        inputPassword.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ctx = this;
        // COPY DB FROM ASSETS ONLY IF IT DOESN'T EXIST ON PHONE
        if (!ConnectionDb.checkIfDatabaseExists(ctx)) {
            ConnectionDb.copyFromAssets(ctx);
        }
        btnSingIn = findViewById(R.id.btnSingIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        messageText = findViewById(R.id.messageText);

        inputEmail.setText("");
        inputPassword.setText("");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to SignUpActivity
                Intent intent = new Intent(ctx, SignUpActivity.class);
                startActivity(intent);
            }
        });


        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        messageText= (TextView) findViewById(R.id.messageText);
        btnSingIn = (Button) findViewById(R.id.btnSingIn);
        //


        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageText.setVisibility(View.INVISIBLE);
                Users user = UsersManager.checkUser(ctx, String.valueOf(inputEmail.getText()), String.valueOf(inputPassword.getText()));
                if (user == null) {
                    messageText.setText("Invalid email or password");
                    messageText.setVisibility(View.VISIBLE);
                } else {
                    // SAVE USER ID IN SHARED PREFERENCES
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
                    editor.putInt("ShopAppUserId", user.getId());
                    editor.commit();
                    // redirect to MainActivity
                    Intent intent = new Intent(ctx, MainActivity.class);
                    Gson gson = new Gson();
                    String JSON = gson.toJson(user);
                    intent.putExtra("user", JSON);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}