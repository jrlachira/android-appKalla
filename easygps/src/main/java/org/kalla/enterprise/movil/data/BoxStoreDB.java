package org.kalla.enterprise.movil.data;

import android.content.Context;

import org.kalla.enterprise.movil.gps.com.vo.MyObjectBox;

import io.objectbox.BoxStore;

public class BoxStoreDB {
    private static  BoxStoreDB myBoxStoreDB ;
    private BoxStore box;

    private static void createInstance(Context ctx) {
        if (myBoxStoreDB  == null) {
            // Solo se accede a la zona sincronizada
            // cuando la instancia no está creada
            synchronized(BoxStoreDB.class) {
                // En la zona sincronizada sería necesario volver
                // a comprobar que no se ha creado la instancia
                if (myBoxStoreDB  == null) {
                    myBoxStoreDB  = new BoxStoreDB();
                    myBoxStoreDB.initBD(ctx);

                }
            }
        }
    }

    public static BoxStoreDB getInstance(Context ctx) {
        if (myBoxStoreDB == null) createInstance(ctx);
        return myBoxStoreDB;
    }
    private void initBD(Context ctx){
        box= MyObjectBox.builder().androidContext(ctx).build();
    }

    public BoxStore getBox(){
        return box;
    }

}
