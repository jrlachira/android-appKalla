package org.kalla.enterprise.movil.appupdate.vo;


import org.kalla.enterprise.movil.com.vo.ObjetoSincronizar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VersionAppVO extends ObjetoSincronizar {

    public static final byte	ESTADO_SIN_TRABAJAR	= 0;

    private String				urlAPK;
    private int						versionCodigo;
    private String					nombreVersion;
    private String 				nombreApp;
    private String			imeiSIM;

    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {
        setVersion(in.readUTF());
        setVersionCodigo(in.readInt());
        setURLAPK(in.readUTF());

        return true;
    }

    @Override
    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
        dataOut.writeUTF(imeiSIM);
        dataOut.writeUTF(nombreApp);
    }

    private void setURLAPK(String readUTF) {
        urlAPK = readUTF;

    }

    private void setVersionCodigo(int readInt) {
        versionCodigo = readInt;

    }

    private void setVersion(String readUTF) {
        nombreVersion = readUTF;

    }

    public String getNombreVersion() {
        return nombreVersion;
    }

    public int getVersionCodigo() {
        return versionCodigo;
    }

    public String getUrlAPK() {
        return urlAPK;
    }

    public void setNombreApp(String nombreApp) {
        if (nombreApp==null) nombreApp="";
        this.nombreApp = nombreApp;
    }
    public void setImeiSIM(String imeiSIM) {
        this.imeiSIM = imeiSIM;
    }

    @Override
    public void finalize() {
        // TODO Auto-generated method stub

    }

}