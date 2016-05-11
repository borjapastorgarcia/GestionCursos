package com.example.borja.falton20;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by borja on 06/04/2016.
 */

public class AdaptadorRecyclerView extends RecyclerView.Adapter<AdaptadorRecyclerView.ItemViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private ArrayList<Item> datos;
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView  tituloItem, resumenItem, tiempoRestanteItem, fechaItem;
        private Button tipoItem;
        public ItemViewHolder(View itemView){
            super(itemView);
            tituloItem=(TextView) itemView.findViewById(R.id.TitleItem);
            resumenItem=(TextView) itemView.findViewById(R.id.resumenItem);
            tiempoRestanteItem=(TextView)itemView.findViewById(R.id.tiempoRestante);
            fechaItem=(TextView)itemView.findViewById(R.id.fechaItem);
            tipoItem=(Button)itemView.findViewById(R.id.btnTipoItem);

        }
        public void bindTitular(Item t) {
            tituloItem.setText(t.getTituloItem());
            resumenItem.setText(t.getResumenItem());
            tiempoRestanteItem.setText(t.getTiempoRestanteItem());
            fechaItem.setText(String.valueOf(t.getFechaItem()));
            tipoItem.setText(t.getTipoItem());
        }
    }
    public AdaptadorRecyclerView(ArrayList<Item> datos) {
        this.datos = datos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        itemView.setOnClickListener(this);
        ItemViewHolder tvh = new ItemViewHolder(itemView);
        return tvh;
    }
    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int pos) {
        Item item = datos.get(pos);
        viewHolder.bindTitular(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

}
