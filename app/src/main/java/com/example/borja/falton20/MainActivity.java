package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private Button iniciaSesion, registro;
    private String emailUsuario, passUsuario;
    private ProgressDialog pDialog;
    private static String url_inicia_sesion=WebServices.desarrollo;
    private static final String TAG_SUCCESS = "success";
    View v;
    JSONParser jsonParser = new JSONParser();
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("FaltOn", 0);
        editor = pref.edit();
        if(pref.getString("email","").equals("")){
            setContentView(R.layout.activity_main);
            findViewById(R.id.btnIniciaSesion).setOnClickListener(this);
            findViewById(R.id.btnRegistro).setOnClickListener(this);
        }else
            startActivity(new Intent(this,PantalllaInicio.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        this.v=v;
        switch (v.getId()){
            case R.id.btnIniciaSesion:
                //validar datos e iniciar sesion
                iniciarSesion();
                break;
            case R.id.btnRegistro:
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public void iniciarSesion(){
        if(((EditText)findViewById(R.id.EmailUsuario)).getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Inserta un email", Toast.LENGTH_SHORT).show();
            ((EditText)findViewById(R.id.EmailUsuario)).requestFocus();
        }else{
            if(((EditText)findViewById(R.id.PasswordUsuario)).getText().toString().equals("")){
                Toast.makeText(MainActivity.this, "Inserta una contraseña", Toast.LENGTH_SHORT).show();
                ((EditText)findViewById(R.id.PasswordUsuario)).requestFocus();
            }else{
                 emailUsuario=((EditText)findViewById(R.id.EmailUsuario)).getText().toString();
                 passUsuario=((EditText)findViewById(R.id.PasswordUsuario)).getText().toString();
                inicia_sesion();
            }
        }
    }
    public void inicia_sesion(){
        if (haveNetworkConnection()) {
            new iniciaSesion().execute();
        }else{
            Snackbar.make(v, "No hay conexión a internet", Snackbar.LENGTH_LONG) .setAction("Reintentar", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   inicia_sesion();
                }
            }).show();
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

    class iniciaSesion extends AsyncTask<String,String,Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.iniciando_sesion));
            pDialog.show();
            //////
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "login"));
            params.add(new BasicNameValuePair("usu_email", emailUsuario));
            params.add(new BasicNameValuePair("usu_pass", passUsuario));
            JSONObject json = jsonParser.makeHttpRequest(url_inicia_sesion,"GET", params);
            Log.i("DIB",url_inicia_sesion+" "+json);
            try {
                JSONArray jArray  = json.getJSONArray("fila");
                JSONObject fila=jArray.getJSONObject(0);
                if(fila==null){
                    return 0;
                }else{
                    if (jArray!=null){
                        if (fila.getString("Email").equals(emailUsuario)) {
                            //set preferences con datos de usuario
                            pref = getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putInt("idUsuario",fila.getInt("id"));
                            editor.putString("nombre",fila.getString("Nombre"));
                            editor.putString("email",fila.getString("Email"));
                            editor.putString("telefono",fila.getString("Telefono"));
                            editor.commit();
                            return 1;
                        } else {
                            return 0;
                        }
                    }else
                        return 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
        @Override
        protected void onPostExecute(Integer i) {
            pDialog.dismiss();
            if (i==1) {
                Toast.makeText(getApplicationContext(), R.string.inicio_sesion_correcto, Toast.LENGTH_LONG).show();
                Intent i2=new Intent(MainActivity.this,PantalllaInicio.class);
                i2.putExtra("email",emailUsuario);
                startActivity(i2);
            }else
                Toast.makeText(getApplicationContext(),R.string.inicio_sesion_error, Toast.LENGTH_LONG).show();
        }
    }
}
