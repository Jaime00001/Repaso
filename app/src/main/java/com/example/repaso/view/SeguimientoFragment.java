package com.example.repaso.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.repaso.R;
import com.example.repaso.databinding.FragmentSeguimientoBinding;
import com.example.repaso.viewmodel.SeguimientoViewModel;

public class SeguimientoFragment extends Fragment {

    private SeguimientoViewModel viewModel;
    private SeguimientoAdapter adapter;
    private FragmentSeguimientoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeguimientoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);

        binding.listaSeguimiento.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SeguimientoAdapter();
        adapter.setListener(id -> mostrarDetalleSeguimiento(id));
        binding.listaSeguimiento.setAdapter(adapter);

        viewModel.getSeguimientos().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            if (items == null || items.isEmpty()) {
                binding.estadoVacio.setVisibility(View.VISIBLE);
                binding.listaSeguimiento.setVisibility(View.GONE);
            } else {
                binding.estadoVacio.setVisibility(View.GONE);
                binding.listaSeguimiento.setVisibility(View.VISIBLE);
            }
        });

        binding.btnAnadir.setOnClickListener(v -> mostrarPantallaAnadir());
    }

    private void mostrarDetalleSeguimiento(int id) {
        Bundle b = new Bundle();
        b.putInt("seguimientoId", id);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_seguimientoFragment_to_detalleSeguimientoFragment, b);
    }

    private void mostrarPantallaAnadir() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_seguimientoFragment_to_anadirSeguimientoFragment);
    }
}
