package es.upm.agmota.proyectomovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.upm.agmota.proyectomovies.ApiCaller.Movie;

public class FilmActivity extends AppCompatActivity {

    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView releaseDateTextView;
    private TextView overviewTextView;
    private Button likeButton;
    private Button dislikeButton;
    private TextView likesTextView;
    private TextView commentsTextView;
    private EditText commentEditText;
    private Button postCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        posterImageView = findViewById(R.id.film_poster_image);
        titleTextView = findViewById(R.id.film_title_text);
        releaseDateTextView = findViewById(R.id.film_release_date_text);
        overviewTextView = findViewById(R.id.overview_text);
        likeButton = findViewById(R.id.like_button);
        dislikeButton = findViewById(R.id.dislike_button);
        likesTextView = findViewById(R.id.like_count_text);
        commentsTextView = findViewById(R.id.comment_section_label);
        commentEditText = findViewById(R.id.comment_edit_text);
        postCommentButton = findViewById(R.id.comment_submit_button);

        // Retrieve movie data
        Movie movie = (Movie) getIntent().getExtras().get("movie");


        // Display movie data
        titleTextView.setText(movie.getTitle());
        Picasso.get().load(movie.getPosterPath()).into(posterImageView);
        releaseDateTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        likeButton.setText(String.valueOf(movie.getLikes()));
        dislikeButton.setText(String.valueOf(movie.getDislikes()));

    }
}