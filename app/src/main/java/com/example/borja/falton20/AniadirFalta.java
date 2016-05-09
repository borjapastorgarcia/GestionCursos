package com.example.borja.falton20;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AniadirFalta extends AppCompatActivity implements View.OnClickListener{
    private String fecha;
    private ProgressDialog pDialog;
    private ArrayList<String> arrayAsinaturas;
    private Spinner spAsignaturasUsuario;
    private EditText etFecha;
    private String pYear;
    private String pMonth;
    private String pDay;
    private ClaseFalta claseFalta;
    JSONParser jsonParser = new JSONParser();

    private static String url_alta_usuario=WebServices.desarrollo;

    private  String emailUsuario, nombreUsuario, telefonoUsuario;
    private  int idUsuario;
    final Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_falta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arrayAsinaturas=new ArrayList<String>();
        rellenaSpinner();
        spAsignaturasUsuario=(Spinner)findViewById(R.id.spAsignatura);
        findViewById(R.id.btnAniadirFalta).setOnClickListener(this);
        etFecha=(EditText)findViewById(R.id.etFecha);
        etFecha.setOnClickListener(this);
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        emailUsuario=DatosUsuario.getEmailUsuario();
        nombreUsuario=DatosUsuario.getNombreUsuario();
        telefonoUsuario=DatosUsuario.getTelefonoUsuario();
        idUsuario=DatosUsuario.getIdUsuario();
    }
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = "" + year;
                    int MonthOfYearOk = monthOfYear + 1;
                    if (MonthOfYearOk < 10) {
                        pMonth = "0" + MonthOfYearOk;
                    } else {
                        pMonth = "" + MonthOfYearOk;
                    }
                    pDay = "" + dayOfMonth;
                    updateDisplay();
                }
            };

    /* Updates the date in the edittext */
    private void updateDisplay() {
        etFecha.setText(
                new StringBuilder().append(pDay)
                                    .append("/")
                                    .append(pMonth)
                                    .append("/")
                                    .append(pYear)
                                    .append(" "));
    }
    public void rellenaSpinner() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAniadirFalta:
                comprobarCampos();
                break;
            case R.id.etFecha:
                Log.e("ONClickAniadirFalta","ONCLICK");
                new DatePickerDialog(AniadirFalta.this,
                        pDateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }
    public void  comprobarCampos(){
        fecha=((EditText)findViewById(R.id.etFecha)).getText().toString();
        claseFalta=(ClaseFalta) spAsignaturasUsuario.getSelectedItem();
        if(fecha.equals("dd/mm/yyyy")){
            Toast.makeText(AniadirFalta.this, "Elige una fecha para poder añadir la falta", Toast.LENGTH_SHORT).show();
        }
        else{
            //se manda al ws
            new aniadirFalta().execute();
        }
    }
    class aniadirFalta extends AsyncTask<String,String,Integer>{

        @Override
        protected Integer doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "anade_falta"));
            SimpleDateFormat formateador=new SimpleDateFormat("dd/mm/yyy");
            try {
                Date d=formateador.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            params.add(new BasicNameValuePair("fa_fecha", fecha));
            params.add(new BasicNameValuePair("asig_id",String.valueOf(claseFalta.getIdAsignatura())));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AniadirFalta.this);
            pDialog.setMessage(getResources().getString(R.string.aniadiendoFalta));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            pDialog.hide();
        }
    }

    class devuelveAsignaturasDeUsuario extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasAsignaturas"));
            params.add(new BasicNameValuePair("usu_id", fecha));
            params.add(new BasicNameValuePair("cu_id",String.valueOf(claseFalta.getIdAsignatura())));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AniadirFalta.this);
            pDialog.setMessage(getResources().getString(R.string.aniadiendoFalta));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void Void) {
            super.onPostExecute(Void);
            pDialog.hide();
        }
    }
}
