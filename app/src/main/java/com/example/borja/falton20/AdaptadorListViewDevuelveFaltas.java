package com.example.borja.falton20;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by borja on 10/05/2016.
 */
public class AdaptadorListViewDevuelveFaltas extends ArrayAdapter<ClaseFalta> {
    private List<ClaseFalta> listaFaltas=new ArrayList<ClaseFalta>();
    public AdaptadorListViewDevuelveFaltas(Context context, ArrayList<ClaseFalta> listaFaltas) {
        super(context,0, listaFaltas);
    }
}
