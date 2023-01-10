package com.rbt.comunity.activites.post;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rbt.comunity.Helper.helper;
import com.rbt.comunity.R;

public class StatusActivity extends AppCompatActivity {

    private EditText et_name, et_status;
    private Button btnSave;
    private helper db = new helper(this);
    private String id, name, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        et_name = findViewById(R.id.et_nama);
        et_status = findViewById(R.id.et_status);
        btnSave = findViewById(R.id.btn_save);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        status = getIntent().getStringExtra("status");

        if (id == null || id.equals("")) {
            setTitle("Tambah Status");
        } else {
            setTitle("Status User");
            et_name.setText(name);
            et_status.setText(status);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (id == null || id.equals("")) {
                        save();
                    } else {
                        edit();
                    }
                } catch (Exception e) {
                    Log.e("Saving", e.getMessage());
                }
            }
        });
    }

    private void save() {
        if (String.valueOf(et_name.getText()).equals("") || (String.valueOf(et_status.getText()).equals(""))) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            db.insert(et_name.getText().toString(), et_status.getText().toString());
            finish();
        }
    }

    private void edit() {
        if (String.valueOf(et_name.getText()).equals("") || (String.valueOf(et_status.getText()).equals(""))) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            db.update(Integer.parseInt(id), et_name.getText().toString(), et_status.getText().toString());
            finish();
        }
    }
}