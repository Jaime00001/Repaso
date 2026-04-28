package com.example.repaso.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.databinding.ItemPendienteBinding;
import com.example.repaso.model.Pendiente;

import java.util.ArrayList;
import java.util.List;

public class PendienteAdapter extends RecyclerView.Adapter<PendienteAdapter.Holder> {

    public interface OnEliminar { void eliminar(Pendiente p); }
    public interface OnClick { void onClick(Pendiente p); }

    private List<Pendiente> list = new ArrayList<>();
    private OnEliminar eliminarListener;
    private OnClick clickListener;

    public PendienteAdapter(OnEliminar eliminarListener, OnClick clickListener) {
        this.eliminarListener = eliminarListener;
        this.clickListener = clickListener;
    }

    public void setItems(List<Pendiente> items) {
        list = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPendienteBinding binding = ItemPendienteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Pendiente p = list.get(pos);

        h.binding.tituloPrincipal.setText(p.titulo);

        // Sin subtítulo ni sinopsis en pendientes
        h.binding.subtituloItem.setVisibility(android.view.View.GONE);
        h.binding.sinopsisCorta.setVisibility(android.view.View.GONE);

        boolean isWifiOnly = com.example.repaso.repository.PreferencesManager.getInstance().isWifiOnly();

        if (isWifiOnly) {
            Glide.with(h.itemView.getContext())
                    .load(android.R.drawable.ic_menu_gallery)
                    .into(h.binding.portadaItem);
        } else {
            Glide.with(h.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + p.imagenPath)
                    .into(h.binding.portadaItem);
        }

        h.itemView.setOnClickListener(v -> clickListener.onClick(p));
        h.binding.btnQuitarPendiente.setOnClickListener(v -> eliminarListener.eliminar(p));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        ItemPendienteBinding binding;
        Holder(ItemPendienteBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
