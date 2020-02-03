package com.example.u2tema1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ConsultaCliente extends AppCompatActivity {

    HttpURLConnection conexion;
    private String res;
    String codpersona;
    TextView cod;
    private EditText apellido;
    private Spinner sexo;
    private EditText nombre;
    private EditText telefono;
    private EditText direccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_cliente);
        Bundle extras = getIntent().getExtras();
        codpersona = extras.getString("Codpersona");
        cod = (TextView) findViewById(R.id.t_codpersona);
        cod.setText(codpersona);

        nombre = (EditText) findViewById(R.id.txtnombre);
        apellido = (EditText) findViewById(R.id.txtapellido);
        telefono = (EditText) findViewById(R.id.txttelefono);
        direccion = (EditText) findViewById(R.id.txtdireccion);

        try {
            String miurl = this.getString(R.string.dominio) + this.getString(R.string.verdetallecliente) + codpersona;
            URL url = new URL(miurl);
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                res = linea;
            } else {
                Log.e("mierror", conexion.getResponseMessage());
            }
        } catch (Exception e) {

        }

        try {
            JSONArray json_array = new JSONArray(res);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                //Clientes.add(new Cliente(objeto.getString("Cod_persona"), objeto.getString("Nombre"), objeto.getString("Apellidos")));
                nombre.setText(objeto.getString("Nombre"));
                apellido.setText(objeto.getString("Apellidos"));
                telefono.setText(objeto.getString("celular"));
                direccion.setText(objeto.getString("Domicilio"));
            }
        } catch (JSONException e) {
            Log.i("MI erro", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<Cliente> ListaCliente() {
        ArrayList<Cliente> Clientes = new ArrayList<>();
        try {
            String miurl = this.getString(R.string.dominio) + this.getString(R.string.verdetallecliente) + codpersona;
            URL url = new URL(miurl);
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                res = linea;
                JSONArray json_array = new JSONArray(res);
                for (int i = 0; i < json_array.length(); i++) {
                    JSONObject objeto = json_array.getJSONObject(i);
                    Clientes.add(new Cliente((objeto.getString("Cod_persona")), objeto.getString("Nombre"), objeto.getString("Apellidos")));
                    nombre.setText(objeto.getString("Nombre"));
                    apellido.setText(objeto.getString("Apellidos"));
                    telefono.setText(objeto.getString("celular"));
                    direccion.setText(objeto.getString("Domicilio"));
                }
            } else {
                Log.e("mierror", conexion.getResponseMessage());
            }
        } catch (Exception e) {

        } finally {
            if (conexion != null) conexion.disconnect();
        }
        return Clientes;
    }
}
