package org.kalla.enterprise.movil.appupdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import androidx.core.content.FileProvider;

import java.io.File;

public class VerificarApp {
    Context contexto;
    IAppUpdateListener notificador;
    boolean notificarUpdate;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            if (message.arg1 == Activity.RESULT_OK && data != null) {

                String path = data.getString("absolutePath");

                Intent intent = new Intent(Intent.ACTION_VIEW);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    Uri apkURI = FileProvider.getUriForFile(
                            contexto,
                            contexto.getApplicationContext()
                                    .getPackageName() + ".provider", new File(path));
                    intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                }
                contexto.startActivity(intent);
            } else {

                if (notificador != null && message != null && message.obj != null && message.what > 0) {
                    notificador.notificarUpdate(message.what, message.obj.toString(), data);
                }

            }

        }

        ;
    };


    public VerificarApp(Context contex) {
        this.contexto = contex;
        this.notificador = null;
        this.notificarUpdate = false;
    }

    public VerificarApp(Context contex, IAppUpdateListener notificador, boolean notificarUpdate) {
        this.contexto = contex;
        this.notificador = notificador;
        this.notificarUpdate = notificarUpdate;

    }

    public void installApp(Bundle data) {
        String path = data.getString("absolutePath");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        contexto.startActivity(intent);

    }

    public IAppUpdateListener getAcercaDe() {
        return notificador;

    }

    public void setAcercaDe(IAppUpdateListener acercaDe) {
        this.notificador = acercaDe;
    }

    public void verificarApp(boolean verificarApp, boolean synConfig) {
        Intent intent = null;
        intent = new Intent(contexto, AppUpdate.class);

        Messenger messenger = new Messenger(handler);

        intent.putExtra("MESSENGER", messenger);
        intent.putExtra("SyncConf", synConfig);
        intent.putExtra("VerificarApp", verificarApp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contexto.startForegroundService(intent);
        } else {
            contexto.startService(intent);
        }
    }

    public void verificarApp() {

        verificarApp(true, true);
    }


}
