package org.kalla.enterprise.movil.appupdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import org.kalla.enterprise.movil.util.Utilidades;

public class ShowingDialog extends Activity {

    Handler mHandler;
    //final static AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
    private AlertDialog alerta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //mHandler = new Handler();
        super.onCreate(savedInstanceState);


        View ly = new View(this);

        setContentView(ly);
        showalerta();



    }

    public void showalerta(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ALERTA");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("El telfono no est autorizado para hacer uso de esta aplicacin. por favor contacte al proveedor. (IMEI="+Utilidades.getImei(this)+" ) ");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        alerta = alertDialog.create();
        alerta.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alerta.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //showalerta();





//    	 if(mHandler!=null){
//    		 mHandler.postDelayed(new Runnable() {
//    	            @Override
//    	            public void run() {
//
//    	            	Intent intent = new Intent(Intent.ACTION_MAIN);
//    	            	intent.addCategory(Intent.CATEGORY_HOME);
//    	            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	            	startActivity(intent);
//
//    	            }
//    	         },120000L);
//    	 }
        //Alerta();

    }





}