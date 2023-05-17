package com.optic.redsocial.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.optic.redsocial.R;
import com.optic.redsocial.models.User;
import com.optic.redsocial.providers.AuthProvider;
import com.optic.redsocial.providers.UserProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {

    TextInputEditText mtextInpuntUsername;
    Button mButtonRegistro;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mtextInpuntUsername = findViewById(R.id.textInputUsername);
        mButtonRegistro = findViewById(R.id.btnRegistro);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere por favor")
                .setCancelable(false).build();

        mButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    private void register() {
        String username = mtextInpuntUsername.getText().toString();
        if (!username.isEmpty()) {
            updateUser(username);
        }
        else {
            Toast.makeText(this, "Para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username) {
            String id = mAuthProvider.getUid();
            User user = new User();
            user.setUsername(username);
            user.setId(id);
            mDialog.show();
            mUserProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mDialog.dismiss();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent( CompleteProfileActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(CompleteProfileActivity.this, "Hubo un problema al registrar al Usuario", Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }
}
