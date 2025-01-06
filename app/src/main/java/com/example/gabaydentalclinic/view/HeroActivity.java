package com.example.gabaydentalclinic.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.gabaydentalclinic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nfvHero);

        if (fragment instanceof NavHostFragment) {
            NavHostFragment nvfHero = (NavHostFragment) fragment;
            NavigationUI.setupWithNavController(bnvHero, nvfHero.getNavController());
        }
    }
}
