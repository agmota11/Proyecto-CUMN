package es.upm.agmota.proyectomovies.LocalPersistence;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.upm.agmota.proyectomovies.ApiCaller.Caller;
import es.upm.agmota.proyectomovies.ApiCaller.IMovieService;
import es.upm.agmota.proyectomovies.ApiCaller.Movie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteList {

    private static FavoriteList instance = new FavoriteList();

    private FileManager fileManager;

    private List<Movie> movies = new ArrayList<>();

    private String Url = "https://api.themoviedb.org/3/";
    private String language = "es-ES";
    private String key = "b8146d7df04facbb68dc4e520da46a93";

    private final String TAG = "Fav_act";

    private boolean firsTimeLoad = false;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create a MovieService instance
    IMovieService movieService = retrofit.create(IMovieService.class);

    public static FavoriteList getInstance() {
        return instance;
    }

    public List<Integer> getMoviesIds() {
        List<Integer> ids = new ArrayList<>();
        movies.forEach(x -> ids.add(x.getId()));
        return ids;
    }

    public void setContext(Context context) {
        fileManager = new FileManager("Favourites.txt", context);
        if (!firsTimeLoad) {
            firsTimeLoad = true;
            getMoviesByIds(fileManager.loadFile());
        }
    }

    public List<Movie> getMovies(RecyclerView recyclerView) {
        try {
            List<Movie> aux = new ArrayList<>();
            Caller caller = new Caller(aux, recyclerView);
            caller.getMoviesByIds(getMoviesIds());
            this.movies = aux;
            return aux;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void addMovie(Movie movie) {
        this.movies.add(movie);
        fileManager.saveFile(getMoviesIds());
    }

    public void removeMovie(Movie movie) {
        this.movies.remove(movies.stream().filter(x -> x.getId() == movie.getId()).findFirst().get());
        fileManager.saveFile(getMoviesIds());
    }


    private void getMoviesByIds(List<Integer> ids){
        List<Movie> aux = new ArrayList<>();
        for (int id : ids) {
            Log.i(TAG, "Request sended");
            Call<Movie> call = movieService.getMovieById(id, language, key);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if (response.isSuccessful()) {
                        aux.add(response.body());
                        Log.i(TAG, "Request delivered");
                    } else {
                        Log.e(TAG, "Couldn't get favourite movies from the api");
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(TAG, "Couldn't get favourite movies from the api");
                }
            });
        }
        this.movies = aux;
    }
}
