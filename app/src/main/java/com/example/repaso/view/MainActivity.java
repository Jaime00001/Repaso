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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import com.example.repaso.repository.PreferencesManager;

import com.example.repaso.R;
import com.example.repaso.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferencesManager.init(this);
        if (PreferencesManager.getInstance().isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

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

            if (savedInstanceState == null) {
                checkWelcomeDialog(navController);
            }
        }
    }

    private void checkWelcomeDialog(NavController navController) {
        String username = PreferencesManager.getInstance().getUsername();
        if (username == null || username.trim().isEmpty()) {
            showWelcomeDialog(
                    "¡Hola!",
                    "Para personalizar tu experiencia, puedes configurar tu nombre en los ajustes. ¿Quieres hacerlo ahora?",
                    "Ir a Ajustes",
                    "Más tarde",
                    () -> navController.navigate(R.id.settingsFragment)
            );
        } else {
            com.example.repaso.repository.AppDatabase.obtener(this).pendienteDao().obtenerTodos()
                    .observe(this, new androidx.lifecycle.Observer<java.util.List<com.example.repaso.model.Pendiente>>() {
                        @Override
                        public void onChanged(java.util.List<com.example.repaso.model.Pendiente> pendientes) {
                            // Remove observer to avoid multiple dialogs
                            com.example.repaso.repository.AppDatabase.obtener(MainActivity.this)
                                    .pendienteDao().obtenerTodos().removeObserver(this);

                            if (pendientes != null && !pendientes.isEmpty()) {
                                int randomIndex = new java.util.Random().nextInt(pendientes.size());
                                com.example.repaso.model.Pendiente randomPendiente = pendientes.get(randomIndex);

                                String msg = "¿Has visto ya tu película o serie pendiente " + randomPendiente.titulo + "?";
                                showWelcomeDialog(
                                        "Hola, " + username,
                                        msg,
                                        "Ir a Seguimiento",
                                        "Aún no la he visto",
                                        () -> {
                                            Bundle args = new Bundle();
                                            args.putString("titulo", randomPendiente.titulo);
                                            args.putString("tipo", randomPendiente.tipo);
                                            navController.navigate(R.id.anadirSeguimientoFragment, args);
                                        }
                                );
                            }
                        }
                    });
        }
    }

    private void showWelcomeDialog(String title, String message, String primaryText, String secondaryText, Runnable primaryAction) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_welcome, null);
        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        android.widget.TextView tvTitle = dialogView.findViewById(R.id.dialogTitle);
        android.widget.TextView tvMsg = dialogView.findViewById(R.id.dialogMessage);
        android.widget.Button btnPrimary = dialogView.findViewById(R.id.btnPrimaryAction);
        android.widget.Button btnSecondary = dialogView.findViewById(R.id.btnSecondaryAction);

        tvTitle.setText(title);
        tvMsg.setText(message);
        btnPrimary.setText(primaryText);
        btnSecondary.setText(secondaryText);

        btnPrimary.setOnClickListener(v -> {
            dialog.dismiss();
            primaryAction.run();
        });

        btnSecondary.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                // We will navigate using an action from a global scope, or just directly to the ID.
                navController.navigate(R.id.settingsFragment);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}