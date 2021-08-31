package org.kalla.enterprise.movil.gps.com;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.kalla.enterprise.movil.com.Comunicacion;
import org.kalla.enterprise.movil.com.IConexionServer;
import org.kalla.enterprise.movil.com.vo.ErrorComunicacion;
import org.kalla.enterprise.movil.com.vo.ObjetoRecibir;
import org.kalla.enterprise.movil.com.vo.ObjetoSincronizar;
import org.kalla.enterprise.movil.data.BoxStoreDB;
import org.kalla.enterprise.movil.gps.com.vo.EnvioUbicacionesVO;
import org.kalla.enterprise.movil.gps.com.vo.SeguimientoVO;
import org.kalla.enterprise.movil.gps.com.vo.TrackVO;
import org.kalla.enterprise.movil.gps.interfaces.SeguimientoManagerInterface;
import org.kalla.enterprise.movil.gps.vo.ResponseVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class EnvioSeguimientoManager implements IConexionServer {

    private static final String NOTIFICATION = "org.seratic.enterprise.movil.boot";
    private static final int STATUS_OK = 200;
    private static final String TAG = EnvioSeguimientoManager.class.getSimpleName();
    private int version;
    private Context contexto;
    private short codSinc;
    private String url;
    private EnvioUbicacionesVO enviosEnviados;
    private List<TrackVO> traksEnviados;
    private Gson g = new Gson();
    private SharedPreferences prefSEguimiento;

    public EnvioSeguimientoManager(Context ctx) {
        prefSEguimiento = ctx.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_PRIVATE);
        codSinc = (short) prefSEguimiento.getInt("codigo", 91);
        version = prefSEguimiento.getInt("version", 3);
        contexto = ctx;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCodSinc(short codSinc) {
        this.codSinc = codSinc;
    }

    @Override
    public void resSincEnd(int codSinc, ObjetoRecibir rta) throws Exception {
        // TODO Auto-generated method stub
        EnvioUbicacionesVO envio = (EnvioUbicacionesVO) rta;
        procesarRespuestaExitosa();
        if (envio.isHayNotificacion()) {
            String notificacion = envio.getMensaje();
            Intent intent = new Intent(NOTIFICATION);
            intent.putExtra("Mensaje", notificacion);
            contexto.sendBroadcast(intent);
        }

        notificarUI("OK " + (envio.isHayNotificacion() ? "N" : "-"));
    }

    @Override
    public void resSincEnd(int codSinc, ArrayList<?> rta, boolean esFinal, int numPaginas, int pagActual) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void resError(ErrorComunicacion error) {
        // TODO Auto-generated method stub
        Log.i(TAG, "resError: " + error.getDetalleMsg());
        prefSEguimiento.edit().putLong("ocupado", 0).commit();
        long ocupado = prefSEguimiento.getLong("ocupado", 0);
        Log.i(TAG, "resError, ocupado: " + ocupado);
        notificarUI("Error CX");
    }

    protected void notificarUI(String msg) {
        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        intent.setAction("org.seratic.enterprise.movil.gps.SEGUIMIENTO");
        //	contexto.sendBroadcast(intent);

        Log.i(TAG, "notificarUI: " + msg);

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

    public void enviarPeticion(short cod_sinc, ObjetoSincronizar objeto, Class<?> resultado, boolean unicaRespueta) {
        //url = "http://192.168.0.50:8084/GestorVisitas/Comunicacion";

        Comunicacion com = new Comunicacion(this);
        com.setURL(url);
        com.setTimeout(120000);
        com.setContext(contexto);
        com.sincronizar(version, cod_sinc, objeto, resultado, unicaRespueta);
        com.start();
    }


    public void enviarSeguimiento(EnvioUbicacionesVO envios) {
        Log.i(TAG, "enviarSeguimiento");
        long ocupado = prefSEguimiento.getLong("ocupado", 0);
        long actual = System.currentTimeMillis();
        long delta = (1000 * 60 * 5) + ocupado;
        Log.i(TAG, "enviarSeguimiento, tamano: " + envios.getSegumientosEnviar().size() + " ocupado: " + ocupado);
        if ((ocupado) == 0 || (actual > delta)) {
            Log.i(TAG, "enviarSeguimiento, se procede a enviar");
            prefSEguimiento.edit().putLong("ocupado", actual).commit();
            enviosEnviados = envios;
            enviarPeticion(codSinc, envios, EnvioUbicacionesVO.class, true);

        }
    }

    public void enviarTracks(List<TrackVO> tracks) {
        Log.i(TAG, "enviarTracks");
        boolean integracionLogistic = prefSEguimiento.getBoolean("integracionLogistic", false);
        long ocupado;
        if (integracionLogistic) {
            ocupado = prefSEguimiento.getLong("ocupadoTrack", 0);
        } else {
            ocupado = prefSEguimiento.getLong("ocupado", 0);
        }
        long actual = System.currentTimeMillis();
        long delta = (1000 * 60 * 5) + ocupado;
        Log.i(TAG, "enviarTracks, tamano: " + tracks.size() + " ocupado: " + ocupado);
        if ((ocupado) == 0 || (actual > delta)) {
            Log.i(TAG, "enviarTracks, se procede a enviar");
            if (integracionLogistic) {
                prefSEguimiento.edit().putLong("ocupadoTrack", actual).commit();
            } else {
                prefSEguimiento.edit().putLong("ocupado", actual).commit();
            }
            traksEnviados = tracks;
            boolean monitoreoGPS = prefSEguimiento.getBoolean("monitoreoGPS", false);
            if (monitoreoGPS) {
                enviarSeguimiento(tracks);
            }
        }
    }

    @Override
    public Context getContext() {
        // TODO Auto-generated method stub
        return contexto;
    }

    public void enviarSeguimientosPendientes(Context ctx, List<SeguimientoVO> listaUbicacionePendientes) {
        EnvioUbicacionesVO envioVO = new EnvioUbicacionesVO();
        long actual = System.currentTimeMillis();
        int tamanoBloqueSeguimiento = prefSEguimiento.getInt("tamano_bloque_seguimiento", 100);
        url = prefSEguimiento.getString("url", "");

        List<SeguimientoVO> listaUbicacionePaginada = new ArrayList<SeguimientoVO>();

        tamanoBloqueSeguimiento = listaUbicacionePendientes.size() > tamanoBloqueSeguimiento ? tamanoBloqueSeguimiento : listaUbicacionePendientes.size();

        for (int i = 0; i < tamanoBloqueSeguimiento; i++) {
            if (listaUbicacionePendientes.get(i).getFechaEnvio().getTime() + (60000) < actual) {
                listaUbicacionePendientes.get(i).setEnLinea(false);
            }
            listaUbicacionePaginada.add(listaUbicacionePendientes.get(i));
        }

        if (listaUbicacionePaginada != null && !listaUbicacionePaginada.isEmpty()) {
            setCodSinc((short) codSinc);
            envioVO.setSegumientosEnviar(listaUbicacionePaginada);
            enviarSeguimiento(envioVO);
        }
    }

    public void enviarTracksPendientes(Context ctx, List<TrackVO> tracks) {

        int tamanoBloqueSeguimiento = prefSEguimiento.getInt("tamano_bloque_seguimiento", 100);
        url = prefSEguimiento.getString("url", "");

        List<TrackVO> listaUbicacionePaginada = new ArrayList<TrackVO>();

        tamanoBloqueSeguimiento = tracks.size() > tamanoBloqueSeguimiento ? tamanoBloqueSeguimiento : tracks.size();

        for (int i = 0; i < tamanoBloqueSeguimiento; i++) {
            listaUbicacionePaginada.add(tracks.get(i));
        }

        if (listaUbicacionePaginada != null && !listaUbicacionePaginada.isEmpty()) {
            setCodSinc((short) codSinc);
            enviarTracks(listaUbicacionePaginada);
        }
    }

    private void enviarSeguimiento(List<TrackVO> trackVOS) {

        String urlTrack = prefSEguimiento.getString("urlTrack", "");
        if (!urlTrack.isEmpty()) {
            final String baseUrl = urlTrack;

            String trackS = g.toJson(trackVOS);
            Log.i(TAG, "enviarSeguimiento, Envio seg JSON: " + trackS);

            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(baseUrl);
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = retrofitBuilder.build();
            SeguimientoManagerInterface seguimientoManagerInterface = retrofit.create(SeguimientoManagerInterface.class);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), trackS);

            Call<ResponseVO> repos = seguimientoManagerInterface.enviarSeguimiento(body);

            repos.enqueue(new Callback<ResponseVO>() {

                @Override
                public void onResponse(Call<ResponseVO> call, Response<ResponseVO> response) {
                    if (response.raw().code() == STATUS_OK) {
                        if (response.body().getSuccess().equalsIgnoreCase("true")) {
                            procesarRespuestaExitosaTrack();
                        } else {
                            procesarRespuestaFallidaTrack();
                        }

                    } else {
                        procesarRespuestaFallidaTrack();
                    }
                }

                @Override
                public void onFailure(Call<ResponseVO> call, Throwable t) {
                    procesarRespuestaFallidaTrack();
                }

            });
        } else {
            Log.i(TAG, "enviarSeguimiento, No fue posible enviar porque no se ha establecido la url base");
        }
    }

    public void procesarRespuestaExitosa() {
        Editor e = null;
        SharedPreferences sh = contexto.getSharedPreferences(SeguimientoVO.SHARED_PREFERENCES_SEGUIMIENTO, Context.MODE_PRIVATE);
        List<SeguimientoVO> segumientosEnviados = enviosEnviados.getSegumientosEnviar();
        e = sh.edit();
        for (SeguimientoVO seguimientoVO : segumientosEnviados) {
            e.remove(seguimientoVO.getFechaEnvio().getTime() + "").commit();
        }
        e = prefSEguimiento.edit();
        e.putLong("actualizacion", new Date().getTime());
        e.putLong("ocupado", 0);
        e.commit();
        long ocupado = prefSEguimiento.getLong("ocupado", 0);
        Log.i(TAG, "procesarRespuestaExitosa, ocupado: " + ocupado);
    }

    public void procesarRespuestaExitosaTrack() {
        Log.i(TAG, "procesarRespuestaExitosaTrack");
        BoxStoreDB.getInstance(contexto).getBox().boxFor(TrackVO.class).remove(traksEnviados);
        Editor e = prefSEguimiento.edit();
        boolean integracionLogistic = prefSEguimiento.getBoolean("integracionLogistic", false);
        if (integracionLogistic) {
            e.putLong("ocupadoTrack", 0);
            e.commit();
        } else {
            e.putLong("ocupado", 0);
            e.putLong("actualizacion", new Date().getTime());
            e.commit();
        }
        Log.i(TAG, "procesarRespuestaExitosaTrack");
    }

    public void procesarRespuestaFallidaTrack() {
        Log.i(TAG, "procesarRespuestaFallidaTrack");
        boolean integracionLogistic = prefSEguimiento.getBoolean("integracionLogistic", false);
        if (integracionLogistic) {
            prefSEguimiento.edit().putLong("ocupadoTrack", 0);
            prefSEguimiento.edit().commit();
        } else {
            prefSEguimiento.edit().putLong("ocupado", 0).commit();
            long ocupado = prefSEguimiento.getLong("ocupado", 0);
            Log.i(TAG, "procesarRespuestaFallidaTrack, ocupado: " + ocupado);
            notificarUI("Error CX");
        }
    }

    public interface EnvioSeguimientoManagerListener {
        public void finEnvioSeguimientos();
    }

    private class EnviarSeguimiento extends AsyncTask<List<TrackVO>, Object, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(List<TrackVO>... tracks) {

            Boolean data = Boolean.FALSE;
            if (tracks != null) {
                String trackS = g.toJson(tracks);
                Log.i(TAG, "doInBackground, Envio seg JSON: " + trackS);
                String response = "";
                try {
                    response = HttpConnector.sendJsonPOST(url, trackS);
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(response.toString()).getAsJsonObject();
                    if (obj.has("success")) {
                        data = Boolean.parseBoolean(g.toJson(obj.get("success")));
                    }
                } catch (Exception e) {
                    prefSEguimiento.edit().putLong("ocupado", 0).commit();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                procesarRespuestaExitosa();
            } else {
                prefSEguimiento.edit().putLong("ocupado", 0).commit();
            }
            super.onPostExecute(result);
        }

    }
}
