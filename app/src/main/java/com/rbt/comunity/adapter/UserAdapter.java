package com.rbt.comunity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rbt.comunity.R;
import com.rbt.comunity.model.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<ModelUser> modelUsers;
    private boolean isFragment;

    private FirebaseUser fu;

    public UserAdapter(Context mContext, List<ModelUser> modelUsers,boolean isFragment) {
        this.mContext = mContext;
        this.modelUsers = modelUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_search_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fu = FirebaseAuth.getInstance().getCurrentUser();
        ModelUser user = modelUsers.get(position);

        holder.username.setText(user.getUsername());
        holder.bio.setText(user.getBio());

        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfil);

    }

    @Override
    public int getItemCount() {
        return modelUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageProfil;
        public TextView username;
        public TextView bio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfil = itemView.findViewById(R.id.ci_image_profil_search);
            username = itemView.findViewById(R.id.tv_user_name_search);
            bio = itemView.findViewById(R.id.tv_bio_search);
        }
    }
}
