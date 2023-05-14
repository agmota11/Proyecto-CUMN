package es.upm.agmota.proyectomovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import es.upm.agmota.proyectomovies.ApiCaller.Movie;
import es.upm.agmota.proyectomovies.LocalPersistence.FavoriteList;
import es.upm.agmota.proyectomovies.ui.main.MovieAdapter;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        RecyclerView movieRecyclerView = findViewById(R.id.movieFav_recyclerView);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        FavoriteList.getInstance().setContext(this);
        List<Movie> movieList = FavoriteList.getInstance().getMovies(movieRecyclerView);
        MovieAdapter movieAdapter = new MovieAdapter(movieList);

        movieRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                 intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                Log.i("Usuers", "User logged out");
                intent = new Intent(this, EmailPasswordActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}