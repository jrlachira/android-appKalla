package org.kalla.enterprise.movil.com.vo;

import android.annotation.SuppressLint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressLint("ParcelCreator")
public class ArregloByteVO extends ObjetoSincronizar{
    private String nombre;
    private int  longitudArreglo;
    private byte[] arreglo;


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getLongitudArreglo() {
        return longitudArreglo;
    }
    public void setLongitudArreglo(int longitudArreglo) {
        this.longitudArreglo = longitudArreglo;
    }
    public byte[] getArreglo() {
        return arreglo;
    }
    public void setArreglo(byte[] arreglo) {
        this.arreglo = arreglo;
    }

    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
        dataOut.writeUTF(getNombre());
        dataOut.writeInt(getLongitudArreglo());
        dataOut.write(getArreglo());
    }

    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( (other == null ) ) return false;
        if ( !(other instanceof ArregloByteVO) ) return false;
        ArregloByteVO castOther = ( ArregloByteVO ) other;

        return ( (this.nombre==castOther.nombre) || ( this.nombre!=null && castOther.nombre!=null && this.nombre.equals(castOther.nombre) ) );
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + ( nombre == null ? 0 : this.nombre.hashCode());
        return result;
    }


}