package com.example.repaso.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.R;
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_seguimiento, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Seguimiento s = list.get(pos);
        h.titulo.setText(s.titulo);
        h.fecha.setText(s.fecha);

        if (s.imagenPath != null && !s.imagenPath.isEmpty()) {
            Glide.with(h.itemView.getContext())
                 .load("https://image.tmdb.org/t/p/w200" + s.imagenPath)
                 .into(h.imagen);
        } else {
            h.imagen.setImageResource(android.R.color.darker_gray);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(s.id);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;
        TextView fecha;

        Holder(View v) {
            super(v);
            imagen = v.findViewById(R.id.imagenSeguimiento);
            titulo = v.findViewById(R.id.tituloSeguimiento);
            fecha = v.findViewById(R.id.fechaSeguimiento);
        }
    }
}
