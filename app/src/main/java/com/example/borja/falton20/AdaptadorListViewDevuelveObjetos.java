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
 * Created by borja on 13/05/2016.
 */
public class AdaptadorListViewDevuelveObjetos  extends ArrayAdapter<ClaseObjeto> {
    private List<ClaseObjeto> listaObjetos=new ArrayList<ClaseObjeto>();
    private Button btnTipoObjeto;
    private TextView etTituloObjeto,etDescripcionObjeto,etPrecioObjeto;
    public AdaptadorListViewDevuelveObjetos(Context context, ArrayList<ClaseObjeto> listaObjetos) {
        super(context, 0, listaObjetos);
        this.listaObjetos=listaObjetos;
        Log.e("AdaptadorObjeto","");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClaseObjeto objeto=getItem(position);
        Log.e("CursoEnAdaptador",objeto+"");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_objeto, parent, false);
        }
        btnTipoObjeto=(Button)convertView.findViewById(R.id.btnTipoObjeto);
        etTituloObjeto=(TextView) convertView.findViewById(R.id.etTituloObjeto);
        etDescripcionObjeto=(TextView) convertView.findViewById(R.id.etDescripcionObjeto);
        etPrecioObjeto=(TextView) convertView.findViewById(R.id.etPrecioObjeto);
        btnTipoObjeto.setText(objeto.getTipo());
        etTituloObjeto.setText(objeto.getTipo());
        etDescripcionObjeto.setText(objeto.getDescripcion());
        etPrecioObjeto.setText(String.valueOf(objeto.getPrecio()));
        return convertView;
    }
}
