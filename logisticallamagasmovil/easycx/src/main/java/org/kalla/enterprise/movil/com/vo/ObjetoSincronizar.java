package org.kalla.enterprise.movil.com.vo;

import android.annotation.SuppressLint;
import android.os.Parcel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


@SuppressLint("ParcelCreator")
public class ObjetoSincronizar extends ObjetoRecibir {

    private short	numPags;
    private short	paginaActual;

    public short getNumPags() {
        return numPags;
    }

    public void setNumPags(short numPags) {
        this.numPags = numPags;
    }

    public short getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(short contPag) {
        this.paginaActual = contPag;
    }

    public void finalize() {
    }

    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
    }

    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }

}

