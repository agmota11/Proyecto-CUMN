package es.upm.agmota.proyectomovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.upm.agmota.proyectomovies.ApiCaller.Movie;
import es.upm.agmota.proyectomovies.LocalPersistence.FavoriteList;

public class FilmActivity extends AppCompatActivity {

    private final String TAG = "Film";
    private DatabaseReference likesRef;
    private DatabaseReference dislikesRef;
    private TextView likesTextView;
    private TextView dislikesTextView;
    private EditText commentEditText;
    private TextView commentsTextView;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        ImageView posterImageView = findViewById(R.id.film_poster_image);
        TextView titleTextView = findViewById(R.id.film_title_text);
        TextView releaseDateTextView = findViewById(R.id.film_release_date_text);
        TextView filmViews = findViewById(R.id.film_views);
        TextView overviewTextView = findViewById(R.id.overview_text);
        Button likeButton = findViewById(R.id.like_button);
        Button dislikeButton = findViewById(R.id.dislike_button);
        Button favButton = findViewById(R.id.fav_button);
        likesTextView = findViewById(R.id.like_count_text);
        dislikesTextView = findViewById(R.id.dislike_count_text);
        commentEditText = findViewById(R.id.comment_edit_text);
        Button postCommentButton = findViewById(R.id.comment_submit_button);
        commentsTextView = findViewById(R.id.comments);

        // Retrieve movie data
        movie = (Movie) getIntent().getExtras().get("movie");

        // Display movie data
        titleTextView.setText(movie.getTitle());
        Picasso.get().load(movie.getPosterPath()).into(posterImageView);
        releaseDateTextView.setText("Fecha de salida: " + movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());

        // Write a message to the database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser() ;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        likesRef = database.getReference(movie.getId() + "_likes");
        dislikesRef = database.getReference(movie.getId() + "_dislikes");
        DatabaseReference viewsRef = database.getReference(movie.getId() + "_views");
        DatabaseReference commentsRef = database.getReference(movie.getId() + "_comments");

        viewsRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    // If the value is null, initialize it to 0
                    value = 0;
                }
                value++; // Increment the value
                mutableData.setValue(value); // Set the updated value
                movie.setViews(value);
                return Transaction.success(mutableData); // Indicate success
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                if (error != null) {
                    // Transaction failed
                    Log.w(TAG, "Transaction failed.", error.toException());
                } else {
                    // Transaction succeeded
                    filmViews.setText("Esta película ha sido visitada " + String.valueOf(movie.getViews()) + " veces.");
                    Log.d(TAG, "Transaction succeeded.");
                }
            }
        });

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                movie.setLikes(dataSnapshot.getValue(Integer.class) != null
                        ? dataSnapshot.getValue(Integer.class)
                        : 0);
                runOnUiThread(() -> likesTextView.setText(String.valueOf(movie.getLikes())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        dislikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                movie.setDislikes(dataSnapshot.getValue(Integer.class) != null
                        ? dataSnapshot.getValue(Integer.class)
                        : 0);
                runOnUiThread(() -> dislikesTextView.setText(String.valueOf(movie.getDislikes())));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        final Boolean[] hasUserLiked = {Boolean.FALSE};
        assert currentUser != null;
        DatabaseReference userLike = database.getReference(movie.getId() + currentUser.getUid() + "_like");
        userLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                hasUserLiked[0] = dataSnapshot.getValue(Boolean.class) != null
                        ? dataSnapshot.getValue(Boolean.class)
                        : Boolean.FALSE;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final Boolean[] hasUserDisliked = {Boolean.FALSE};
        DatabaseReference userDislike = database.getReference(movie.getId() + currentUser.getUid() + "_dislike");
        userDislike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                hasUserDisliked[0] = dataSnapshot.getValue(Boolean.class) != null
                        ? dataSnapshot.getValue(Boolean.class)
                        : Boolean.FALSE;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        likeButton.setOnClickListener((view) -> {
            if (!hasUserLiked[0] && !hasUserDisliked[0]) {
                userLike.setValue(true);
                likesRef.setValue(movie.getLikes() + 1);
                Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
            } else if (!hasUserLiked[0] && hasUserDisliked[0]) {
                userLike.setValue(true);
                likesRef.setValue(movie.getLikes() + 1);
                dislikesRef.setValue(movie.getDislikes() - 1);
                userDislike.setValue(false);
                Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
            } else {
                userLike.setValue(false);
                likesRef.setValue(movie.getLikes() - 1);
                Toast.makeText(this, "Eliminando like", Toast.LENGTH_SHORT).show();            }
        });
        dislikeButton.setOnClickListener((view) -> {
            if (!hasUserLiked[0] && !hasUserDisliked[0]) {
                userDislike.setValue(true);
                dislikesRef.setValue(movie.getDislikes() + 1);
                Toast.makeText(this, "Disliked", Toast.LENGTH_SHORT).show();
            } else if (hasUserLiked[0] && !hasUserDisliked[0]) {
                userDislike.setValue(true);
                likesRef.setValue(movie.getLikes() - 1);
                dislikesRef.setValue(movie.getDislikes() + 1);
                userLike.setValue(false);
                Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
            } else {
                userDislike.setValue(false);
                dislikesRef.setValue(movie.getDislikes() - 1);
                Toast.makeText(this, "Eliminando dislike", Toast.LENGTH_SHORT).show();
            }
        });

        postCommentButton.setOnClickListener((view) ->{
            if(commentEditText.getText().toString().isBlank()) {
                Toast.makeText(this, "Porfavor, escribe un comentario", Toast.LENGTH_SHORT).show();
                return;
            }
            movie.getComments().add("Usuario: " + currentUser.getDisplayName());
            movie.getComments().add(commentEditText.getText().toString());
            commentsRef.setValue(TextUtils.join(";;", movie.getComments()));
            commentEditText.setText("");
        });

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                runOnUiThread(() -> {
                    // Update the view here
                    String commentsString = dataSnapshot.getValue(String.class) != null
                            ? dataSnapshot.getValue(String.class)
                            : "";
                    String[] commentsList = TextUtils.split(commentsString, ";;");
                    movie.setComments(new ArrayList<>(Arrays.asList(commentsList)));
                    commentsTextView.setText(TextUtils.join("\n\n", commentsList));
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        favButton.setOnClickListener((view) -> {
            List<Integer> favs = FavoriteList.getInstance().getMoviesIds();
            if (favs.stream().anyMatch(x -> x == movie.getId())) {
                FavoriteList.getInstance().removeMovie(movie);
                Toast.makeText(this, "Eliminando de favoritos", Toast.LENGTH_SHORT).show();
            } else {
                FavoriteList.getInstance().addMovie(movie);
                Toast.makeText(this, "Añadiendo de favoritos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}