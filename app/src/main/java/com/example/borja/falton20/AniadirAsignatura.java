package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AniadirAsignatura extends AppCompatActivity implements View.OnClickListener{
    int idCurso;
    View v;
    ProgressDialog progressDialog;
    private static String url_alta_usuario=WebServices.desarrollo;
    private static final String TAG_SUCCESS = "success";

    private JSONParser jsonParser = new JSONParser();
    String nombreAsignatura;
    int horasAsignatura, porcentajePermitidoFaltas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_asignatura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i=getIntent();
        idCurso=i.getIntExtra("idcurso",0);
        findViewById(R.id.btnAniadeAsignatura).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.v=v;
        switch (v.getId()){
            case R.id.btnAniadeAsignatura:
                compruebaCampos();
            break;
        }
    }

    public void compruebaCampos(){
        nombreAsignatura=((EditText)findViewById(R.id.etNombreAsignatura)).getText().toString();
        String horasAsignaturaStr=((EditText)findViewById(R.id.etTotalHorasAsignatura)).getText().toString();
        String porcentajePermitidoFaltasStr=((EditText)findViewById(R.id.tvPorcentajePermitidoFaltasAsignatura)).getText().toString();
        if(nombreAsignatura.toString().equals("")||horasAsignaturaStr.equals("")||porcentajePermitidoFaltasStr.equals("")) {
            Toast.makeText(AniadirAsignatura.this, "Introduce bien los datos", Toast.LENGTH_SHORT).show();
        }else{
            horasAsignatura=Integer.parseInt(horasAsignaturaStr);
            porcentajePermitidoFaltas=Integer.parseInt(porcentajePermitidoFaltasStr);
            aniadeAsignatura();

        }
    }
    public void aniadeAsignatura(){
        if (haveNetworkConnection()) {
            new aniadeNuevaAsignatura().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    aniadeAsignatura();
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
    class aniadeNuevaAsignatura extends AsyncTask<String,String, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AniadirAsignatura.this);
            progressDialog.setMessage(getResources().getString(R.string.guardando_asignatura));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            progressDialog.dismiss();
            if (i==1) {
                Toast.makeText(getApplicationContext(), R.string.asignatura_creada, Toast.LENGTH_LONG).show();
                startActivity(new Intent(AniadirAsignatura.this, Asignatura.class));
            }else {
                Toast.makeText(getApplicationContext(), R.string.asignatura_creada_error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "anade_asignatura"));
            params.add(new BasicNameValuePair("cu_id", String.valueOf(idCurso)));
            params.add(new BasicNameValuePair("asig_porcentaje_faltas", String.valueOf(porcentajePermitidoFaltas)));
            params.add(new BasicNameValuePair("asig_total_horas",String.valueOf(horasAsignatura) ));
            params.add(new BasicNameValuePair("asig_nombre", nombreAsignatura));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("URL aniadeAsignatura","-->"+url_alta_usuario+"<-- "+json);
            try {
                Log.i("TAG_SUCCESS-->","-->"+json.getString(TAG_SUCCESS)+"<-- ");
                int success = Integer.parseInt(json.getString(TAG_SUCCESS));
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
