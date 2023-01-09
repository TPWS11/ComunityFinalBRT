package com.rbt.comunity.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.rbt.comunity.R;
import com.rbt.comunity.fragment.comunity.ComunityFragment;
import com.rbt.comunity.fragment.home.HomeFragment;
import com.rbt.comunity.fragment.search.SearchFragment;
import com.rbt.comunity.fragment.user.UserFragment;

import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Deque<Integer> integers = new ArrayDeque<>(4);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bnv);

        integers.push(R.id.bnv);

        loadFragment(new HomeFragment());

        bottomNavigationView.setSelectedItemId(R.id.bnv);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (integers.contains(id)) {
                    if (id == R.id.bnv) {
                        if (integers.size() != 1) {
                            if (flag) {
                                integers.addFirst(R.id.bnv);
                                flag = false;
                            }
                        }
                    }
                    integers.remove(id);
                }
                integers.push(id);

                loadFragment(getFragment(item.getItemId()));

                return true;
            }
        });
    }
    private Fragment getFragment(int itemId) {
        switch (itemId) {
            case R.id.bnv_home:
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                return new HomeFragment();
            case R.id.search:
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                return new SearchFragment();
            case R.id.comunity:
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                return new ComunityFragment();
            case R.id.user:
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                return new UserFragment();
        }
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        return new HomeFragment();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        integers.pop();

        if (!integers.isEmpty()) {
            loadFragment(getFragment(integers.peek()));
        } else {
            finish();
        }
    }
}