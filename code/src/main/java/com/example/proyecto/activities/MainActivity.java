package com.example.proyecto.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.proyecto.databinding.ActivityMainBinding;
import com.example.proyecto.fragments.FavouritesFragment;
import com.example.proyecto.fragments.HomeFragment;
import com.example.proyecto.fragments.ProfileFragment;
import com.example.proyecto.R;
import com.example.proyecto.fragments.RentFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationview.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.menu_prodile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.menu_rent:
                    replaceFragment(new RentFragment());
                    break;
                case R.id.menu_favourite:
                    replaceFragment(new FavouritesFragment());
                default:

            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout,fragment);
        fragmentTransaction.commit();
    }
}