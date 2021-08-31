package org.kalla.enterprise.movil.gps.util;
import java.util.ArrayList;
import java.util.List;
public class Codigos {
    public static final String DEFAULT_URL = "192.168.0.12:9520";
    public final static short COD_VERSIONES_INCOMPATIBLES = -1;
    public final static short COD_AUTENTICAR = 1;
    public final static short COD_SINC_RUTA_PUNTOS_VENTA = 2;
    public final static short COD_SINC_CATEGORIAS = 3;
    public final static short COD_SINC_PRODUCTOS = 4;
    public final static short COD_SINC_ACTIVIDADES = 5;
    public final static short COD_SINC_DATOS_CATALOGO = 6;
    public final static short COD_SINC_CATEGORIAS_ACTIVIDAD = 7;
    public final static short COD_SINC_MOTIVOS_NOVISITA = 8;
    public final static short COD_SINC_RELEVO_ANTERIOR = 9;
    public final static short COD_SINC_RELEVO_ANTERIOR_OPCION = 10;
    public final static short COD_REG_RELEVO = 11;
    public final static short COD_REG_INI_VISITA = 12;
    public final static short COD_REG_FIN_VISITA = 13;
    public final static short COD_REG_VISITAS = 14;
    public final static short COD_SINC_NOTIFICACIONES = 15;
    public final static short COD_REG_POSICION_PUNTOVENTA = 16;
    public final static short COD_SIN_PRODUCTOS_PUNTOVENTA = 17;
    public final static short COD_REG_PUNTO_VENTA_NUEVO = 18;
    public final static short COD_REG_ASISTENCIA = 19;
    public final static short COD_SINC_DIRECCIONES = 20;
    public final static short COD_ACTUALIZA_STOCK = 21;
    public final static short COD_ACTUALIZA_STOCK_LIST = 22;
    public final static short COD_CONSULTA_PEDIDO = 23;
    public final static short COD_CONSULTA_PEDIDO_TRACKING = 24;
    public final static short COD_CONSULTA_PEDIDO_ITEMS = 25;
    public final static short COD_CONSULTA_INFORMACION_VISUALIZAR = 26;
    public final static short COD_CONSULTA_CLIENTE_LINEA = 27;

    public static final short CODSEGUIMIENTO = 28;
    public final static short COD_GRUPO_TIPO_DOCUMENTO = 125;
    public final static short COD_PARAMETROS_CONFIG = 30;
    public final static short COD_SINC_UNIDADES_PRODUCTOS = 31;
    public final static short COD_SINC_LISTA_PRECIOS = 32;
    public final static short COD_SINC_LISTA_PRECIO_PRODUCTO = 33;
    public final static short COD_SINC_CAMPANAS = 34;
    public final static short COD_SINC_CAMPANAS_PTO_VENTA = 35;
    public final static short COD_SINC_TIPO_DOCUMENTO = 36;
    public static final short COD_CAMBIO_PASSWORD = 37;
    public static final short COD_ACT_PUNTO_VENTA = 38;
    public static final short COD_SINC_CITAS = 39;
    public static final short COD_REG_CITAS = 40;
    public static final short COD_SINC_PERSONALIZACION_ETIQUETAS = 41;
    public final static short COD_SINC_OPORTUNIDAD_ANTERIOR = 42;
    public final static short COD_SINC_OPORTUNIDAD_ANTERIOR_OPCION = 43;
    public final static short COD_SINC_SERIES_PRODUCTO = 44;
    public static final short COD_SINC_COMPONENTES_DATOSGRUPO = 45;
    public final static short COD_CONSULTA_VISITAS = 46;
    public final static short COD_SINC_PEDIDO_ANTERIOR = 47;
    public final static short COD_SINC_PEDIDO_ANTERIOR_OPCION = 48;
    public final static short COD_SINC_PEDIDO_ANTERIOR_DETALLE = 49;
    public final static short COD_CONSULTA_REPORTE_EQUIPOS = 50;
    public final static short COD_CONSULTA_CLIENTE_EXISTENTE = 51;
    public final static short COD_SINC_INDICADORES = 52;
    public final static short COD_CONSULTA_REPORTE_CONSOLIDADO = 53;
    public final static short COD_REG_VISITA_CORREO = 54;
    public static final short SIN_CONTACTOS_PUNTO_VENTA = 55;
    public static final short SIN_CITAS_CONTACTOS= 56;
    public final static short COD_SINC_USUARIOS_EQUIPO = 57;
    public final static short COD_REG_ASIGNAR_SERIES = 58;
    public final static short COD_SINC_MOTIVOS_ACTIVIDAD = 59;
    public final static short COD_SINC_CONSTANTES = 60;

