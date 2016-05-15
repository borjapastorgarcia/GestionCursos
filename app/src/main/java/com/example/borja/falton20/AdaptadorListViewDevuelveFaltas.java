package com.example.borja.falton20;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by borja on 15/05/2016.
 */
public class AdaptadorListViewDevuelveFaltas extends ArrayAdapter<ClaseFalta>{
    private List<ClaseFalta> listaFaltas=new ArrayList<ClaseFalta>();
    private List<ClaseAsignatura>listaAsignaturas=new ArrayList<>();
    private Button btnAsignatura;
    private TextView tvTotalFaltasAsignatura;
    public AdaptadorListViewDevuelveFaltas(Context context, List<ClaseFalta> listaFaltas, List<ClaseAsignatura>listaAsignaturas) {
        super(context,0, listaFaltas);
        this.listaFaltas=listaFaltas;
        this.listaAsignaturas=listaAsignaturas;
       // Log.e("AdaptadorFaltas","FaltasN-->"+listaFaltas.size()+", AsigN-->"+listaAsignaturas.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClaseFalta falta=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_falta, parent, false);
        }
        String nombreAsignatura="";
        for(int i=0;i<listaAsignaturas.size();i++){
            if(listaFaltas.get(position).getIdAsignatura()==listaAsignaturas.get(i).getIdAsignatura())
                nombreAsignatura=listaAsignaturas.get(i).getNombreAsignatura();
            Log.e("NombreAsignatura",nombreAsignatura);
        }
        btnAsignatura=(Button)convertView.findViewById(R.id.btnNombreAsignatura);
        btnAsignatura.setText(nombreAsignatura);
        tvTotalFaltasAsignatura=(TextView)convertView.findViewById(R.id.etFaltasTotalesAsignatura);
        tvTotalFaltasAsignatura.setText(" "+listaFaltas.get(position).getTotalFaltasXAsignatura());
        return convertView;
    }
}
