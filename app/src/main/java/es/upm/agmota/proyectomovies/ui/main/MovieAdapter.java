package es.upm.agmota.proyectomovies.ui.main;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.agmota.proyectomovies.ApiCaller.Movie;
import es.upm.agmota.proyectomovies.FavouritesActivity;
import es.upm.agmota.proyectomovies.FilmActivity;
import es.upm.agmota.proyectomovies.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }
    private final String IMAGES_BASE = "https://image.tmdb.org/t/p/w500/";

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.getTitleText().setText(movie.getTitle());
        holder.getDateText().setText(movie.getReleaseDate());
        Picasso.get().load(IMAGES_BASE + movie.getPosterPath()).into(holder.getPosterImage());
        holder.itemView.setOnClickListener((view) -> {
            Intent intent = new Intent(view.getContext(), FilmActivity.class);
            intent.putExtra("movie", movie);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;
        private TextView titleText;
        private TextView dateText;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.movie_poster);
            titleText = itemView.findViewById(R.id.movie_title);
            dateText = itemView.findViewById(R.id.movie_release_date);
        }

        public ImageView getPosterImage() {
            return posterImage;
        }

        public TextView getTitleText() {
            return titleText;
        }

        public TextView getDateText() {
            return dateText;
        }
    }
}
