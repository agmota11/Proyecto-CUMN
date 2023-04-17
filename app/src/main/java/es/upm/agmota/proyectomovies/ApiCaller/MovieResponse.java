package es.upm.agmota.proyectomovies.ApiCaller;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
        @SerializedName("page")
        private int page;

        @SerializedName("total_results")
        private int totalResults;

        @SerializedName("total_pages")
        private int totalPages;

        @SerializedName("results")
        private List<Movie> movies;

        // Getters and setters

        public List<Movie> getMovies() {
                return movies;
        }
}
