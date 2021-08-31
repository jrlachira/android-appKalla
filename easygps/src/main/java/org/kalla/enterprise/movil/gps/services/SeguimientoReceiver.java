package org.kalla.enterprise.movil.gps.services;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;

import org.kalla.enterprise.movil.cron.Predictor;
import org.kalla.enterprise.movil.cron.SchedulingPattern;
import org.kalla.enterprise.movil.gps.EasyGPS;
import org.kalla.enterprise.movil.gps.IUbicacionListener;
import org.kalla.enterprise.movil.gps.LocationManager;
import org.kalla.enterprise.movil.gps.LocationUtils;
import org.kalla.enterprise.movil.gps.com.EnvioSeguimientoManager;
import org.kalla.enterprise.movil.gps.com.vo.SeguimientoVO;
import org.kalla.enterprise.movil.gps.com.vo.TrackVO;
import org.kalla.enterprise.movil.gps.util.ConstantesTipoTrack;
import org.kalla.enterprise.movil.gps.util.Utilidades;
import org.kalla.enterprise.movil.gps.vo.UbicacionVO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
//import android.location.Criteria;
//import android.provider.Settings.System;

public class SeguimientoReceiver extends BroadcastReceiver implements IUbicacionListener, Runnable {
    public static final String UBICACION = "org.seratic.enterprise.movil.gps.Ubicacion";
    private static final int CIEN_METROS = 100;
    private static final int DIES_METROS = 10;
    private static final String TAG = SeguimientoReceiver.class.getSimpleName();
    public final String PREFERENCES_SERVIO_SEGUIMIENTO = "org.seratic.enterprise.movil.gps.SEGUIMIENTO";
    Context ctx;
    SharedPreferences mPrefs;
    private long esperar_buena_ubicacion;
    private EasyGPS easyGPS;
    private int idUsuario;
    private String url;
    private int codigoSync;
    private SharedPreferences prefSEguimiento;
    private SharedPreferences prefSEguimientoTriger;
    private long fechaActual = System.currentTimeMillis();

