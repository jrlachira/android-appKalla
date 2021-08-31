package org.kalla.enterprise.movil.gps.com.vo;

import android.util.Log;

import org.kalla.enterprise.movil.com.vo.ObjetoSincronizar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class EnvioUbicacionesVO extends ObjetoSincronizar{

    List<SeguimientoVO> segumientosEnviar;
    private boolean hayNotificacion;
    private String mensaje;

    @Override
    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
        if(segumientosEnviar!=null &&segumientosEnviar.size()>0){
            dataOut.writeInt(segumientosEnviar.size());
            for (SeguimientoVO seguimiento : segumientosEnviar) {
                seguimiento.setValoresOnOuputStream(dataOut);
            }
        }
    }

    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {
        short tam=  in.readShort();
        for (int i = 0; i < tam; i++) {
            in.readBoolean();

        }


        hayNotificacion = in.readBoolean();
        if(hayNotificacion){

            Log.i("EnvioUbicacionVO", "hay notificacion alternativa");
            mensaje = in.readUTF();

        }

        return true;
    }

    public void setSegumientosEnviar(List<SeguimientoVO> segumientosEnviar) {
        this.segumientosEnviar = segumientosEnviar;
    }

    public List<SeguimientoVO> getSegumientosEnviar() {
        return segumientosEnviar;
    }

    public boolean isHayNotificacion() {
        return hayNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


}
