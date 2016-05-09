package com.example.borja.falton20;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

public class EligeCurso extends AppCompatActivity implements View.OnClickListener {
    Spinner spCurso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_curso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btnSiguienteCurso).setOnClickListener(this);
        spCurso=(Spinner)findViewById(R.id.spEligeTuCurso);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSiguienteCurso:
                Intent i=new Intent(EligeCurso.this,Curso.class);
                String curso=spCurso.getSelectedItem().toString();
                i.putExtra("nameCurso",curso);
                startActivity(i);
        }
    }
}
