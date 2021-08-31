package org.kalla.enterprise.movil.com.vo;

import android.annotation.SuppressLint;
import android.os.Parcel;

import java.io.DataInputStream;
import java.io.IOException;

@SuppressLint("ParcelCreator")
public class TextosPersonalizadosVO extends ObjetoRecibir {
    private int id;
    private String etiquetaSingular;
    private String etiquetaPlural;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEtiquetaSingular() {
        return etiquetaSingular;
    }
    public void setEtiquetaSingular(String etiquetaSingular) {
        this.etiquetaSingular = etiquetaSingular;
    }
    public String getEtiquetaPlural() {
        return etiquetaPlural;
    }
    public void setEtiquetaPlural(String etiquetaPlural) {
        this.etiquetaPlural = etiquetaPlural;
    }

    @Override
    public boolean setValoresFromInputStream(DataInputStream in)
            throws IOException {
        setId(in.readInt());
        setEtiquetaSingular(in.readUTF());
        setEtiquetaPlural(in.readUTF());
        return true;
    }

    @Override
    public void finalize() {
        // TODO Auto-generated method stub

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
