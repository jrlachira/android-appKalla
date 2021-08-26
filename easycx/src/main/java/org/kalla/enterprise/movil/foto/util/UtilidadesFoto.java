package org.kalla.enterprise.movil.foto.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class  UtilidadesFoto {

    static final String TAG = UtilidadesFoto.class.getSimpleName();
    public static int id;

    private static String fullPath;
    private static final int MAX_INTENTOS_CAMARA = 10;
    private static int COMPRESION_RATE = 50;
    private static int MAX_SIZE = 800;

    public static void setMaxSizeAndCompresionRate(int maxSize, int compresionRate) {
        MAX_SIZE = maxSize;
        COMPRESION_RATE = compresionRate;
    }

    public static byte[] getLastImageByteArray(Context contex) {
        return getLastImageByteArray(contex, null);
    }

    public static byte[] getLastImageByteArray(String fullPath) {
        return getLastImageByteArray(null, fullPath);
    }

    private static byte[] getLastImageByteArray(Context contex, String fullPath) {
        byte[] byteArray = null;
//Espera a que la foto esté lista. Para algunos celulares no alcanza a estar en el primer llamado.
        Bitmap bitmap = null;
        int intentos = 0;
        do {
            if (fullPath != null) {
                bitmap = getBitmap(fullPath);
            } else if (contex != null) {
                bitmap = getBitMapfromLastPhoto(contex);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.e(TAG, "getLastImageByteArray fallo, Error el hilo para espera", e);
            }
        } while ((bitmap == null) && (++intentos < MAX_INTENTOS_CAMARA));

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESION_RATE, stream);
            byteArray = stream.toByteArray();
        }
        bitmap = null;
        return byteArray;
    }

    public static Bitmap getBitMapfromLastPhoto(Context contex) {
        fullPath = getFullPathfromLastPhoto(contex);
        if (fullPath == null) {
            return null;
        }
        return getBitmap(fullPath);
    }

    public static String getFullPathfromLastPhoto(Context contex) {
        SharedPreferences preferencias = contex.getSharedPreferences("org.seratic.fotos", Context.MODE_PRIVATE);
        Map<String, ?> fotosMap = preferencias.getAll();
        Collection<?> fotos = fotosMap.values();
        if (fotos == null || fotos.isEmpty()) {
            return null;
        }
        int i = 0;
        long lista_fotos[] = new long[fotos.size()];

        for (Object foto_i : fotos) {
            String ruta = (String) foto_i;
            ruta = ruta.substring(ruta.indexOf("foto_") + 5);
            ruta = ruta.substring(0, ruta.indexOf("_"));
            lista_fotos[i] = (new Long(ruta));
            i++;
        }
        lista_fotos = ordenarArray(lista_fotos);
        i--;
        id = (i < 0 ? 0 : i);
        Log.i(TAG, "getFullPathfromLastPhoto id: " + id + " size:" + lista_fotos.length);
        return fullPath = preferencias.getString("foto_" + lista_fotos[id] + "_", "");
    }

    private static long[] ordenarArray(long[] n) {
        long aux;
        for (int i = 0; i < n.length - 1; i++) {
            for (int x = i + 1; x < n.length; x++) {
                if (n[x] < n[i]) {
                    aux = n[i];
                    n[i] = n[x];
                    n[x] = aux;
                }
            }
        }
        return n;
    }

    public static Bitmap getBitmap(String ruta) {
        Bitmap fotoCargada = null;
        try {
            FileInputStream fis = new FileInputStream(ruta);
            ExifInterface exif = new ExifInterface(ruta);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            fotoCargada = UtilidadesFoto.scalarFoto(fis);// BitmapFactory.decodeStream(fis);////

            // bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            Log.i(TAG, "getLastImageId::id " + id);
            Log.i(TAG, "getLastImageId::path " + ruta);

            if (fotoCargada != null) {
                if (orientation == 6) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    fotoCargada = Bitmap.createBitmap(fotoCargada, 0, 0, fotoCargada.getWidth(), fotoCargada.getHeight(), matrix, true);

                    Log.i(TAG, "Cambiando orientacion " + orientation);
                }

                Log.i(TAG, "ancho" + (fotoCargada.getWidth()) + "alto " + fotoCargada.getHeight() + "orientacion" + orientation);

            }
            fis.close();
            fis = null;
        } catch (Exception e) {
            Log.e(TAG, "getBitmap fallo", e);
        }
        return fotoCargada;
    }

    public static Bitmap scalarFoto(FileInputStream fd) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;

        // abrimos el archivo sin reservar memoria

        try {
            BitmapFactory.decodeFileDescriptor(fd.getFD(), null, op);
        } catch (Exception e1) {
            Log.e(TAG, "scalarFoto fallo", e1);
        }

        // cambiamos el tamaoo de la imagen, a una pequeoa para
        // no desperdiciar memoria y que se vea bien.
        Log.i(TAG, "ancho O " + (op.outWidth) + "alto O" + op.outHeight);
        int scale = 1;
        if (op.outHeight > MAX_SIZE || op.outWidth > MAX_SIZE) {
            while ((op.outHeight / scale) > MAX_SIZE || (op.outWidth / scale) > MAX_SIZE) {
                scale *= 2;
            }
        }
        op = new BitmapFactory.Options();
        op.inSampleSize = scale;
        op.inJustDecodeBounds = false;

        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeFileDescriptor(fd.getFD(), null, op);
        } catch (Exception e) {
            Log.e(TAG, "scalarFoto fallo", e);
        }

        return bm;
    }

    public static String getFullPath() {
        return fullPath;
    }

    public static int getId() {
        return id;
    }

    public static Uri crearArchivoImagne(Context ctx) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        imageFileName = "foto_" + new Date().getTime() + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = null;

        try {
            file = File.createTempFile(imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ctx, "Error creando foto", Toast.LENGTH_LONG);

        }

        // Store image in dcim
        // File file = new File(Environment.getDownloadCacheDirectory() +
        // "/DCIM/", "diego" + new Date().getTime() + ".png");

        if (file == null) {
            String path = Environment.getExternalStorageDirectory() + "/Seratic";

            final File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs(); // create folders where write files
            }
            file = new File(dir, imageFileName + ".jpg");

        }
        Uri imgUri = Uri.fromFile(file);

        SharedPreferences preferencias = ctx.getSharedPreferences("org.seratic.fotos", Context.MODE_PRIVATE);
        String imgPath = file.getAbsolutePath();
        preferencias.edit().putString(imageFileName, imgPath).commit();

        return imgUri;
    }

    public static void limpiar_fotos(Context ctx) {
        SharedPreferences preferencias = ctx.getSharedPreferences("org.seratic.fotos", Context.MODE_PRIVATE);
        preferencias.edit().clear().commit();
    }

}
