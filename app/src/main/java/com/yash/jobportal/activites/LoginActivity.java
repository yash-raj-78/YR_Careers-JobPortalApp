package com.yash.jobportal.activites;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yash.jobportal.MainActivity;
import com.yash.jobportal.R;

public class LoginActivity extends AppCompatActivity {

    EditText etEmailOrPhone, etPassword;
    Button btnLogin;
    TextView txtRegister;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailOrPhone = findViewById(R.id.etEmailOrPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        createNotificationChannel();

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {

            String emailOrPhone =
                    etEmailOrPhone.getText().toString().trim();

            String password =
                    etPassword.getText().toString().trim();

            // Validation
            if (emailOrPhone.isEmpty()) {
                etEmailOrPhone.setError("Enter Email or Phone");
                etEmailOrPhone.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Enter Password");
                etPassword.requestFocus();
                return;
            }

            // 🔥 NEW VALIDATION ADDED (IMPORTANT)
            if (!emailOrPhone.contains("@") &&
                    !emailOrPhone.matches("[0-9]{10}")) {

                etEmailOrPhone.setError("Enter valid Email or 10 digit Phone Number");
                etEmailOrPhone.requestFocus();
                return;
            }

            // Email Login
            if (emailOrPhone.contains("@")) {

                loginWithEmail(emailOrPhone, password);

            }
            // Phone Login
            else {

                loginWithPhone(emailOrPhone, password);

            }

        });

    }

    private void loginWithEmail(String email, String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (!auth.getCurrentUser().isEmailVerified()) {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Please verify your email first",
                                    Toast.LENGTH_SHORT
                            ).show();

                            auth.signOut();
                            return;
                        }

                        Toast.makeText(
                                LoginActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        showLoginNotification();

                        startActivity(
                                new Intent(
                                        LoginActivity.this,
                                        MainActivity.class
                                )
                        );

                        finish();

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                });

    }

    private void loginWithPhone(String phone, String password) {

        db.collection("Users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {

                        String email =
                                queryDocumentSnapshots
                                        .getDocuments()
                                        .get(0)
                                        .getString("email");

                        if (email != null) {

                            loginWithEmail(email, password);

                        } else {

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Email not found",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    } else {

                        Toast.makeText(
                                LoginActivity.this,
                                "Phone number not found",
                                Toast.LENGTH_SHORT
                        ).show();

                    }

                })
                .addOnFailureListener(e ->
                        Toast.makeText(
                                LoginActivity.this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show()
                );

    }


    // Notification
    private void showLoginNotification() {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "jobportal_channel")
                        .setSmallIcon(R.drawable.outline_notifications_24)
                        .setContentTitle("Login Successful")
                        .setContentText("You are successfully logged in to YR Careers Job Portal")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(101, builder.build());
    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    "jobportal_channel",
                    "Job Portal Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

}