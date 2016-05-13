package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Perfil extends AppCompatActivity implements View.OnClickListener{
private SharedPreferences pref;
    private EditText etNombre, etTelefono;
    private Button btnGuardar, btnCancelar;
    private String nuevoNombre, nuevoTelefono;
    private ProgressDialog progressDialog;
    private static String url_alta_usuario=WebServices.desarrollo;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE);
        String emailusuario= pref.getString("email","");
        Log.i("Email pref-->'"+emailusuario+"'","<----xx");
        ((TextView)findViewById(R.id.tvSaludoEmail)).append(emailusuario);
        etNombre=(EditText) findViewById(R.id.etnombre);
        etTelefono=(EditText) findViewById(R.id.ettelefono);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE);
        DatosUsuario.recuperarPreferences(pref);
        etNombre.setText(DatosUsuario.getNombreUsuario());
        etTelefono.setText(DatosUsuario.getTelefonoUsuario());
        btnGuardar=(Button)findViewById(R.id.btnGuardar);
        btnCancelar=(Button)findViewById(R.id.btnCancelar);
        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.v=v;
        switch (v.getId()){
            case R.id.btnGuardar:
                comprobarDatos();
                break;
            case R.id.btnCancelar:
                startActivity(new Intent(Perfil.this,PantalllaInicio.class));
                break;
        }
    }
    public void comprobarDatos(){
        nuevoNombre=etNombre.getText().toString();
        nuevoTelefono=etTelefono.getText().toString();
        if(nuevoNombre.equals("")){
            Toast.makeText(Perfil.this, "Escribe un nombre", Toast.LENGTH_SHORT).show();
        }else{
            if(nuevoTelefono.equals("")){
                Toast.makeText(Perfil.this, "Escribe un telefono", Toast.LENGTH_SHORT).show();
            }else{
                mandarDatosAServer();
            }
        }
    }
    public void mandarDatosAServer(){
        if (haveNetworkConnection()) {
            new UpdatePerfil().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet, intentelo mas tarde", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (haveNetworkConnection())
                        new UpdatePerfil().execute();
                }
            }).show();
        }
    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    class UpdatePerfil extends AsyncTask <String, String, Integer>{

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "actualiza_usuario"));
            params.add(new BasicNameValuePair("nombre", nuevoNombre));
            params.add(new BasicNameValuePair("telefono", nuevoTelefono));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("URL","URL-->"+url_alta_usuario+"<-->"+json+"<--'"+nuevoNombre+"' '"+nuevoTelefono);
            try {
                Log.i("TAG_SUCCESS-->","-->"+json.getString(TAG_SUCCESS)+"<-- ");
                int success = Integer.parseInt(json.getString(TAG_SUCCESS));
                if (success == 1)
                    return 1;
                 else
                    return 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Perfil.this);
            progressDialog.setMessage(getResources().getString(R.string.actualizando_usuario));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            progressDialog.dismiss();
            if (i==1) {
                Toast.makeText(Perfil.this, "Datos actualizados", Toast.LENGTH_SHORT).show();

            }else
                Toast.makeText(getApplicationContext(),R.string.datos_actualizados_error, Toast.LENGTH_LONG).show();
        }
    }
}
