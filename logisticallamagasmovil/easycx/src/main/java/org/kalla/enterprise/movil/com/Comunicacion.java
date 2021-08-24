package org.kalla.enterprise.movil.com;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.kalla.enterprise.movil.com.vo.ErrorComunicacion;
import org.kalla.enterprise.movil.com.vo.ManejadorExcepciones;
import org.kalla.enterprise.movil.com.vo.ObjetoRecibir;
import org.kalla.enterprise.movil.com.vo.ObjetoSincronizar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Comunicacion extends Thread {
    public static int NUM_MAX_REINTENTOS = 3;
    private String url;
    private HttpURLConnection connection;
    private ArrayList<ObjetoSincronizar> objEnviar;
    private Class<?> respuesta;
    // private RespuestaSincronizacion respSinc;
    private ManejadorExcepciones exceptionMan;
    private IConexionServer iControlCxServer;
    private int versionSinc;
    private boolean notificarBySincEndGeneral = true;
    /**
     * indica el nmero de reintentos que se han establecido en la comunicacin
     * despus de un error de cx.
     */
    private int reintentoCx = 0;
    private final static String TAG = Comunicacion.class.getSimpleName();
    private int cambioServer = 0; // contador para Cambio entre servidores
    // registrados
    private int numeroServidores; // Numero de Servidores de Respaldo
    private short cod_sinc = 0;
    @SuppressWarnings("unused")
    private boolean waitResp = false;
    private long timeout = 60000;
    // private long tiempoExpiracion;
    private boolean isTimeOut = false;
    private boolean finalizeOk = false;
    private boolean notificado = false;
    private boolean reintentando = false;
    private boolean unicaRespuesta = true;
    private boolean multipleEnvio = true;
    // private String contenType = "application/octet-stream";

    private String contenType = "text/html;charset=UTF-8";
    public static final short COD_SINC_VERSIONESINCOMPATIBLES = -1;
    public static final short COD_SINC_SERVIDOR_CAMBIO = 41;

    private boolean consultaPaginada = false;
    private Context context;
    private static int MAX_VALUE_TIMEOUT = 30000;

    public void setContentType(String contentType) {
        this.contenType = contentType;
    }

    public void setContext(Context ctx) {
        this.context = ctx;
        if (context != null) {
            setA();
        }
    }

    public Comunicacion(IConexionServer interfaz) {
        iControlCxServer = interfaz;
        // exceptionMan = new ManejadorExcepciones();
        this.notificado = false;
        multipleEnvio = true;
    }

    private void setA() {

        SharedPreferences sp = this.context.getSharedPreferences("xxx_mmm", Context.MODE_PRIVATE);
        String cn = sp.getString("ca", "");
        long fg = sp.getLong("f", 0);
        long fa = new Date().getTime();
        if (cn.equals("") | ((fa - fg) > 360000)) {
            Editor e = sp.edit();
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            cn = manager.getNetworkOperatorName();
            final String M = "MD5";
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance(M);
                digest.update(cn.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String h = Integer.toHexString(0xFF & aMessageDigest);
                    while (h.length() < 2)
                        h = "0" + h;
                    hexString.append(h);
                }
                e.putString("ca", hexString.toString());
                e.putLong("f", new Date().getTime());
                e.commit();

            } catch (NoSuchAlgorithmException ex) {
                Log.e(TAG, "setA fallo", ex);
            }
        }

    }

    public void setConsultaPaginada(boolean consultaPaginada) {
        this.consultaPaginada = consultaPaginada;
    }

    public boolean isConsultaPaginada() {
        return consultaPaginada;
    }

    private void inicializar() {
        connection = OpenConnection();
        if (connection == null) {
            try {
                throw new Exception(MensajesComunicacion.MSG_ERROR_OPEN_CONNECTION);
            } catch (Exception ex) {
                if (!url.contains("GestorDescargas")) {

                    if (ex.getMessage() != null && !ex.getMessage().contains("failed") && !ex.getMessage().contains("Protocol")) {
                        incrementaIntento(ex);

                    } else {

                        cambiaServidor(ex);
                    }

                } else {
                    incrementaIntento(ex);
                }
            }
        }

    }

    private HttpURLConnection OpenConnection() {

        URL url_L = null;
        HttpURLConnection connection = null;
        Log.i("Comunicacion", "Enviado Peticion:" + cod_sinc + "URL:" + url);
        try {
            url_L = new URL(url);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return connection;
        }

        try {
            if (url_L != null) {
                CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
                connection = (HttpURLConnection) url_L.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout((int) timeout);
                connection.setRequestProperty("version", String.valueOf(versionSinc));
                connection.setRequestProperty("Content-Type", contenType);
                connection.setRequestProperty("SO", "Android");
                if (context != null) {
                    connection.setRequestProperty("Model", org.kalla.enterprise.movil.util.Utilidades.getImei(context) + org.kalla.enterprise.movil.util.Utilidades.getDeviceName());
                }
                connection.setReadTimeout((int) timeout);


                if (!validIP(connection.getURL().getHost())) {

                    boolean redirect = true;
                    int status = connection.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                                || status == HttpURLConnection.HTTP_MOVED_PERM
                                || status == HttpURLConnection.HTTP_SEE_OTHER)
                            redirect = true;
                    }

                    String newUrl = connection.getURL().getProtocol() + "://" + connection.getURL().getAuthority() + connection.getURL().getFile();
                    // get the cookie if need, for login
                    String cookies = connection.getHeaderField("Set-Cookie");

                    if (redirect) {
                        // open the new connnection again
                        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
                        connection = (HttpURLConnection) new URL(newUrl).openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setConnectTimeout((int) timeout);
                        connection.setRequestProperty("version", String.valueOf(versionSinc));
                        connection.setRequestProperty("Content-Type", contenType);
                        connection.setRequestProperty("SO", "Android");
                        if (context != null) {
                            connection.setRequestProperty("Model", org.kalla.enterprise.movil.util.Utilidades.getImei(context) + org.kalla.enterprise.movil.util.Utilidades.getDeviceName());
                        }
                        connection.setReadTimeout((int) timeout);
                        connection.setRequestProperty("Cookie", cookies);
                        connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                        System.out.println("Redirect to URL : " + newUrl);
                    }
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return connection;
        }
        return connection;
    }

    private void incrementaIntento(Exception ex) {
        connection = null;
        if (reintentoCx >= NUM_MAX_REINTENTOS) {
            reintentoCx = 0;
            Log.e("Comunicacion", "Error Abriendo comunicacion con " + url);
            notificarResErrorCx(ErrorComunicacion.EN_OPEN, cod_sinc, MensajesComunicacion.MSG_ERROR_OPEN_CONNECTION, ex.getMessage(), false);
        } else {
            reintentar();
        }
    }

    private void cambiaServidor() throws Exception {
        numeroServidores = Utilidades.getNumUrl(context);
        if (numeroServidores > cambioServer) {
            connection = null;
            url = Utilidades.getURL(context, cambioServer);
            if (!url.isEmpty()) {
                Log.e("Comunicacion", "Cambio de Servidor:" + url);
                cambioServer++;
                inicializar();
            } else {
                cambioServer++;
                cambiaServidor();
            }

        } else {
            throw new Exception(MensajesComunicacion.MSG_ERROR_OPEN_CONNECTION);
        }
    }

    private void cambiaServidor(Exception ex) {
        numeroServidores = Utilidades.getNumUrl(context);
        if (numeroServidores > cambioServer) {
            connection = null;
            url = Utilidades.getURL(context, cambioServer);
            cambioServer++;
            Log.e("Comunicacion", "Cambio de Servidor: " + url);
            inicializar();

        } else {
            Log.e("Comunicacion", "Error Abriendo comunicacion con" + url);
            notificarResErrorCx(ErrorComunicacion.EN_OPEN, cod_sinc, MensajesComunicacion.MSG_ERROR_OPEN_CONNECTION, ex.getMessage(), false);
        }
    }

    public void sincronizar(int versionSinc, short cod_sinc, ArrayList<?> objetosE, Class<?> res, boolean unicaRespueta) {
        this.versionSinc = versionSinc;
        this.cod_sinc = cod_sinc;
        this.objEnviar = (ArrayList<ObjetoSincronizar>) objetosE;
        this.respuesta = res;
        this.unicaRespuesta = unicaRespueta;

    }

    public void sincronizar(int version, short cod_sincro, ObjetoSincronizar objetoE, Class<?> res, boolean unicaRespueta) {

        ArrayList<ObjetoSincronizar> objEnviarTemp = null;
        if (objetoE == null) {
            objetoE = new ObjetoSincronizar();
        }
        if (objetoE != null) {
            objEnviarTemp = new ArrayList<ObjetoSincronizar>();
            objEnviarTemp.add(objetoE);
        }
        sincronizar(version, cod_sincro, objEnviarTemp, res, unicaRespueta);
        this.multipleEnvio = false;

    }

    private void reintentar() {
        Log.i("Comunicacion", "Reintentando la conexion...");
        waitResp = false; // Con esto, el hilo del timeOut no notificar el
        // error
        // de timeout por que la respuest ya lleg y fu un
        // error
        reintentando = true;
        Comunicacion comunicacion = new Comunicacion(iControlCxServer);
        comunicacion.setContext(context);
        comunicacion.setURL(url);
        comunicacion.setTimeout(timeout);
        comunicacion.sincronizar(versionSinc, cod_sinc, objEnviar, respuesta, unicaRespuesta);
        comunicacion.setReintento(++reintentoCx);
        comunicacion.setMultipleEnvio(multipleEnvio);
        comunicacion.setConsultaPaginada(consultaPaginada);
        comunicacion.start();
    }

    public void setURL(String u) {
        url = u;
    }

    public void setTimeout(long tiempo) {
        this.timeout = tiempo;
    }

    private void fijarDatosEnvio(OutputStream outputMan) throws Exception {
        DataOutputStream dataOut = new DataOutputStream(outputMan);
        dataOut.writeShort(cod_sinc);
        int tam = 0;
        if (objEnviar != null && (tam = objEnviar.size()) > 0) {
            if (!unicaRespuesta) {
                short numPagina = objEnviar.get(0).getPaginaActual();
                dataOut.writeShort(numPagina);
            }
            if (multipleEnvio) {
                dataOut.writeShort(tam);
            }
            for (ObjetoSincronizar objEnviar_i : objEnviar) {
                if (objEnviar_i != null) {
                    objEnviar_i.setValoresOnOuputStream(dataOut);
                    dataOut.flush();
                }
            }
        } else {
            if (!unicaRespuesta) {
                dataOut.writeShort(0);
            }
        }

    }

    /**
     * Arroja excepciones de lectura de datos
     */
    private void fijarDatosRecibidos(InputStream inputMan) throws Exception {

        short codResp = 0;
        DataInputStream dataIn = new DataInputStream(inputMan);
        codResp = dataIn.readShort();
        Log.i("Comunicacion", "Respuesta Codigo" + codResp);
        // respSinc.setCodSinc(codResp);
        // boolean reintentarGeneral = false;

        // if (codResp > 0) {
        notificarBySincEndGeneral = true;
        switch (codResp) {
            case COD_SINC_VERSIONESINCOMPATIBLES:
                ObjetoRecibir respuestaRx = null;
                iControlCxServer.resSincEnd((int) codResp, respuestaRx);
                // throw versIncomp(dataIn);
                break;
            // case COD_SINC_SERVIDOR_CAMBIO:
            // //ObjetoRecibir respuestaRx = null;
            // iControlCxServer.resSincEnd((int) codResp, respuestaRx);
            // break;
            default:
                if (!unicaRespuesta) {
                    short numPaginas = 0;
                    if (consultaPaginada) {
                        numPaginas = dataIn.readShort();
                    }

                    Log.i("Comunicacion", "Num Paginas" + numPaginas);
                    ObjetoSincronizar objEnviar_0 = objEnviar.get(0);
                    //
                    if (numPaginas > 1) {
                        objEnviar_0.setNumPags(numPaginas);
                    } else if (numPaginas == 0) {
                        numPaginas = objEnviar_0.getNumPags();
                    }

                    int tamRespuesta = dataIn.readUnsignedShort();
                    ArrayList<ObjetoRecibir> respuestasRecibidas = new ArrayList<ObjetoRecibir>();

                    short paginActual = objEnviar_0.getPaginaActual();
                    paginActual++;
                    boolean esFinal = (paginActual >= numPaginas);
                    //
                    for (int i = 0; i < tamRespuesta; i++) {
                        ObjetoRecibir respuestaRecibida = (ObjetoRecibir) this.respuesta.newInstance();
                        if (!respuestaRecibida.setValoresFromInputStream(dataIn)) {
                            respuestaRecibida = null;
                        }

                        respuestasRecibidas.add(respuestaRecibida);

                    }
                    //
                    if (notificarBySincEndGeneral) {
                        iControlCxServer.resSincEnd(cod_sinc, respuestasRecibidas, esFinal, numPaginas, paginActual);
                    }

                    if (!esFinal) {
                        objEnviar_0.setPaginaActual(paginActual);
                        Log.i("Comunicacion", "Descargando Pagina" + paginActual);
                        descargarNuevaPagina();
                    }
                } else {
                    ObjetoRecibir respuestaRecibida = (ObjetoRecibir) respuesta.newInstance();
                    if (!respuestaRecibida.setValoresFromInputStream(dataIn)) {
                        respuestaRecibida = null;
                    }
                    if (notificarBySincEndGeneral) {
                        iControlCxServer.resSincEnd(cod_sinc, respuestaRecibida);

                    }
                }
                break;
        }
    }

    //Metodo para cargar el valor de timeout del ping desde configuración.
    public static void setValueTimeout(int maxValue) {
        MAX_VALUE_TIMEOUT = maxValue;
    }


    @Override
    public void run() {
        Log.i("Comunicacion", "Iniciando Hilo de Com");
        int valida2 = 1;
        this.context = context;
        // Validación de conexión
        boolean conStatus = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        //boolean isConnected = true;
        if (isConnected) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setConnectTimeout(MAX_VALUE_TIMEOUT);
                httpURLConnection.setDoInput(false);
                httpURLConnection.setDoOutput(false);
                httpURLConnection.connect();
                conStatus = true;
            } catch (Exception e) {
                Log.e(TAG, "run fallo", e);
            }
        } else {
            notificarResErrorCx(ErrorComunicacion.EN_OPTENER_RESPUESTA, cod_sinc, MensajesComunicacion.MSG_ERROR_CONNECTION, null, false);
        }
        if (conStatus) {
            if (this.context != null) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("Seguridad", Context.MODE_PRIVATE);
                // Abre la conexion
                valida2 = sharedPreferences.getInt("valida2", 1);

            }
            if (valida2 == 1) {
                inicializar();

                if (connection != null) {
                    if (cod_sinc > 0) {
                        // se abre el flujo por donde se enviaran los datos
                        OutputStream outputMan;
                        try {
                            outputMan = (connection.getOutputStream());
                        } catch (Exception ex) {
                            Log.i("Comunicacion", "Error Conectando" + ex.getMessage());
                            notificarResErrorCx(ErrorComunicacion.EN_FIJANDO_DATOS_ENVIO, cod_sinc, MensajesComunicacion.MSG_ERROR_OPEN_OUT_STREAM, ex.getMessage(), false);
                            finalice();
                            return;
                        }
                        // Se fijan los datos a enviar.
                        boolean okDataEnvio = true;
                        try {
                            fijarDatosEnvio(outputMan);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            notificarResErrorCx(ErrorComunicacion.EN_FIJANDO_DATOS_ENVIO, cod_sinc, MensajesComunicacion.MSG_ERROR_DATA_ENVIO, ex.getMessage(), false);
                            okDataEnvio = false;
                        } finally {
                            if (okDataEnvio) {
                                Log.i("Comunicacion", "Enviando Peticion:" + cod_sinc);
                            }
                            waitResp = true;
                            // Se corre el hilo manual de control de TimeOut //No
                            // implementado
                            correrHiloTimeOut();
                            okDataEnvio = cerrarOutputMan(outputMan, okDataEnvio);
                        }

                        if ((!isTimeOut) && okDataEnvio) {
                            int response = 0;
                            if (connection != null) {
                                try {
                                    // Se lee la respuesta del servidor.
                                    response = connection.getResponseCode();
                                    Log.i("Comunicacion", "RESPONSE: " + response + "-----------------");
                                    waitResp = false;
                                } catch (IOException e) {
                                    waitResp = false;
                                    if (!isTimeOut) {
                                        if (reintentoCx == NUM_MAX_REINTENTOS) {
                                            notificarResErrorCx(ErrorComunicacion.EN_OPTENER_RESPUESTA, cod_sinc, MensajesComunicacion.MSG_ERROR_DATA_ENVIO, e.getMessage(), false);

                                        } else {
                                            reintentar();
                                        }
                                        finalice();
                                    }
                                    return;
                                }
                                if (!isTimeOut) {
                                    try {
                                        response = connection.getResponseCode();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        Log.e(TAG, "run fallo", e);
                                    }
                                    switch (response) {
                                        case HttpURLConnection.HTTP_OK:
                                            DataInputStream inputMan = null;
                                            try {
                                                inputMan = new DataInputStream(connection.getInputStream());
                                            } catch (IOException ex) {
                                                Log.e(TAG, "run fallo", ex);
                                                notificarResErrorCx(ErrorComunicacion.EN_FIJANDO_DATOS_ENVIO, cod_sinc, MensajesComunicacion.MSG_ERROR_OPEN_INPUT_STREAM, ex.getMessage(), false);
                                            }

                                            if (inputMan != null) {
                                                try {
                                                    fijarDatosRecibidos(inputMan);
                                                } catch (Exception ex) {
                                                    Log.e(TAG, "run fallo", ex);
                                                    notificarResErrorCx(ErrorComunicacion.EN_FIJANDO_DATOS_RECIBIDOS, cod_sinc, MensajesComunicacion.MSG_ERROR_LEYENDO_DATOS, ex.getMessage(), false);
                                                } finally {
                                                    try {
                                                        inputMan.close();
                                                    } catch (Exception ex) {
                                                        Log.e(TAG, "run fallo", ex);
                                                    }
                                                }
                                            }
                                            break;
                                        case -1:
                                            iControlCxServer.notificarReintento(reintentoCx);
                                            if (reintentoCx == NUM_MAX_REINTENTOS) {
                                                notificarResErrorCx(ErrorComunicacion.TIPO_ERROR_CX, cod_sinc, getMensajeError_RtaDiferente_HTTP_OK(response), "", false);
                                            } else {
                                                reintentar();
                                            }
                                            break;
                                        default:

                                            // No reintenta porque llegó respuesta
                                            notificarResErrorCx(ErrorComunicacion.TIPO_ERROR_CX, cod_sinc, getMensajeError_RtaDiferente_HTTP_OK(response), "", false);
                                            break;
                                    }
                                    finalice();
                                }
                            }
                        }
                    }
                }
            } else {
                if (context != null) {
                    TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getSimSerialNumber();
                    if (mPhoneNumber == null)
                        mPhoneNumber = "123456";
                    notificarResErrorCx(0, (short) -10, "Licencia No Valida", "Pongase en contacto con el Administrador y Suministre este Numero:" + mPhoneNumber, false);
                }
            }
        } else {
            notificarResErrorCx(ErrorComunicacion.EN_OPTENER_RESPUESTA, cod_sinc, MensajesComunicacion.MSG_ERROR_CONNECTION_SERVIDOR, null, false);
        }


    }

    private String getMensajeError_RtaDiferente_HTTP_OK(int response) {
        String msgError = "";
        try {
            if (connection != null) {
                InputStream is = connection.getErrorStream();
                if (is != null) {
                    byte[] datos = new byte[is.available()];
                    is.read(datos);
                    String repuesta = new String(datos);
                    if (repuesta != null && !repuesta.isEmpty()) {
                        int index = repuesta.indexOf("mensaje</b> <u>") + 15;
                        if (index != -1) {
                            String valor = repuesta.substring(index, repuesta.indexOf("</u>", index));
                            if (valor != null && !valor.isEmpty()) {
                                Log.i("respuesta", valor);
                                msgError = valor;
                            }
                        }
                    }
                }
                if (msgError.isEmpty()) {
                    msgError = connection.getResponseMessage();
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "getMensajeError_RtaDiferente_HTTP_OK fallo", ex);
        }

        return msgError;
    }

    private boolean cerrarOutputMan(OutputStream outputMan, boolean okDataEnvio) {
        try {
            outputMan.close();
        } catch (IOException ex) {
            // Ocurre si la ip no existe
            ex.printStackTrace();
            if (okDataEnvio) {
                /*
                 * notificarResErrorCx(ErrorComunicacion.TIPO_ERROR_CX, (int)
                 * cod_sinc, getMensajeError(ex), ex.getMessage(), false);
                 */
            }
            okDataEnvio = false;
        }
        return okDataEnvio;
    }

    private void notificarResErrorCx(int tipoError, short errorEn, String msg, String detalleMsg, boolean reintentando) {
        if (!notificado) {
            notificado = true;
            cerrarConexion();
            notificarBySincEndGeneral = false;
            ErrorComunicacion error = new ErrorComunicacion(tipoError, errorEn, msg, detalleMsg, reintentando);
            iControlCxServer.resError(error);
        }
    }

    private String getMensajeError(Exception e) {
        if (exceptionMan == null) {
            exceptionMan = new ManejadorExcepciones();
        }
        return exceptionMan.manejadorExcepciones(e);
    }

    private IOException versIncomp(DataInputStream inputMan) {
        notificarBySincEndGeneral = false;
        String mens = "";
        try {
            if (this.versionSinc > 9) {

                mens = MensajesComunicacion.VERSIONES_INCOMPATIBLES + ". " + MensajesComunicacion.VERSION_SERVER + ": " + inputMan.readInt() + ". " + MensajesComunicacion.VERSION_MOVIL + ": " + this.versionSinc;

            } else {
                mens = MensajesComunicacion.VERSIONES_INCOMPATIBLES + ". " + MensajesComunicacion.VERSION_SERVER + ": " + inputMan.readUTF() + ". " + MensajesComunicacion.VERSION_MOVIL + ": " + this.versionSinc;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new IOException(mens);

    }

    private void setReintento(int reintento) {
        this.reintentoCx = reintento;
    }

    private void cerrarConexion() {
        if (connection != null) {
            try {
                connection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            connection = null;
        }
    }

    private void finalice() {
        if (!finalizeOk) {
            finalizeOk = true;
            cerrarConexion();
            url = null;

            if (!reintentando && objEnviar != null) {
                if (objEnviar.size() > 0) {
                    for (ObjetoSincronizar objEnviar_i : objEnviar) {
                        if (objEnviar_i != null)
                            objEnviar_i.finalize();
                    }

                }

            }

            exceptionMan = null;
            // iControlCxServer = null;
            System.gc();
        }
    }

    public boolean isUnicaRespuesta() {
        return unicaRespuesta;
    }

    public void setUnicaRespuesta(boolean unicaRespuesta) {
        this.unicaRespuesta = unicaRespuesta;
    }

    public boolean isMultipleEnvio() {
        return multipleEnvio;
    }

    public void setMultipleEnvio(boolean multipleEnvio) {
        this.multipleEnvio = multipleEnvio;
    }

    private void descargarNuevaPagina() {

        Comunicacion comunicacion = new Comunicacion(iControlCxServer);
        comunicacion.setContext(context);
        comunicacion.setURL(url);
        comunicacion.setTimeout(this.timeout);
        comunicacion.sincronizar(versionSinc, this.cod_sinc, objEnviar, this.respuesta, this.unicaRespuesta);
        comunicacion.setConsultaPaginada(true);
        comunicacion.setMultipleEnvio(this.isMultipleEnvio());
        comunicacion.start();
    }

    private void correrHiloTimeOut() {
        if (timeout > 0) {
            new Thread() {

                public void run() {
                    try {
                        Thread.sleep(timeout);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (waitResp) {
                        isTimeOut = true;
                        notificarResErrorCx(ErrorComunicacion.EN_TIMEOUT, cod_sinc, MensajesComunicacion.MSG_TIMEOUT, "", false);
                        finalice();
                    }
                }
            }.start();
        }
    }

    public static boolean validIP(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        ip = ip.trim();
        if ((ip.length() < 6) & (ip.length() > 15)) return false;

        try {
            Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }


}
