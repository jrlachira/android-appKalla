package org.kalla.enterprise.movil.com;

import android.content.Context;
import android.content.SharedPreferences;

import org.kalla.enterprise.movil.com.vo.ConfiguracionVO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Utilidades {

    public static void writeFecha(DataOutputStream dos, Date fecha) throws IOException {
        long lFecha = 0;
        if (fecha != null) {
            lFecha = fecha.getTime() + TimeZone.getDefault().getRawOffset();
        }
        dos.writeLong(lFecha);
    }

    public static void writeFecha(DataOutputStream dos, long fecha) throws IOException {
        if (fecha != 0) {
            fecha += TimeZone.getDefault().getRawOffset();
        }
        dos.writeLong(fecha);
    }

    public static Date readFechaDate(DataInputStream dis) throws IOException {
        Date retorno = null;
        long lFecha = dis.readLong();
        if (lFecha != 0) {
            retorno = new Date(lFecha - TimeZone.getDefault().getRawOffset());
        }
        return retorno;
    }

    public static long readFechaLong(DataInputStream dis) throws IOException {
        long lFecha = dis.readLong();
        if (lFecha != 0) {
            lFecha -= TimeZone.getDefault().getRawOffset();
        }
        return lFecha;
    }



    public static void setUrlPredeterminada(String URL, Context context) {
        if(context!=null){
            SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlDefault", Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("url", URL);
            editor.commit();
        }
    }

    public static String getURLPredeterminada(Context context) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlDefault", Context.MODE_PRIVATE );
        String ip = SaveData.getString("url", "-1");
        return ip;
    }

    public static void setUrlNotificacion(String URL, Context context) {
        if(context!=null){
            SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlDataNotificacion", Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("url", URL);
            editor.commit();
        }
    }

    public static String getURLNotificacion(Context context) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlDataNotificacion", Context.MODE_PRIVATE );
        String ip = SaveData.getString("url", "-1");
        return ip;
    }

    public static void setActualUrl(String URL, Context context) {
        if(context!=null){
            SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("url", URL);
            editor.commit();
        }
    }

    public static String getActualURL(Context context) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
        String ip = SaveData.getString("url", "-1");
        return ip;
    }

    public static void setUrl(String URL, int pos, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("url" + pos, URL);
        editor.commit();
    }

    public static String getURL(Context context, int pos) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
        String ip = SaveData.getString("url" + pos, "-1");
        return ip;
    }

    public static void setNumUrl(int cantidad, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
        // Log.i("ingresando timepo:",now.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cantidad", cantidad);
        editor.commit();
    }

    public static int getNumUrl(Context context) {
        if (context != null) {
            SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
            int cantidad = SaveData.getInt("cantidad", 0);
            return cantidad;
        } else {
            return 0;
        }
    }


    public static void setUrlNotificar(String URL, Context context) {
        if(context!=null){
            SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlNotificar", Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("url", URL);
            editor.commit();
        }
    }

    public static String getURLNotificar(Context context) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlNotificar", Context.MODE_PRIVATE );
        String ip = SaveData.getString("url", "-1");
        return ip;
    }


    public static void setConfiguracion(String nombre,String valor, int pos, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PreferenciasConfiguracion", Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("valor"+pos, valor);
        editor.putString("nombre"+pos, nombre);
        editor.commit();
    }


    public static ConfiguracionVO getConfiguracion(Context context, int pos) {
        ConfiguracionVO ret = new ConfiguracionVO();
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlData", Context.MODE_PRIVATE );
        ret.setNombre(SaveData.getString("nombre"+pos, "-1"));
        ret.setValor(SaveData.getString("valor"+pos, "-1"));
        return ret;
    }




    public static void setNumConfiguraciones(int cantidad, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PreferenciasConfiguracionData", Context.MODE_PRIVATE );
        // Log.i("ingresando timepo:",now.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("cantidad", cantidad);
        editor.commit();
    }

    public static int getNumConfiguraciones(Context context) {
        if (context != null) {
            SharedPreferences SaveData = context.getSharedPreferences("PreferenciasConfiguracionData", Context.MODE_PRIVATE );
            int cantidad = SaveData.getInt("cantidad", 0);
            return cantidad;
        } else {
            return 0;
        }
    }



    public static List<String> getProperty(String nombre,Context context){
        List<String> retorno = new ArrayList<String>();
        int n = getNumConfiguraciones(context);

        for(int i = 0 ;i<=n ; i++){
            ConfiguracionVO objConfiguracion = getConfiguracion(context,i);
            if(objConfiguracion.getNombre().compareTo(nombre)==0){
                retorno.add(objConfiguracion.getValor());
            }

        }
        return retorno;
    }



}
