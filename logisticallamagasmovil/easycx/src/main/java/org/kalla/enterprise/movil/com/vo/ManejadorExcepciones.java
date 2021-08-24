package org.kalla.enterprise.movil.com.vo;


public class ManejadorExcepciones {

    public String manejadorExcepciones(Exception ex) {
        String mensajeError = "";
        mensajeError = "ERROR: " + ex.toString();
//            if (ex.getClass().isInstance(new java.io.IOException())) {
//                if (ex.getMessage().startsWith("No response from")) {
//                    mensajeError = IntAvisos.MSG_ERROR_NO_RESPONSE;
//                } else if (ex.getMessage().startsWith("Http")) {
//                    mensajeError = IntAvisos.MSG_CX_CLOSE;
//                } else if (ex.getMessage().startsWith("TCP")) {
//                    mensajeError = ex.getMessage().toUpperCase();
//                } else if (ex.getMessage().startsWith("Versiones Incompatibles")) {
//                    mensajeError = ex.getMessage();
//                } else if (ex.getMessage().startsWith("java.lang.IllegalArgumentExceptionUnsupported return type")) {
//                    //mensajeError = IntAIControlComunicacionesServer_DEFAULT;
//                } else if (ex.getMessage().startsWith("error 10054 during TCP read")) {
//                    mensajeError = IntAvisos.MSG_ERROR_CONEXION_CERRADA;
//                } else if (ex.getMessage().startsWith("Error in HTTP operation")) {
//                    mensajeError = IntAvisos.MSG_ERROR_HTTP_OPERATION;
//                } else if (ex.getMessage().startsWith("response empty")) {
//                    mensajeError = IntAvisos.MSG_ERROR_RESPONSE_EMPTY;
//                } else if ((ex.getMessage() != null) && ((ex.getMessage().startsWith("TIEMPO")) || (ex.getMessage().startsWith("Tiempo")))) {
//                    mensajeError = IntAvisos.MSG_TIMEOUT;
//                } else if (new Integer(Integer.parseInt(ex.getMessage().substring(0, 3))) instanceof Integer) {
//                    //#if Consola == "Si"
////#                     System.out.println("Error " + Integer.parseInt(ex.getMessage().substring(0, 3)) + " en el servidor: " + ex.getMessage().substring(4));
//                    //#endif
//                    mensajeError = "Error " + Integer.parseInt(ex.getMessage().substring(0, 3)) + " en el servidor.  Por favor intente más tarde.";
//                }
//            } else if (ex.getClass().isInstance(new javax.microedition.io.ConnectionNotFoundException())) {
//                mensajeError = IntAvisos.MSG_TCP;
//            } else if (ex.getClass().isInstance(new java.lang.SecurityException())) {
//                mensajeError = IntAvisos.MSG_PERMISOS;
//            } else if (ex.getClass().isInstance(new java.lang.IllegalArgumentException())) {
//                mensajeError = IntAvisos.MSG_ERROR_INVALID_ADDRESS;
//            } else if (ex.getClass().isInstance(new java.lang.Exception())) {
//                //#if Consola == "Si"
////#                 System.out.println("ENTRAMOS AL MANEJADOR DE EXCEPCIONES POR UNA EXCEPCION DE TIPO java.lang.Exception");
//                //#endif
//                if (ex.getMessage().startsWith("TIEMPO")) {
//                    mensajeError = IntAvisos.MSG_TIMEOUT;
//                } else if (ex.getMessage().startsWith("TCP")) {
//                    mensajeError = IntAvisos.MSG_TCP;
//                } else if (ex.getMessage().startsWith("No se puede realizar la peticion")) {
//                    mensajeError = IntAvisos.MSG_NO_OPERACION;
//                } else {
//                    //#if Consola == "Si"
////#                     System.out.println("- Fijando descripcion del mensaje de error (Java lang exception)");
////#                     ex.printStackTrace();
//                    //#endif
//                }
//            } else {
//                //#if Consola == "Si"
////#                 ex.printStackTrace();
////#                 System.out.println("- Fijando msg por defecto al mensaje de error");
//                //#endif
//                mensajeError = IntAvisos.MSG_DEFAULT;
//            }
//        } catch (Exception e) {
//            //#if Consola == "Si"
////#             System.out.println("Capturamos una excepción en el manejador de excepciones: "+e.getMessage());
//            //#endif
//            if ((e.getMessage() != null) && ((e.getMessage().startsWith("TIEMPO")) || (e.getMessage().startsWith("Tiempo")))) {
//                mensajeError = IntAvisos.MSG_TIMEOUT;
//            } else if ((e.getMessage() != null) && (e.getMessage().startsWith("TCP"))) {
//                mensajeError = IntAvisos.MSG_TCP;
//            } else {
//                //#if Consola == "Si"
////#                 System.out.println("- Fijando descripcion del mensaje de error (Excepcion en manejador de excepciones)");
//                //#endif
//            }
//        }
        return mensajeError;
    }
}

