package es.upm.agmota.proyectomovies.ui.main;
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
import es.upm.agmota.proyectomovies.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

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
        Picasso.get().load(movie.getPosterPath()).into(holder.getPosterImage());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterImage;
        private TextView titleText;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.movie_poster);
            titleText = itemView.findViewById(R.id.movie_title);
        }

        public ImageView getPosterImage() {
            return posterImage;
        }

        public TextView getTitleText() {
            return titleText;
        }
    }
}
