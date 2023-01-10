package com.rbt.comunity.fragment.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rbt.comunity.Helper.helper;
import com.rbt.comunity.R;
import com.rbt.comunity.activites.post.StatusActivity;
import com.rbt.comunity.adapter.PostAdapter;
import com.rbt.comunity.adapter.StatusAdapter;
import com.rbt.comunity.model.ModelPost;
import com.rbt.comunity.model.ModelStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<ModelPost> postList;

    ListView listView;
    AlertDialog.Builder dialog;
    List<ModelStatus> modelStatuses = new ArrayList<>();

    StatusAdapter adapter;
    helper db = new helper(getContext());
    Button btn_status;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.rcv_post_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        db = new helper(getActivity().getApplicationContext());
        btn_status = root.findViewById(R.id.btn_status);
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StatusActivity.class);
                startActivity(intent);
            }
        });

        listView = root.findViewById(R.id.list_status);
        adapter = new StatusAdapter(getContext(), modelStatuses);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String statusId = modelStatuses.get(position).getId();
                final String name = modelStatuses.get(position).getName();
                final String status = modelStatuses.get(position).getStatus();

                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                dialog = new AlertDialog.Builder(getContext());
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(getContext(), StatusActivity.class);
                                intent.putExtra("id", statusId);
                                intent.putExtra("name", name);
                                intent.putExtra("status", status);
                                startActivity(intent);
                                break;
                            case 1:
                                db.delete(Integer.parseInt(statusId));
                                modelStatuses.clear();

                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
        getStatus();


        readPost();
        return root;
    }

    private void getStatus() {
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for (int i = 0; i < rows.size(); i++) {

            String id = rows.get(i).get("id");
            String name = rows.get(i).get("name");
            String status = rows.get(i).get("status");

            ModelStatus data = new ModelStatus();
            data.setId(id);
            data.setName(name);
            data.setStatus(status);
            modelStatuses.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    private void readPost() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelPost post = dataSnapshot.getValue(ModelPost.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        modelStatuses.clear();
        getStatus();
    }
}