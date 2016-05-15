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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Falta extends AppCompatActivity {

    private ArrayList<ClaseCurso> listaCursos = new ArrayList<ClaseCurso>();
    private ArrayList<ClaseAsignatura> listaAsignaturas = new ArrayList<ClaseAsignatura>();
    private ArrayList<ClaseFalta> listaFaltas = new ArrayList<ClaseFalta>();
    private ArrayList<Integer>idAsignaturas=new ArrayList<Integer>();
    private AdaptadorListViewDevuelveFaltas listAdapterFaltas;
    public ProgressDialog progressDialog;
    int idUsuario,idCurso,idAsignatura;
    private static final String TAG_SUCCESS = "success";
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    ListView listViewFaltas;
    private Context ctx;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewFaltas=(ListView)findViewById(R.id.listViewListaFaltas);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(Falta.this, AniadirFalta.class));
            }
        });
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        ctx=this.getApplicationContext();
        //devolver la lista de cursos de usuario
        listaCursos = new ArrayList<ClaseCurso>();
        listaAsignaturas = new ArrayList<ClaseAsignatura>();
        v=this.findViewById(android.R.id.content);
        muestraFaltas();
    }
    public void muestraFaltas(){
        if (haveNetworkConnection()) {
            new devuelveTodosCursos().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    muestraFaltas();
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
    class devuelveTodosCursos extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodosCursosUsuario"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario, "GET", params);
            Log.i("devuelveTodosCursos", "-->" + url_alta_usuario + "<-- " + json);
            try {
                JSONArray jArray = json.getJSONArray("cursos");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject fila = jArray.getJSONObject(i);
                    ClaseCurso claseCurso = new ClaseCurso(fila.getInt("cu_id"),
                            fila.getString("cu_nombre"),
                            fila.getString("cu_desc"));
                    listaCursos.add(claseCurso);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Falta.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista));
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            for (int i = 0; i < listaCursos.size(); i++)
                Log.i("Curso-->", listaCursos.get(i).toString());
            if(listaCursos.size()>0)
                    new devuelveTodasAsignaturas().execute();
            else
                progressDialog.dismiss();
        }
    }
    class devuelveTodasAsignaturas extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for(int i=0;i<listaCursos.size();i++){
                params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasAsignaturas"));
                params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
                params.add(new BasicNameValuePair("cu_id", String.valueOf(listaCursos.get(i).getId())));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveAsigXCurso","-->"+url_alta_usuario+"<-- "+json+"cu_id"+idCurso);
            try {
                JSONArray jArray  = json.getJSONArray("asignaturas");
                for(int j=0;j<jArray.length();j++){
                    JSONObject fila=jArray.getJSONObject(j);
                    ClaseAsignatura claseAsignatura=new ClaseAsignatura(fila.getInt("asig_id"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getString("asig_nombre"));
                    listaAsignaturas.add(claseAsignatura);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }

            return 0;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(listaAsignaturas.size()>0){//sacar faltas x asignatura
                for(int i=0;i<listaAsignaturas.size();i++){
                    idAsignaturas.add(listaAsignaturas.get(i).getIdAsignatura());
                }
                new devuelveFaltasXAsignatura().execute();
            }else
                progressDialog.dismiss();
        }
    }
    class devuelveFaltasXAsignatura extends AsyncTask<String, String, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(String... args) {
            for(int k=0;k<idAsignaturas.size();k++){
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasFaltas"));
                params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
                params.add(new BasicNameValuePair("asig_id",String.valueOf(idAsignaturas.get(k))));
                JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
                Log.i("devuelveTodasFaltas","-->"+url_alta_usuario+"<-- "+json+"idasign-->"+String.valueOf(idAsignaturas.get(k)));
                try {
                    JSONArray jArray  = json.getJSONArray("faltas");
                    for(int i=0;i<jArray.length();i++){
                        JSONObject fila=jArray.getJSONObject(i);
                        ClaseFalta claseFalta=null;
                        if(fila.getInt("total")>0){
                            claseFalta=new ClaseFalta(fila.getInt("total"),idAsignaturas.get(k));
                            listaFaltas.add(claseFalta);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            for(int i=0;i<listaFaltas.size();i++)
                Log.i("falta-->",listaFaltas.get(i).toString());
            if (listaFaltas.size()==0){
                TextView tvNoFaltas=(TextView)findViewById(R.id.tvnofaltas);
                tvNoFaltas.setVisibility(View.VISIBLE);
            }else{
                listAdapterFaltas = new AdaptadorListViewDevuelveFaltas(ctx, listaFaltas,listaAsignaturas);
                Toast.makeText(ctx, "Mostramos faltas", Toast.LENGTH_SHORT).show();
                listViewFaltas.setAdapter(listAdapterFaltas);
            }
            progressDialog.dismiss();
        }
    }
}
