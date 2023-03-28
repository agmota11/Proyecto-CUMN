package es.upm.agmota.proyectomovies;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.activity.result.ActivityResultLauncher;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


import es.upm.agmota.proyectomovies.ui.main.SectionsPagerAdapter;
import es.upm.agmota.proyectomovies.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    final static String LOG_TAG = "Main";

    private static final int RC_SIGN_IN = 2022;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);
        });

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    CharSequence username = user.getDisplayName();
                    Toast.makeText(MainActivity.this, getString(R.string.firebase_user_fmt, username), Toast.LENGTH_LONG).show();
                    Log.i(LOG_TAG, "onAuthStateChanged() " + getString(R.string.firebase_user_fmt, username));
                    ((TextView) findViewById(R.id.textView)).setText(getString(R.string.firebase_user_fmt, username));
                } else {
                    // user is signed out
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().
                                    createSignInIntentBuilder().
                                    setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()
                                    )).
                                    setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                    //setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                                            build(),
                            RC_SIGN_IN
                    );

                    //Remains logged in when unique gmail account
                    // setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */).
                    //setIsSmartLockEnabled(BuildConfig.DEBUG /* credentials */, true /* hints */).

                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_barra, menu);
        return true;
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "TODO Settings activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log_out:
                Toast.makeText(this, "TODO Log out", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_in));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_cancelled, Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "onActivityResult " + getString(R.string.signed_cancelled));
                finish();
            }
        }
    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
}