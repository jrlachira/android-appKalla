package org.kalla.enterprise.movil.gps.com.vo;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.kalla.enterprise.movil.com.Utilidades;
import org.kalla.enterprise.movil.com.vo.ObjetoSincronizar;
import org.kalla.enterprise.movil.gps.vo.UbicacionVO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
public class SeguimientoVO extends ObjetoSincronizar implements Parcelable {

    private UbicacionVO ubicacion;
    private Date fechaEnvio;
    private int idUsuario;
    private String imei;
    private boolean enLinea;
    private int version;
    private byte nivelBateria;
    private boolean gpsActivo;
    private static int versionGps;
    private String velocidad;
    private String sent;
    private String telefono;


    //Campos para Monitoreo GPS, se enviarn cuando la versionGPS == 4
    private String nombreEquipo;
    private String colorEquipo;
    private String nombreUsuario;
    private String unidad;
    private String proveedorTransporte;
    private int idAplicacion;
    private int idEmpresa;
    private String estado;

    public static final String SHARED_PREFERENCES_SEGUIMIENTO = "enviospendientes";

    public SeguimientoVO() {
        super();
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    @Override
    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {
        if (imei == null)
            dataOut.writeUTF("");
        else
            dataOut.writeUTF(imei);

        dataOut.writeDouble(ubicacion.getLatitude());
        dataOut.writeDouble(ubicacion.getLongitude());
        dataOut.writeFloat(ubicacion.getmPresision());
        Utilidades.writeFecha(dataOut, fechaEnvio);
        Utilidades.writeFecha(dataOut, ubicacion.getFecha());
        dataOut.writeBoolean(enLinea);
        dataOut.writeInt(idUsuario);
        dataOut.writeInt(version);
        dataOut.writeByte(nivelBateria);
        dataOut.writeBoolean(gpsActivo);

        // Version 2 para Gestor de Visitas
        if (versionGps == 2) {
            dataOut.writeUTF(ubicacion.getmVelocidad());
        }
        // Version 3 para Logistic
        else if (versionGps == 3) {
            dataOut.writeUTF(ubicacion.getmVelocidad());
            dataOut.writeFloat(ubicacion.getmSentido());
            dataOut.writeUTF(telefono != null ? telefono : "");
        }
        // Version 4 para AMET
        else if (versionGps == 4) {
            dataOut.writeUTF(ubicacion.getmVelocidad());
            dataOut.writeUTF(nombreEquipo != null ? nombreEquipo : "");
            dataOut.writeUTF(colorEquipo != null ? colorEquipo : "");
            dataOut.writeUTF(nombreUsuario != null ? nombreUsuario : "");
            dataOut.writeUTF(unidad != null ? unidad : "");
            dataOut.writeUTF(proveedorTransporte != null ? proveedorTransporte : "");
            dataOut.writeUTF(estado != null ? estado : "");
            dataOut.writeInt(idAplicacion);
            dataOut.writeInt(idEmpresa);
        }


        Log.i("", "Escribiendo datos de seguimineto -->");
        Log.i("", "Version GPS -->" + versionGps);
    }

    public void setIdUusuario(int idUusuario) {
        this.idUsuario = idUusuario;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setUbicacion(UbicacionVO ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setEnLinea(boolean enLinea) {
        this.enLinea = enLinea;
    }

    public boolean isEnLinea() {
        return enLinea;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(byte nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public boolean isGpsActivo() {
        return gpsActivo;
    }

    public void setGpsActivo(boolean gpsActivo) {
        this.gpsActivo = gpsActivo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getVersionGps() {
        return versionGps;
    }

    public void setVersionGps(int versionGps) {
        this.versionGps = versionGps;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getColorEquipo() {
        return colorEquipo;
    }

    public void setColorEquipo(String colorEquipo) {
        this.colorEquipo = colorEquipo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getProveedorTransporte() {
        return proveedorTransporte;
    }

    public void setProveedorTransporte(String proveedorTransporte) {
        this.proveedorTransporte = proveedorTransporte;
    }

    public int getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(int idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public static SeguimientoVO creaInstancia(String seguimiento_string) {
        SeguimientoVO s = null;
        if (seguimiento_string.contains("|")) {
            s = new SeguimientoVO();
            StringTokenizer st = new StringTokenizer(seguimiento_string, "|");

            s.setIdUusuario(Integer.parseInt(st.nextToken()));
            s.setEnLinea("1".equals(st.nextToken()));
            s.setFechaEnvio(new Date(Long.parseLong(st.nextToken())));
            s.setImei(st.nextToken());
            UbicacionVO uVO = new UbicacionVO(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()), Float.parseFloat(st.nextToken()), Long.parseLong(st.nextToken()), s.getVelocidad() == null ? "0" : s.getVelocidad(), Float.parseFloat(s.getSent() == null ? "0" : s.getSent()), s.getTelefono() == null ? "" : s.getTelefono(), 0);
            s.setUbicacion(uVO);
            s.setVersion(Integer.parseInt(st.nextToken()));
            s.setNivelBateria(Byte.parseByte(st.nextToken()));
            s.setGpsActivo("1".equals(st.nextToken()));

            // Version 2 para Gestor Visitas
            if (versionGps == 2) {
                s.setVelocidad(st.nextToken());
            }
            // Version 3 para Logistic
            else if (versionGps == 3) {
                s.setVelocidad(st.nextToken());
                s.setSent(st.nextToken());
                s.setTelefono(st.nextToken());
                if ("000000000".equalsIgnoreCase(s.getTelefono())) {
                    s.setTelefono("");
                }
            }
            // Version 4 para AMET
            else if (versionGps == 4) {
                s.setVelocidad(st.nextToken());
                //Campos para Monitoreo GPS, se enviarón cuando la versionGPS == 4
                s.setNombreEquipo(st.nextToken());
                s.setColorEquipo(st.nextToken());
                s.setNombreUsuario(st.nextToken());
                s.setUnidad(st.nextToken());
                s.setProveedorTransporte(st.nextToken());
                s.setIdAplicacion(Integer.parseInt(st.nextToken()));
                s.setIdEmpresa(Integer.parseInt(st.nextToken()));
                s.setEstado(st.nextToken());
                //Campos para Monitoreo GPS, se enviarn cuando la versionGPS == 4
                if ("null".equals(s.getNombreEquipo())) {
                    s.setNombreEquipo(null);
                }
                if ("null".equals(s.getColorEquipo())) {
                    s.setColorEquipo(null);
                }
                if ("null".equals(s.getNombreUsuario())) {
                    s.setNombreUsuario(null);
                }
                if ("null".equals(s.getUnidad())) {
                    s.setUnidad(null);
                }
                if ("null".equals(s.getProveedorTransporte())) {
                    s.setProveedorTransporte(null);
                }
                if ("null".equals(s.getIdAplicacion())) {
                    s.setIdAplicacion(Integer.parseInt(null));
                }
                if ("null".equals(s.getIdEmpresa())) {
                    s.setIdEmpresa(Integer.parseInt(null));
                }
                if ("null".equals(s.getEstado())) {
                    s.setEstado(null);
                }
            }
        }
        return s;
    }

    public String getImei() {
        return imei;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public int getIdUusuario() {
        return idUsuario;
    }

    public UbicacionVO getUbicacion() {
        return ubicacion;
    }

    @Override
    public String toString() {
        String retorno = "Ubicacion NULL";
        if (ubicacion != null) {
            StringBuilder sb = new StringBuilder(idUsuario + "|" + (enLinea ? "1" : "0") + "|" + fechaEnvio.getTime() + "|" + imei + "|" + ubicacion.getLatitude() + "|" + ubicacion.getLongitude() + "|" + ubicacion.getmPresision() + "|" + ubicacion.getFecha() + "|" + version + "|" + nivelBateria + "|" + (gpsActivo ? "1" : "0"));
            if (versionGps == 2) {
                sb.append("|").append(ubicacion.getmVelocidad());
            } else if (versionGps == 3) {
                sb.append("|").append(ubicacion.getmVelocidad()).
                        append("|").append(ubicacion.getmSentido()).
                        append("|").append(telefono);
            } else if (versionGps == 4) {
                sb.append("|").append(ubicacion.getmVelocidad()).
                        append("|").append(nombreEquipo != null && nombreEquipo != "" ? nombreEquipo : null).
                        append("|").append(colorEquipo != null && colorEquipo != "" ? colorEquipo : null).
                        append("|").append(nombreUsuario != null && nombreUsuario != "" ? nombreUsuario : null).
                        append("|").append(unidad != null && unidad != "" ? unidad : null).
                        append("|").append(proveedorTransporte != null && proveedorTransporte != "" ? proveedorTransporte : null).
                        append("|").append(idAplicacion != 0 ? String.valueOf(idAplicacion) : null).
                        append("|").append(idEmpresa != 0 ? String.valueOf(idEmpresa) : null).
                        append("|").append(estado != null && estado != "" ? estado : null);
            }
            retorno = sb.toString();
            Log.i("SeguimientoVO", "Serializando datos de seguimineto -->" + sb.toString());
        }
        return retorno;
    }

    public SeguimientoVO(Parcel in) {
        imei = in.readString();
        ubicacion = in.readParcelable(UbicacionVO.class.getClassLoader());
        fechaEnvio = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imei);
        dest.writeParcelable(ubicacion, flags);
        dest.writeLong(fechaEnvio.getTime());
    }

    public static final SeguimientoVO.Creator<SeguimientoVO> CREATOR = new SeguimientoVO.Creator<SeguimientoVO>() {
        public SeguimientoVO createFromParcel(Parcel in) {
            return new SeguimientoVO(in);
        }

        public SeguimientoVO[] newArray(int size) {
            return new SeguimientoVO[size];
        }
    };

    public static Comparator<SeguimientoVO> COMPARE_BY_DATE = new Comparator<SeguimientoVO>() {
        public int compare(SeguimientoVO one, SeguimientoVO other) {
            Date one_ = one.getFechaEnvio() == null ? new Date() : one.getFechaEnvio();
            Date other_ = other.getFechaEnvio() == null ? new Date() : other.getFechaEnvio();
            return one_.compareTo(other_);
        }
    };

}
