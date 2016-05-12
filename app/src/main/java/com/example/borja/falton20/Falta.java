package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
    public ProgressDialog progressDialog;
    int idUsuario,idCurso,idAsignatura;
    private static final String TAG_SUCCESS = "success";
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        new devuelveTodosCursos().execute();
        listaAsignaturas = new ArrayList<ClaseAsignatura>();

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
            if(listaCursos.size()>0){
                new devuelveTodasAsignaturas().execute();
                for(int i=1;i<listaCursos.size();i++){
                    idCurso=listaCursos.get(i).getId();
                    Log.i("arraylistcursos","-->"+listaCursos.get(i).getId()+"<-- ");
                    new devuelveTodasAsignaturas().execute();
                }
            }else
                progressDialog.dismiss();
        }
    }
    class devuelveTodasAsignaturas extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasAsignaturas"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("cu_id", String.valueOf(idCurso)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveAsigXCurso","-->"+url_alta_usuario+"<-- "+json+"cu_id"+idCurso);
            try {
                JSONArray jArray  = json.getJSONArray("asignaturas");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    ClaseAsignatura claseAsignatura=new ClaseAsignatura(fila.getInt("asig_id"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getString("asig_nombre"));
                    listaAsignaturas.add(claseAsignatura);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
            for(int i=0;i<listaAsignaturas.size();i++)
                Log.i("asignatura-->",listaAsignaturas.get(i).toString());

            if(listaAsignaturas.size()>0){//sacar faltas x asignatura
                for(int i=0;i<listaAsignaturas.size();i++){
                    idAsignatura=listaAsignaturas.get(i).getIdAsignatura();
                    new devuelveFaltasXAsignatura().execute();
                }
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
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            for(int i=0;i<listaFaltas.size();i++)
                Log.i("falta-->",listaFaltas.get(i).toString());
            if (listaFaltas.size()==0){
                Toast.makeText(ctx, "No tienes ninguna falta", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasFaltas"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("asig_id",String.valueOf(idAsignatura)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveAsigXCurso","-->"+url_alta_usuario+"<-- "+json+"idasign-->"+idAsignatura);
            try {
                JSONArray jArray  = json.getJSONArray("faltas");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    String fechaStr=fila.getString("fa_fecha");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = formatter.parse(fechaStr);
                    ClaseFalta claseFalta=new ClaseFalta(fila.getInt("fa_id"),
                                                        date,
                                                        fila.getInt("asig_id"));
                    listaFaltas.add(claseFalta);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