    //codigo permiso para descrga de conactos y visuliacion de contacto en cita y visita
    public static final short PERMISO_CONTACTOS= 106;
    public static final short PERMISO_LISTA_PRECIOS_ALMACEN= 102;



    // CODIGOS PROPIOS DE APLICACION MOVIL
    public static final short ERROR_CX = 0;
    public final static short COD_ACTUALIZAR_APP = 29;
    public static final int COD_AUTENTICACION_ERROR = 200;
    public static final int COD_AUTENTICACION_EXITOSA = 201;
    public final static short COD_ENVIO_ACTIVIDADES_EXITOSO = 202;
    public final static short COD_ENVIO_ACTIVIDADES_ERROR = 203;
    public final static short COD_ENVIO_INI_VISITA_EXITOSO = 204;
    public final static short COD_ENVIO_INI_VISITA_ERROR = 205;
    public final static short COD_ENVIO_VISITAS_PENDIENTES_EXITOSO = 206;
    public final static short COD_ENVIO_VISITAS_PENDIENTES_ERROR = 207;
    public final static short COD_ENVIO_ID_NOTIFICACIONES_EXITOSO = 208;
    public final static short COD_ENVIO_UBICACION_PUNTOVENTA_EXITOSO = 209;
    public final static short COD_ENVIO_UBICACION_PUNTOVENTA_ERROR = 210;
    public final static short COD_ENVIO_ID_NOTIFICACIONES_ERROR = 211;
    public final static short COD_ACTUALIZA_STOCK_LIST_ERROR = 212;
    public final static short COD_CONSULTA_PEDIDO_ERROR = 213;
    public final static short COD_CONSULTA_PEDIDO_TRACKING_ERROR = 214;
    public final static short COD_CONSULTA_PEDIDO_ITEMS_ERROR = 215;
    public final static short COD_CONSULTA_CLIENTE_LINEA_ERROR = 216;
    public final static short COD_PARAMETROS_CONFIG_ERROR = 217;
    public final static short COD_REG_PUNTO_VENTA_NUEVO_ERROR = 218;
    public static final short COD_CAMBIO_PASSWORD_EXITOSO = 219;
    public static final short COD_CAMBIO_PASSWORD_ERROR = 220;
    public final static short COD_ENVIO_FIN_VISITA_EXITOSO = 221;
    public final static short COD_ENVIO_FIN_VISITA_ERROR = 222;
    public final static short COD_ENVIO_ALGUNAS_INI_VISITA_ERROR = 223;
    public final static short COD_ENVIO_ALGUNAS_FIN_VISITA_ERROR = 224;
    public final static short COD_ENVIO_ALGUNAS_ACTIVIDADES_ERROR = 225;
    public final static short COD_ENVIO_ALGUNAS_ASISTENCIAS_ERROR = 226;
    public final static short COD_ENVIO_ASISTENCIAS_ERROR = 227;
    public final static short COD_ENVIO_ASISTENCIAS_EXITOSO = 228;
    public final static short COD_ACT_PUNTO_VENTA_ERROR = 229;
    public final static short COD_ENVIO_CITAS_EXITOSO = 230;
    public final static short COD_ENVIO_CITAS_ERROR = 231;
    public final static short COD_ENVIO_ALGUNAS_CITAS_ERROR = 232;
    public final static short COD_DESCARGA_DATA_ESQUEMAS_ACT = 233;
    public static final short COD_FINALIZO_ENVIO_PENDIENTES = 234;
    public final static short COD_CONSULTA_VISITAS_ERROR = 235;
    public final static short COD_CONSULTA_VISITAS_EXITOSO = 236;
    public final static short COD_ENVIO_ASIGNACION_SERIES_EXITOSO = 237;
    public final static short COD_ENVIO_ASIGNACION_SERIES_ERROR = 238;
    public final static short COD_ENVIO_ALGUNAS_ASIGNACION_SERIES_ERROR = 239;
    public final static short COD_CONSULTA_CLIENTE_EXISTENTE_ERROR = 240;
    public final static short NOTIFY_SORT_NOT_FOUND = 241;
    public final static short NOTIFY_SORT_FOUND = 242;
    public final static short COD_SINC_MAESTROS_EXITOSA = 243;
    public final static short COD_SINC_MAESTROS_INTEGRACION_EXITOSA = 244;

