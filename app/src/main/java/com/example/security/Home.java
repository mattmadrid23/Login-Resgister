package com.example.security;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{
    private EditText get1;
    private EditText get2;
    private TextView nombres;
    private TextView correo;
    private TextView latitud;
   // private TextView longitud;
    private String name;
    private String email;
    private static final String LOGTAG = "android-localizacion";
   // private GoogleApiClient googleApiClient;
    private  LocationRequest locationRequest;
 //private static final int PETITION_PERMIT_LOCATION = 101;
    private static final int UPDATE_INTERVAL_IN_MILLISECONDS = 500;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =500;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    private GoogleApiClient apiClient;
   private DatabaseReference mDatabase;// ...
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nombres = (TextView) findViewById(R.id.textViewNombres);
        correo = (TextView) findViewById(R.id.textViewEmail);
        latitud = (TextView) findViewById(R.id.textViewLatitud);
       // longitud = (TextView) findViewById(R.id.textViewlongitud);
        Button botonget1 = (Button) findViewById(R.id.botonGet1);
        Button botonget2 = (Button) findViewById(R.id.botonGet2);
        Button salvar = (Button) findViewById(R.id.botonSalvar);
        get1 = (EditText) findViewById(R.id.editTextGet1);
        get2 = (EditText) findViewById(R.id.editTextGet2);
        locationRequest= new LocationRequest();
        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        //longitud.setText("Longitud: " + longi.toString());
        //latitud.setText("Latitud: " + lat.toString());
        getAll();


        salvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //escribimos en la BD
                escribir(name,email);
            }
        });

    }

    private void getAll() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name=user.getUid();
            // name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            nombres.setText(name);
            correo.setText(email);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    private void leer(){
               mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    private void escribir(String userId, String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child("UID").setValue(userId);
        mDatabase.child("User").child("Email").setValue(email);
        Toast.makeText(getApplicationContext(),"ID"+userId+"Guardado exitosamente",Toast.LENGTH_LONG).show();

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
       // DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
           updateLocation();
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            latitud.setText("Longitud: " + String.valueOf(loc.getLongitude()+ " Latitud: "+String.valueOf(loc.getLatitude())));
            //longitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
        } else {
            latitud.setText("Latitud o Longitud: (desconocida)");
            //longitud.setText("Longitud: (desconocida)");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    private void updateLocation(){
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
        updateUI(location);
    }
}
