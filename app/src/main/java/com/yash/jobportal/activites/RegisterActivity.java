package com.yash.jobportal.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.jobportal.R;
import com.yash.jobportal.model.UserModel;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etPhone;
    Button btnRegister;
    TextView txtLogin;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            // Name Validation
            if (name.isEmpty()) {
                etName.setError("Enter your name");
                etName.requestFocus();
                return;
            }

            if (name.length() < 3) {
                etName.setError("Name must be at least 3 characters");
                etName.requestFocus();
                return;
            }

            // Phone Validation
            if (phone.isEmpty()) {
                etPhone.setError("Enter phone number");
                etPhone.requestFocus();
                return;
            }

            if (!phone.matches("[0-9]{10}")) {
                etPhone.setError("Enter valid 10 digit phone number");
                etPhone.requestFocus();
                return;
            }

            // Email Validation
            if (email.isEmpty()) {
                etEmail.setError("Enter email");
                etEmail.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter valid email address");
                etEmail.requestFocus();
                return;
            }

            // Password Validation
            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                etPassword.requestFocus();
                return;
            }

            if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) {
                etPassword.setError(
                        "Password must be at least 8 characters and contain Uppercase, Lowercase, Number and Special Character");
                etPassword.requestFocus();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            String userId =
                                    auth.getCurrentUser().getUid();

                            UserModel userModel =
                                    new UserModel(

                                            name,
                                            email,
                                            phone

                                    );

                            db.collection("Users")
                                    .document(userId)
                                    .set(userModel)
                                    .addOnSuccessListener(unused -> {

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Registration Successful!",
                                                Toast.LENGTH_SHORT).show();

                                        auth.getCurrentUser().sendEmailVerification()
                                                .addOnSuccessListener(it -> {
                                                    Toast.makeText(
                                                            RegisterActivity.this,
                                                            "Verification Email Sent",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                });

                                        startActivity(
                                                new Intent(
                                                        RegisterActivity.this,
                                                        LoginActivity.class));

                                        finish();

                                    });

                        }   else {

                            Toast.makeText(
                                    RegisterActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    });

        });
    }
}