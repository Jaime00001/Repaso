package com.example.repaso.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.databinding.ItemMovieBinding;
import com.example.repaso.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.Holder> {

    public interface OnClick { void onClick(Movie m); }

    private List<Movie> list = new ArrayList<>();
    private OnClick listener;

    public MovieAdapter(OnClick listener) { this.listener = listener; }

    public void setItems(List<Movie> movies) {
        list = movies;
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

        Glide.with(h.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w500" + imgPath)
                .into(h.binding.portadaItem);

        h.itemView.setOnClickListener(v -> listener.onClick(m));
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

