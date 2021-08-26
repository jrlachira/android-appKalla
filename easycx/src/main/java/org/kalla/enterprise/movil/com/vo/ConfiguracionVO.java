package org.kalla.enterprise.movil.com.vo;

import android.annotation.SuppressLint;
import android.util.DisplayMetrics;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SuppressLint("ParcelCreator")
public class ConfiguracionVO extends ObjetoSincronizar {

    private String nombre;
    private String valor;
    private String nombreApp;
    private String imeiSIM;
    private byte[] recurso_ldpi;
    private byte[] recurso_mdpi;
    private byte[] recurso_hdpi;
    private byte[] recurso_xhdpi;
    private byte[] recurso_xxhdpi;
    private byte[] recurso_xxxhdpi;

    public ConfiguracionVO() {
    }

    public ConfiguracionVO(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public byte[] getRecurso_ldpi() {
        return recurso_ldpi;
    }

    public void setRecurso_ldpi(byte[] recurso_ldpi) {
        this.recurso_ldpi = recurso_ldpi;
    }

    public byte[] getRecurso_mdpi() {
        return recurso_mdpi;
    }

    public void setRecurso_mdpi(byte[] recurso_mdpi) {
        this.recurso_mdpi = recurso_mdpi;
    }

    public byte[] getRecurso_hdpi() {
        return recurso_hdpi;
    }

    public void setRecurso_hdpi(byte[] recurso_hdpi) {
        this.recurso_hdpi = recurso_hdpi;
    }


    public byte[] getRecurso_xhdpi() {
        return recurso_xhdpi;
    }

    public void setRecurso_xhdpi(byte[] recurso_xhdpi) {
        this.recurso_xhdpi = recurso_xhdpi;
    }

    public byte[] getRecurso_xxhdpi() {
        return recurso_xxhdpi;
    }

    public void setRecurso_xxhdpi(byte[] recurso_xxhdpi) {
        this.recurso_xxhdpi = recurso_xxhdpi;
    }

    public byte[] getRecurso_xxxhdpi() {
        return recurso_xxxhdpi;
    }

    public void setRecurso_xxxhdpi(byte[] recurso_xxxhdpi) {
        this.recurso_xxxhdpi = recurso_xxxhdpi;
    }

    public byte[] getRecurso(int densidad){
        byte[] recurso = null;
        switch(densidad){
            case DisplayMetrics.DENSITY_LOW:
                if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                else if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                break;
            case DisplayMetrics.DENSITY_HIGH:
                if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                else if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                break;
            default:
                if(recurso_hdpi!=null){
                    recurso = recurso_hdpi;
                }
                else if(recurso_mdpi!=null){
                    recurso = recurso_mdpi;
                }
                else if(recurso_xhdpi!=null){
                    recurso = recurso_xhdpi;
                }
                else if(recurso_xxhdpi!=null){
                    recurso = recurso_xxhdpi;
                }
                else if(recurso_xxxhdpi!=null){
                    recurso = recurso_xxxhdpi;
                }
                else if(recurso_ldpi!=null){
                    recurso = recurso_ldpi;
                }
                break;
        }
        return recurso;
    }


    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {
        setNombre(in.readUTF());
        setValor(in.readUTF());
        /*********** cuando este habilitado el envio de recursos ************/
//		int tam = in.readShort();
//		if (tam > 0) {
//			recurso_ldpi = new byte[tam];
//			in.read(recurso_ldpi);
//		}
//		tam = in.readShort();
//		if (tam > 0) {
//			recurso_mdpi = new byte[tam];
//			in.read(recurso_mdpi);
//		}
//		tam = in.readShort();
//		if (tam > 0) {
//			recurso_hdpi = new byte[tam];
//			in.read(recurso_hdpi);
//		}
//		tam = in.readShort();
//		if (tam > 0) {
//			recurso_xhdpi = new byte[tam];
//			in.read(recurso_xhdpi);
//		}
//		tam = in.readShort();
//		if (tam > 0) {
//			recurso_xxhdpi = new byte[tam];
//			in.read(recurso_xxhdpi);
//		}
//		tam = in.readShort();
//		if (tam > 0) {
//			recurso_xxxhdpi = new byte[tam];
//			in.read(recurso_xxxhdpi);
//		}
        return true;
    }

    @Override
    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
        dataOut.writeUTF(imeiSIM);
        dataOut.writeUTF(nombreApp);
    }

    public String getNombreApp() {
        return nombreApp;
    }

    public void setNombreApp(String nombreApp) {
        this.nombreApp = nombreApp;
    }

    public String getImeiSIM() {
        return imeiSIM;
    }

    public void setImeiSIM(String imeiSIM) {
        this.imeiSIM = imeiSIM;
    }

}
