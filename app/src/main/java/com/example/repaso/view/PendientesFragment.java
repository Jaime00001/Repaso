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
import com.example.repaso.model.Pendiente;
import com.example.repaso.viewmodel.PendientesViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PendientesFragment extends Fragment {

    private PendientesViewModel viewModel;
    private PendienteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pendientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PendientesViewModel.class);

        View estadoVacio = view.findViewById(R.id.estadoVacio);
        RecyclerView recycler = view.findViewById(R.id.listaPendientes);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PendienteAdapter(
                p -> viewModel.eliminar(p.id),
                p -> openDetail(p.id, p.tipo)
        );
        recycler.setAdapter(adapter);

        viewModel.getPendientes().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            if (items == null || items.isEmpty()) {
                estadoVacio.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            } else {
                estadoVacio.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }
        });

        // Botón "VER TODAS LAS PELÍCULAS" → ir a pestaña Explorar
        view.findViewById(R.id.btnVerPeliculas).setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_explorar);
            }
        });
    }

    private void openDetail(int id, String tipo) {
        Bundle args = new Bundle();
        args.putInt("itemId", id);
        args.putString("type", tipo != null ? tipo : "movie");
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_pendientesFragment_to_detallesFragment, args);
    }
}
