package org.kalla.enterprise.movil.noty.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Utilidades {

    public static final String NOTIFICATION = "org.kalla.enterprise.movil.boot";

    public static void setUrlNotificacion(String URL, Context context) {

        SharedPreferences prefs = context.getSharedPreferences("PreferenciasUrlDataNotificacion", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("url", URL);
        editor.commit();
    }


    public static void setParametersUrlNotificacion(String parNombre, String parValor, int pos, Context context) {

        SharedPreferences prefs = context.getSharedPreferences("PreferenciasDataNotificacion", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PNombre" + pos, parNombre);
        editor.putString("PValor" + pos, parValor);
        //editor.apply();
        if (editor.commit()) {
            Log.i("NotificarApp.java ", "commit true");
        } else {
            Log.i("NotificarApp.java ", "commit false");
        }

        String valor3 = getValorParametersUrl(context, pos);
        Log.i("NotificarApp.java setParametersUrlNotificacion PValor3: ", valor3);
    }


    public static String getNombreParametersUrl(Context context, int pos) {

        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasDataNotificacion", Context.MODE_MULTI_PROCESS);
        String valor = SaveData.getString("PNombre" + pos, "");
        Log.i("NotificarApp.java getNombreParametersUrl PNombre: ", valor);
        return valor;
    }

    public static String getValorParametersUrl(Context context, int pos) {

        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasDataNotificacion", Context.MODE_MULTI_PROCESS);
        String valor = SaveData.getString("PValor" + pos, "");
        Log.i("NotificarApp.java getNombreParametersUrl PValor: ", valor);
        return valor;
    }

    public static String getURLNotificacion(Context context) {
        SharedPreferences SaveData = context.getSharedPreferences("PreferenciasUrlDataNotificacion", Context.MODE_MULTI_PROCESS);
        String ip = SaveData.getString("url", "-1");
        return ip;
    }

    public static String getImei(Context ctx) {
        String phoneId;
        try {
            TelephonyManager tMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 29) {
                phoneId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                phoneId = tMgr.getDeviceId();
                if (phoneId == null || Long.parseLong(phoneId) == 0)
                    phoneId = "000000000000000";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "000000000000000";
        }
//        phoneId = "357863106370364";
        Log.d("Utilidades", "phoneId: " + phoneId);
        return phoneId;
    }
}
