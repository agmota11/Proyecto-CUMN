package es.upm.agmota.proyectomovies.ui.main;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.agmota.proyectomovies.ApiCaller.Movie;
import es.upm.agmota.proyectomovies.FavouritesActivity;
import es.upm.agmota.proyectomovies.FilmActivity;
import es.upm.agmota.proyectomovies.LocalPersistence.FavoriteList;
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
        holder.itemView.setOnLongClickListener((view) -> {
            showPopupMenu(view, movie);
            return true;
        });
    }

    private void showPopupMenu(View view, Movie movie) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popupMenu.getMenu());

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click

                switch (item.getItemId()) {
                    case R.id.menuItemFav:
                        List<Integer> favs = FavoriteList.getInstance().getMoviesIds();
                        if (favs.stream().anyMatch(x -> x == movie.getId())) {
                            FavoriteList.getInstance().removeMovie(movie);
                            Toast.makeText(view.getContext(), "Eliminando de favoritos", Toast.LENGTH_SHORT).show();
                        } else {
                            FavoriteList.getInstance().addMovie(movie);
                            Toast.makeText(view.getContext(), "Añadiendo de favoritos", Toast.LENGTH_SHORT).show();
                        }
                        return true;

                }
                return false;
            }
        });

        popupMenu.show();
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
