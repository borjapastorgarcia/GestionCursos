package com.example.borja.falton20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
private Button iniciaSesion, registro;
    private String emailUsuario, passUsuario;
    private ProgressDialog pDialog;
    private static String url_inicia_sesion=WebServices.URL_INICIO_SESION;
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnIniciaSesion).setOnClickListener(this);
        findViewById(R.id.btnRegistro).setOnClickListener(this);
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
                new iniciaSesion().execute();
            }
        }
    }
    class iniciaSesion extends AsyncTask<String,String,Integer> {
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.iniciando_sesion));
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", emailUsuario));
            params.add(new BasicNameValuePair("password", passUsuario));
            JSONObject json = jsonParser.makeHttpRequest(url_inicia_sesion,"GET", params);
            Log.i("DIB",url_inicia_sesion+"----"+json);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 2) {
                    return 1;
                    // closing this screen
                } else {
                    // failed to create product
                    return 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
