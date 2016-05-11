package com.example.borja.falton20;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
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
    private ArrayList<ClaseAsignatura> listAsignaturas;
    ClaseAsignatura asignaturaSeleccionada;
    private ArrayList<ClaseCurso> listCursos;
    private Spinner spAsignaturasUsuario, spCursosUsuario;
    private EditText etFecha;
    private String pYear, pMonth, pDay;
    private ClaseFalta claseFalta;
    private ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    private Context ctx;
    private static String url_alta_usuario=WebServices.desarrollo;
    private  String emailUsuario, nombreUsuario, telefonoUsuario;
    private  int idUsuario, idCurso, idAsignatura;
    final Calendar cal = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_falta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rellenaSpinnerCurso();
        spAsignaturasUsuario=(Spinner)findViewById(R.id.spAsignatura);
        spCursosUsuario=(Spinner)findViewById(R.id.spCurso);
        findViewById(R.id.btnAniadirFalta).setOnClickListener(this);
        etFecha=(EditText)findViewById(R.id.etFecha);
        etFecha.setOnClickListener(this);
        DatosUsuario.recuperarPreferences(getApplicationContext().getSharedPreferences("FaltOn", MODE_PRIVATE));
        idUsuario=DatosUsuario.getIdUsuario();
        ctx=this.getApplicationContext();
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
    public void rellenaSpinnerCurso() {
        new devuelveTodosCursos().execute();
    }

    public void rellenaSpinnerAsignatura() {
        new devuelveTodasAsignaturas().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAniadirFalta:
                comprobarCampos();
                break;
            case R.id.etFecha:
                Log.e("ONClickAniadirFalta","Pulsamos date");
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
        asignaturaSeleccionada=(ClaseAsignatura) spAsignaturasUsuario.getSelectedItem();
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

    class devuelveTodosCursos extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodosCursosUsuario"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario, "GET", params);
            Log.i("devuelveTodosCursos", "-->" + url_alta_usuario + "<-- " + json);
            try {
                JSONArray jArray = json.getJSONArray("cursos");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject fila = jArray.getJSONObject(i);
                    ClaseCurso claseCurso = new ClaseCurso(fila.getInt("cu_id"),
                            fila.getString("cu_nombre"),
                            fila.getString("cu_desc"));
                    listCursos.add(claseCurso);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listCursos=new ArrayList<ClaseCurso>();
            progressDialog = new ProgressDialog(AniadirFalta.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista));
            progressDialog.show();
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            for (int i = 0; i < listCursos.size(); i++)
                Log.i("Curso-->", listCursos.get(i).toString());
            ArrayAdapter spinner_adapter = new ArrayAdapter(ctx, R.layout.spinner_item, listCursos);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCursosUsuario.setAdapter(spinner_adapter);
            spCursosUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    idCurso=((ClaseCurso)spCursosUsuario.getSelectedItem()).getId();
                    new devuelveTodasAsignaturas().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            progressDialog.dismiss();
        }
    }

    class devuelveTodasAsignaturas extends AsyncTask<String,String,Integer> {
        @Override
        protected Integer doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tipo_consulta", "devuelveTodasAsignaturas"));
            params.add(new BasicNameValuePair("usu_id", String.valueOf(idUsuario)));
            params.add(new BasicNameValuePair("cu_id", String.valueOf(idCurso)));
            JSONObject json = jsonParser.makeHttpRequest(url_alta_usuario,"GET", params);
            Log.i("devuelveAsigXCurso","-->"+url_alta_usuario+"<-- "+json);
            try {
                JSONArray jArray  = json.getJSONArray("asignaturas");
                for(int i=0;i<jArray.length();i++){
                    JSONObject fila=jArray.getJSONObject(i);
                    ClaseAsignatura claseAsignatura=new ClaseAsignatura(fila.getInt("asig_id"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getInt("asig_porcentaje_faltas"),
                            fila.getString("asig_nombre"));
                    listAsignaturas.add(claseAsignatura);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             listAsignaturas=new ArrayList<ClaseAsignatura>();
            progressDialog = new ProgressDialog(AniadirFalta.this);
            progressDialog.setMessage(getResources().getString(R.string.cargando_lista));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ArrayAdapter spinner_adapterAsign = new ArrayAdapter(ctx, R.layout.spinner_item, listAsignaturas);
            spinner_adapterAsign.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spAsignaturasUsuario.setAdapter(spinner_adapterAsign);
            progressDialog.dismiss();
        }
    }

}
