package com.example.repaso.view;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.repaso.R;
import com.example.repaso.model.Movie;
import com.example.repaso.model.MovieResponse;
import com.example.repaso.model.Seguimiento;
import com.example.repaso.repository.RetrofitClient;
import com.example.repaso.repository.TMDBApi;
import com.example.repaso.viewmodel.SeguimientoViewModel;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.lifecycle.ViewModelProvider;

public class AnadirSeguimientoFragment extends Fragment {

    private String tipoSeleccionado = "movie";
    private String fechaSeleccionada = "";
    private Uri imagenRecuerdoUri = null;
    private List<Movie> resultadosBusqueda = new ArrayList<>();
    private String posterBuscado = "";
    
    private MaterialButtonToggleGroup toggleTipo;
    private EditText editBuscar;
    private Spinner spinnerResultados;
    private TextView textFecha;
    private RatingBar ratingBar;
    private ImageView imagenPrevia;
    private LinearLayout hintRecuerdo;
    private ImageButton btnBorrarImagen;
    private FrameLayout cajaImagen;

    private TMDBApi api;
    private SeguimientoViewModel viewModel;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imagenRecuerdoUri = uri;
                    imagenPrevia.setImageURI(uri);
                    imagenPrevia.setVisibility(View.VISIBLE);
                    hintRecuerdo.setVisibility(View.GONE);
                    btnBorrarImagen.setVisibility(View.VISIBLE);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anadir_seguimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        api = RetrofitClient.getClient().create(TMDBApi.class);
        viewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);

        toggleTipo = view.findViewById(R.id.toggleTipo);
        editBuscar = view.findViewById(R.id.editBuscar);
        spinnerResultados = view.findViewById(R.id.spinnerResultados);
        textFecha = view.findViewById(R.id.textFecha);
        ratingBar = view.findViewById(R.id.ratingBar);
        imagenPrevia = view.findViewById(R.id.imagenPrevia);
        hintRecuerdo = view.findViewById(R.id.hintRecuerdo);
        btnBorrarImagen = view.findViewById(R.id.btnBorrarImagen);
        cajaImagen = view.findViewById(R.id.cajaImagen);

        toggleTipo.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnPelicula) tipoSeleccionado = "movie";
                else if (checkedId == R.id.btnSerie) tipoSeleccionado = "tv";
            }
        });

        view.findViewById(R.id.btnBuscarTMDB).setOnClickListener(v -> buscarEnTMDB());

        textFecha.setOnClickListener(v -> mostrarDatePicker());

        cajaImagen.setOnClickListener(v -> mGetContent.launch("image/*"));

        btnBorrarImagen.setOnClickListener(v -> {
            imagenRecuerdoUri = null;
            imagenPrevia.setImageDrawable(null);
            imagenPrevia.setVisibility(View.GONE);
            hintRecuerdo.setVisibility(View.VISIBLE);
            btnBorrarImagen.setVisibility(View.GONE);
        });

        view.findViewById(R.id.btnGuardarSeguimiento).setOnClickListener(v -> guardarSeguimiento());
    }

    private void buscarEnTMDB() {
        String query = editBuscar.getText().toString().trim();
        if (query.isEmpty()) return;

        Call<MovieResponse> call = "tv".equals(tipoSeleccionado) ? api.searchSeries(query) : api.searchMovies(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultadosBusqueda = response.body().results;
                    List<String> titulos = new ArrayList<>();
                    for (Movie m : resultadosBusqueda) {
                        titulos.add(m.getDisplayTitle());
                    }
                    if (titulos.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                        spinnerResultados.setVisibility(View.GONE);
                        return;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, titulos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerResultados.setAdapter(adapter);
                    spinnerResultados.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatePicker() {
        Calendar calc = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            textFecha.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
        }, calc.get(Calendar.YEAR), calc.get(Calendar.MONTH), calc.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void guardarSeguimiento() {
        if (fechaSeleccionada.isEmpty()) {
            Toast.makeText(getContext(), "Selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = "";
        String imagenPathDB = "";
        int tmdbId = 0;

        // Coger info del TMDB si hay resultados seleccionados, si no, del texto que buscaron.
        if (spinnerResultados.getVisibility() == View.VISIBLE && spinnerResultados.getSelectedItemPosition() >= 0) {
            int pos = spinnerResultados.getSelectedItemPosition();
            if (pos < resultadosBusqueda.size()) {
                Movie m = resultadosBusqueda.get(pos);
                titulo = m.getDisplayTitle();
                tmdbId = m.id;
                imagenPathDB = m.poster_path != null ? m.poster_path : m.backdrop_path;
            }
        } else {
            titulo = editBuscar.getText().toString().trim();
        }

        if (titulo.isEmpty()) {
            Toast.makeText(getContext(), "Añade un titulo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si subió su propia imagen, lo guardamos como un uri string para usarlo luego
        if (imagenRecuerdoUri != null) {
            imagenPathDB = imagenRecuerdoUri.toString(); 
        }

        String finalTitulo = titulo;
        String finalImagenPathDB = imagenPathDB;
        int finalTmdbId = tmdbId;

        // Comprobación de existencia. El "repository" permite insertar
        // Como nos piden "Un detalle interesante es que la aplicación no permita añadir seguimiento de una peli o serie de la que ya hay"
        viewModel.getSeguimientos().observe(getViewLifecycleOwner(), items -> {
            // Desinscribirse de esta observación directamente o hacer una validación on-shot.
            // Para simplificar "hazlo de la manera mas sencilla posible", validamos aquí y luego ejecutamos insert/popbackstack.
            boolean existe = false;
            if (items != null) {
                for (Seguimiento s : items) {
                    if (s.titulo.equalsIgnoreCase(finalTitulo) && s.tipo.equals(tipoSeleccionado)) {
                        existe = true;
                        break;
                    }
                }
            }
            if (existe) {
                Toast.makeText(getContext(), "Error: Ya tienes este contenido en seguimiento", Toast.LENGTH_LONG).show();
            } else {
                Seguimiento nuevoSeguimiento = new Seguimiento();
                nuevoSeguimiento.titulo = finalTitulo;
                nuevoSeguimiento.fecha = fechaSeleccionada;
                nuevoSeguimiento.puntuacion = ratingBar.getRating();
                nuevoSeguimiento.tipo = tipoSeleccionado;
                nuevoSeguimiento.imagenPath = finalImagenPathDB; // TMDB path (empieza por /) o Local Uri content://
                nuevoSeguimiento.tmdbId = finalTmdbId;

                viewModel.insertar(nuevoSeguimiento);
                Toast.makeText(getContext(), "Guardado con éxito", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack(); // O navController.navigateUp()
            }
            // Importante quitar el observer para no ciclar
            viewModel.getSeguimientos().removeObservers(getViewLifecycleOwner());
        });
    }
}
