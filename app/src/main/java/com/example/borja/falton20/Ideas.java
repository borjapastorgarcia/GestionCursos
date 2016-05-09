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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ideas extends AppCompatActivity {

    private ListView listViewIdeas;
    private AdaptadorListViewDevuelveIdea listAdapterIdeas;
    private ArrayList<ClaseIdea> listaIdeas = new ArrayList<ClaseIdea>();
    public ProgressDialog progressDialog;
    int idUsuario;
    private static final String TAG_SUCCESS = "success";
    private SwipeRefreshLayout swipeRefreshLayout;
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshIdea);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Log.e("SwipeRefresh","Se pulsa swipeRefresh");
                listViewIdeas.setAdapter(null);
                listaIdeas = new ArrayList<ClaseIdea>();
                new devuelveTodasIdeas().execute();
                listViewIdeas=(ListView)findViewById(R.id.listViewListaIdeas);
                listAdapterIdeas = new AdaptadorListViewDevuelveIdea(ctx, listaIdeas);
                listViewIdeas.setAdapter(listAdapterIdeas);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        ctx=this.getApplicationContext();
        new devuelveTodasIdeas().execute();
        listViewIdeas=(ListView)findViewById(R.id.listViewListaIdeas);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Ideas.this,AniadirIdea.class));
            }
        });

    }
    class devuelveTodasIdeas extends AsyncTask<String, String, Integer>{
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveIdeasUsuario"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveTodasIdeas","-->"+url_alta_usuario+"<-- "+json);
            try {
                JSONArray jArray  = json.getJSONArray("ideas");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    ClaseIdea claseIdea=new ClaseIdea(fila.getInt("id_id"),
                            fila.getString("id_titulo"),
                            fila.getString("id_desc"));
                    listaIdeas.add(claseIdea);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Ideas.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista_ideas));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            listAdapterIdeas = new AdaptadorListViewDevuelveIdea(ctx, listaIdeas);
            for(int i=0;i<listaIdeas.size();i++)
                Log.i("idea-->",listaIdeas.get(i).toString());
            listViewIdeas.setAdapter(listAdapterIdeas);
            progressDialog.dismiss();
        }
    }
}
