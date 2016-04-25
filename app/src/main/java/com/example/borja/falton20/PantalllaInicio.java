package com.example.borja.falton20;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
    private View viewBtnFaltas,viewBtncVenta,viewBtnTareas,viewBtnIdeas,viewBtnCurso,viewBtnPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantallla_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            ((View)findViewById(R.id.btnFaltas)).setOnClickListener(this);
            ((View)findViewById(R.id.btncv)).setOnClickListener(this);
            ((View)findViewById(R.id.btnTareas)).setOnClickListener(this);
            ((View)findViewById(R.id.btnIdeas)).setOnClickListener(this);
            ((View)findViewById(R.id.btnCurso)).setOnClickListener(this);
            ((View)findViewById(R.id.btnPerful)).setOnClickListener(this);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recView.setLayoutManager(new GridLayoutManager(this,3));

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

        }
    }
}
