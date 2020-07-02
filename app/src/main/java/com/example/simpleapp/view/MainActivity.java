package com.example.simpleapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.simpleapp.R;
import com.example.simpleapp.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    FragmentTransaction ft;
    MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        initFragment();
    }

    private void initFragment() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.menu_nowplaying : {
                        fragment = new MoviesFragment();
                        break;
                    }
                    case R.id.menu_liked : {
                        fragment = new LikedFragment();
                        break;
                    }
                }

                if (fragment != null){
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,fragment);
                    ft.commit();
                    return true;
                }else{
                    return false;
                }
            }
        });

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,new MoviesFragment());
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}