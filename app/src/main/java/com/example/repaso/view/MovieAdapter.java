package com.example.repaso.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.databinding.ItemMovieBinding;
import com.example.repaso.model.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {

    public interface OnClick { void onClick(Movie m); }
    public interface OnAddClick { void onAdd(Movie m); }
    public interface OnFavoriteClick { void onToggleFavorite(Movie m, boolean isCurrentlyFavorite); }

    private List<Movie> list = new ArrayList<>();
    private Set<String> favoriteIds = new HashSet<>();
    private OnClick listener;
    private OnAddClick addListener;
    private OnFavoriteClick favoriteListener;

    public MovieAdapter(OnClick listener, OnAddClick addListener, OnFavoriteClick favoriteListener) {
        this.listener = listener;
        this.addListener = addListener;
        this.favoriteListener = favoriteListener;
    }

    public void setItems(List<Movie> movies) {
        this.list = movies;
        notifyDataSetChanged();
    }

    public void setFavoriteIds(Set<String> ids) {
        this.favoriteIds = ids != null ? ids : new HashSet<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Movie m = list.get(pos);

        h.binding.tituloPrincipal.setText(m.getDisplayTitle());

        String date = m.getDisplayDate();
        if (date == null) date = "";
        else if (date.length() > 4) date = date.substring(0, 4);

        String subtitle = date + " • Puntuación: " + String.format("%.1f", m.vote_average) + " ⭐";
        h.binding.subtituloItem.setText(subtitle);

        if (m.overview != null && !m.overview.isEmpty()) {
            h.binding.sinopsisCorta.setText(m.overview);
            h.binding.sinopsisCorta.setVisibility(android.view.View.VISIBLE);
        } else {
            h.binding.sinopsisCorta.setVisibility(android.view.View.GONE);
        }

        String imgPath = m.backdrop_path != null ? m.backdrop_path : m.poster_path;
        boolean isWifiOnly = com.example.repaso.repository.PreferencesManager.getInstance().isWifiOnly();
        if (isWifiOnly) {
            Glide.with(h.itemView.getContext())
                    .load(android.R.drawable.ic_menu_gallery)
                    .into(h.binding.portadaItem);
        } else {
            Glide.with(h.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + imgPath)
                    .into(h.binding.portadaItem);
        }

        // Click listeners
        h.itemView.setOnClickListener(v -> listener.onClick(m));
        h.binding.btnAnadirPendiente.setOnClickListener(v -> addListener.onAdd(m));

        // Favorite button state
        boolean isFav = favoriteIds.contains(String.valueOf(m.id));
        int starRes = isFav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off;
        h.binding.btnFavorito.setImageResource(starRes);
        h.binding.btnFavorito.setOnClickListener(v -> {
            if (favoriteListener != null) {
                favoriteListener.onToggleFavorite(m, isFav);
            }
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ItemMovieBinding binding;
        Holder(ItemMovieBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}


