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
public class AdaptadorListViewDevuelveIdea  extends ArrayAdapter<ClaseIdea> {
    private List<ClaseIdea> listaIdeas=new ArrayList<ClaseIdea>();
    private Button btnNombreIdea;
    private TextView etDescripcionIdea;
    public AdaptadorListViewDevuelveIdea(Context context, ArrayList<ClaseIdea> listaIdeas) {
        super(context,0, listaIdeas);
        this.listaIdeas=listaIdeas;
        Log.e("AdaptadorIdeas","");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClaseIdea idea=getItem(position);
        Log.e("ideaEnAdaptador",idea+"");
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_idea, parent, false);
        }
        btnNombreIdea=(Button)convertView.findViewById(R.id.btnTituloIdea);
        etDescripcionIdea=(TextView) convertView.findViewById(R.id.tvDescripcionIdea);
        btnNombreIdea.setText(idea.getTituloIdea());
        etDescripcionIdea.setText(idea.getDescripcionIdea());
        Log.e("AdaptadorTodasLosIdeas",idea.getTituloIdea()+",descripcion: "+idea.getDescripcionIdea());
        return convertView;
    }
}
