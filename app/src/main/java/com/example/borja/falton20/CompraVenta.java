package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompraVenta extends AppCompatActivity{
    private ListView listViewObjetos;
    private View v;
    private ProgressDialog progressDialog;
    private AdaptadorListViewDevuelveObjetos listAdapter;
    private ArrayList<ClaseObjeto> listaObjetos = new ArrayList<ClaseObjeto>();
    int idUsuario;
    Context ctx;
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    private static final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_venta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        v=this.findViewById(android.R.id.content);
        listViewObjetos=(ListView)findViewById(R.id.listViewListaObjetosCompraVenta);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ctx=this.getApplicationContext();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompraVenta.this,AniadirObjeto.class));
            }
        });
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        comprobarInternet();
    }
    public void comprobarInternet(){
        if (haveNetworkConnection()) {
            new cargaListaObjetos().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet, intentelo mas tarde", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (haveNetworkConnection())
                        new cargaListaObjetos().execute();
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
    class cargaListaObjetos extends AsyncTask<String,String,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompraVenta.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista_objetos));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            if (listaObjetos.size()==0)
                Toast.makeText(getApplicationContext(), R.string.cargando_lista_objetos_error, Toast.LENGTH_LONG).show();
            else{
                listAdapter = new AdaptadorListViewDevuelveObjetos(ctx, listaObjetos);
                listViewObjetos.setAdapter(listAdapter);
            }
            progressDialog.dismiss();
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveObjetosUsuario"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveTodosObjetos","-->"+url_alta_usuario+"<-- "+json);
            try {
                JSONArray jArray  = json.getJSONArray("objetos");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    float precio;

                    if (fila.getString("ob_precio").equals(""))
                        precio=0.0f;
                    else
                        precio=Float.parseFloat(fila.getString("ob_precio"));

                    ClaseObjeto claseObjeto=new ClaseObjeto(fila.getString("ob_titulo"),
                                                            fila.getString("ob_desc"),
                                                            fila.getString("ob_tipo"),
                                                            precio);

                    listaObjetos.add(claseObjeto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

}
