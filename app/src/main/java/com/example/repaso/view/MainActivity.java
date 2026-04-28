package com.example.repaso.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavDestination;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

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

            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    R.id.menuFragment, R.id.pendientesFragment, R.id.seguimientoFragment
            ).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);

            NavigationUI.setupWithNavController(binding.bottomNav, navController);

            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    int id = destination.getId();
                    if (id == R.id.menuFragment || id == R.id.pendientesFragment || id == R.id.seguimientoFragment) {
                        binding.bottomNav.setVisibility(View.VISIBLE);
                    } else {
                        binding.bottomNav.setVisibility(View.GONE);
                    }
                }
            });
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