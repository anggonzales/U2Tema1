package com.example.u2tema1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class MainActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MiNuevoAdaptador adaptador;
    private ArrayList<Cliente> misdatos;
    public Vector<String> valor;
    private String res;
    HttpURLConnection conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        misdatos = new ArrayList<>();
        misdatos.add(new Cliente("1", "Juanito", "Perez"));
        misdatos.add(new Cliente("2", "Pablito", "Canto"));
        //adaptador = new MiNuevoAdaptador(this, misdatos);
        adaptador = new MiNuevoAdaptador(this,
                ListaClientes(conseguirstring()));
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    //Leyendo JSON
    private ArrayList<Cliente> ListaClientes(String string) {
        ArrayList<Cliente> Clientes = new ArrayList<>();
        try {
            JSONArray json_array = new JSONArray(string);
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject objeto = json_array.getJSONObject(i);
                Clientes.add(new Cliente(objeto.getString("Cod_persona"), objeto.getString("Nombre"),objeto.getString("Apellidos")));
            }
        } catch (JSONException e) {
            Log.i("MI erro", e.toString());
            e.printStackTrace();
        }
        return Clientes;
    }

    public String conseguirstring() {
        try {
            String miurl= this.getString(R.string.dominio)+this.getString(R.string.vercliente);
            URL url=new URL(miurl);
            conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                res=linea;
            } else {
                Log.e("mierror", conexion.getResponseMessage());
            }
        } catch (Exception e) {
            return res="";
        } finally {
            if (conexion!=null) conexion.disconnect();
        }
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_insertar:
                startActivity(new Intent(this, InsertarCliente.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adaptador.update(ListaClientes(conseguirstring()));
    }
}
