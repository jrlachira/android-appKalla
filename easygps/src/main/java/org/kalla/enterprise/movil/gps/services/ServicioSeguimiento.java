package org.kalla.enterprise.movil.gps.services;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationRequest;

import org.kalla.enterprise.movil.gps.EasyGPS;
import org.kalla.enterprise.movil.gps.receiver.GeneralReceiver;
public class ServicioSeguimiento extends Service {
    // Restart service every 60 seconds
    protected static final long REPEAT_TIME = 1000 * 60;
    private static final String TAG = ServicioSeguimiento.class.getSimpleName();
    long frecuencia = 0;
    private GeneralReceiver mReceiver;
    private String frecuencia_cron;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(TAG.toLowerCase(), TAG, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(1, new NotificationCompat.Builder(this, TAG).setPriority(NotificationCompat.PRIORITY_HIGH).build());
        }
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);


        mReceiver = new GeneralReceiver();
        registerReceiver(mReceiver, filter);
        Log.i(TAG, "onCreate()");
    }

    protected void notificarUI(String msg) {
        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        intent.setAction("org.kalla.enterprise.movil.gps.SEGUIMIENTO");
        //	getApplicationContext().sendBroadcast(intent);

        Log.i("EnvioPeticiones", "KALLA ENVIO" + msg);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        super.onStartCommand(intent, flags, startId);

        SharedPreferences prefSEguimiento = getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_MULTI_PROCESS);
        frecuencia = prefSEguimiento.getLong("frecuencia_seguimiento", REPEAT_TIME);
        frecuencia_cron = prefSEguimiento.getString("frecuencia_seguimiento_crontab", "");

        //
        notificarUI("Iniciando");
        if (frecuencia < REPEAT_TIME) {
            frecuencia = REPEAT_TIME;
        }
        //
        int codigoSync = prefSEguimiento.getInt("codigo", 0);
        if (codigoSync > 0) {
            iniciando();
            Log.i(TAG, "onStartCommand " + startId);
        } else {
            Log.i(TAG, "on Servicio Seguimiento NO INICIADO");
        }
        return START_STICKY;
    }

    public void iniciando() {
        //String frecString="10,30,50 * * * 1,3,5";

        EasyGPS easyGPS = new EasyGPS(this.getApplicationContext(), frecuencia, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        easyGPS.iniciarSeguimientoPeriodico();

        Log.i(TAG, "on Servicio Seguimiento para frecuencia " + frecuencia + "--" + frecuencia_cron + " and " + frecuencia);
        SharedPreferences prefSEguimiento = getApplicationContext().getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_PRIVATE);

        int tiempoRestante = SeguimientoReceiver.getTiempoRestante(getApplicationContext(), prefSEguimiento);
        if (tiempoRestante == 0) {
            Intent intent = new Intent(getApplicationContext(), SeguimientoReceiver.class);
            intent.putExtra("tiempoRestante", tiempoRestante);
            getApplicationContext().sendBroadcast(intent);
            notificarUI("NotInicio");
        }
        SeguimientoReceiver.setAlarm(getApplicationContext(), 10000);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "on SERATIC onDestroy() Servicio de Seguimiento ");
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        // frecuencia=intent.getLongExtra("frecuencia", REPEAT_TIME);
        // Log.i(TAG, "on Servicio Bind frecuencia "+frecuencia);
        return null;
    }

}
