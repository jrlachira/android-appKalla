package org.kalla.enterprise.movil.boot;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class ServiceBootSeratic extends Service {


    // Variables de la notificacion

    public static final int ID_NOTIFICACION = 1;
    //Mensaje BroadCast
    public static final String NOTIFICATION = "org.kalla.enterprise.movil.boot";
    //Variables de Comunicacion
    static final int MSG_SAY_HELLO = 1;
    static final int MSG_NOTIFICATION = 2;
    static final int MSG_WHAT_PORT = 3;
    static final int RUN_SERVER = 4;
    static final int STOP_SERVER = 5;
    static final String RESPUESTA = "org.kalla.enterprise.movil.boot.respuesta";
    private final static String TAG = ServiceBootSeratic.class.getSimpleName();
    static NotificationManager notificationManager;
    private final WebSocketConnection mConnection = new WebSocketConnection();
    int icono_r = android.R.drawable.alert_light_frame;
    int IDNotify;
    String url = "";
    //Hilo Conectividad
    Thread t;
    Handler mHandlerHilo = new Handler();
    private String Imei = "";
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private Messenger msg = new Messenger(new IncomingHandler());
    final Runnable conectarNotificador = new Runnable() {
        public void run() {
            startWS();
        }
    };
    private BroadcastReceiver receiverRespuesta = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.i(TAG, "receiverRespuesta");
            if (bundle != null) {
                String stringMensaje = bundle.getString("Mensaje");
                if (mConnection != null && mConnection.isConnected()) {
                    mConnection.sendTextMessage("*" + stringMensaje);
                }
            }
        }
    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.i(TAG, "receiver");
            if (bundle != null) {
                final String id = bundle.getString("id", "");

                if (!id.isEmpty()) {
                    Log.i(TAG, "receiver id:" + id);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                NotificarRecibioNotificacion(Integer.valueOf(id));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                }
                String stop = bundle.getString("stopThread", "");
                if (!stop.isEmpty()) {
                    if (t != null && t.isAlive()) {
                        t.interrupt();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        t = null;

                    }
                }

                String init = bundle.getString("init", "");
                if (!init.isEmpty()) {

                    boolean tip = bundle.getBoolean("tipo");
                    GarantizarConectividad(tip);

                }


            }
        }
    };

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(TAG.toLowerCase(), TAG, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            startForeground(2, new NotificationCompat.Builder(this, TAG).setPriority(NotificationCompat.PRIORITY_HIGH).build());
        }
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            Log.e(TAG, "onCreate fallo", e);
        }
        registerReceiver(receiver, new IntentFilter(NOTIFICATION));
        try {
            unregisterReceiver(receiverRespuesta);
        } catch (Exception e) {
            Log.e(TAG, "onCreate fallo", e);
        }
        registerReceiver(receiverRespuesta, new IntentFilter(RESPUESTA));
        IDNotify = 1;
        Log.i(TAG, "Servicio Creado");
        Imei = org.kalla.enterprise.movil.noty.util.Utilidades.getImei(getApplicationContext());
        Log.i(TAG, "Servidor RUN");

        url = org.kalla.enterprise.movil.noty.util.Utilidades.getURLNotificacion(getApplicationContext());

        Log.i(TAG, "url: " + url);


        if (url.compareTo("-1") != 0) {
            if (url.contains("http")) {
                iniciarServicioEscucha(true);
            } else {
                iniciarServicioEscucha(false);
            }
        }


    }

    public void iniciarServicioEscucha(boolean i) {
        GarantizarConectividad(i); //Hilo de Conectividad
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        t = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return msg.getBinder();
    }

    public void GarantizarConectividad(final boolean i) {
        t = new Thread() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(15000);
                        if (i) {
                            solicitarMensajeASP();
                            Log.i(TAG, "GarantizarConectividad");
                        } else {
                            if (mConnection == null || !mConnection.isConnected()) {
                                mHandlerHilo.post(conectarNotificador);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        t.start();
    }

    //Este método crea una nueva notificación en la barra
    public Notification crearNotificacion(int icon, String tickerText,
                                          long when, boolean vibrar) {
        //Crea la nueva notificación con un icono, texto y el tiempo en la cual fue generada
        Notification notification = new Notification(icon, tickerText, when);
        //Fija el tipo audio para la notificación
        notification.audioStreamType = AudioManager.STREAM_NOTIFICATION;
        //Fija el sonido que se reproducira al generar la notificación
        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Indica que la notificación se debe remover cuando el usuario da click en ella


        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //evalúa si va a ser una alerta vibratoria
        if (vibrar) {
            long[] vibrate = new long[]{0, 2000, 0 /* , 1000, 1000 */};
            //Fija el tiempo de vibración
            notification.vibrate = vibrate;
        }
        return notification;
    }

    //Fija el layout de la notificación en la barra de notificaciones expandida
    @SuppressWarnings("deprecation")
    public Notification setContents(Notification notification, Context context,
                                    String contentTitle, String contentText, Intent intent) {

        PendingIntent launchIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        //Fija el layout para la notificación

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setContentIntent(launchIntent);
        builder.build();

        notification = builder.getNotification();

        return notification;
    }

    //Muestra la notificación en la barra de notificaciones
    public void notificar(Notification notification, int idNotificacion) {
        notificationManager.notify(idNotificacion, notification);
    }

    //Cancela la notificación de la barra de notificaciones
    public void cancelNotificacion(int idNotificacion) {
        notificationManager.cancel(idNotificacion);
    }

    private void publishNotificacion(String CodNotificacion, String APP) {
        CodNotificacion = CodNotificacion.substring(1, CodNotificacion.length());
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("Mensaje", CodNotificacion);
        intent.putExtra("App", APP);
        sendBroadcast(intent);//

        Log.i(TAG, "publishNotificacion");
    }

    private void publishNotificacion(String CodNotificacion) {
        CodNotificacion = CodNotificacion.substring(1, CodNotificacion.length());
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("Mensaje", CodNotificacion);
        sendBroadcast(intent);
        Log.i(TAG, "publishNotificacion");
    }


    @SuppressLint("LongLogTag")
    private void solicitarMensajeASP() {
        boolean parameter = false;
        String parNombre = org.kalla.enterprise.movil.noty.util.Utilidades.getNombreParametersUrl(getApplicationContext(), 0);
        String parValor = org.kalla.enterprise.movil.noty.util.Utilidades.getValorParametersUrl(getApplicationContext(), 0);

        SharedPreferences SaveData = getSharedPreferences("PreferenciasDataNotificacion", Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        String valor = SaveData.getString("PValor", "");
        Log.i(TAG, "solicitarMensajeASP, parValor sin metodo: " + valor);


        if (!parNombre.isEmpty() && !parValor.isEmpty()) {
            parameter = true;
        } else {
            parameter = false;
        }

        Log.i(TAG, "solicitarMensajeASP, parValor con metdo: " + parValor);

        String DISPATCHER = url + "Dispatcher.aspx";
        if (parameter) {
            DISPATCHER = url + "Dispatcher.aspx?" + parNombre + "=" + parValor;
        }
        Log.i(TAG, "solicitarMensajeASP, " + DISPATCHER);
        String Mensaje = "";
        String imei;
        URL url;
        ArrayList<String> Lines = new ArrayList<String>();

        BufferedReader bufferedReader = null;
        InputStreamReader in = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(DISPATCHER);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new InputStreamReader(urlConnection.getInputStream());
            bufferedReader = new BufferedReader(in);
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
                buffer.append('\r');
                Log.i(TAG, "solicitarMensajeASP, Line" + line);
                Lines.add(line);

            }
            bufferedReader.close();
            in.close();

            for (int i = 0; i < Lines.size(); i++) {
                if (Lines.get(i).startsWith("{")) {
                    JSONObject json_data = new JSONObject(Lines.get(i));
                    Mensaje = json_data.getString("msg");
                    imei = json_data.getString("imei");
                    String id = json_data.getString("id");
                    String App = json_data.getString("app");
                    //Mensaje = Mensaje + " id = " + id;

                    /***ENvio Mensaje***/
                    if ((Imei.compareTo(imei) == 0 || Imei.compareTo("*") == 0) && Mensaje != null && !Mensaje.isEmpty()) {
                        NotificarRecibioNotificacion(Integer.valueOf(id));
                        i = 1000;
                        Log.i(TAG, "solicitarMensajeASP, mensaje: " + Mensaje + ", id:" + id);
                        if (Mensaje.contains("#")) {
                            publishNotificacion(Mensaje, App);
                        } else {

                            Message resp = Message.obtain(null, MSG_NOTIFICATION);
                            Bundle bResp = new Bundle();
                            bResp.putString("notificacion", Mensaje);
                            resp.setData(bResp);

                            try {
                                msg.send(resp);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "solicitarMensajeASP, " + Mensaje);

    }


    public void NotificarRecibioNotificacion(int id) {
        Log.i(TAG, "Notifica Recibio Notificacion " + id);
        String NAMESPACE = "http://tempuri.org/";
        String URL = url + "WsNotificaciones.asmx";
        String METHOD_NAME = "NotificarLeido";
        String SOAP_ACTION = "http://tempuri.org/NotificarLeido";


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("id", id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
            String resultado = resultado_xml.toString();
        } catch (Exception e) {
            Log.i(TAG, "NotificarRecibioNotificacion fallo", e);
        }

    }


    private void startWS() {

        //final String wsuri = "ws://192.168.0.23:36969/5/d04004162";
        final String wsuri = org.kalla.enterprise.movil.noty.util.Utilidades.getURLNotificacion(getApplicationContext());
        Log.i(TAG, "startWS: " + wsuri);
        if (wsuri.compareTo("-1") != 0) {
            Log.i(TAG, "startWS.connect: " + wsuri);

            try {
                mConnection.connect(wsuri, new WebSocketHandler() {

                    @Override
                    public void onOpen() {
                        Log.i(TAG, "onOpen");
                        //String Imei = org.seratic.enterprise.movil.util.Utilidades.getImei(getApplicationContext());
                        mConnection.sendTextMessage(Imei);
                        Log.i(TAG, "onOpen, imei: " + Imei);
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.i(TAG, "onTextMessage: " + payload);
                        if (payload != null && !payload.contains("OK") && !payload.contains("#")) {
                            Message resp = Message.obtain(null, MSG_NOTIFICATION);
                            Bundle bResp = new Bundle();
                            bResp.putString("notificacion", payload);
                            resp.setData(bResp);

                            try {
                                msg.send(resp);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        if (payload != null && payload.contains("#")) {
                            publishNotificacion(payload);
                        }
                        Log.i(TAG, "Got echo: " + payload);
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.i(TAG, "Connection lost.");
                    }
                });
            } catch (WebSocketException e) {
                Log.e(TAG, "startWS fallo", e);
            }
        } else {
            Log.i(TAG, "startWS , url de notificacion no configurada");
        }

    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "IncomingHandler.handleMessage: " + msg);
            switch (msg.what) {
                case MSG_SAY_HELLO:

                    try {

                        icono_r = getApplicationContext().getResources().getIdentifier("ic_launcher", "drawable", getApplicationContext().getPackageName());

                        int stringId = getApplicationContext().getApplicationInfo().labelRes;
                        String nameApp = getApplicationContext().getString(stringId);

                        Bundle data = msg.getData();

                        String ContenidoMensaje = data.getString("data");
                        String Clase = data.getString("Clase");

                        Message resp = Message.obtain(null, MSG_SAY_HELLO);
                        Toast.makeText(getApplicationContext(), "data desde activity: " + data, Toast.LENGTH_LONG).show();

                        Notification hello = crearNotificacion(icono_r, ContenidoMensaje, (new Date()).getTime(), true);

                        Intent inten = new Intent();

                        if (Clase != null && Clase.length() > 0) {
                            inten = new Intent(ServiceBootSeratic.this, Class.forName(Clase));
                        }

                        hello = setContents(hello, getApplicationContext(), nameApp, ContenidoMensaje, inten);

                        notificar(hello, IDNotify);
                        IDNotify++;

                        Bundle bResp = new Bundle();
                        bResp.putString("respData", ContenidoMensaje);
                        resp.setData(bResp);
                        msg.replyTo.send(resp);

                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;


                case MSG_NOTIFICATION:

                    Bundle data = msg.getData();
                    String Contenido = data.getString("notificacion");

                    Message resp = Message.obtain(null, MSG_SAY_HELLO);
                    //Toast.makeText(getApplicationContext(), "Notificacion Socket: " + data, Toast.LENGTH_LONG).show();

                    icono_r = getApplicationContext().getResources().getIdentifier("ic_launcher", "drawable", getApplicationContext().getPackageName());

                    int stringId = getApplicationContext().getApplicationInfo().labelRes;
                    String nameApp = getApplicationContext().getString(stringId);

                    Notification hello = crearNotificacion(icono_r, Contenido, (new Date()).getTime(), true);

                    Intent inten = new Intent();

                    hello = setContents(hello, getApplicationContext(), nameApp, Contenido, inten);

                    notificar(hello, IDNotify);
                    IDNotify++;

                    break;


                case STOP_SERVER:
                    //mConnection.disconnect();
                    break;

                case RUN_SERVER:
                    //start();
                    break;


                default:
                    super.handleMessage(msg);
            }
        }
    }


}