    /**
     * Este metodo obtiene los seguimientos pendientes almacenados en preferences
     *
     * @param ctx - Contexto
     * @return List<SeguimientoVO>.     *
     * @author Eliana Andrea Concha Agredo
     */
    public static List<SeguimientoVO> obtenerUbicacionesPendientes(Context ctx) {
        int validarCodigo = ctx.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_MULTI_PROCESS).getInt("validarCodigo", 1);
        List<SeguimientoVO> listaUbicacionePendientes = new ArrayList<SeguimientoVO>();
        SharedPreferences sh = ctx.getSharedPreferences(SeguimientoVO.SHARED_PREFERENCES_SEGUIMIENTO, Context.MODE_PRIVATE);
        long actual = System.currentTimeMillis();
        Collection<String> seguimietnos = (Collection<String>) sh.getAll().values();
        Log.i(TAG, "obtenerUbicacionesPendientes, tamano almacenado en preferences: " + seguimietnos.size());
        for (String sVO : seguimietnos) {
            SeguimientoVO sg = SeguimientoVO.creaInstancia(sVO);
            if (sg != null) {
                SharedPreferences prefSEguimiento = ctx.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_MULTI_PROCESS);
                sg.setVersionGps(prefSEguimiento.getInt("versionGps", -1));
                listaUbicacionePendientes.add(sg);
            }
        }

        Collections.sort(listaUbicacionePendientes, SeguimientoVO.COMPARE_BY_DATE);
        if (validarCodigo != 1) {
            for (int i = 0; i < listaUbicacionePendientes.size(); i++) {
                if (listaUbicacionePendientes.get(i).getFechaEnvio().getTime() + (60000) < actual) {
                    listaUbicacionePendientes.get(i).setEnLinea(false);
                }
            }
        }
        return listaUbicacionePendientes;

    }

    public static void setAlarm(Context ctx, long frecuencia) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        Intent intent = new Intent(ctx, SeguimientoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 11998547, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        SharedPreferences prefSEguimientoTriger = ctx.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO_TRIGER, Context.MODE_MULTI_PROCESS);
        long proximo_lanzaminto = prefSEguimientoTriger.getLong("fecha_enviar", 0);

        long tiempo_actual = new Date().getTime();
        if (proximo_lanzaminto < tiempo_actual) {
            prefSEguimientoTriger.edit().putLong("fecha_enviar", (proximo_lanzaminto = tiempo_actual + frecuencia)).commit();
        } else {
            prefSEguimientoTriger.edit().putLong("fecha_enviar", proximo_lanzaminto).commit();
        }
        Log.i(TAG, "on SERATIC fijando Alarma para: " + new Date(proximo_lanzaminto));
        alarmManager.set(AlarmManager.RTC_WAKEUP, proximo_lanzaminto, pendingIntent);

    }

    public static boolean isGPSACtivo(Context context) {
        return ((android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    public static int getTiempoRestante(Context context, SharedPreferences prefSEguimiento) {

        int res = -1;
        //
        long frecuencia = prefSEguimiento.getLong("frecuencia_seguimiento", ServicioSeguimiento.REPEAT_TIME);
        String frecuencia_cron = prefSEguimiento.getString("frecuencia_seguimiento_crontab", "");
        //

        if (frecuencia < ServicioSeguimiento.REPEAT_TIME) {
            frecuencia = ServicioSeguimiento.REPEAT_TIME;
        }
        Date factual = new Date();
        long tiempoActaul = factual.getTime();
        int codigoSync = prefSEguimiento.getInt("codigo", 0);
        if (codigoSync > 0) setAlarm(context, 10000);
        long ultimo_evio = prefSEguimiento.getLong("ultimoRegistro", 0);
        //
        long crontabOld = 0;
        long comparar = 0;
        long crontabNext = 0;
        long cronNextNext = 0;

        Date fseg = null;
        if (!frecuencia_cron.equals("")) {
            //
            crontabOld = prefSEguimiento.getLong("crontab", 0);
            crontabNext = prefSEguimiento.getLong("crontabNext", 0);
            //
            SchedulingPattern sh = new SchedulingPattern(frecuencia_cron);
            Predictor p = new Predictor(sh, tiempoActaul);
            fseg = p.nextMatchingDate();
            //
            cronNextNext = fseg.getTime();
            if (crontabOld == 0 || crontabNext != cronNextNext) {
                prefSEguimiento.edit().putLong("crontab", crontabNext).commit();
            }
            if (crontabNext == 0 || crontabNext != cronNextNext) {
                prefSEguimiento.edit().putLong("crontabNext", cronNextNext).commit();
            }
            comparar = crontabNext;
        } else {
            comparar = ultimo_evio + frecuencia;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.SSS a");
        if (((ultimo_evio < crontabOld) || (tiempoActaul > comparar)) && codigoSync > 0) {
            prefSEguimiento.edit().putLong("crontab", tiempoActaul).commit();
            Log.i(TAG, "on SERATIC SeguimientoReceiver EJECUTAR");
            res = 0;
        } else {
            if (codigoSync != 0) {
                res = (int) ((comparar - tiempoActaul) / 1000);
            } else {
                res = -1;
            }
        }

        Log.i(TAG, "on SERATIC SeguimientoReceiver SEG" +
                " |" + frecuencia +
                " |" + frecuencia_cron +
                " |" + dateFormat.format(tiempoActaul) +
                " |" + dateFormat.format(crontabOld) +
                " |" + dateFormat.format(crontabNext) +
                " |" + dateFormat.format(cronNextNext) +
                " |" + dateFormat.format(ultimo_evio) +
                " |" + (ultimo_evio < crontabOld) +
                " |" + dateFormat.format(comparar) +
                " |" + (tiempoActaul > comparar) +
                " |" + codigoSync +
                " |" + res);
        return res;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        prefSEguimiento = context.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_MULTI_PROCESS);
        mPrefs = ctx.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        prefSEguimientoTriger = ctx.getSharedPreferences(org.kalla.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO_TRIGER, Context.MODE_MULTI_PROCESS);
        int tiempoRestante = 0;
        if (intent != null) {
            tiempoRestante = intent.getIntExtra("tiempoRestante", -1);
        }
        if (tiempoRestante == -1) {
            tiempoRestante = getTiempoRestante(context, prefSEguimiento);
        }
        if (tiempoRestante == 0) {
            Thread t = new Thread(this);
            t.start();
            easyGPS = new EasyGPS(context, (LocationUtils.MILLISECONDS_PER_SECOND) * 5, LocationRequest.PRIORITY_HIGH_ACCURACY);
            easyGPS.setUbicacionListener(this);
            easyGPS.iniciarSeguimientoPeriodico();
        } else {
            String ok = " ";
            ok = "(" + (tiempoRestante) + " seg)";
            notificarUI(ok);
        }
    }

    @Override
    public void nuevaUbicacionListener(UbicacionVO uVO) {
        if (uVO != null) {
            Log.i(TAG, "nuevaUbicacionListener, uVO-->" + uVO);
            long fechaUbicacion = uVO.getFecha();
            if (fechaUbicacion == 0 || (fechaUbicacion > fechaActual)) {
                Log.i(TAG, "nuevaUbicacionListener, precision de ubicacion -->" + uVO.getmPresision());
                int precision = prefSEguimiento.getInt("precision", 5 * DIES_METROS);
                if (precision < 5 * DIES_METROS) {
                    precision = 5 * DIES_METROS;
                }
                if (uVO.getmPresision() < precision) {
                    Log.i(TAG, "nuevaUbicacionListener, Good");
                    synchronized (this) {
                        //Se actualiza a la fecha actual con el fin de que al compararse con la fecha de proxima ubicacion pueda ser tenida en cuenta
                        //@author Eliana Andrea Concha Agredo
                        fechaActual = java.lang.System.currentTimeMillis();
                        notify();
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                notificarUI("Esperando Ubicacion..");
                long frecuencia = prefSEguimiento.getLong("frecuencia_seguimiento", ServicioSeguimiento.REPEAT_TIME);
                if (frecuencia < ServicioSeguimiento.REPEAT_TIME) {
                    frecuencia = ServicioSeguimiento.REPEAT_TIME;
                }
                long tiempoEspera = prefSEguimiento.getLong("tiempo_espera", frecuencia / 2);
                if (tiempoEspera > frecuencia / 2) {
                    tiempoEspera = frecuencia / 2;
                }
                wait(tiempoEspera);
                if (easyGPS != null) {
                    easyGPS.detenerSeguimientoPeriodico();
                    lanzarSeguimiento(easyGPS.getUltimaUbicacionConTrack(-1, ConstantesTipoTrack.TIPO_TRACK_UBICACION, ctx));
                } else {
                    Log.i(TAG, "run, " + easyGPS == null ? "Null" : "NO null");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void notificarUI(String msg) {
        Intent intent = new Intent();
        intent.putExtra("msg", msg);
        intent.setAction(PREFERENCES_SERVIO_SEGUIMIENTO);
        ctx.sendBroadcast(intent);

        Log.i(TAG, "notificarUI, SERATIC ENVIO" + msg);

    }

    private void lanzarSeguimiento(UbicacionVO uVO) {

        long ultimo_evio = prefSEguimiento.getLong("ultimoRegistro", 0);
        long comparar = 0;
        Long factual = java.lang.System.currentTimeMillis();
        //Se formatean las fechas para no tener en cuenta los milisegundos en las comparaciones
        //@author Eliana Andrea Concha Agredo
        long fechaActualW = LocationUtils.getDateWithoutMilliseconds(new Date(fechaActual));
        long factualW = LocationUtils.getDateWithoutMilliseconds(new Date(factual));
        //Se calcula cual seria la fecha de proxim envio el seguimiento segun la frecuencia configurada
        //@author Eliana Andrea Concha Agredo
        long frecuencia = prefSEguimiento.getLong("frecuencia_seguimiento", ServicioSeguimiento.REPEAT_TIME);
        if (ultimo_evio == 0) {
            comparar = LocationUtils.getDateWithoutMilliseconds(new Date(fechaActual));
        } else {
            if (frecuencia < ServicioSeguimiento.REPEAT_TIME) {
                frecuencia = ServicioSeguimiento.REPEAT_TIME;
            }
            comparar = LocationUtils.getDateWithoutMilliseconds(new Date(ultimo_evio)) + frecuencia;
        }
        //Se compara la fecha en que se lanzo el seguimiento, pero si por algun motivo no se obtuvo una ubicacion con buena precision
        //y se espero hasta el minuto para enviar la que se logro obtener, se actualiza dicha fecha a la actual para que pase la validacion contra la fecha de proximo envio
        //@author Eliana Andrea Concha Agredo
        if (fechaActualW < factualW) {
            fechaActualW = factualW;
        }

        //Se compara la fecha actual contra la fecha de proximo envio para evitar el error de envios con diferencias de apenas segundos entre fechas
        //@author Eliana Andrea Concha Agredo
        if (fechaActualW >= comparar) {
            Log.i(TAG, "lanzarSeguimiento, la fecha es mayor a la de proximo envio");
            String telefono = "";

            Log.i(TAG, "lanzarSeguimiento(" + new Date().toLocaleString() + ") UVO-->" + uVO);
            notificarUI("Conectando..");

            if (uVO != null && (uVO.getFecha() + LocationUtils.UPDATE_INTERVAL_IN_ONE_MINUTE) < factual) {
                Log.i(TAG, "lanzarSeguimiento, entra por validacion uVO.getFecha()+LocationUtils.UPDATE_INTERVAL_IN_ONE_MINUTE) < factual");
                Location l = easyGPS.getLastLocationOnLine();
                if (l != null) {
                    String speed = LocationManager.getVelocidad(l.getSpeed());
                    float orientacion = l.getBearing();
                    uVO = new UbicacionVO(l.getLatitude(), l.getLongitude(), l.getAccuracy(), l.getTime(), speed, orientacion, telefono, 0);
                    Log.i(TAG, "lanzarSeguimiento(" + new Date().toLocaleString() + ") nuevo UVO-->" + uVO);
                }
            }

            SeguimientoVO s = new SeguimientoVO();
            idUsuario = prefSEguimiento.getInt("idUsuario", 0);
            codigoSync = prefSEguimiento.getInt("codigo", 0);
            int validarCodigo = prefSEguimiento.getInt("validarCodigo", 1);
            //Campos para Monitoreo GPS, se enviarn cuando la versionGPS == 4
            String nombreEquipo = prefSEguimiento.getString("nombreEquipo", "");
            String colorEquipo = prefSEguimiento.getString("colorEquipo", "");
            String nombreUsuario = prefSEguimiento.getString("nombreUsuario", "");
            String unidad = prefSEguimiento.getString("unidad", "");
            String proveedorTransporte = prefSEguimiento.getString("proveedorTransporte", "");
            int idAplicacion = prefSEguimiento.getInt("idAplicacion", 0);
            int idEmpresa = prefSEguimiento.getInt("idEmpresa", 0);
            String estado = prefSEguimiento.getString("estado", "");
            String sucursal = prefSEguimiento.getString("sucursal", "");
            String nombreAPP = prefSEguimiento.getString("nombreAPP", "");
            Long fecha_enviar = prefSEguimientoTriger.getLong("fecha_enviar", 0);
            String versionAPP = prefSEguimiento.getString("versionAPP", "0");
            boolean integracionLogistic = prefSEguimiento.getBoolean("integracionLogistic", false);
            if (idAplicacion == 0 || integracionLogistic) {
                if (codigoSync > 0) {
                    s.setVersionGps(prefSEguimiento.getInt("versionGps", -1));
                    s.setIdUusuario(idUsuario);
                    s.setEnLinea(true);
                    s.setFechaEnvio(new Date());
                    s.setImei(org.kalla.enterprise.movil.util.Utilidades.getImei(ctx));
                    s.setUbicacion(uVO);
                    s.setVersion(org.kalla.enterprise.movil.util.Utilidades.obtenerVersionCode(ctx));
                    s.setNivelBateria(org.kalla.enterprise.movil.util.Utilidades.getNivelBateria(ctx));
                    s.setGpsActivo(SeguimientoReceiver.isGPSACtivo(ctx));
                    s.setNombreEquipo(nombreEquipo);
                    s.setColorEquipo(colorEquipo);
                    s.setNombreUsuario(nombreUsuario);
                    s.setUnidad(unidad);
                    s.setProveedorTransporte(proveedorTransporte);
                    s.setIdAplicacion(idAplicacion);
                    s.setIdEmpresa(idEmpresa);
                    s.setEstado(estado);
                    s.setTelefono(telefono.equalsIgnoreCase("") ? "000000000" : telefono);

                    SharedPreferences sh = ctx.getSharedPreferences(SeguimientoVO.SHARED_PREFERENCES_SEGUIMIENTO, Context.MODE_PRIVATE);
                    sh.edit().putString("" + s.getFechaEnvio().getTime(), s.toString()).commit();

                    prefSEguimiento.edit().putLong("ultimoRegistro", s.getFechaEnvio().getTime()).commit();

                    if (validarCodigo == 1) {
                        EnvioSeguimientoManager envio = new EnvioSeguimientoManager(ctx);
                        envio.enviarSeguimientosPendientes(ctx, obtenerUbicacionesPendientes(ctx));
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Ubicaciones", (Serializable) obtenerUbicacionesPendientes(ctx));
                        Intent intent = new Intent(UBICACION);
                        intent.putExtras(bundle);
                        ctx.sendBroadcast(intent);
                    }
                }
            }
            if (idAplicacion != 0) {
                EnvioSeguimientoManager envio = new EnvioSeguimientoManager(ctx);
                List<TrackVO> tracks = easyGPS.getTracksSinEnviar(ctx);
                for (TrackVO tracks_i : tracks) {
                    tracks_i.setEstado(estado);
                    tracks_i.setIdAplicacion(idAplicacion);
                    tracks_i.setIdEmpresa(idEmpresa);
                    tracks_i.setColorEquipoTrabajo(colorEquipo);
                    tracks_i.setNombreUsuario(nombreUsuario);
                    tracks_i.setEquipoTrabajo(nombreEquipo);
                    tracks_i.setIdUsuario(idUsuario);
                    tracks_i.setFrecuencia((double) frecuencia);
                    tracks_i.setImeiMovil(org.kalla.enterprise.movil.util.Utilidades.getImei(ctx));
                    tracks_i.setSucursal(sucursal);
                    tracks_i.setTipoTrack(tracks_i.getIdTipoTrack());
                    tracks_i.setNombreAplicacion(nombreAPP);
                    tracks_i.setFechaProximoSeguimiento(Utilidades.dateFormatSeguimiento.format(new Date(fecha_enviar)));
                    tracks_i.setNumeroMovil((long) 000000);
                    tracks_i.setUbicacion(SeguimientoReceiver.isGPSACtivo(ctx));
                    tracks_i.setVelocidad(Double.valueOf(uVO.getmVelocidad()));
                    tracks_i.setVersionAplicacion(versionAPP);
                    if (SeguimientoReceiver.isGPSACtivo(ctx) || org.kalla.enterprise.movil.util.Utilidades.getNivelBateria(ctx) > 30) {
                        tracks_i.setAlertaUbicacion("");
                    } else {
                        if (!SeguimientoReceiver.isGPSACtivo(ctx) && org.kalla.enterprise.movil.util.Utilidades.getNivelBateria(ctx) > 30) {
                            tracks_i.setAlertaUbicacion("GPS_OFF");
                        } else if (SeguimientoReceiver.isGPSACtivo(ctx) && org.kalla.enterprise.movil.util.Utilidades.getNivelBateria(ctx) < 30) {
                            tracks_i.setAlertaUbicacion("BATERIA_BAJA");
                        } else {
                            tracks_i.setAlertaUbicacion("GPS_OFF");
                        }
                    }
                }
                if (!integracionLogistic) {
                    prefSEguimiento.edit().putLong("ultimoRegistro", new Date().getTime()).commit();
                }
                envio.enviarTracksPendientes(ctx, tracks);
            }

        }
    }


}
