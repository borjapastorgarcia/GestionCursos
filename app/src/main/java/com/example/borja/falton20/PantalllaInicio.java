package com.example.borja.falton20;

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

public class PantalllaInicio extends AppCompatActivity {

    private ArrayList<Item> datos;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
