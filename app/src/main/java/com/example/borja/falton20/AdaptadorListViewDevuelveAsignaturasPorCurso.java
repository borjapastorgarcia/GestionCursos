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
 * Created by borja on 10/05/2016.
 */
public class AdaptadorListViewDevuelveAsignaturasPorCurso extends ArrayAdapter<ClaseAsignatura> {
    private List<ClaseAsignatura> listaAsignaturas=new ArrayList<ClaseAsignatura>();
    private Button btnNombreAsignatura;
    private TextView etPorcentajeFaltas, etFaltasllevas;
    public AdaptadorListViewDevuelveAsignaturasPorCurso(Context context, ArrayList<ClaseAsignatura> listaAsignaturas){
        super(context,0,listaAsignaturas);
        this.listaAsignaturas=listaAsignaturas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClaseAsignatura asignatura=getItem(position);
        Log.e("asignaturaEnAdaptador",asignatura+"");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_asignatura, parent, false);
        }
        btnNombreAsignatura=(Button)convertView.findViewById(R.id.btnNombreAsignatura);
        etPorcentajeFaltas=(TextView) convertView.findViewById(R.id.etPorcentajeFaltasAsignatura);
        etFaltasllevas=(TextView) convertView.findViewById(R.id.etFaltasTotalesAsignatur);
        btnNombreAsignatura.setText(asignatura.getNombreAsignatura());
        etPorcentajeFaltas.setText(getContext().getResources().getString(R.string.porcentaje_faltas));
        etPorcentajeFaltas.append(String.valueOf(asignatura.getPorcentajeFaltas()));
        etFaltasllevas.setText(getContext().getResources().getString(R.string.total_faltas));
        etFaltasllevas.append(String.valueOf(asignatura.getFaltasActuales()));
        Log.e("AdaptadorAsigXCurso",asignatura.getNombreAsignatura()+",descripcion: "+etPorcentajeFaltas.getText().toString());
        return convertView;
    }
}
