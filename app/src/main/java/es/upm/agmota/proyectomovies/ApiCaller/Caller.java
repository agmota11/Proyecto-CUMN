package es.upm.agmota.proyectomovies.ApiCaller;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Caller {

    private String Url = "https://api.themoviedb.org/3/";
    private String language = "es-ES";
    private String key = "b8146d7df04facbb68dc4e520da46a93";
    private final String TAG = "API_Caller";
    private List<Movie> movies;
    private RecyclerView view;

    // Create a Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create a MovieService instance
    IMovieService movieService = retrofit.create(IMovieService.class);

    public Caller(List<Movie> movies, RecyclerView view) {
        this.movies = movies;
        this.view = view;
    }

    public void getPopularMovies(){
        Call<MovieResponse> call = movieService.getPopularMovies(key, language, 50);
        Log.i(TAG, "Request sended");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    movies.addAll(response.body().getMovies());
                    Log.i(TAG,  "Request delivered");
                    view.getAdapter().notifyDataSetChanged();
                } else {
                    Log.e(TAG,  "Couldn't get popular movies from the api");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG,  "Couldn't get popular movies from the api");
            }
        });
    }

}
