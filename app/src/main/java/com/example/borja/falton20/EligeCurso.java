package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EligeCurso extends AppCompatActivity implements View.OnClickListener {
    Spinner spCurso;
    String nombreCurso, descripcionCurso;
    EditText etNombreCurso, etDescripcionCurso;
    private ProgressDialog progressDialog;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    int idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elige_curso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btnAniadeCurso).setOnClickListener(this);
        etNombreCurso=(EditText)findViewById(R.id.etNombreDelCurso);
        etDescripcionCurso=(EditText)findViewById(R.id.etDescripcionCurso);
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAniadeCurso:
                comprobarCamposCurso();
                break;
        }
    }
    public void comprobarCamposCurso(){
        nombreCurso=etNombreCurso.getText().toString();
        descripcionCurso=etDescripcionCurso.getText().toString();
        if(nombreCurso.equals("")||descripcionCurso.equals("")){
            Toast.makeText(EligeCurso.this, "Rellena todos los campos del curso", Toast.LENGTH_SHORT).show();
        }else{
            new AniadeCurso().execute();
        }
    }
    class AniadeCurso extends AsyncTask<String,String,Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EligeCurso.this);
            progressDialog.setMessage(getResources().getString(R.string.guardando_curso));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer  i) {
            progressDialog.dismiss();
            if (i==1) {
                Toast.makeText(getApplicationContext(), R.string.curso_creado, Toast.LENGTH_LONG).show();
                startActivity(new Intent(EligeCurso.this, Curso.class));
            }else {
                Toast.makeText(getApplicationContext(), R.string.curso_creado_error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "alta_curso"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("descripcion", descripcionCurso));
            params.add(new BasicNameValuePair("nombre", nombreCurso));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("URL aniadirCurso-->","-->"+url_alta_usuario+"<-- ");
            try {
                Log.i("TAG_SUCCESS-->","-->"+json.getString(TAG_SUCCESS)+"<-- ");
                int success = Integer.parseInt(json.getString(TAG_SUCCESS));
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
