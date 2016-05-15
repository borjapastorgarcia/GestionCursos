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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Curso extends AppCompatActivity{
    private ListView listViewCursos;
    private AdaptadorListViewDevuelveCursos listAdapter;
    private ArrayList<ClaseCurso> listaCursos = new ArrayList<ClaseCurso>();
    public ProgressDialog progressDialog;
    int idUsuario;
    private static final String TAG_SUCCESS = "success";
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    TextView tvNoCursos;
    private Context ctx;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewCursos=(ListView)findViewById(R.id.listaCursos);

        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        ctx=this.getApplicationContext();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Curso.this,EligeCurso.class));
            }
        });
        tvNoCursos=(TextView) findViewById(R.id.tvNoCursos);
        v=this.findViewById(android.R.id.content);
        muestraCursos();
    }
    public void muestraCursos(){
        if (haveNetworkConnection()) {
            new devuelveTodosCursos().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    muestraCursos();
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
    class devuelveTodosCursos extends AsyncTask<String,String,Integer>{
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodosCursosUsuario"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveTodosCursos","-->"+url_alta_usuario+"<-- "+json);
            try {
                JSONArray jArray  = json.getJSONArray("cursos");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    ClaseCurso claseCurso=new ClaseCurso(fila.getInt("cu_id"),
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
            progressDialog = new ProgressDialog(Curso.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            listAdapter = new AdaptadorListViewDevuelveCursos(ctx, listaCursos);
            for(int i=0;i<listaCursos.size();i++)
                Log.i("Curso-->",listaCursos.get(i).toString());
            listViewCursos.setAdapter(listAdapter);
            listViewCursos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("onItemClick","Cursos click lista, idCurso: "+listaCursos.get(position).getId());
                    Intent i=new Intent(Curso.this,Asignatura.class);
                    i.putExtra("idcurso",listaCursos.get(position).getId());
                    i.putExtra("nombreCurso",listaCursos.get(position).getNombre());
                    startActivity(i);
                }
            });
            if (listaCursos.size()==0)
                tvNoCursos.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }
    }


}
