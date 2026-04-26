package com.example.repaso.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.repaso.viewmodel.ApiState;
import com.example.repaso.R;
import com.example.repaso.databinding.FragmentSeriesBinding;
import com.example.repaso.model.Pendiente;
import com.example.repaso.repository.PendientesRepository;
import com.example.repaso.viewmodel.SeriesViewModel;

public class SeriesFragment extends Fragment {

    private FragmentSeriesBinding binding;
    private SeriesViewModel viewModel;
    private MovieAdapter adapter;
    private PendientesRepository pendientesRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pendientesRepository = new PendientesRepository(requireContext());

        binding.listaVista.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieAdapter(
                movie -> openDetail(movie.id),
                movie -> {
                    Pendiente p = new Pendiente();
                    p.id = movie.id;
                    p.titulo = movie.getDisplayTitle();
                    p.imagenPath = movie.backdrop_path != null ? movie.backdrop_path : movie.poster_path;
                    p.tipo = "tv";
                    pendientesRepository.insertar(p);
                    Toast.makeText(requireContext(), "Añadido a pendientes", Toast.LENGTH_SHORT).show();
                }
        );
        binding.listaVista.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SeriesViewModel.class);

        viewModel.getItems().observe(getViewLifecycleOwner(), adapter::setItems);
        viewModel.getState().observe(getViewLifecycleOwner(), s -> binding.refrescoDeslizable.setRefreshing(s == ApiState.LOADING));

        binding.refrescoDeslizable.setOnRefreshListener(() -> {
            viewModel.nextPage();
            viewModel.load();
        });

        viewModel.load();
    }

    private void openDetail(int id) {
        Bundle args = new Bundle();
        args.putInt("itemId", id);
        args.putString("type", "tv");
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_detallesFragment, args);
    }
}