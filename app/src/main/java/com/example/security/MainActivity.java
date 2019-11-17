package com.example.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText nombre = (EditText) findViewById(R.id.editTextEmail);
        EditText contraseña = (EditText) findViewById(R.id.editTextContraseña);
        Button enviar = (Button) findViewById(R.id.botonEnviar);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);


enviar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        System.out.println("hola mundo");
        Intent ob=new Intent(getApplicationContext(),Home.class);
        startActivity(ob);
    }
});

registrar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent ob=new Intent(getApplicationContext(),Registrar.class);
        startActivity(ob);
    }
});

    }
}
