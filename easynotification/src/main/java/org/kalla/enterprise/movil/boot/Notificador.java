package org.kalla.enterprise.movil.boot;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class Notificador extends Activity {

    private final static String TAG = Notificador.class.getSimpleName();
    final Handler mHandlerHilo = new Handler();
    String texto;
    String Ldestino;
    private ServiceConnection sConn;
    private Messenger messenger;
    final Runnable ejecutarAccion = new Runnable() {
        public void run() {
            if (Ldestino != null) {
                EnviarMensaje(texto, Ldestino);
            } else {
                EnviarMensaje(texto);
            }
        }
    };
    private boolean conectado;
    private Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciarConexionServicio();
        Ldestino = this.getClass().getSimpleName();
    }

    public void iniciarConexionServicio() {
        sConn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                messenger = null;
                Log.i(TAG, "onServiceDisconnected");
                conectado = false;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // We are conntected to the service
                messenger = new Messenger(service);
                Log.i(TAG, "onServiceConnected");
                conectado = true;

            }
        };
        bindService(new Intent(this, ServiceBootSeratic.class), sConn, Context.BIND_AUTO_CREATE);
    }

    public boolean EnviarMensaje(String Texto) {
        Log.i(TAG, "EnviarMensaje");
        if (messenger != null) {
            Message msg = Message.obtain(null, ServiceBootSeratic.MSG_SAY_HELLO);
            msg.replyTo = new Messenger(new ResponseHandler());
            Bundle b = new Bundle();
            b.putString("data", Texto);
            msg.setData(b);

            try {
                messenger.send(msg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean EnviarMensaje(String Texto, String obj) {
        Log.i(TAG, "EnviarMensaje");
        if (messenger != null) {
            Message msg = Message.obtain(null, ServiceBootSeratic.MSG_SAY_HELLO);
            msg.replyTo = new Messenger(new ResponseHandler());
            Bundle b = new Bundle();
            b.putString("data", Texto);
            b.putString("Clase", Ldestino);
            msg.setData(b);

            try {
                messenger.send(msg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    public boolean SendMensajePush(String Texto, Class<?> dest) {
        texto = Texto;
        Ldestino = dest.getName();
        t = new Thread() {
            public void run() {
                try {

                    while (!conectado) {
                        Thread.sleep(2000);
                    }

                    mHandlerHilo.post(ejecutarAccion);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t.start();

        return true;
    }


    public void stopServices() {
        if (sConn != null) {
            Notificador.this.stopService(new Intent(this, ServiceBootSeratic.class));
            Notificador.this.unbindService(sConn);
        }
    }

    class ResponseHandler extends Handler {

        public void handleMessage(Message msg) {
            int respCode = msg.what;

            switch (respCode) {
                case ServiceBootSeratic.MSG_SAY_HELLO: {
                    String result = msg.getData().getString("respData");
                }
            }
        }

    }

}
