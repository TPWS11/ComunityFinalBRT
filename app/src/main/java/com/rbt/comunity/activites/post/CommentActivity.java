package com.rbt.comunity.activites.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rbt.comunity.R;
import com.rbt.comunity.adapter.CommentAdapter;
import com.rbt.comunity.model.ModelComment;
import com.rbt.comunity.model.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    private EditText et_comment;
    private ImageView iv_back_comment;
    private CircleImageView ci_profil;
    private CardView cd_post;

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<ModelComment> commentList;

    private String postId, authorId;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        iv_back_comment = findViewById(R.id.iv_back_comment);
        et_comment = findViewById(R.id.et_comment);
        ci_profil = findViewById(R.id.ci_profil_comment);
        cd_post = findViewById(R.id.cd_post);
        recyclerView = findViewById(R.id.rcv_comment);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        authorId = intent.getStringExtra("authorId");

        iv_back_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        adapter = new CommentAdapter(this, commentList, postId);
        recyclerView.setAdapter(adapter);

        user = FirebaseAuth.getInstance().getCurrentUser();

        getUserProfil();
        cd_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_comment.getText().toString())) {

                } else {
                    putComment();
                }
            }
        });

        getComment();

    }

    private void getComment() {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelComment comment = dataSnapshot.getValue(ModelComment.class);
                    commentList.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putComment() {
        HashMap<String, Object> map = new HashMap<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postId);
        String id = reference.push().getKey();

        map.put("id", id);
        map.put("comment", et_comment.getText().toString());
        map.put("publisher", user.getUid());

        et_comment.setText("");
        reference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getUserProfil() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = snapshot.getValue(ModelUser.class);
                if (modelUser.getImageurl().equals("default")) {
                    ci_profil.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(modelUser.getImageurl()).into(ci_profil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}