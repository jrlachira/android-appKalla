package org.kalla.enterprise.movil.com.vo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ConfiguracionVORPT extends ObjetoSincronizar{

    List<ConfiguracionVO> configuraciones;

    public List<ConfiguracionVO> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(List<ConfiguracionVO> configuraciones) {
        this.configuraciones = configuraciones;
    }

    @Override
    public void setValoresOnOuputStream(DataOutputStream dataOut) throws IOException {

    }


    @Override
    public boolean setValoresFromInputStream(DataInputStream in) throws IOException {

        int tamConfiguraciones = in.readInt();
        if (tamConfiguraciones>0) {
            configuraciones = new ArrayList<ConfiguracionVO>();
            for (int i = 0; i < tamConfiguraciones; i++) {
                ConfiguracionVO act = new ConfiguracionVO();
                act.setValoresFromInputStream(in);
                configuraciones.add(act);
            }

        }
        return true;
    }



}