    public final static List<String> COLUMNAS_PERMANENTES_PDV = new ArrayList<String>(5);
    static {
        COLUMNAS_PERMANENTES_PDV.add("codigo");
        COLUMNAS_PERMANENTES_PDV.add("nombre");
        COLUMNAS_PERMANENTES_PDV.add("direccion");
        COLUMNAS_PERMANENTES_PDV.add("ubicacion");
        COLUMNAS_PERMANENTES_PDV.add("latitud");
        COLUMNAS_PERMANENTES_PDV.add("longitud");
        COLUMNAS_PERMANENTES_PDV.add("principal");
        COLUMNAS_PERMANENTES_PDV.add("codPadre");
    }
    public final static List<String> COLUMNAS_PERMANENTES_PDV_MOVIL = new ArrayList<String>(5);
    static {
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("cod_pventa");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("nom_pventa");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("dir_pventa");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("lat_pventa");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("lng_pventa");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("dia");
        COLUMNAS_PERMANENTES_PDV_MOVIL.add("orden_rutero");
    }
    public final static List<String> COLUMNAS_ARREGLOS_PDV_MOVIL = new ArrayList<String>();
    static {
        COLUMNAS_ARREGLOS_PDV_MOVIL.add("foto");

    }

    public final static String TIPO_DOCUMENTO_BUSQUEDA = "dni";
    public final static String ASISTENCIA_TITULO = "Asistencia";
    public final static String ENVIAR_UBICACION_TITULO = "Enviar Ubicación";
    public final static String CREAR_CLIENTES_TITULO = "Crear Clientes";
    public final static String CONSULTA_PEDIDO_TITULO = "Consulta de Pedidos";
    public final static String CONSULTA_PRODUCTO_TITULO = "Consulta de Productos";
    public final static String ACTIVIDADES_NO_ENVIADAS_TITULO = "No Enviados";
    public final static String CAMBIAR_CONTRASENA_TITULO = "Cambio Contraseña";
    public final static String AGENDAR_CITA_TITULO = "Agendar Cita";
    public final static String MENSAJE_SINCRONIZACION = "Ruta";
    public final static String ASIGNAR_SERIE_TITULO = "Asignación de Series";
    public final static String SUCURSAL = "0";
    public final static String SIN_FILTRO_DATOS = "";
    public final static String DIRECCION_ENTREGA = "0";
    public final static String EMPRESA_TRANSPORTE = "0";
    public final static String LISTA_PRECIOS = "0";
    public final static String COMPONENTE_DESCUENTO = "0";
    public final static String COMPONENTE_OBSERVACION = "0";
    public final static String COMPONENTES_OPORTUNIDAD = "";
    public final static String NUMERO_PEDIDOS = "20";
    public final static String COMPONENTE_ALMACEN = "0";
    public final static String COMPONENTES_ALMACEN = "";
    public final static String RANGO_VISITA = "100";
    public final static String VALOR_RUTERO_SALDO_CERO = "";
    public final static String VALOR_CAMPO_VALIDACION_LECTOR_PDV = "";
    public final static boolean existe_porcentaje_descuento = false;
    public final static boolean existe_precio_lista = false;
    public final static boolean existe_stock_almacen = false;
    public final static boolean existe_stock_tiendas = false;
    public final static boolean existe_motivo_canje = false;
    public final static boolean mostrar_mensajes_confirmacion = true;
    public final static boolean validar_stock = false;
    public final static boolean validar_precion_minimo = false;
    public final static boolean formato_entrada_cant_pedido = false;
    public final static boolean validacion_eliminar_carpeta_dcim = false;


    public final static String DIAS_FECHA_MAX_ENTREGA = "0";
    public final static String COMPONENTE_NUM_PEDIDO = "0";
    public final static String COMPONENTE_TIPO_PEDIDO = "0";
    public final static String COMPONENTE_TIPO_VENTA = "0";
    public final static String COMPONENTE_TIPO_FACTURACION = "0";
    public final static String COD_OPC_GRUPO_TIPO_PED_PROFORMA = "0";
    public final static String COD_OPC_GRUPO_TIPO_PED_PEDIDO = "0";
    public final static String MONEDA = "0";
    public final static String AGENCIA_TRANSPORTE = "0";
    public final static String DIRECCION_AGENCIA = "0";
    public final static byte TIPO_DIRECCION_ENTREGA = 2;
    public final static byte TIPO_DIRECCION_FACTURACION = 1;
    public final static byte TIPO_DIRECCION_COBRANZA = 3;
    public final static boolean quitar_sufijo_pdv = false;
    public final static boolean orden_google_api_pdvs = false;
    public final static boolean existe_opcion_llamada = false;
    public final static boolean modo_citas = false;
    public final static boolean modulo_oportunidades = false;
    public final static boolean data_esquemas_actividad = false;
    public final static boolean mapa_final_detalle = false;
    public final static boolean activar_version_pro = false;
    public final static boolean multiple_pedido = false;
    public final static boolean mostrar_observacion_visita = false;
    public final static boolean integracion_logistic = false;
    public final static String cfg_key_api_google_directions = "";
    public final static String COD_ACTIVIDAD_LLAMADA = "0";
    public final static String COMPONENTE_FECHA_LLAMADA = "0";
    public final static String COMPONENTE_NUMERO_LLAMADA = "0";
    public final static String COMPONENTE_DURACION_LLAMADA = "0";

