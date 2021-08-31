package org.kalla.enterprise.movil.gps.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.kalla.enterprise.movil.gps.com.EnvioSeguimientoManager;
import org.kalla.enterprise.movil.gps.services.SeguimientoReceiver;
import org.kalla.enterprise.movil.gps.services.ServicioSeguimiento;

public class GeneralReceiver extends BroadcastReceiver {
    private static final String TAG = GeneralReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive, " + intent.getAction());
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.isConnected()) {
                Log.i("GeneralReceiver ", "GeneralReceiver network is connected");
                EnvioSeguimientoManager envio = new EnvioSeguimientoManager(context);
                envio.enviarSeguimientosPendientes(context, SeguimientoReceiver.obtenerUbicacionesPendientes(context));
            }
        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.i("GeneralReceiver ", "-------Se inicio el móvil---------");
            Intent mIntent = new Intent(context, ServicioSeguimiento.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(mIntent);
            } else {
                context.startService(mIntent);
            }
        }
    }

}
