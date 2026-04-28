package com.example.repaso.view;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.repaso.databinding.FragmentAnadirSeguimientoBinding;
import com.example.repaso.viewmodel.SeguimientoViewModel;

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
    
    private FragmentAnadirSeguimientoBinding binding;
    private SeguimientoViewModel viewModel;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imagenRecuerdoUri = uri;
                    binding.imagenPrevia.setImageURI(uri);
                    binding.imagenPrevia.setVisibility(View.VISIBLE);
                    binding.hintRecuerdo.setVisibility(View.GONE);
                    binding.btnBorrarImagen.setVisibility(View.VISIBLE);
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAnadirSeguimientoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);

        if (getArguments() != null) {
            String argTitulo = getArguments().getString("titulo");
            String argTipo = getArguments().getString("tipo");
            if (argTitulo != null && !argTitulo.isEmpty()) {
                binding.editBuscar.setText(argTitulo);
            }
            if (argTipo != null) {
                if ("tv".equals(argTipo)) {
                    binding.toggleTipo.check(R.id.btnSerie);
                    tipoSeleccionado = "tv";
                } else {
                    binding.toggleTipo.check(R.id.btnPelicula);
                    tipoSeleccionado = "movie";
                }
            }
        }

        binding.toggleTipo.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnPelicula) tipoSeleccionado = "movie";
                else if (checkedId == R.id.btnSerie) tipoSeleccionado = "tv";
            }
        });

        binding.btnBuscarTMDB.setOnClickListener(v -> buscarEnTMDB());

        binding.textFecha.setOnClickListener(v -> mostrarDatePicker());

        binding.cajaImagen.setOnClickListener(v -> mGetContent.launch("image/*"));

        binding.btnBorrarImagen.setOnClickListener(v -> {
            imagenRecuerdoUri = null;
            binding.imagenPrevia.setImageDrawable(null);
            binding.imagenPrevia.setVisibility(View.GONE);
            binding.hintRecuerdo.setVisibility(View.VISIBLE);
            binding.btnBorrarImagen.setVisibility(View.GONE);
        });

        binding.btnGuardarSeguimiento.setOnClickListener(v -> guardarSeguimiento());
    }

    private void buscarEnTMDB() {
        String query = binding.editBuscar.getText().toString().trim();
        if (query.isEmpty()) return;

        viewModel.buscarEnTMDB(query, tipoSeleccionado, new Callback<MovieResponse>() {
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
                        binding.spinnerResultados.setVisibility(View.GONE);
                        return;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, titulos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinnerResultados.setAdapter(adapter);
                    binding.spinnerResultados.setVisibility(View.VISIBLE);
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
            binding.textFecha.setText(String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year));
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
        if (binding.spinnerResultados.getVisibility() == View.VISIBLE && binding.spinnerResultados.getSelectedItemPosition() >= 0) {
            int pos = binding.spinnerResultados.getSelectedItemPosition();
            if (pos < resultadosBusqueda.size()) {
                Movie m = resultadosBusqueda.get(pos);
                titulo = m.getDisplayTitle();
                tmdbId = m.id;
                imagenPathDB = m.poster_path != null ? m.poster_path : m.backdrop_path;
            }
        } else {
            titulo = binding.editBuscar.getText().toString().trim();
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
                nuevoSeguimiento.puntuacion = binding.ratingBar.getRating();
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
