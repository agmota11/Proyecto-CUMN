package es.upm.agmota.proyectomovies.ApiCaller;

import android.nfc.Tag;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Caller {

    private String endpoint = "movie/popular?api_key=&language=&page=1";
    private String Url = "https://api.themoviedb.org/3/";
    private String lenguage = "es-ES";
    private String key = "b8146d7df04facbb68dc4e520da46a93";
    private final String TAG = "API_Caller";

    // Create a Retrofit instance
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create a MovieService instance
    IMovieService movieService = retrofit.create(IMovieService.class);

    public void getPopularMovies(){
        Call<MovieResponse> call = movieService.getPopularMovies(key);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getMovies();
                    // Do something with the movie list
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
