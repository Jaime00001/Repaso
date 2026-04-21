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
import com.example.repaso.model.Pendiente;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class PendienteAdapter extends RecyclerView.Adapter<PendienteAdapter.Holder> {

    public interface OnEliminar { void eliminar(Pendiente p); }

    private List<Pendiente> list = new ArrayList<>();
    private OnEliminar listener;

    public PendienteAdapter(OnEliminar listener) {
        this.listener = listener;
    }

    public void setItems(List<Pendiente> items) {
        list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pendiente, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Pendiente p = list.get(pos);
        h.titulo.setText(p.titulo);
        Glide.with(h.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w200" + p.imagenPath)
                .into(h.imagen);
        h.btnEliminar.setOnClickListener(v -> listener.eliminar(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;
        MaterialButton btnEliminar;

        Holder(View v) {
            super(v);
            imagen = v.findViewById(R.id.imagenPendiente);
            titulo = v.findViewById(R.id.tituloPendiente);
            btnEliminar = v.findViewById(R.id.btnEliminarPendiente);
        }
    }
}
