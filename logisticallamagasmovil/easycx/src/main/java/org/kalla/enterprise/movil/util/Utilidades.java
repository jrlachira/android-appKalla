package org.kalla.enterprise.movil.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


public class Utilidades {

    public static int obtenerVersionCode(Context context) {

        int versionCode = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Toast.makeText(this,
        // "PackageName = " + info.packageName + "\nVersionCode = "
        // + info.versionCode + "\nVersionName = "
        // + info.versionName + "\nPermissions = " + info.permissions,
        // Toast.LENGTH_SHORT).show();

        // PackageManager pm = context.getPackageManager();
        // try {
        // PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        // return pi.versionCode;
        // } catch (NameNotFoundException ex) {
        // }
        // return 0;
        return versionCode;
    }

    public static String obtenerVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (NameNotFoundException ex) {
        }
        return null;
    }

    public static String getImei(Context ctx) {
        String phoneId;
        try {
            TelephonyManager tMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 29) {
                phoneId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                phoneId = tMgr.getDeviceId();
                if (phoneId == null || Long.parseLong(phoneId) == 0)
                    phoneId = "000000000000000";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "000000000000000";
        }
//        phoneId = "357863106370364";
        Log.d("Utilidades", "phoneId: " + phoneId);
        return phoneId;
    }

    public static String getAplicacionName(Context context) {
        PackageManager pm = context.getPackageManager();
        CharSequence name = "";
        try {
            ApplicationInfo pi = pm.getApplicationInfo(context.getPackageName(), 0);
            name = (String) pm.getApplicationLabel(pi);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return name.toString();

    }

    public static byte getNivelBateria(Context context) {
        Intent batteryStatus = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return (byte) (batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1));
    }

    public static String getEmpresa(Context _context) {
        ApplicationInfo ai;
        String ret = null;
        try {
            ai = _context.getPackageManager().getApplicationInfo(_context.getPackageName(), PackageManager.GET_META_DATA);
            ret = (String) ai.metaData.get("Empresa");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    public static String getNumeroTelefono(Context context) {
        String telefono = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getLine1Number() != null) {
                telefono = telephonyManager.getLine1Number();
            }
        }catch (Exception e){

        }
        return telefono;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
