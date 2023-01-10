package com.rbt.comunity.fragment.user;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rbt.comunity.R;
import com.rbt.comunity.activites.login.LoginActivity;
import com.rbt.comunity.activites.post.PostActivity;
import com.rbt.comunity.adapter.PostProfilAdapter;
import com.rbt.comunity.model.ModelPost;
import com.rbt.comunity.model.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment {

    private Button btn_edit, create;
    private ImageView logout, setting, profil, background, refresh;
    private TextView user_name, user_name_top, bio;

    private PostProfilAdapter postProfilAdapter;
    private List<ModelPost> posts;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int MY_READ_PERMISSION_CODE = 101;

    private RecyclerView rcv_com, rcv_post;

    private FirebaseAuth auth;
    private FirebaseUser user;
    String profilId;

    private Uri imageUri, backgroundUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;


    private ImageView back_edit, iv_background, save_edit;
    private CircleImageView ci_profil_edit;
    private CardView change_profil, change_background;
    private EditText et_edit_user, et_edit_bio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        btn_edit = root.findViewById(R.id.btn_edit_profil);
        create = root.findViewById(R.id.btn_create_profil);
        setting = root.findViewById(R.id.iv_setting);
        profil = root.findViewById(R.id.iv_foto_profil);
        background = root.findViewById(R.id.iv_background_profil);
        refresh = root.findViewById(R.id.iv_refresh);
        user_name = root.findViewById(R.id.tv_user_profil);
        user_name_top = root.findViewById(R.id.user_app_bar);
        bio = root.findViewById(R.id.tv_bio_profil);

        rcv_post = root.findViewById(R.id.rcv_post_profil);
        rcv_com = root.findViewById(R.id.rcv_com_profil);

        logout = root.findViewById(R.id.iv_logout);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("Uploads");

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profilId", "none");

        if (data.equals("none")) {
            profilId = user.getUid();
        } else {
            profilId = data;
        }

        rcv_post.setHasFixedSize(true);
        rcv_post.setLayoutManager(new GridLayoutManager(getContext(), 3));
        posts = new ArrayList<>();
        postProfilAdapter = new PostProfilAdapter(getContext(), posts);
        rcv_post.setAdapter(postProfilAdapter);

        userInfo();
        myPost();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                BottomSheetBehavior<View> bottomSheetBehavior;
                View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.create_post_bottom_sheet_dialog, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // Sheet behavior
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                // set to behavior to expanded and minimum height to parent layout
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                // set min height to parent view
                CoordinatorLayout layout = bottomSheetDialog.findViewById(R.id.bsl_create);
                assert layout != null;
                layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);

                ImageView back;
                ConstraintLayout postingan, comunity;

                back = bottomSheetDialog.findViewById(R.id.iv_back_create);
                postingan = bottomSheetDialog.findViewById(R.id.cl_post);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                postingan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), PostActivity.class));
                    }
                });
                // show sheet
                bottomSheetDialog.show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                BottomSheetBehavior<View> bottomSheetBehavior;
                View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.sheet_edit, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // Sheet behavior
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                // set to behavior to expanded and minimum height to parent layout
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                // set min height to parent view
                CoordinatorLayout layout = bottomSheetDialog.findViewById(R.id.sheet_id);
                assert layout != null;
                layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);


                back_edit = bottomSheetDialog.findViewById(R.id.iv_back_edit);
                iv_background = bottomSheetDialog.findViewById(R.id.iv_background_edit);
                save_edit = bottomSheetDialog.findViewById(R.id.iv_save_edt);
                change_profil = bottomSheetDialog.findViewById(R.id.cd_change_photo);
                change_background = bottomSheetDialog.findViewById(R.id.cd_bakground);
                ci_profil_edit = bottomSheetDialog.findViewById(R.id.ci_profil_edit);
                et_edit_user = bottomSheetDialog.findViewById(R.id.et_edit_user);
                et_edit_bio = bottomSheetDialog.findViewById(R.id.et_edit_bio);

                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModelUser modelUser = snapshot.getValue(ModelUser.class);
                        et_edit_user.setText(modelUser.getUsername());
                        et_edit_bio.setText(modelUser.getBio());
                        Picasso.get().load(modelUser.getImageurl()).into(ci_profil_edit);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                back_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                change_profil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFileChoose();
                    }
                });

                change_background.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                save_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfil();
                        bottomSheetDialog.dismiss();
                    }
                });
                // show sheet
                bottomSheetDialog.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        return root;
    }

    private void updateProfil() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", et_edit_user.getText().toString());
        map.put("bio", et_edit_bio.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).updateChildren(map);

    }

    private void myPost() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelPost post = dataSnapshot.getValue(ModelPost.class);

                    if (post.getPublisher().equals(profilId)) {
                        posts.add(post);
                    }
                }
                Collections.reverse(posts);
                postProfilAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profilId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser modelUser = snapshot.getValue(ModelUser.class);

                Picasso.get().load(modelUser.getImageurl()).into(profil);
                Picasso.get().load(modelUser.getImageurl()).into(background);
                user_name.setText(modelUser.getUsername());
                user_name_top.setText(modelUser.getUsername());
                bio.setText(modelUser.getBio());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            ci_profil_edit.setImageURI(imageUri);

            backgroundUri = data.getData();
            iv_background.setImageURI(backgroundUri);

            uploadImage();
        }
    }

    private String getFile(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.show();

        if (imageUri != null) {
            StorageReference fReference = storageReference.child(System.currentTimeMillis() + ".jpeg");

            uploadTask = fReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();

                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("imageurl").setValue(url);
                        pd.dismiss();
                    } else {

                    }
                }
            });
        } else {

        }

    }
}