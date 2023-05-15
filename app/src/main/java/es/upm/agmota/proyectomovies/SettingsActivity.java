package es.upm.agmota.proyectomovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SettingsActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private Button btnSave;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSave);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        String newUsername = editUsername.getText().toString().trim();
        String newPassword = editPassword.getText().toString().trim();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // Update username
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingsActivity.this, "Nombre de usuario actualizado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingsActivity.this, "No se ha podido actualizar el nombre de usuario.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update password
            if (!TextUtils.isEmpty(newPassword)) {
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this, "No se ha podido actualizar la contraseña", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}