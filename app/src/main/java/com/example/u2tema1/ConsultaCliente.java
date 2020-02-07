package com.example.u2tema1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ConsultaCliente extends AppCompatActivity {

    HttpURLConnection conexion;
    private String res;
    String per;
    String codpersona;
    private TextView cod;
    private EditText apellido;
    private Spinner sexo;
    private EditText nombre;
    private EditText telefono;
    private int isexo;
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
        sexo = (Spinner) findViewById(R.id.sexo);
        isexo=(sexo.getSelectedItem().toString().equals("Masculino"))?0:1;

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

    public void Editar(View view) {
        //POST
        try {
            JSONObject postData = new JSONObject();
            postData.put("codpersona", codpersona);
            postData.put("nombre", nombre.getText().toString());
            postData.put("apellido", apellido.getText().toString());
            postData.put("sexo", isexo);
            postData.put("telefono", telefono.getText().toString());
            postData.put("direccion", direccion.getText().toString());
            String myurl= this.getString(R.string.dominio)+this.getString(R.string.editarcliente);
            Log.i("respuesta",myurl);
            URL url = new URL(myurl);
            conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setDoInput(true);
            conexion.setChunkedStreamingMode(0);
            OutputStream out = new BufferedOutputStream(conexion.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    out, "UTF-8"));
            writer.write(postData.toString());
            writer.flush();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                if (!linea.equals("OK\\n")) Log.e("mierror2","Error en servicio Web nueva");
                else
                { Toast.makeText(this, "Actualización Exitosa", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.e("mierror","No hay error");}
            } else {Log.e("mi error", conexion.getResponseMessage());}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }
}
