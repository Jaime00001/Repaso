package com.example.repaso.view;

import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.repaso.R;
import com.example.repaso.databinding.FragmentDetallesBinding;
import com.example.repaso.model.MovieDetail;
import com.example.repaso.viewmodel.DetailViewModel;

public class DetallesFragment extends Fragment {

    private FragmentDetallesBinding binding;
    private DetailViewModel viewModel;
    private String videoKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetallesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        int id = getArguments().getInt("itemId");
        String type = getArguments().getString("type", "movie");
        
        ((androidx.appcompat.app.AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detalle");
        ((androidx.appcompat.app.AppCompatActivity)getActivity()).getSupportActionBar().show();

        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        viewModel.loadDetail(id, type);

        viewModel.getDetail().observe(getViewLifecycleOwner(), d -> {
            binding.tituloDestacado.setText(d.title != null ? d.title : d.name);
            binding.textoDescripcion.setText(d.overview);
            
            String networks = "";
            if (d.networks != null && !d.networks.isEmpty()) {
                networks = " · " + d.networks.get(0).name;
            }
            
            String seasonsStr = "";
            if (d.number_of_seasons > 0) {
                seasonsStr = d.number_of_seasons + " temporadas";
            }
            
            String subtitle = seasonsStr + networks;
            
            if (!"tv".equals(type) && d.runtime > 0) {
                int hours = d.runtime / 60;
                int mins = d.runtime % 60;
                subtitle = hours + " h " + mins + " min";
            }
            
            if (subtitle.startsWith(" · ")) subtitle = subtitle.substring(3);
            binding.textoSecundario.setText(subtitle);

            binding.cajaGeneros.removeAllViews();
            if (d.genres != null) {
                float density = getResources().getDisplayMetrics().density;
                int pxH = (int)(16 * density);
                int pxV = (int)(6 * density);
                
                for (MovieDetail.Genre g : d.genres) {
                    android.widget.TextView tv = new android.widget.TextView(getContext());
                    tv.setText(g.name);
                    tv.setBackgroundResource(R.drawable.bg_chip);
                    tv.setPadding(pxH, pxV, pxH, pxV);
                    tv.setTextColor(0xFF444444);
                    tv.setTextSize(14f);
                    android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, (int)(8 * density), 0);
                    tv.setLayoutParams(params);
                    binding.cajaGeneros.addView(tv);
                }
            }

            if ("tv".equals(type)) {
                binding.etiquetaTipo.setText("Serie de TV");
                binding.etiquetaTemporadas.setText(String.valueOf(d.number_of_seasons));
                binding.etiquetaPlataforma.setText(networks.replace(" · ", ""));
            } else {
                binding.etiquetaTipo.setText("Película");
                binding.seccionTemporadas.setVisibility(View.GONE);
                binding.seccionPlataforma.setVisibility(View.GONE);
            }

            String imgPath = d.backdrop_path != null ? d.backdrop_path : d.poster_path;
            Glide.with(this).load("https://image.tmdb.org/t/p/w500" + imgPath)
                    .into(binding.portadaDetalle);
        });

        viewModel.getVideoKey().observe(getViewLifecycleOwner(), key -> videoKey = key);

        binding.botonVerTrailer.setOnClickListener(v -> {
            if (videoKey == null) return;

            String url = "https://www.youtube.com/watch?v=" + videoKey;

            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            try {
                startActivity(appIntent);
            } catch (ActivityNotFoundException e) {
                startActivity(webIntent);
            }
        });
    }
}