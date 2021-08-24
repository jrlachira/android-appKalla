package org.kalla.enterprise.movil.com;

import android.content.Context;

import org.kalla.enterprise.movil.com.vo.ErrorComunicacion;
import org.kalla.enterprise.movil.com.vo.ObjetoRecibir;

import java.util.ArrayList;

public interface IConexionServer {
    /**
     * Estrega a la gui los datos recibidos de la conexion con el servidor.
     * La gui intentaro guardar los datos, y en caso de no guardarlos lanzaro una excepcion que sero atendida por
     * la comunicacion
     */
    public void resSincEnd(int codSinc,ObjetoRecibir rta) throws Exception;

    /**
     * Notifica a la gui la ocurrencia de un error de conexion con el servidor
     */

    public void resSincEnd(int codSinc, ArrayList<?> rta,boolean esFinal,int numPaginas, int pagActual) throws Exception;
    public void resError(ErrorComunicacion error);

    /**
     * Metodo utilizado para notificar a la gui que borre la data correspondiente a codSinc,
     * data que fuo anteriormente guardada con error
     * codSinc indica cual es la data que hay que borrar.
     *
     * Segon la respuesta de la gui (true o false, pudo borrar o no) la comunicacian decidira si
     * reintentaraa la descarga.
     *
     * Ase:
     * - si las pudo borrar y no ha llegado al maximo de reintentos, entonces reintentara
     * la descarga, si la grui las pudo borrar pero ya realiza el maximo de reintentos en tonces la gui no reintentara
     * y notificare a la gui un resError.
     *
     * - Si no las pudo borrar la comunicacian notificara a la gui un resError.
     */
    public boolean borrarData(int codSinc);

    public void traceCx(String trace);

    public void notificarReintento(int numReintento);

    public Context getContext();

}
