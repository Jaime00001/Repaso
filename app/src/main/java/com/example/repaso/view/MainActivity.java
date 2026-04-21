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

            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    R.id.menuFragment, R.id.pendientesFragment, R.id.seguimientoFragment
            ).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);

            // Navegación manual del Bottom Nav
            binding.bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_pendientes) {
                    navController.navigate(R.id.pendientesFragment);
                    return true;
                } else if (id == R.id.nav_explorar) {
                    navController.navigate(R.id.menuFragment);
                    return true;
                } else if (id == R.id.nav_seguimiento) {
                    navController.navigate(R.id.seguimientoFragment);
                    return true;
                }
                return false;
            });

            // Seleccionar "Explorar" por defecto
            binding.bottomNav.setSelectedItemId(R.id.nav_explorar);
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