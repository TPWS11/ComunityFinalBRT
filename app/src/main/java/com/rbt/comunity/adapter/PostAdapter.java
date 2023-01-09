package com.rbt.comunity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rbt.comunity.R;
import com.rbt.comunity.model.ModelPost;
import com.rbt.comunity.model.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context context;
    private List<ModelPost> posts;

    private FirebaseUser user;

    public PostAdapter(Context context, List<ModelPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();

        ModelPost post = posts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.iv_post);
        holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = snapshot.getValue(ModelUser.class);

                if (modelUser.getImageurl().equals("default")) {
                    holder.iv_profil.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(modelUser.getImageurl()).into(holder.iv_profil);
                }
                holder.username.setText(modelUser.getUsername());
                holder.author.setText(modelUser.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_profil, iv_like, iv_chat, iv_more, iv_download, iv_post;
        private TextView username, jumLike, author, description, jumCom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profil = itemView.findViewById(R.id.iv_foto_post);
            iv_post = itemView.findViewById(R.id.iv_post_display);
            iv_like = itemView.findViewById(R.id.iv_like);
            iv_chat = itemView.findViewById(R.id.iv_chat);
            iv_download = itemView.findViewById(R.id.iv_download);

            username = itemView.findViewById(R.id.tv_user_post);
            jumLike = itemView.findViewById(R.id.tv_jum_like);
            author = itemView.findViewById(R.id.tv_user_author_post);
            description = itemView.findViewById(R.id.tv_desc);
            jumCom = itemView.findViewById(R.id.tv_jum_comment);
        }
    }
}