    //Columnas adicionales al reporte de Pedido
    public final static String COLUMNA_MONTO = "Monto";
    public final static String COLUMNA_ESTADO = "Estado";
    public final static String COLUMNA_FECHA = "Fecha Pedido";

    public final static List<String> COLUMNAS_PERMANENTES_USUARIO = new ArrayList<String>(5);

    static {
        COLUMNAS_PERMANENTES_USUARIO.add("codigo");
        COLUMNAS_PERMANENTES_USUARIO.add("usuario");
        COLUMNAS_PERMANENTES_USUARIO.add("clave");
        COLUMNAS_PERMANENTES_USUARIO.add("nombre");
        COLUMNAS_PERMANENTES_USUARIO.add("codCampana");
        COLUMNAS_PERMANENTES_USUARIO.add("codAlmacen");
        COLUMNAS_PERMANENTES_USUARIO.add("nombreCampana");
        COLUMNAS_PERMANENTES_USUARIO.add("nombreCuenta");
        COLUMNAS_PERMANENTES_USUARIO.add("nomAlmacen");
        COLUMNAS_PERMANENTES_USUARIO.add("idSeguimiento");
        COLUMNAS_PERMANENTES_USUARIO.add("tipoCambio");
        COLUMNAS_PERMANENTES_USUARIO.add("frecuenciaSeguimiento");
    }

    public final static byte COD_TIPO_BUSQUEDA_INICIO = 1;
    public final static byte COD_TIPO_BUSQUEDA_CONTENIDO = 2;
    public final static byte COD_TIPO_BUSQUEDA_FIN = 3;

    public final static String COD_INTERNO_MONEDA_EURO = "EUR";
    public final static String COD_INTERNO_MONEDA_NUEVOS_SOLES = "S/.";
    public final static String COD_INTERNO_MONEDA_DOLARES = "US$";

    public final static String COD_LISTA_PRECIOS_L02 = "L02";
    public final static String COD_LISTA_PRECIOS_L03 = "L03";
    public final static String COD_LISTA_PRECIOS_L05 = "L05";

    public final static String COD_INTERNO_TIPO_PEDIDO_PEDIDO = "300";
    public final static String COD_INTERNO_TIPO_PEDIDO_PROFORMA = "301";

    public final static String COD_INTERNO_VENTA_NORMAL = "1";
    public final static String COD_INTERNO_TIPO_FACT_MAS_GUIA = "1";

    public final static byte TIPO_MOTIVO_VISITA = 1;
    public final static byte TIPO_MOTIVO_NO_VISITA = 2;
    public final static byte TIPO_MOTIVO_CANJE = 3;
    public final static byte TIPO_MOTIVO_CANCELACION_CITA = 4;

    public final static double PORCENTAJE_MAXIMO_INDICADORES_CALCULAR_ALTO = 0.45;
    public final static double PORCENTAJE_MAXIMO_LISTAS_REP_CONS_CALCULAR_ALTO = 0.3;
    public final static int MARGIN_BOTTON_LISTAS_REP_CONS = 50;
    public final static String COMPONENTES_INHABILITAR_GRID = "";
    public final static String COMPONENTES_CARGAR_VALOR_GRID = "";
    public final static String COMPONENTES_OBLIGATORIOS_GRID = "";

    public final static String NOMBRE_CLIENTE_SONY = "SONY";

    public final static int SP_UBICACION = 0;
    public final static int SP_AUTENTICACION = 1;
    public final static List<String> SP_SUITE = new ArrayList<String>();
    static {
        SP_SUITE.add(SP_UBICACION,"org.seratic.ubicacion");
        SP_SUITE.add(SP_UBICACION,"PreferenciasAutenticacionGPS");
    }
}
