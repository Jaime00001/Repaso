package com.example.repaso.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.repaso.R;
import com.example.repaso.repository.PendientesRepository;

public class PendientesFragment extends Fragment {

    private PendientesRepository repository;
    private PendienteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pendientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        repository = new PendientesRepository(requireContext());

        RecyclerView recycler = view.findViewById(R.id.listaPendientes);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PendienteAdapter(p -> repository.eliminar(p.id));
        recycler.setAdapter(adapter);

        repository.obtenerTodos().observe(getViewLifecycleOwner(), adapter::setItems);
    }
}
