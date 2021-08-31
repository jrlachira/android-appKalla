package org.kalla.enterprise.movil.gps.com.vo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class TipoTrackVO {
    @Id
    private long id;
    private String nombre;
    private String color;


    public TipoTrackVO() {
    }

    public TipoTrackVO(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public TipoTrackVO(long id, String nombre, String color) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
