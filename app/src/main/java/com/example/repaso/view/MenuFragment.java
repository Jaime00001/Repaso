package com.example.repaso.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.repaso.databinding.FragmentMenuBinding;
import com.example.repaso.R;
import com.google.android.material.tabs.TabLayout;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.pestanas.addTab(binding.pestanas.newTab().setText("Series"));
        binding.pestanas.addTab(binding.pestanas.newTab().setText("Películas"));

        loadFragment(new SeriesFragment());

        binding.pestanas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    loadFragment(new SeriesFragment());
                else
                    loadFragment(new PeliculasFragment());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadFragment(Fragment f) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(binding.contenedorMenus.getId(), f)
                .commit();
    }
}