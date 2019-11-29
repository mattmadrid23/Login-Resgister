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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private FirebaseAnalytics mFirebaseAnalytics;
private ProgressDialog progressDialog;
private EditText nombre;
private EditText contraseña;
    private static SecretKeySpec secret;
    static String clave="jesusmadridgomez";
    byte[] encriptada = new byte[0];
    String desencriptada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        nombre = (EditText) findViewById(R.id.editTextEmail);
        contraseña = (EditText) findViewById(R.id.editTextContraseña);
        Button enviar = (Button) findViewById(R.id.botonEnviar);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);


enviar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //System.out.println("hola mundo");
        login();

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
    private void login(){
        String correo = nombre.getText().toString().trim();
        String contraseñña = contraseña.getText().toString().trim();
//encriptar y desencriptar
        try {
            SecretKey secret = generateKey();
            encriptada= encryptMsg(contraseñña,secret);
            desencriptada=decrryptMsg(encriptada,secret);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(contraseñña)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }


            mAuth.signInWithEmailAndPassword(correo,desencriptada)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                              //  Toast.makeText(MainActivity.this, "Se ha logueado el usuario con el email: " + nombre.getText().toString(), Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent ob=new Intent(getApplicationContext(),Home.class);
                                startActivity(ob);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Usuario o contraseña incorecto ",Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });


    }

    //clave
    public static SecretKey generateKey()throws NoSuchAlgorithmException, InvalidKeyException{
        return secret = new SecretKeySpec(clave.getBytes(),"AES");
    }
    //encriptar
    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;    }


    //desencriptar
    public static String decrryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }
}
