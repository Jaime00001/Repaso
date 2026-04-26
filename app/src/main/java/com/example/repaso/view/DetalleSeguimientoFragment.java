package com.example.repaso.view;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.repaso.R;
import com.example.repaso.model.MovieDetail;
import com.example.repaso.repository.RetrofitClient;
import com.example.repaso.viewmodel.SeguimientoViewModel;
import com.example.repaso.repository.TMDBApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.lifecycle.ViewModelProvider;

public class DetalleSeguimientoFragment extends Fragment {

    private SeguimientoViewModel viewModel;
    private TMDBApi api;

    private ImageView imagenTMDB;
    private TextView tituloDetalle;
    private TextView subtituloTMDB;
    private TextView fechaDetalle;
    private RatingBar ratingDetalle;
    private ImageView imagenRecuerdoDetalle;
    private View cardImagenRecuerdo;
    private TextView labelImagenRecuerdo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_seguimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);
        api = RetrofitClient.getClient().create(TMDBApi.class);

        imagenTMDB = view.findViewById(R.id.imagenTMDB);
        tituloDetalle = view.findViewById(R.id.tituloDetalle);
        subtituloTMDB = view.findViewById(R.id.subtituloTMDB);
        fechaDetalle = view.findViewById(R.id.fechaDetalle);
        ratingDetalle = view.findViewById(R.id.ratingDetalle);
        imagenRecuerdoDetalle = view.findViewById(R.id.imagenRecuerdoDetalle);
        cardImagenRecuerdo = view.findViewById(R.id.cardImagenRecuerdo);
        labelImagenRecuerdo = view.findViewById(R.id.labelImagenRecuerdo);

        int id = getArguments() != null ? getArguments().getInt("seguimientoId", -1) : -1;

        if (id != -1) {
            viewModel.getSeguimientoById(id).observe(getViewLifecycleOwner(), seguimiento -> {
                if (seguimiento != null) {
                    tituloDetalle.setText(seguimiento.titulo);
                    fechaDetalle.setText(seguimiento.fecha);
                    ratingDetalle.setRating(seguimiento.puntuacion);

                    // La imagen custom de recuerdo
                    if (seguimiento.imagenPath != null && seguimiento.imagenPath.startsWith("content://")) {
                        imagenRecuerdoDetalle.setImageURI(Uri.parse(seguimiento.imagenPath));
                        cardImagenRecuerdo.setVisibility(View.VISIBLE);
                        labelImagenRecuerdo.setVisibility(View.VISIBLE);
                    } else {
                        cardImagenRecuerdo.setVisibility(View.GONE);
                        labelImagenRecuerdo.setVisibility(View.GONE);
                    }

                    // Cargar info extra de TMDB
                    if (seguimiento.tmdbId > 0) {
                        Call<MovieDetail> call = "tv".equals(seguimiento.tipo) 
                                ? api.getTvDetail(seguimiento.tmdbId) 
                                : api.getMovieDetail(seguimiento.tmdbId);
                        
                        call.enqueue(new Callback<MovieDetail>() {
                            @Override
                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    MovieDetail detail = response.body();
                                    
                                    // Poner el backdrop en la cabecera
                                    String img = detail.backdrop_path != null ? detail.backdrop_path : detail.poster_path;
                                    Glide.with(requireContext()).load("https://image.tmdb.org/t/p/w500" + img).into(imagenTMDB);

                                    // Construir subtitulos como "4 temporadas • Ciencia Ficción, Terror"
                                    StringBuilder sub = new StringBuilder();
                                    if ("tv".equals(seguimiento.tipo) && detail.number_of_seasons > 0) {
                                        sub.append(detail.number_of_seasons).append(detail.number_of_seasons == 1 ? " temporada" : " temporadas");
                                    } else if ("movie".equals(seguimiento.tipo) && detail.runtime > 0) {
                                        int h = detail.runtime / 60;
                                        int m = detail.runtime % 60;
                                        sub.append(h).append(" h ").append(m).append(" min");
                                    }

                                    if (detail.genres != null && !detail.genres.isEmpty()) {
                                        if (sub.length() > 0) sub.append(" • ");
                                        for (int i = 0; i < detail.genres.size(); i++) {
                                            sub.append(detail.genres.get(i).name);
                                            if (i < detail.genres.size() - 1) sub.append(", ");
                                        }
                                    }
                                    subtituloTMDB.setText(sub.toString());
                                }
                            }
                            @Override
                            public void onFailure(Call<MovieDetail> call, Throwable t) {}
                        });
                    } else {
                        subtituloTMDB.setText("Seguimiento manual");
                        imagenTMDB.setImageResource(android.R.color.darker_gray);
                    }
                }
            });
        }
    }
}
