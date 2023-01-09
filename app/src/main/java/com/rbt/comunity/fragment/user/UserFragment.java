package com.rbt.comunity.fragment.user;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rbt.comunity.R;
import com.rbt.comunity.activites.login.LoginActivity;
import com.rbt.comunity.activites.post.PostActivity;


public class UserFragment extends Fragment {

    private Button btn_edit, create;
    private ImageView logout;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        btn_edit = root.findViewById(R.id.btn_edit_profil);
        create = root.findViewById(R.id.btn_create_profil);
        logout = root.findViewById(R.id.iv_logout);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

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
}