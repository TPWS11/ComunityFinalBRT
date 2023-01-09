package com.rbt.comunity.fragment.search;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rbt.comunity.R;
import com.rbt.comunity.adapter.UserAdapter;
import com.rbt.comunity.model.ModelUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


public class SearchFragment extends Fragment {

    private LinearLayout search;
    private ImageView backSearch;
    EditText et_search;
    List<ModelUser> mUsers;
    UserAdapter userAdapter;
    private RecyclerView rcv_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        search = root.findViewById(R.id.ll_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                BottomSheetBehavior<View> bottomSheetBehavior;
                View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.sheet_search_dialog, null);
                bottomSheetDialog.setContentView(bottomSheetView);
                // Sheet behavior
                bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
                // set to behavior to expanded and minimum height to parent layout
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                // set min height to parent view
                CoordinatorLayout layout = bottomSheetDialog.findViewById(R.id.bsl_search);
                assert layout != null;
                layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                // show sheet

                et_search = bottomSheetDialog.findViewById(R.id.et_search);
                backSearch = bottomSheetDialog.findViewById(R.id.iv_back_search);
                rcv_user = bottomSheetDialog.findViewById(R.id.rcv_user);

                rcv_user.setHasFixedSize(true);
                rcv_user.setLayoutManager(new LinearLayoutManager(getContext()));

                mUsers = new ArrayList<>();
                userAdapter = new UserAdapter(getContext(),mUsers, true);
                rcv_user.setAdapter(userAdapter);

                readUser();
                backSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                et_search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        SearchUser(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                bottomSheetDialog.show();
            }
        });
        return root;
    }

    private void readUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (TextUtils.isEmpty(et_search.getText().toString())) {
                    mUsers.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ModelUser user = snapshot1.getValue(ModelUser.class);
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SearchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelUser modelUser = dataSnapshot.getValue(ModelUser.class);
                    mUsers.add(modelUser);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}