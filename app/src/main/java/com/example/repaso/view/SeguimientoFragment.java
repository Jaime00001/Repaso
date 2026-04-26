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
import androidx.recyclerview.widget.RecyclerView;

import com.example.repaso.R;
import com.example.repaso.viewmodel.SeguimientoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SeguimientoFragment extends Fragment {

    private SeguimientoViewModel viewModel;
    private SeguimientoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seguimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);

        View estadoVacio = view.findViewById(R.id.estadoVacio);
        RecyclerView recycler = view.findViewById(R.id.listaSeguimiento);
        FloatingActionButton btnAnadir = view.findViewById(R.id.btnAnadir);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SeguimientoAdapter();
        adapter.setListener(id -> mostrarDetalleSeguimiento(id));
        recycler.setAdapter(adapter);

        viewModel.getSeguimientos().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            if (items == null || items.isEmpty()) {
                estadoVacio.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            } else {
                estadoVacio.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }
        });

        btnAnadir.setOnClickListener(v -> mostrarPantallaAnadir());
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
