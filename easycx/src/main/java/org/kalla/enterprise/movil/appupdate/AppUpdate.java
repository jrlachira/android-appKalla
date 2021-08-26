package org.kalla.enterprise.movil.appupdate;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.kalla.enterprise.movil.appupdate.vo.VersionAppVO;
import org.kalla.enterprise.movil.com.Comunicacion;
import org.kalla.enterprise.movil.com.IConexionServer;
import org.kalla.enterprise.movil.com.vo.ConfiguracionVO;
import org.kalla.enterprise.movil.com.vo.ConfiguracionVORPT;
import org.kalla.enterprise.movil.com.vo.ErrorComunicacion;
import org.kalla.enterprise.movil.com.vo.ObjetoRecibir;
import org.kalla.enterprise.movil.util.Utilidades;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class AppUpdate extends IntentService implements IConexionServer {


    //public static final String GestorDescargas = "http://seraticcowindev.eastus2.cloudapp.azure.com:8084/ManagerTelefonica/Comunicacion";
    //public static final String GestorDescargas = "http://200.48.131.39:8113/ManagerTelefonica/Comunicacion";
    public static final String GestorDescargas = "http://52.70.227.200:8680/ManagerTelefonica/Comunicacion";
    private static final int NOTIFY_ME_ID = 1337;
    private static final String PREFERENCES_SEGUIMIENTO = "org.seratic.enterprise.movil.appupdate.AppUdate";
    public static int VERSION_COMUNICACION = 2;
    //public static final String GestorDescargas = "http://192.168.1.6:8084/ManagerTelefonica/Comunicacion";
    Messenger messenger;
    BroadcastReceiver receiver;
    boolean verificar;
    boolean syncConf;
    private int result = Activity.RESULT_CANCELED;
    private Bundle extras;
    private Context ctx;
    private long enqueue;
    private DownloadManager dm;
    private String empresa;
    private static final String TAG = AppUpdate.class.getSimpleName();

    public AppUpdate() {
        super("AppUpdate");
    }

    // Will be called asynchronously be Android
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("AppUpdate", "Actualizando Services en " + GestorDescargas);
        extras = intent.getExtras();
        messenger = (Messenger) extras.get("MESSENGER");
        syncConf = (Boolean) extras.get("SyncConf");
        verificar = (Boolean) extras.get("VerificarApp");

        ctx = getApplicationContext();

        Log.i("AppUpdate", "La empresa es: " + Utilidades.getEmpresa(ctx));
        empresa = Utilidades.getEmpresa(ctx);
        if (syncConf) {
            syncConf();
        } else {
            verificarVersion();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(TAG.toLowerCase(), TAG, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(3, new NotificationCompat.Builder(this, TAG).setPriority(NotificationCompat.PRIORITY_HIGH).build());
        }
    }

    private void syncConf() {
        Comunicacion com = new Comunicacion(this);
        com.setContext(ctx);
        com.setURL(GestorDescargas);
        com.setTimeout(60000);
        VersionAppVO os = new VersionAppVO();
        Log.i("AppUpdate", "verificando version en: " + GestorDescargas);
        os.setImeiSIM(Utilidades.getImei(ctx));
        if (empresa != null) {
            os.setNombreApp(Utilidades.getAplicacionName(ctx) + '|' + empresa);
        } else {
            os.setNombreApp(Utilidades.getAplicacionName(ctx));
        }
        com.sincronizar(VERSION_COMUNICACION, (short) 4, os, ConfiguracionVORPT.class, true);
        com.start();
    }

    private void verificarVersion() {
        ctx = getApplicationContext();
        Comunicacion com = new Comunicacion(this);
        com.setContext(ctx);
        com.setURL(GestorDescargas);
        com.setTimeout(60000);
        VersionAppVO os = new VersionAppVO();
        os.setImeiSIM(Utilidades.getImei(ctx));
        Log.i("AppUpdate", "verificando version en: " + GestorDescargas);
        if (empresa != null) {
            os.setNombreApp(Utilidades.getAplicacionName(ctx) + '|' + empresa);
        } else {
            os.setNombreApp(Utilidades.getAplicacionName(ctx));
        }
        com.sincronizar(VERSION_COMUNICACION, (short) 1, os, VersionAppVO.class, true);
        com.start();
    }

    @Override
    public void resSincEnd(int codSinc, ObjetoRecibir rta) throws Exception {
        if (codSinc == 1) {

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Seguridad", Context.MODE_PRIVATE | Context.MODE_PRIVATE);
            sharedPreferences.edit().putInt("valida2", 1).commit();
            Log.i("AppUpdate", "IMEI y Aplicacion Validad2");
            VersionAppVO versionApp = (VersionAppVO) rta;
            String version = versionApp.getNombreVersion();
            int versionCode = versionApp.getVersionCodigo();
            Message msg = new Message();
            if (versionCode == -1) {
                sharedPreferences.edit().putBoolean("imei_valido", false).commit();
                if (messenger != null) {

                    msg.what = Constantes.IMEI_NOT_VALID;
                    msg.obj = (String) "IMEI NO VALIDO";
                    mostrarAlertaLicencia();
                    try {
                        messenger.send(msg);
                    } catch (android.os.RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }

                }

            } else {
                sharedPreferences.edit().putBoolean("imei_valido", true).commit();

                if (messenger != null) {

                    msg.what = Constantes.IMEI_OK;
                    msg.obj = (String) "OK";

                    try {
                        messenger.send(msg);
                    } catch (android.os.RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }

                }

                int VersionCodeAPP = org.kalla.enterprise.movil.util.Utilidades.obtenerVersionCode(ctx);
                Log.i("AppUpdate", "Version Instalada - Version code:" + VersionCodeAPP + "]");

                Log.i("AppUpdate", "Version Desplegada[Version name:" + versionApp.getNombreVersion() + " - Version code:" + versionCode + "]");
                String fileName = "app" + versionApp.getNombreVersion() + "." + versionCode + ".apk";// data.getLastPathSegment();

                // Context ctx = ActSplash.this;
                if (org.kalla.enterprise.movil.util.Utilidades.obtenerVersionCode(ctx) < (versionCode)) {
                    boolean exitoso = true;
                    File output = new File(Environment.getExternalStoragePublicDirectory("seratic"), fileName);
                    if (output.exists()) {
                        result = Activity.RESULT_OK;
                        Thread.sleep(5000);
                    } else {
                        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        Request request = new Request(Uri.parse(versionApp.getUrlAPK()));
                        request.setTitle(versionApp.getNombreVersion()).setDescription("Descargado actualizacion.").setDestinationInExternalPublicDir("/seratic", fileName);
                        enqueue = dm.enqueue(request);
                        boolean descargando = true;
                        Query query = new Query();
                        query.setFilterById(enqueue);
                        while (descargando) {

                            Cursor c = dm.query(query);
                            if (c.moveToFirst()) {
                                int estatus = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                                if (DownloadManager.STATUS_PENDING == estatus || DownloadManager.STATUS_PAUSED == estatus || DownloadManager.STATUS_RUNNING == estatus) {
                                    descargando = true;
                                } else {
                                    descargando = false;
                                }

                                if (DownloadManager.STATUS_SUCCESSFUL == estatus) {
                                    result = Activity.RESULT_OK;
                                }
                            }
                            Thread.sleep(2000);
                        }
                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int estatus = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (DownloadManager.STATUS_SUCCESSFUL == estatus) {
                                result = Activity.RESULT_OK;
                            }
                            int status = DownloadManager.STATUS_SUCCESSFUL;
                        }

                    }
                    if (extras != null) {
                        Messenger messenger = (Messenger) extras.get("MESSENGER");
                        msg = Message.obtain();
                        msg.arg1 = result;
                        Bundle bundle = new Bundle();
                        bundle.putString("absolutePath", output.getAbsolutePath());
                        msg.setData(bundle);
                        try {
                            messenger.send(msg);
                        } catch (android.os.RemoteException e1) {
                            Log.w(getClass().getName(), "Exception sending message", e1);
                        }

                    }
                }

            }

        } else {
            if (codSinc == 3) {
                Toast.makeText(ctx, "Licencia no Valida2", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Seguridad", Context.MODE_PRIVATE | Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("valida2", 2).commit();
            }

            if (codSinc == 4) {

                ctx = getApplicationContext();
                ConfiguracionVORPT obj = (ConfiguracionVORPT) rta;

                int contUrl = 0;
                int contConfiguraciones = 0;
                if (obj != null && obj.getConfiguraciones() != null && obj.getConfiguraciones().size() > 0) {

                    for (ConfiguracionVO conf : obj.getConfiguraciones()) {
                        contConfiguraciones = contConfiguraciones + 1;
                        if (conf.getNombre().compareTo("url") == 0) {
                            Utilidades.getImei(ctx);
                            org.kalla.enterprise.movil.com.Utilidades.setUrl(conf.getValor(), contUrl, ctx);
                            contUrl++;
                        }

                        if (conf.getNombre().compareTo("urlNotificar") == 0) {
                            org.kalla.enterprise.movil.com.Utilidades.setUrlNotificar(conf.getValor(), ctx);
                        }

                        org.kalla.enterprise.movil.com.Utilidades.setConfiguracion(conf.getNombre(), conf.getValor(), contConfiguraciones, ctx);

                    }
                    org.kalla.enterprise.movil.com.Utilidades.setNumUrl(contUrl, ctx);
                    org.kalla.enterprise.movil.com.Utilidades.setNumConfiguraciones(contConfiguraciones, ctx);
                }

                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Configuraciones", (Serializable) obj.getConfiguraciones());

                if (messenger != null && obj != null && obj.getConfiguraciones() != null) {

                    msg.what = Constantes.DOWNLOAD_CONFIG_OK;
                    msg.obj = (String) "OK";
                    msg.setData(bundle);

                    try {
                        messenger.send(msg);
                    } catch (android.os.RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }

                } else {

                    msg.what = Constantes.DOWNLOAD_CONFIG_NO_DATA;
                    msg.obj = (String) "IMEI:" + Utilidades.getImei(ctx) + " Sin Configuracion";

                    try {
                        messenger.send(msg);
                    } catch (android.os.RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }

                }

                if (verificar) {
                    verificarVersion();
                }

            }

        }

    }

    @Override
    public void resSincEnd(int codSinc, ArrayList<?> rta, boolean esFinal, int numPaginas, int pagActual) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void resError(ErrorComunicacion error) {
        // TODO Auto-generated method stub

        if (messenger != null && error.getErrorEn() > 1) {
            Message msg = new Message();
            msg.what = Constantes.ERROR_COMUNICACION_UPDATE;
            msg.obj = (String) error.getMsg();

            try {
                messenger.send(msg);
            } catch (android.os.RemoteException e1) {
                Log.w(getClass().getName(), "Exception sending message", e1);
            }

        }

    }

    @Override
    public boolean borrarData(int codSinc) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void traceCx(String trace) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notificarReintento(int numReintento) {
        // TODO Auto-generated method stub

    }

    public void mostrarAlertaLicencia() {
        Intent i = new Intent(getApplicationContext(), org.kalla.enterprise.movil.appupdate.ShowingDialog.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        // unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        // TODO Auto-generated method stub
        return ctx;
    }

}
