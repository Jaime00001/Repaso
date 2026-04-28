package com.example.repaso.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.repaso.databinding.FragmentSettingsBinding;
import com.example.repaso.viewmodel.SettingsViewModel;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    private final String[] languageNames = {"Español", "Inglés", "Francés", "Alemán"};
    private final String[] languageCodes = {"es", "en", "fr", "de"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        setupLanguageSpinner();
        setupThemeRadios();
        setupButtons();
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLanguage.setAdapter(adapter);

        viewModel.language.observe(getViewLifecycleOwner(), lang -> {
            if (lang != null) {
                for (int i = 0; i < languageCodes.length; i++) {
                    if (languageCodes[i].equals(lang)) {
                        binding.spinnerLanguage.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void setupThemeRadios() {
        viewModel.darkMode.observe(getViewLifecycleOwner(), isDark -> {
            if (isDark != null) {
                if (isDark) {
                    binding.radioDark.setChecked(true);
                } else {
                    binding.radioLight.setChecked(true);
                }
            }
        });

        binding.radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.darkMode.setValue(checkedId == binding.radioDark.getId());
        });
    }

    private void setupButtons() {
        binding.btnSave.setOnClickListener(v -> {
            // Update language from spinner before saving
            int selectedPosition = binding.spinnerLanguage.getSelectedItemPosition();
            viewModel.language.setValue(languageCodes[selectedPosition]);
            
            viewModel.savePreferences();

            // Apply Theme if changed
            if (viewModel.darkMode.getValue() != null && viewModel.darkMode.getValue()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            Toast.makeText(requireContext(), "Ajustes guardados", Toast.LENGTH_SHORT).show();
        });

        binding.btnReset.setOnClickListener(v -> {
            viewModel.resetPreferences();
            binding.spinnerLanguage.setSelection(0);
            Toast.makeText(requireContext(), "Preferencias restablecidas", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
