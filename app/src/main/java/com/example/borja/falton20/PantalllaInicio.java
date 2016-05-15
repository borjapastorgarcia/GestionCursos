package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class PantalllaInicio extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Item> datos;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int idUsuario;
    private static final String TAG_SUCCESS = "success";
    private JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    private Context ctx;
    private ProgressDialog progressDialog;
    private View btnFaltas,btnObjetos, btnTareas, btnIdeas, btnCurso, btnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallla_inicio);
        findViewById(R.id.btnMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findViewById(R.id.menu).getVisibility()==View.VISIBLE)
                    findViewById(R.id.menu).setVisibility(View.GONE);
                else
                    findViewById(R.id.menu).setVisibility(View.VISIBLE);
            }
        });

        //Botones menu superior
            findViewById(R.id.btnFaltas).setOnClickListener(this);
            findViewById(R.id.btncv).setOnClickListener(this);
            findViewById(R.id.btnTareas).setOnClickListener(this);
            findViewById(R.id.btnIdeas).setOnClickListener(this);
            findViewById(R.id.btnCurso).setOnClickListener(this);
            findViewById(R.id.btnPerful).setOnClickListener(this);

        btnFaltas=(View)findViewById(R.id.btnFaltas);
        btnObjetos=(View)findViewById(R.id.btncv);
        btnTareas=(View)findViewById(R.id.btnTareas);
        btnIdeas=(View)findViewById(R.id.btnIdeas);
        btnCurso=(View)findViewById(R.id.btnCurso);
        btnPerfil=(View)findViewById(R.id.btnPerful);

        //botones FAB menu
            findViewById(R.id.fabAniadirFalta).setOnClickListener(this);
            findViewById(R.id.fabAniadirIdea).setOnClickListener(this);
            findViewById(R.id.fabAniadirNota).setOnClickListener(this);
            findViewById(R.id.fabAniadirTarea).setOnClickListener(this);

        datos=new ArrayList<Item>();
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));
        datos.add(new Item("Examen","Examen de programacion","Estas son las notas del examen de programacion...","Pasado",new Date()));

        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewResumenDia);
        recyclerView.setHasFixedSize(true);
        final AdaptadorRecyclerView adaptador = new AdaptadorRecyclerView(datos);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + recyclerView.getChildPosition(v));
            }

        });
        if(datos.size()>0)
            ((TextView)findViewById(R.id.texto_nada_mostrar)).setVisibility(View.INVISIBLE);
        else
            ((TextView)findViewById(R.id.texto_nada_mostrar)).setVisibility(View.VISIBLE);

        recyclerView.setAdapter(adaptador);
        LinearLayoutManager ll=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(ll);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                Toast.makeText(PantalllaInicio.this, "Refrescando lista...", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);

            }


        });
    }
    class devuelveTodo extends AsyncTask<String, String, Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PantalllaInicio.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_dia));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //cargar listados
        }
    }
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnFaltas:
                Intent i = new Intent(this, Falta.class);
                startActivity(i);
                break;
            case R.id.btncv:
                 i = new Intent(this, CompraVenta.class);
                startActivity(i);
                break;
            case R.id.btnTareas:
                i = new Intent(this, Tareas.class);
                startActivity(i);
                break;
            case R.id.btnIdeas:
                i = new Intent(this, Ideas.class);
                startActivity(i);
                break;
            case R.id.btnCurso:
                i = new Intent(this, Curso.class);
                startActivity(i);
                break;
            case R.id.btnPerful:
                i = new Intent(this, Perfil.class);
                startActivity(i);
                break;
            case R.id.fabAniadirFalta:
                i = new Intent(this, AniadirFalta.class);
                startActivity(i);
                break;
            case R.id.fabAniadirIdea:
                i = new Intent(this, AniadirIdea.class);
                startActivity(i);
                break;
            case R.id.fabAniadirTarea:
                i = new Intent(this, AniadirTarea.class);
                startActivity(i);
                break;
            case R.id.fabAniadirNota:
                i = new Intent(this, AniadirNota.class);
                startActivity(i);
                break;

        }
    }
}
