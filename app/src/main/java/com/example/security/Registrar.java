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

public class Registrar extends AppCompatActivity {
    private static SecretKeySpec secret;
    private ProgressDialog progressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private EditText etemail;
    private EditText etcontraseña;
    private EditText etconfirmacionC;
    static String clave="jesusmadridgomez";
    byte[] encriptada = new byte[0];
    String desencriptada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Button entrar = (Button) findViewById(R.id.botonEntrar);
        Button registrar = (Button) findViewById(R.id.botonRegistrar);
        etemail = (EditText) findViewById(R.id.editTextEmail);
        etcontraseña = (EditText) findViewById(R.id.editTextContraseña);
        etconfirmacionC = (EditText) findViewById(R.id.editTextConfirmacion);

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

            }
        });

        progressDialog = new ProgressDialog(this);



    }


    private void registrarUsuario(){
        String correo = etemail.getText().toString().trim();
        String contraseña = etcontraseña.getText().toString().trim();
        String confirmar = etconfirmacionC.getText().toString().trim();

        //encriptamos la contraseña

        try {
            SecretKey secret = generateKey();
           encriptada= encryptMsg(contraseña,secret);
            desencriptada=decrryptMsg(encriptada,secret);
           //Toast.makeText(getApplicationContext(),"hola"+encriptada,Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"hola"+desencriptada,Toast.LENGTH_LONG).show();
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

            if (TextUtils.isEmpty(contraseña)) {
                Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(confirmar)) {
                Toast.makeText(this, "Falta ingresar la confirmacion contraseña", Toast.LENGTH_LONG).show();
                return;
            }


            progressDialog.setMessage("Realizando registro en linea...");
            progressDialog.show();


            FirebaseUser currentUser = mAuth.getCurrentUser();
            //creating a new user

        if(contraseña.equals(confirmar)){


            if (currentUser != null) {

                mAuth.createUserWithEmailAndPassword(correo, desencriptada)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if (task.isSuccessful()) {
                                    Toast.makeText(Registrar.this, "Se ha registrado el usuario con el email: " + etemail.getText(), Toast.LENGTH_LONG).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent ob = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(ob);
                                } else {

                                    Toast.makeText(Registrar.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        } else Toast.makeText(this, "Las contraseñas no coinciden, intente de nuevo", Toast.LENGTH_LONG).show();//else
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
