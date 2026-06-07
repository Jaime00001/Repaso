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
import com.example.repaso.databinding.FragmentPeliculasBinding;
import com.example.repaso.model.Pendiente;
import com.example.repaso.viewmodel.PeliculasViewModel;
import com.example.repaso.viewmodel.FavoritesViewModel;
import com.example.repaso.model.FavoriteItem;
import java.util.HashSet;
import java.util.Set;

public class PeliculasFragment extends Fragment {

    private FragmentPeliculasBinding binding;
    private PeliculasViewModel viewModel;
    private MovieAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPeliculasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PeliculasViewModel.class);

        binding.listaVista.setLayoutManager(new LinearLayoutManager(getContext()));
        FavoritesViewModel favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        adapter = new MovieAdapter(
                movie -> openDetail(movie.id),
                movie -> {
                    Pendiente p = new Pendiente();
                    p.id = movie.id;
                    p.titulo = movie.getDisplayTitle();
                    p.imagenPath = movie.backdrop_path != null ? movie.backdrop_path : movie.poster_path;
                    p.tipo = "movie";
                    p.userId = com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
                    viewModel.anadirAPendientes(p);
                    Toast.makeText(requireContext(), "Añadido a pendientes", Toast.LENGTH_SHORT).show();
                },
                (movie, isFav) -> {
                    if (isFav) {
                        favoritesViewModel.removeFavorite(String.valueOf(movie.id));
                    } else {
                        FavoriteItem item = new FavoriteItem(String.valueOf(movie.id), movie.getDisplayTitle(), "movie", movie.backdrop_path != null ? movie.backdrop_path : movie.poster_path);
                        favoritesViewModel.addFavorite(item);
                    }
                }
        );
        favoritesViewModel.getFavorites().observe(getViewLifecycleOwner(), favList -> {
            Set<String> ids = new HashSet<>();
            for (FavoriteItem fi : favList) {
                ids.add(fi.getId());
            }
            adapter.setFavoriteIds(ids);
        });
        favoritesViewModel.loadFavorites();
        binding.listaVista.setAdapter(adapter);

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
        args.putString("type", "movie");
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_menuFragment_to_detallesFragment, args);
    }
}