package org.kalla.enterprise.movil.gps.com.vo;

import org.kalla.enterprise.movil.gps.util.ConstantesTipoTrack;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class TrackVO {
    @Id
    private long id;
    @Transient
    private TipoTrackVO tipoTrack;
    private long idTipoTrack;
    private int idEmpresa;
    private int idAplicacion;
    private String nombreAplicacion;
    private String versionAplicacion;
    private Long numeroMovil;
    private String imeiMovil;
    private Integer idUsuario;
    private String nombreUsuario;
    private String equipoTrabajo;
    private String colorEquipoTrabajo;
    private String sucursal;
    private int idTransporte;
    private String nombreTransporte;
    private String tipoTransporte;
    private String proveedorTransporte;
    private String colorProveedorTransporte;
    private String estado;
    private String colorEstado;
    private String idTrack;
    private String fechaMovil;
    private String fechaCapturaGps;
    private Double latitudPosicion;
    private Double longitudPosicion;
    private Double presicion;
    private Double velocidad;
    private Double origen;
    private Double orientacion;
    private String direccion;
    private Double bateria;
    private Boolean ubicacion;
    private Double frecuencia;
    private String fechaProximoSeguimiento;
    private String evento;
    private String alertaUbicacion;

    public void setColorEquipoTrabajo(String colorEquipoTrabajo) {
        this.colorEquipoTrabajo = colorEquipoTrabajo;
    }

    public void setEquipoTrabajo(String equipoTrabajo) {
        this.equipoTrabajo = equipoTrabajo;
    }

    public void setFrecuencia(Double frecuencia) {
        this.frecuencia = frecuencia;
    }

    public void setIdAplicacion(int idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setImeiMovil(String imeiMovil) {
        this.imeiMovil = imeiMovil;
    }

    public void setNombreAplicacion(String nombreAplicacion) {
        this.nombreAplicacion = nombreAplicacion;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setNumeroMovil(Long numeroMovil) {
        this.numeroMovil = numeroMovil;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }


    public void setVersionAplicacion(String versionAplicacion) {
        this.versionAplicacion = versionAplicacion;
    }

    public Double getLatitudPosicion() {
        return latitudPosicion;
    }

    public void setLatitudPosicion(Double latitudPosicion) {
        this.latitudPosicion = latitudPosicion;
    }

    public Double getLongitudPosicion() {
        return longitudPosicion;
    }

    public void setLongitudPosicion(Double longitudPosicion) {
        this.longitudPosicion = longitudPosicion;
    }

    public Double getPresicion() {
        return presicion;
    }

    public void setPresicion(Double presicion) {
        this.presicion = presicion;
    }

    public Double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Double velocidad) {
        this.velocidad = velocidad;
    }

    public Double getOrigen() {
        return origen;
    }

    public void setOrigen(Double origen) {
        this.origen = origen;
    }

    public Double getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(Double orientacion) {
        this.orientacion = orientacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getBateria() {
        return bateria;
    }

    public void setBateria(Double bateria) {
        this.bateria = bateria;
    }

    public Boolean getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Boolean ubicacion) {
        this.ubicacion = ubicacion;
    }


    public int getIdEmpresa() {
        return idEmpresa;
    }

    public int getIdAplicacion() {
        return idAplicacion;
    }

    public String getNombreAplicacion() {
        return nombreAplicacion;
    }

    public String getVersionAplicacion() {
        return versionAplicacion;
    }

    public Long getNumeroMovil() {
        return numeroMovil;
    }

    public String getImeiMovil() {
        return imeiMovil;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEquipoTrabajo() {
        return equipoTrabajo;
    }

    public String getColorEquipoTrabajo() {
        return colorEquipoTrabajo;
    }

    public String getSucursal() {
        return sucursal;
    }

    public Double getFrecuencia() {
        return frecuencia;
    }

    public int getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(int idTransporte) {
        this.idTransporte = idTransporte;
    }

    public String getNombreTransporte() {
        return nombreTransporte;
    }

    public void setNombreTransporte(String nombreTransporte) {
        this.nombreTransporte = nombreTransporte;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getProveedorTransporte() {
        return proveedorTransporte;
    }

    public void setProveedorTransporte(String proveedorTransporte) {
        this.proveedorTransporte = proveedorTransporte;
    }

    public String getColorProveedorTransporte() {
        return colorProveedorTransporte;
    }

    public void setColorProveedorTransporte(String colorProveedorTransporte) {
        this.colorProveedorTransporte = colorProveedorTransporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getColorEstado() {
        return colorEstado;
    }

    public void setColorEstado(String colorEstado) {
        this.colorEstado = colorEstado;
    }

    public String getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(String idTrack) {
        this.idTrack = idTrack;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEvento() {
        return evento;
    }

    public String getAlertaUbicacion() {
        return alertaUbicacion;
    }

    public void setAlertaUbicacion(String alertaUbicacion) {
        this.alertaUbicacion = alertaUbicacion;
    }

    public String getFechaMovil() {
        return fechaMovil;
    }

    public void setFechaMovil(String fechaMovil) {
        this.fechaMovil = fechaMovil;
    }

    public String getFechaCapturaGps() {
        return fechaCapturaGps;
    }

    public void setFechaCapturaGps(String fechaCapturaGps) {
        this.fechaCapturaGps = fechaCapturaGps;
    }

    public String getFechaProximoSeguimiento() {
        return fechaProximoSeguimiento;
    }

    public void setFechaProximoSeguimiento(String fechaProximoSeguimiento) {
        this.fechaProximoSeguimiento = fechaProximoSeguimiento;
    }

    public TipoTrackVO getTipoTrack() {
        return tipoTrack;
    }

    public void setTipoTrack(TipoTrackVO tipoTrack) {
        if (tipoTrack != null) {
            this.tipoTrack = tipoTrack;
            this.idTipoTrack = tipoTrack.getId();
        }
    }

    public void setTipoTrack(long idTipoTrack) {
        for (TipoTrackVO tipoTrack : ConstantesTipoTrack.TIPO_TRACK_LIST) {
            if (tipoTrack.getId() == idTipoTrack) {
                this.tipoTrack = tipoTrack;
            }
        }
    }

    public long getIdTipoTrack() {
        return idTipoTrack;
    }

    public void setIdTipoTrack(long idTipoTrack) {
        this.idTipoTrack = idTipoTrack;
    }

}
