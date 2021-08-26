package org.kalla.enterprise.movil.appupdate;

import android.os.Bundle;

public interface IAppUpdateListener {

    public void notificarUpdate(int codigo,String Msg,Bundle data);

}
