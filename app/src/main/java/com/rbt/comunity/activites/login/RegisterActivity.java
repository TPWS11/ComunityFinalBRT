package com.rbt.comunity.activites.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rbt.comunity.R;
import com.rbt.comunity.activites.MainActivity;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button register, login;
    private EditText user_name, email, password, confirm_password;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private ProgressDialog progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);

        // EditText
        user_name = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email_register);
        password = findViewById(R.id.et_pw_register);
        confirm_password = findViewById(R.id.et_pw_register_2);

        // Button
        register = findViewById(R.id.btn_register_regis);
        login = findViewById(R.id.btn_login_regis);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tx_user = user_name.getText().toString();
                String tx_email = email.getText().toString();
                String tx_password = password.getText().toString();
                String tx_confirm_password = confirm_password.getText().toString();

                if (TextUtils.isEmpty(tx_user)) {
                    user_name.setError("Full name is required");
                    user_name.requestFocus();
                } else if (TextUtils.isEmpty(tx_email)) {
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(tx_email).matches()) {
                    email.setError("Valid email is required");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(tx_confirm_password)) {
                    password.setError("Password is required");
                    password.requestFocus();
                } else if (tx_password.length() < 8) {
                    password.setError("Password too weak");
                    password.requestFocus();
                } else if (TextUtils.isEmpty(tx_confirm_password)) {
                    confirm_password.setError("Password Confirmation is required");
                    confirm_password.requestFocus();
                } else if (!tx_confirm_password.equals(tx_password)) {
                    confirm_password.setError("Password Confirmation is required");
                    confirm_password.requestFocus();
                    // Cleared the entered passwords
                    password.clearComposingText();
                    confirm_password.clearComposingText();
                } else {
                    registerUser(tx_user, tx_email, tx_password, tx_confirm_password);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }

    private void registerUser(final String user, final String email, final String password, final String confirm) {
        progress.setMessage("Sabar yah");
        progress.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", user);
                map.put("email", email);
//                map.put("password", password);
//                map.put("confirm", confirm);
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("bio", "");
                map.put("imageurl", "default");

                reference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}