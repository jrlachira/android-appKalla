package org.kalla.enterprise.movil.com.vo;

/**
 *
 * @author Diana
 */
public class ErrorComunicacion {
    /**
     * Indica que ocurri un error de comunicacin
     */
    public static final int TIPO_ERROR_CX        = 1;
    /**
     * Indica que ocurri un error guardando la data recibida de la comunicacin con el servidor.
     */
    public static final int TIPO_ERROR_GUARDANDO = 2;
    /**
     * Indica que ocurri un error borrando unos RS. Se llama a borrar
     * los RS cuando anteriormente no se pudo guardar la data y se va a hacer el reintento de
     * descargar de la misma data. Si no se pudieron borrar los RS, la comunicacin
     * no intentar una nueva descarga de la data
     */
    public static final int TIPO_ERROR_BORRANDO  = 3;

    public static final int EN_OPEN     = -1;
    public static final int EN_TIMEOUT  = -2;
    public static final int EN_FIJANDO_DATOS_ENVIO     = -3;
    public static final int EN_FIJANDO_DATOS_RECIBIDOS  = -4;
    public static final int EN_NOTIFICANDO_LA_INTERFAZ     = -5;
    public static final int EN_OPTENER_RESPUESTA     = -6;
    public static final int EN_VERIFICAR_APP  = -7;



    /**
     * Corresponde al codSinc o codResp del elemento que se estaba sincronizando y en el que ocurrió el error.
     */
    private int tipoError;
    private int errorEn;
    private String msg;
    /**
     * Cadena que detalle el mensaje de error extraido con getString de la Excepcion capturada
     */
    private String detalleMsg;
    /**
     * Indica si la comuicacion esto reintentando la descarga nuevamente del
     * elemento en el que ocurrio error guardando por que aon podro hacer
     * reintentos ya que no ha intentado el NUM_MAX_REINTENTOS
     */
    private boolean reintentando;



    public ErrorComunicacion(int tipo, String msg){
        this.tipoError = tipo;
        this.errorEn = -1;
        this.msg = msg;
        this.detalleMsg = "";
    }

    public ErrorComunicacion(int tipo, int errorEn, String msg, String detalleMsg, boolean reintentando){
        this.tipoError = tipo;
        this.errorEn = errorEn;
        this.msg = msg;
        this.detalleMsg = detalleMsg;
        this.reintentando = reintentando;
    }

    /**
     * @return the tipoError
     */
    public int getTipoError() {
        return tipoError;
    }

    /**
     * @param tipoError the tipoError to set
     */
    public void setTipoError(int tipoError) {
        this.tipoError = tipoError;
    }

    /**
     * @return the errorEn
     */
    public int getErrorEn() {
        return errorEn;
    }

    /**
     * @param errorEn the errorEn to set
     */
    public void setErrorEn(int errorEn) {
        this.errorEn = errorEn;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the detalleMsg
     */
    public String getDetalleMsg() {
        return detalleMsg;
    }

    /**
     * @param detalleMsg the detalleMsg to set
     */
    public void setDetalleMsg(String detalleMsg) {
        this.detalleMsg = detalleMsg;
    }

    /**
     * @return the reintentando
     */
    public boolean isReintentando() {
        return reintentando;
    }

    /**
     * @param reintentando the reintentando to set
     */
    public void setReintentando(boolean reintentando) {
        this.reintentando = reintentando;
    }

}

