package com.example.boutiqueproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.boutiqueproject.entities.Users;
import com.example.boutiqueproject.managers.UsersManager;
import com.google.gson.Gson;
public class SignUpActivity extends AppCompatActivity {
    EditText lastName, firstName, phone, ed_password, edEmail, ed_postal_code, ed_adresse;
    TextView lastnameError, firstNameError, phoneError, postalCodeError, adresseError, passwordError, emailError;
    Button btnCreateAccount, btnBack;
    boolean isLastNameValid, isFirstNameValid, isPhoneValid, isDateValid;
    Context ctx;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ctx = this;
        user = new Users();
        Intent infIntent = getIntent();
        // GET USER
        String stuser = infIntent.getStringExtra("user");
        Gson gson = new Gson();
        user = gson.fromJson(stuser, Users.class);

        edEmail = findViewById(R.id.edEmail);
        ed_password = findViewById(R.id.ed_password);
        ed_adresse = findViewById(R.id.ed_adresse);
        ed_postal_code = findViewById(R.id.ed_postal_code);
        phone = findViewById(R.id.ed_phone);
        lastName = findViewById(R.id.edLastName);
        firstName = findViewById(R.id.edFirstname);
        lastnameError = findViewById(R.id.lastNameError);
        passwordError = findViewById(R.id.passwordError);
        emailError = findViewById(R.id.emailError);
        postalCodeError = findViewById(R.id.postalCodeError);
        firstNameError = findViewById(R.id.firstNameError);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnBack = findViewById(R.id.btnBack);
        phoneError = findViewById(R.id.phoneError);
        adresseError = findViewById(R.id.adresseError);

        // redirection vers la  page login
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });
    }

    // validations du formulaire
    public void SetValidation() {
        // Check for a valid lastname.
        if (lastName.getText().toString().isEmpty()) {
            lastnameError.setText(getResources().getString(R.string.lastNameError));
            isLastNameValid = false;
        } else {
            lastnameError.setText("");
            isLastNameValid = true;
        }
        // Check for a valid firstname.
        if (firstName.getText().toString().isEmpty()) {
            firstNameError.setText(getResources().getString(R.string.firstNameError));
            isFirstNameValid = false;
        } else {
            firstNameError.setText("");
            isFirstNameValid = true;
        }
        // Check for a valid phone.
        if (phone.getText().toString().isEmpty()) {
            phoneError.setText(getResources().getString(R.string.phoneError));
            isPhoneValid = false;
        } else {
            phoneError.setText("");
            isPhoneValid = true;
        }
        // Check for a valid email.
        if (edEmail.getText().toString().isEmpty()) {
            emailError.setText(getResources().getString(R.string.emailError));
            isPhoneValid = false;
        } else {
            emailError.setText("");
            isPhoneValid = true;
        }
        // Check for a valid password.
        if (ed_password.getText().toString().isEmpty()) {
            passwordError.setText(getResources().getString(R.string.passwordError));
            isPhoneValid = false;
        } else {
            passwordError.setText("");
            isPhoneValid = true;
        }
        // Check for a valid postal code.
        if (ed_postal_code.getText().toString().isEmpty()) {
            postalCodeError.setText(getResources().getString(R.string.postalCodeError));
            isPhoneValid = false;
        } else {
            postalCodeError.setText("");
            isPhoneValid = true;
        }
        // Check for a valid adresse.
        if (ed_adresse.getText().toString().isEmpty()) {
            adresseError.setText(getResources().getString(R.string.adresseError));
            isDateValid = false;
        } else {
            adresseError.setText("");
            isDateValid = true;
        }

        if (isLastNameValid && isFirstNameValid && isDateValid && isPhoneValid) {
            user = new Users();
            user.setFirstName(firstName.getText().toString());
            user.setLastName(lastName.getText().toString());
            user.setEmail(edEmail.getText().toString());
            user.setAddress("");
            user.setCity("");
            user.setIsAdmin("0");
            user.setPassword(ed_password.getText().toString());
            user.setPostalCode("");
            user.setProvince("");

            Users userAux = new Users();
            userAux = UsersManager.getUserByEmail(ctx, edEmail.getText().toString());
            if (userAux.getId() != 0) {
                user.setId(userAux.getId());
                UsersManager.updateUser(ctx, user);
                Intent intent = new Intent(ctx, MainActivity.class);
                Gson gson = new Gson();
                String JSON = gson.toJson(user);
                intent.putExtra("user", JSON);
                startActivity(intent);
            } else {
                if (UsersManager.addUser(ctx, user)) {
                    user = UsersManager.getUserByEmail(ctx, edEmail.getText().toString());
                    // GO TO MAIN ACTIVITY
                    Intent intent = new Intent(ctx, MainActivity.class);
                    Gson gson = new Gson();
                    String JSON = gson.toJson(user);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("user", JSON);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "error insert user", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "invalid user input data", Toast.LENGTH_SHORT).show();
        }
    }
}