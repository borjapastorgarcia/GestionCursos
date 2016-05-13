package com.example.borja.falton20;

import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by borja on 08/05/2016.
 */
public class DatosUsuario {
    private static String emailUsuario, nombreUsuario, telefonoUsuario;
    private static int idUsuario;
    private SharedPreferences pref;
    public static void recuperarPreferences(SharedPreferences pref){
        emailUsuario= pref.getString("email","");
        nombreUsuario=pref.getString("nombre","");
        telefonoUsuario=pref.getString("telefono","");
        idUsuario=pref.getInt("idUsuario",0);
        Log.e("DatosUsuario-->","Email:"+emailUsuario+", ID:"+idUsuario);
    }


    public static String getEmailUsuario() {
        return emailUsuario;
    }

    public static void setEmailUsuario(String emailUsuario) {
        DatosUsuario.emailUsuario = emailUsuario;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        DatosUsuario.nombreUsuario = nombreUsuario;
    }

    public static String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public static void setTelefonoUsuario(String telefonoUsuario) {
        DatosUsuario.telefonoUsuario = telefonoUsuario;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        DatosUsuario.idUsuario = idUsuario;
    }
}
