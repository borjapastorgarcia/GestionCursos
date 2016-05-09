package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AniadirIdea extends AppCompatActivity implements View.OnClickListener{
private  String titulo, descripcion;
    private EditText etTitulo, etDescripcion;
    private Button btnAniadirIdea;
    private ProgressDialog progressDialog;
    int idUsuario;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    private static String url_alta_usuario=WebServices.desarrollo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_idea);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etTitulo=(EditText)findViewById(R.id.etTituloIdea);
        etDescripcion=(EditText)findViewById(R.id.etDescripcionNota);
        btnAniadirIdea=(Button) findViewById(R.id.btnAniadirIdea);
        btnAniadirIdea.setOnClickListener(this);
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
    }

    @Override
    public void onClick(View v) {
        titulo=etTitulo.getText().toString();
        descripcion=etDescripcion.getText().toString();
        if(titulo.equals("")||descripcion.equals(""))
            Toast.makeText(AniadirIdea.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
        else
            new aniadeIdea().execute();

    }
    class aniadeIdea extends AsyncTask<String, String, Integer>{

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "nueva_idea"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("id_desc", descripcion));
            params.add(new BasicNameValuePair("id_titulo", titulo));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("URL ANIADIRIDEA-->","-->"+url_alta_usuario+"<-- ");
            // check for success tag
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AniadirIdea.this);
            progressDialog.setMessage(getResources().getString(R.string.guardando_idea));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            progressDialog.dismiss();
            if (i==1) {
                Toast.makeText(getApplicationContext(), R.string.idea_creada, Toast.LENGTH_LONG).show();
                startActivity(new Intent(AniadirIdea.this, MainActivity.class));
            }else {
                Toast.makeText(getApplicationContext(), R.string.idea_creada_error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
