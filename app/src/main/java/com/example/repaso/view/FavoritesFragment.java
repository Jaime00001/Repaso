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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.repaso.R;
import com.example.repaso.databinding.FragmentFavoritesBinding;
import com.example.repaso.databinding.ItemFavoriteBinding;
import com.example.repaso.model.FavoriteItem;
import com.example.repaso.viewmodel.FavoritesViewModel;

import java.util.List;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private FavoritesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding.listaVista.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoritesAdapter();
        binding.listaVista.setAdapter(adapter);

        viewModel.getFavorites().observe(getViewLifecycleOwner(), favs -> adapter.setFavorites(favs));
        viewModel.loadFavorites();
    }

    private void openDetail(int id, String type) {
        Bundle args = new Bundle();
        args.putInt("itemId", id);
        args.putString("type", type);
        NavHostFragment.findNavController(this)
                .navigate(R.id.action_favoritosFragment_to_detallesFragment, args);
    }

    private class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavViewHolder> {
        private List<FavoriteItem> favorites;

        @NonNull
        @Override
        public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemFavoriteBinding itemBinding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FavViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
            FavoriteItem item = favorites.get(position);
            holder.binding.tvFavTitle.setText(item.getTitle());
            String imgPath = item.getPosterPath();
            if (imgPath != null && !imgPath.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load("https://image.tmdb.org/t/p/w500" + imgPath)
                        .into(holder.binding.imgFav);
            } else {
                holder.binding.imgFav.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            holder.binding.btnDeleteFav.setOnClickListener(v -> {
                // Delete from backend
                viewModel.removeFavorite(item.getId());
                // Remove from local list safely
                if (favorites != null) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        favorites.remove(pos);
                        notifyItemRemoved(pos);
                    }
                }
            });
            holder.itemView.setOnClickListener(v -> {
                openDetail(Integer.parseInt(item.getId()), item.getMediaType());
            });
        }

        @Override
        public int getItemCount() {
            return favorites == null ? 0 : favorites.size();
        }

        void setFavorites(List<FavoriteItem> favs) {
            this.favorites = favs;
            notifyDataSetChanged();
        }

        class FavViewHolder extends RecyclerView.ViewHolder {
            final ItemFavoriteBinding binding;
            FavViewHolder(@NonNull ItemFavoriteBinding b) {
                super(b.getRoot());
                this.binding = b;
            }
        }
    }
}
