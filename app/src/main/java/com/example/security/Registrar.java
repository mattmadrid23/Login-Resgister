package com.example.security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registrar extends AppCompatActivity {
private ProgressDialog progressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private EditText etemail;
    private EditText etcontraseña;
    private EditText etconfirmacionC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        Button entrar = (Button) findViewById(R.id.botonEntrar);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);
        etemail = (EditText) findViewById(R.id.editTextEmail);
        etcontraseña = (EditText) findViewById(R.id.editTextContraseña);
        etconfirmacionC = (EditText) findViewById(R.id.editTextConfirmacion);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ob);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarUsuario();
               Intent ob=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(ob);
            }
        });

        progressDialog = new ProgressDialog(this);



    }


    private void registrarUsuario(){
        String correo = etemail.getText().toString().trim();
        String contraseña = etcontraseña.getText().toString().trim();
        String confirmar = etconfirmacionC.getText().toString().trim();
        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(correo)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(contraseña)){
            Toast.makeText(this,"Falta ingresar la contraseña",Toast.LENGTH_LONG).show();
            return;
        }
       progressDialog.setMessage("Realizando registro en linea...");
       progressDialog.show();



        FirebaseUser currentUser = mAuth.getCurrentUser();
        //creating a new user

        if (currentUser != null) {
            mAuth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                Toast.makeText(Registrar.this, "Se ha registrado el usuario con el email: " + etemail.getText(), Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {

                                Toast.makeText(Registrar.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                            // progressDialog.dismiss();
                        }
                    });
        }

        //Toast.makeText(Registrar.this,"ya existe usuario",Toast.LENGTH_LONG).show();
                                }





}
