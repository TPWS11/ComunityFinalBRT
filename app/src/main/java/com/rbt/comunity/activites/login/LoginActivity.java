package com.rbt.comunity.activites.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rbt.comunity.R;
import com.rbt.comunity.activites.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login, register;

    private ImageView showPw;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase
        auth = FirebaseAuth.getInstance();

        //EditText
        email = findViewById(R.id.et_email_login);
        password = findViewById(R.id.et_pw_login);

        // Button
        login = findViewById(R.id.btn_login);
        register = findViewById(R.id.btn_register);

        // ImageView
        showPw = findViewById(R.id.iv_show_password);

        showPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // If password is visible the hide it
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change icon
                    showPw.setImageResource(R.drawable.password_invisible);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPw.setImageResource(R.drawable.password_visible);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email)) {
                    email.setError("Email is required");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(txt_password)) {
                    password.setError("Password is required");
                    password.requestFocus();
                } else {
                    loginUser(txt_email, txt_password);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "You can login now", Toast.LENGTH_SHORT).show();
        }
    }
}