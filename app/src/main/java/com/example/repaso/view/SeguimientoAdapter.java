package com.example.repaso.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.databinding.ItemSeguimientoBinding;
import com.example.repaso.model.Seguimiento;

import java.util.ArrayList;
import java.util.List;

public class SeguimientoAdapter extends RecyclerView.Adapter<SeguimientoAdapter.Holder> {

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    private List<Seguimiento> list = new ArrayList<>();
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Seguimiento> items) {
        list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeguimientoBinding binding = ItemSeguimientoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Seguimiento s = list.get(pos);
        h.binding.tituloSeguimiento.setText(s.titulo);
        h.binding.fechaSeguimiento.setText(s.fecha);

        if (s.imagenPath != null && !s.imagenPath.isEmpty()) {
            Glide.with(h.itemView.getContext())
                 .load("https://image.tmdb.org/t/p/w200" + s.imagenPath)
                 .into(h.binding.imagenSeguimiento);
        } else {
            h.binding.imagenSeguimiento.setImageResource(android.R.color.darker_gray);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(s.id);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ItemSeguimientoBinding binding;

        Holder(ItemSeguimientoBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
