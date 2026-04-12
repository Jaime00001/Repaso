package com.example.repaso.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.repaso.model.MovieDetail;
import com.example.repaso.model.Video;
import com.example.repaso.model.VideoResponse;
import com.example.repaso.repository.Repository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<MovieDetail> detail = new MutableLiveData<>();
    private MutableLiveData<String> videoKey = new MutableLiveData<>();
    private MutableLiveData<ApiState> state = new MutableLiveData<>();
    private Repository repo = Repository.getInstance();

    public LiveData<MovieDetail> getDetail() { return detail; }
    public LiveData<String> getVideoKey() { return videoKey; }
    public LiveData<ApiState> getState() { return state; }

    public void loadDetail(int id, String type) {
        state.setValue(ApiState.LOADING);

        Callback<MovieDetail> callback = new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detail.setValue(response.body());
                    state.setValue(ApiState.SUCCESS);
                } else {
                    state.setValue(ApiState.ERROR);
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                state.setValue(ApiState.ERROR);
            }
        };

        if ("tv".equals(type)) {
            repo.getTvDetail(id, callback);
        } else {
            repo.getMovieDetail(id, callback);
        }

        Callback<VideoResponse> videoCallback = new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.body() != null && response.body().results != null) {
                    for (Video v : response.body().results) {
                        if ("YouTube".equals(v.site) && ("Trailer".equals(v.type) || "Teaser".equals(v.type))) {
                            videoKey.setValue(v.key);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {}
        };

        if ("tv".equals(type)) {
            repo.getTvVideos(id, videoCallback);
        } else {
            repo.getMovieVideos(id, videoCallback);
        }
    }
}
