package org.kalla.enterprise.movil.gps.util;

import org.kalla.enterprise.movil.gps.com.vo.TipoTrackVO;

import java.util.Arrays;
import java.util.List;

public class ConstantesTipoTrack {
    public static final TipoTrackVO TIPO_TRACK_VISITA = new TipoTrackVO(1,"Visita", "3e95cd");
    public static final TipoTrackVO TIPO_TRACK_UBICACION = new TipoTrackVO(2,"Ubicacion", "8e5ea2");
    public static final TipoTrackVO TIPO_TRACK_UBICACION_MANUAL = new TipoTrackVO(3,"Ubicacion Manual", "3cba9f");
    public static final TipoTrackVO TIPO_TRACK_TRACK = new TipoTrackVO(4,"Track", "e8c3b9");
    public static final TipoTrackVO TIPO_TRACK_ALERTA = new TipoTrackVO(5,"Alerta", "ffa500");
    public static final TipoTrackVO TIPO_TRACK_PEDIDO = new TipoTrackVO(6,"Pedido", "ffa500");
    public static final TipoTrackVO TIPO_TRACK_ESTADO = new TipoTrackVO(7,"Estado", "ffa500");
    public static final TipoTrackVO TIPO_TRACK_ASISTENCIA = new TipoTrackVO(8,"Asistencia", "ffa500");
    public static final TipoTrackVO TIPO_TRACK_FORMULARIO = new TipoTrackVO(9,"Formulario", "ffa500");

    public static final List<TipoTrackVO> TIPO_TRACK_LIST = Arrays.asList(
            TIPO_TRACK_VISITA,
            TIPO_TRACK_UBICACION,
            TIPO_TRACK_UBICACION_MANUAL,
            TIPO_TRACK_TRACK,
            TIPO_TRACK_ALERTA,
            TIPO_TRACK_PEDIDO,
            TIPO_TRACK_ESTADO,
            TIPO_TRACK_ASISTENCIA,
            TIPO_TRACK_FORMULARIO
    );


}
