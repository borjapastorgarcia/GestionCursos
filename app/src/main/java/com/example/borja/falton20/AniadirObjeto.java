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
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AniadirObjeto extends AppCompatActivity implements View.OnClickListener{
    private RadioButton rbCompra, rbVenta, rvPerdido;
    private EditText etTitulo, etDescripcion, etPrecio;
    String strTitulo,strDescripcion;
    String tipoObjeto;
    private Button btnGuardarObjeto;
    ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    private View v;
    private SharedPreferences pref;
    private static String url_alta_usuario=WebServices.desarrollo;
    private static final String TAG_SUCCESS = "success";
    private float precio;
    int idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario= DatosUsuario.getIdUsuario();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_objeto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etTitulo=(EditText)findViewById(R.id.tituloObjeto);
        etDescripcion=(EditText)findViewById(R.id.descripcionObjeto);
        etPrecio=(EditText)findViewById(R.id.precioObjeto);
        btnGuardarObjeto=(Button)findViewById(R.id.btnGuardarObjeto);
        rbCompra=(RadioButton)findViewById(R.id.radioCompra);
        rbVenta=(RadioButton)findViewById(R.id.radioVenta);
        rvPerdido=(RadioButton)findViewById(R.id.radioEncontrado);
        btnGuardarObjeto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.v=v;
        strTitulo=etTitulo.getText().toString();
        strDescripcion=etDescripcion.getText().toString();
        if(rbCompra.isChecked())
            tipoObjeto="Compra";
        else
            if(rbVenta.isChecked())
                tipoObjeto="Venta";
        else
                if(rvPerdido.isChecked())
                    tipoObjeto="Encontrado";
        if(strTitulo.equals("")){
            Toast.makeText(AniadirObjeto.this, "Escribe el titulo del objeto", Toast.LENGTH_SHORT).show();
        }else{
            if(strDescripcion.equals("")){
                Toast.makeText(AniadirObjeto.this, "Escribe la descripcion del objeto", Toast.LENGTH_SHORT).show();
            }else
            if(etPrecio.getText().toString().equals("")){
                precio=0.0f;
            }else{
                precio=Float.parseFloat(etPrecio.getText().toString());

            }
            comprobarInternet();
        }
    }
    public void comprobarInternet(){
        if (haveNetworkConnection()) {
            new guardaObjeto().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet, intentelo mas tarde", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (haveNetworkConnection())
                        new guardaObjeto().execute();
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
    class guardaObjeto extends AsyncTask<String,String,Integer>{

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "anade_objeto"));
            params.add(new BasicNameValuePair("ob_titulo", strTitulo));
            params.add(new BasicNameValuePair("ob_desc", strDescripcion));
            params.add(new BasicNameValuePair("ob_precio", String.valueOf(precio)));
            params.add(new BasicNameValuePair("ob_foto", ""));
            params.add(new BasicNameValuePair("ob_tipo", tipoObjeto));
            params.add(new BasicNameValuePair("usu_id",String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
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
            progressDialog = new ProgressDialog(AniadirObjeto.this);
            progressDialog.setMessage(getResources().getString(R.string.aniadiendo_Objeto));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            progressDialog.dismiss();
            if (i==1) {
                Toast.makeText(AniadirObjeto.this, R.string.objeto_correcto, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AniadirObjeto.this, CompraVenta.class));
            }else
                Toast.makeText(getApplicationContext(),R.string.objeto_correcto_error, Toast.LENGTH_LONG).show();
        }
    }
}
