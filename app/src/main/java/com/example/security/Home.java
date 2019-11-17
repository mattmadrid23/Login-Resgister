package com.example.security;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView nombres = (TextView) findViewById(R.id.textViewNombres);
        TextView email = (TextView) findViewById(R.id.textViewEmail);
        TextView latitud = (TextView) findViewById(R.id.textViewLatitud);
        TextView longitud = (TextView) findViewById(R.id.textViewlongitud);
        Button botonget1 = (Button) findViewById(R.id.botonGet1);
        Button botonget2 = (Button) findViewById(R.id.botonGet2);
        Button salvar = (Button) findViewById(R.id.botonSalvar);
        EditText get1 = (EditText) findViewById(R.id.editTextGet1);
        EditText get2 = (EditText) findViewById(R.id.editTextGet2);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
