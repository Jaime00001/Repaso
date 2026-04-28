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

import com.example.repaso.R;
import com.example.repaso.databinding.FragmentPendientesBinding;
import com.example.repaso.model.Pendiente;
import com.example.repaso.viewmodel.PendientesViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PendientesFragment extends Fragment {

    private PendientesViewModel viewModel;
    private PendienteAdapter adapter;
    private FragmentPendientesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPendientesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PendientesViewModel.class);

        binding.listaPendientes.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PendienteAdapter(
                p -> viewModel.eliminar(p.id),
                p -> openDetail(p.id, p.tipo)
        );
        binding.listaPendientes.setAdapter(adapter);

        viewModel.getPendientes().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items);
            if (items == null || items.isEmpty()) {
                binding.estadoVacio.setVisibility(View.VISIBLE);
                binding.listaPendientes.setVisibility(View.GONE);
            } else {
                binding.estadoVacio.setVisibility(View.GONE);
                binding.listaPendientes.setVisibility(View.VISIBLE);
            }
        });

        // Botón "VER TODAS LAS PELÍCULAS" → ir a pestaña Explorar
        binding.btnVerPeliculas.setOnClickListener(v -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.menuFragment);
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
