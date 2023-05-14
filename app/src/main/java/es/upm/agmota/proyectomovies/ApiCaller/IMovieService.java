package es.upm.agmota.proyectomovies.ApiCaller;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("limit") int limit
    );

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("limit") int limit
    );

    @GET("movie/{id}")
    Call<Movie> getMovieById(
            @Path("id") int id,
            @Query("language") String language,
            @Query("api_key") String apiKey
    );

}
