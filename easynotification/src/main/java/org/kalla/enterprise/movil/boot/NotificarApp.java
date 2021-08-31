package org.kalla.enterprise.movil.boot;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.kalla.enterprise.movil.noty.util.Utilidades;


public class NotificarApp {
    private static final String TAG = NotificarApp.class.getSimpleName();

    public NotificarApp(Context contex, String SERVERPORT, String URL, String METHOD, boolean isVisual) {
        Intent intent = null;
        intent = new Intent(contex, ServiceBootSeratic.class);

        if (isVisual) {
            //intent.putExtra("url", "http://"+URL+":"+SERVERPORT);
            org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("http://" + URL + ":" + SERVERPORT + METHOD, contex);
        } else {
            //intent.putExtra("url", "ws://"+URL+":"+SERVERPORT+METHOD);
            org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://" + URL + ":" + SERVERPORT + METHOD, contex);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contex.startForegroundService(intent);
        } else {
            contex.startService(intent);
        }
        //org.seratic.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://"+URL+":"+SERVERPORT+METHOD, contex);

    }


    public NotificarApp(Context contex, String URL, boolean isVisual) {
        Intent intent = null;
        intent = new Intent(contex, ServiceBootSeratic.class);
        if (URL.compareTo("-1") != 0) {
            if (isVisual) {
                //intent.putExtra("url", "http://"+URL+":"+SERVERPORT);
                org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("http://" + URL, contex);
            } else {
                //intent.putExtra("url", "ws://"+URL+":"+SERVERPORT+METHOD);
                org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://" + URL, contex);
            }
        } else {
            Log.i(TAG, "NotificarApp, Error en Formato de la URL");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contex.startForegroundService(intent);
        } else {
            contex.startService(intent);
        }
        //org.seratic.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://"+URL+":"+SERVERPORT+METHOD, contex);

    }


    public NotificarApp(Context contex, String URL, boolean isVisual, String parNombre, String parValor) {
        Intent intent = null;
        intent = new Intent(contex, ServiceBootSeratic.class);
        if (URL.compareTo("-1") != 0) {
            if (isVisual) {
                //intent.putExtra("url", "http://"+URL+":"+SERVERPORT);
                org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("http://" + URL, contex);
            } else {
                //intent.putExtra("url", "ws://"+URL+":"+SERVERPORT+METHOD);
                org.kalla.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://" + URL, contex);
            }
        } else {
            Log.i(TAG, "NotificarApp, Error en Formato de la URL");
        }

        Log.i(TAG, "NotificarApp");
        org.kalla.enterprise.movil.noty.util.Utilidades.setParametersUrlNotificacion(parNombre, parValor, 0, contex);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contex.startForegroundService(intent);
        } else {
            contex.startService(intent);
        }

        Intent inicio = new Intent(Utilidades.NOTIFICATION);
        inicio.putExtra("init", "ok");
        inicio.putExtra("tipo", isVisual);
        contex.sendBroadcast(inicio);


        //org.seratic.enterprise.movil.noty.util.Utilidades.setUrlNotificacion("ws://"+URL+":"+SERVERPORT+METHOD, contex);

    }


}
