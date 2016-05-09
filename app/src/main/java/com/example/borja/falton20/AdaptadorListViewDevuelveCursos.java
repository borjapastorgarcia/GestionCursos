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
 * Created by borja on 09/05/2016.
 */
public class AdaptadorListViewDevuelveCursos extends ArrayAdapter<ClaseCurso>{
  private List<ClaseCurso>listaCursos=new ArrayList<ClaseCurso>();
    private Button btnNombreCurso;
    private TextView etDescripcionCurso;
    public AdaptadorListViewDevuelveCursos(Context context, ArrayList<ClaseCurso>listaCursos){
        super(context,0,listaCursos);
        this.listaCursos=listaCursos;
        Log.e("CONSTRUCTORAdaptador","");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClaseCurso curso=getItem(position);
        Log.e("CursoEnAdaptador",curso+"");
       if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_curso, parent, false);
        }
        btnNombreCurso=(Button)convertView.findViewById(R.id.btnNombreCurso);
        etDescripcionCurso=(TextView) convertView.findViewById(R.id.etDescripcionCurso);
        btnNombreCurso.setText(curso.getNombre());
        etDescripcionCurso.setText(curso.getDescripcion());
        Log.e("AdaptadorTodosLosCursos",curso.getNombre()+",descripcion: "+curso.getDescripcion());
        return convertView;
    }

}
