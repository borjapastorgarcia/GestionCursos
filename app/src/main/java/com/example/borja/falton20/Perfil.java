package com.example.borja.falton20;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {
private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE);
        String emailusuario= pref.getString("email","");
        Log.i("Email pref-->'"+emailusuario+"'","<----xx");
        ((TextView)findViewById(R.id.tvSaludoEmail)).append(emailusuario);
    }

}
