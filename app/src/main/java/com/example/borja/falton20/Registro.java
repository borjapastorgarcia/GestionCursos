package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity implements View.OnClickListener{
    private String pass1,pass2,email,nombre;
    private EditText etPass1, etPass2, etEmail, etNombre;
    private CheckBox cbbPolitica;
    private static String url_alta_usuario=WebServices.desarrollo;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        etPass1=(EditText)findViewById(R.id.passwordUsuario);
        etPass2=(EditText)findViewById(R.id.repitePasswordUsuario);
        etNombre=(EditText)findViewById(R.id.nombreCompleto);
        etEmail=(EditText)findViewById(R.id.EmailUsuario);
        findViewById(R.id.btnRegistrar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrar:
                if(etNombre.getText().toString().equals(""))
                    Toast.makeText(Registro.this, getResources().getString(R.string.introducir_nombre), Toast.LENGTH_SHORT).show();
                else
                    if(etEmail.getText().toString().equals(""))
                        Toast.makeText(Registro.this, getResources().getString(R.string.introducir_email), Toast.LENGTH_SHORT).show();
                    else
                        if(etPass1.getText().toString().equals(""))
                            Toast.makeText(Registro.this, getResources().getString(R.string.introducir_pass), Toast.LENGTH_SHORT).show();
                        else
                            if(etPass2.getText().toString().equals(""))
                                Toast.makeText(Registro.this, getResources().getString(R.string.introducir_pass2), Toast.LENGTH_SHORT).show();
                            else
                                if (!etPass1.getText().toString().equals(etPass2.getText().toString()))
                                    Toast.makeText(Registro.this, getResources().getString(R.string.introducir_pass3), Toast.LENGTH_SHORT).show();
                                else
                                    if(!((CheckBox)findViewById(R.id.cbAceptarPolitica)).isChecked())
                                        Toast.makeText(Registro.this, getResources().getString(R.string.aceptar_politica), Toast.LENGTH_SHORT).show();
                                    else{
                                        //datos correctos, se introduce usuario
                                        nombre=etNombre.getText().toString();
                                        email=etEmail.getText().toString();
                                        pass1=etPass1.getText().toString();

                                        if (haveNetworkConnection()) {
                                            new Alta().execute();
                                        }else{
                                            Snackbar.make(v, "No hay conexión a internet", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (haveNetworkConnection())
                                                    new Alta().execute();
                                                }
                                            }).show();
                                        }
                                    }
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
    class Alta extends AsyncTask<String,String,Integer>{
        @Override
        protected void onPostExecute(Integer i) {
            pDialog.dismiss();
            if (i==1) {
                Toast.makeText(getApplicationContext(), R.string.usuario_ok_creacion, Toast.LENGTH_LONG).show();
                Intent i2=new Intent(Registro.this,PantalllaInicio.class);
                i2.putExtra("email",email);
                i2.putExtra("nombre",nombre);

                startActivity(i2);
            }else
                Toast.makeText(getApplicationContext(),R.string.usuario_error_creacion, Toast.LENGTH_LONG).show();
            }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage(getResources().getString(R.string.creando_usuario));
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "alta_usuario"));
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", pass1));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("URL","URL-->"+url_alta_usuario+"<-->"+json+"<--'"+nombre+"' '"+email+"' '"+pass1+"'");
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
    }
}
