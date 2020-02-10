package com.example.u2tema1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MiLogin extends AppCompatActivity {
    HttpURLConnection conexion;
    private EditText Login;
    private EditText password;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Config", MODE_PRIVATE);
        login = prefs.getBoolean("onlogin", false);
        if (login) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_mi_login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        Login = (EditText) findViewById(R.id.edtUsuario);
        password = (EditText) findViewById(R.id.edtPassword);


    }

    public int ValidaDatos(String usuario, String pass) {
        int result = 3;
        try {
            Log.i("miruta", getString(R.string.dominio) + getString(R.string.login) + usuario + "&pass=" + pass);
            URL url = new URL(getString(R.string.dominio) + getString(R.string.login) + usuario + "&pass=" + pass);
            conexion = (HttpURLConnection) url
                    .openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                if (linea.equals("success"))
                    result = 0;
                else
                    result = 1;
            } else {
                Log.e("mifallo", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            Log.e("mifallo", e.getMessage());
        } finally {
            if (conexion != null) conexion.disconnect();
            return result;
        }
    }

    public void onLogin(View view) {

        Peticion validar = new Peticion();
        validar.execute();
        /*int V = ValidaDatos(Login.getText().toString(), password.getText().toString());
        if (V == 0) {
            //Para poder editar
            editor = prefs.edit();
            editor.putBoolean("onlogin", true);
            editor.apply();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else
            Toast.makeText(this, "Ingreso Fallido", Toast.LENGTH_SHORT).show();*/
    }

    public class Peticion extends AsyncTask<Void, Void, Integer> {
        String usu = Login.getText().toString();
        String pass = password.getText().toString();
        int validar = 0;
        @Override
        protected Integer doInBackground(Void... voids) {
            validar = ValidaDatos(usu, pass);
            return validar;
        }

        @Override protected void onPostExecute(Integer res) {
            if (res == 0) {
                //Para poder editar
                editor = prefs.edit();
                editor.putBoolean("onlogin", true);
                editor.apply();
                Intent i = new Intent( getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
