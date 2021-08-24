package org.kalla.enterprise.movil.com.vo;

import android.os.Parcelable;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ObjetoRecibir implements Parcelable {


    public abstract boolean setValoresFromInputStream(DataInputStream in) throws IOException;

    public abstract void finalize();


}
