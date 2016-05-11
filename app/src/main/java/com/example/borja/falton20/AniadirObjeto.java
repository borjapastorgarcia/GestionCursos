package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AniadirObjeto extends AppCompatActivity implements View.OnClickListener{
    private RadioButton rbCompra, rbVenta, rvPerdido;
    private EditText etTitulo, etDescripcion, etPrecio;
    String strTitulo,strDescripcion;
    String tipoObjeto;
    private Button btnGuardarObjeto;
    ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    private SharedPreferences pref;
    private static String url_alta_usuario=WebServices.desarrollo;
    private float precio;
    int idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE);
        idUsuario= pref.getInt("idUsuario",0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_objeto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etTitulo=(EditText)findViewById(R.id.tituloObjeto);
        etDescripcion=(EditText)findViewById(R.id.descripcionObjeto);
        etPrecio=(EditText)findViewById(R.id.precioObjeto);
        btnGuardarObjeto=(Button)findViewById(R.id.btnGuardarObjeto);
        rbCompra=(RadioButton)findViewById(R.id.radioCompra);
        rbVenta=(RadioButton)findViewById(R.id.radioVenta);
        rvPerdido=(RadioButton)findViewById(R.id.radioEncontrado);

        btnGuardarObjeto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        strTitulo=etTitulo.getText().toString();
        strDescripcion=etDescripcion.getText().toString();
        precio=Float.parseFloat(etPrecio.getText().toString());
        if(rbCompra.isSelected())
            tipoObjeto="Compra";
        else
            if(rbVenta.isSelected())
                tipoObjeto="Venta";
        else
                if(rvPerdido.isSelected())
                    tipoObjeto="Encontrado";

    }
    class guardaObjeto extends AsyncTask<String,String,Integer>{

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "anade_falta"));

            params.add(new BasicNameValuePair("ob_titulo", strTitulo));
            params.add(new BasicNameValuePair("ob_desc", strDescripcion));
            params.add(new BasicNameValuePair("ob_precio", String.valueOf(precio)));
            params.add(new BasicNameValuePair("ob_tipo", tipoObjeto));
            ////CUIDADO CON EL IDUSUARIO
            params.add(new BasicNameValuePair("usu_id",String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AniadirObjeto.this);
            progressDialog.setMessage(getResources().getString(R.string.aniadiendoFalta));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.hide();
        }
    }
}
