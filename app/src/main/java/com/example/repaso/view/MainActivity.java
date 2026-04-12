package com.example.repaso.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.repaso.R;
import com.example.repaso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

}