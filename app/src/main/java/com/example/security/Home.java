package com.example.security;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    private EditText get1;
    private EditText get2;
    private TextView nombres;
    private TextView email;
    private TextView latitud;
    private TextView longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nombres = (TextView) findViewById(R.id.textViewNombres);
        email = (TextView) findViewById(R.id.textViewEmail);
        latitud = (TextView) findViewById(R.id.textViewLatitud);
        longitud = (TextView) findViewById(R.id.textViewlongitud);
        Button botonget1 = (Button) findViewById(R.id.botonGet1);
        Button botonget2 = (Button) findViewById(R.id.botonGet2);
        Button salvar = (Button) findViewById(R.id.botonSalvar);
        get1 = (EditText) findViewById(R.id.editTextGet1);
        get2 = (EditText) findViewById(R.id.editTextGet2);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getAll();
            }
        });

    }
    private void getAll(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


}
