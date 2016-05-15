package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Asignatura extends AppCompatActivity {
    private ListView listViewAsignaturas;
    int idCurso, idUsuario;
    private ArrayList<ClaseAsignatura> listaAsignaturas = new ArrayList<ClaseAsignatura>();
    private static final String TAG_SUCCESS = "success";
    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    ProgressDialog progressDialog;
    private Context ctx;
    private AdaptadorListViewDevuelveAsignaturasPorCurso listAdapter;
    private TextView tvNoASignaturas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewAsignaturas=(ListView)findViewById(R.id.listaAsignaturasPorCursoPorUsario);
        ctx=this.getApplicationContext();
        Intent intent = getIntent();
        idCurso = intent.getIntExtra("idcurso",0);
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        new devuelveTodasAsignaturas().execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Asignatura.this,AniadirAsignatura.class);
                Log.e("asignatura-->idCurso: ",idCurso+"<--");
                i.putExtra("idcurso",idCurso);
               startActivity(i);
            }
        });
        tvNoASignaturas=(TextView)findViewById(R.id.tvNoAsignaturas);
    }
    class devuelveTodasAsignaturas extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasAsignaturas"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("cu_id", String.valueOf(idCurso)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveAsigXCurso","-->"+url_alta_usuario+"<-- "+json);
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
            progressDialog = new ProgressDialog(Asignatura.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            listAdapter = new AdaptadorListViewDevuelveAsignaturasPorCurso(ctx, listaAsignaturas);
            for(int i=0;i<listaAsignaturas.size();i++)
                Log.i("asignatura-->",listaAsignaturas.get(i).toString());
            listViewAsignaturas.setAdapter(listAdapter);
            if(listaAsignaturas.size()==0) {
                tvNoASignaturas.setVisibility(View.VISIBLE);
                findViewById(R.id.tvAsignaturasParaElCurso).setVisibility(View.GONE);
            }
            progressDialog.dismiss();
        }
    }

}
