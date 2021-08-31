package org.kalla.enterprise.movil.gps;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.kalla.enterprise.movil.gps.util.Codigos;
import org.kalla.enterprise.movil.gps.vo.UbicacionVO;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class LocationManager {
    //	private long					intervaloUbicacion;
//	private LocationClient			locationclient;
//	private UbicacionVO				uvo;
//
    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    public static final String KEY_PRESISION = "KEY_RADIUS";
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_ORIENTATION = "KEY_ORIENTATION";
    public static final String KEY_TELEPHONE = "KEY_TELEPHONE";
    public static final String KEY_LAST_TIME = "KEY_LAST_TIME";
    //	/*
//	 * Invalid values, used to test geofence storage when retrieving geofences
//	 */
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
    public static final String INVALID_STRING_VALUE = "0";
    private static final String TAG = "LocationManager";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationrequest;
    //
    //	private static final String	SHARED_PREFERENCES_VALUE	= "ultimaUbicacion";
//	//FragmentActivity myAactivity;
    private boolean save = false;
    private SimpleUbicacionStore saver;
    //	private boolean	startUpdate = false;
    private Context ctx;
    private Location mLastLocation;
    private IUbicacionListener mUbicacionListener;
    private LocationCallback mLocationCallback;
    private SharedPreferences mPrefs;
//	private SharedPreferences prefSEguimiento;
//	public final String PREFERENCES_SERVIO_SEGUIMIENTO="org.seratic.enterprise.movil.gps.SEGUIMIENTO";
//	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    public LocationManager(Context ctx) {
        this.ctx = ctx;
        saver = new SimpleUbicacionStore(ctx);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx);
    }

    public LocationManager(LocationRequest lr, IUbicacionListener ubicacionListener, Context ctx, boolean save) {
        this(ctx);
        this.locationrequest = lr;
        this.save = save;
        this.ctx = ctx;
        this.mUbicacionListener = ubicacionListener;
        mPrefs = ctx.getSharedPreferences(Codigos.SP_SUITE.get(Codigos.SP_UBICACION), Context.MODE_PRIVATE);

    }

    /**
     * //     * Este metodo redondea la velocidad obtenida desde el Location de google a un numero con 1 cifra decimal
     * //     * @param speed - Velocidad a redondear
     * //     * @return String - Velocidad redondeada y casteada
     * //     * @author Eliana Andrea Concha Agredo
     * //
     */
    public static String getVelocidad(float speed) {
        BigDecimal value = new BigDecimal(speed * 3.6);
        value = value.setScale(1, RoundingMode.HALF_DOWN);
        return String.valueOf(value);
    }

    //
//	public void startPeriodicUpdates() {
//		if (locationclient != null && locationrequest!=null) {
//			try{
//				locationclient.requestLocationUpdates(locationrequest, this);
//			}catch(Exception e){
//
//			}
//		}
//
//	}
//
//	/**
//	 * In response to a request to stop updates, send a request to Location
//	 * Services
//	 */
//	public void stopPeriodicUpdates() {
//		if (locationclient != null) {
//			locationclient.removeLocationUpdates(this);
//		}
//
//	}
//
//	public boolean isConnected() {
//		// TODO Auto-generated method stub
//		return locationclient == null ? false : locationclient.isConnected();
//	}
//
//	public boolean iniciar(boolean starUpdates) {
//		this.startUpdate=starUpdates;
//		if (locationclient != null) {
//			locationclient.connect();
//			return true;
//
//		}
//		return false;
//	}
//
//	public static UbicacionVO getUbicacionVo(Context context) {
//		SharedPreferences sh = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		return getUbicacion(sh);
//	}
//
//	public static LatLng getLatLng(Context context){
//		UbicacionVO ubicacioin = getUbicacionVo(context);
//		return new LatLng(ubicacioin.getLatitude(),ubicacioin.getLongitude());
//	}
//
//
////	public static UbicacionVO getUbicacionVofromService() {
////		 app1 =
////                 createPackageContext("com.manning.aip.app1",
////                          CONTEXT_INCLUDE_CODE);
////		SharedPreferences sh = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
////		return getUbicacion(sh);
////
////	}
//
    public static UbicacionVO getUbicacion(SharedPreferences mPrefs) {
        /*
         * Get the latitude for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        double lat = mPrefs.getFloat(KEY_LATITUDE, INVALID_FLOAT_VALUE);
        /*
         * Get the longitude for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        double lng = mPrefs.getFloat(KEY_LONGITUDE, INVALID_FLOAT_VALUE);
        /*
         * Get the radius for the geofence identified by id, or
         * INVALID_FLOAT_VALUE if it doesn't exist
         */
        float presision = mPrefs.getFloat(KEY_PRESISION, INVALID_FLOAT_VALUE);
        /*
         * Get the speed for the geofence identified by id, or
         * INVALID_STRING_VALUE if it doesn't exist
         */
        String speed = mPrefs.getString(KEY_SPEED, INVALID_STRING_VALUE);
        String telephone = mPrefs.getString(KEY_TELEPHONE, INVALID_STRING_VALUE);
        float orientation = mPrefs.getFloat(KEY_ORIENTATION, INVALID_FLOAT_VALUE);

        long time = 0;
        try {
            time = (long) mPrefs.getLong(KEY_TIME, 0);
        } catch (Exception e) {
            time = (long) mPrefs.getFloat(KEY_TIME, 0);
        }

        long lastTime = 0;
        try {
            lastTime = (long) mPrefs.getLong(KEY_LAST_TIME, 0);
        } catch (Exception e) {
            lastTime = (long) mPrefs.getFloat(KEY_LAST_TIME, 0);
        }

        if (lat != INVALID_FLOAT_VALUE && lng != INVALID_FLOAT_VALUE && presision != INVALID_FLOAT_VALUE) {
            // Return a true Geofence object
            return new UbicacionVO(lat, lng, presision, time, speed, orientation, telephone, lastTime);
            // Otherwise, return null.
        } else {
            return null;
        }
    }

    ///
//		verificarWifi(ctx);
//		if (save)
//		   	saver = new SimpleUbicacionStore(ctx);
//
//		mPrefs = ctx.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
//		prefSEguimiento = ctx.getSharedPreferences(org.seratic.enterprise.movil.gps.LocationUtils.PREFERENCES_SEGUIMIENTO, Context.MODE_MULTI_PROCESS);
//	}
//
//	private final static int	CONNECTION_FAILURE_RESOLUTION_REQUEST	= 9000;
//
    @SuppressWarnings("MissingPermission")
    protected void setLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener((Activity) ctx, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            String result = "setLastLocation() Last known Location Latitude is " +
                                    mLastLocation.getLatitude() + "\n" +
                                    "Last known longitude Longitude is " + mLastLocation.getLongitude() + "fecha:" + new Date(mLastLocation.getTime()).toString();
                            Log.i(TAG, "setLastLocation " + result);
                            long lastTimeAnterior = (long) mPrefs.getLong(KEY_TIME, 0);
                            saver.saveLocationtoUbicacion(mLastLocation, lastTimeAnterior);

                        } else {
                            Log.i(TAG, "setLastLocation, No Last known location found. Try current location..!");
                        }
                    }
                });
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        try {

            mFusedLocationClient.requestLocationUpdates(locationrequest, mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        mLastLocation = (Location) locationResult.getLastLocation();

                        String result = "onLocationResult  -> Current Location Latitude is " +
                                mLastLocation.getLatitude() + "\n" +
                                "Current location Longitude is " + mLastLocation.getLongitude() + "fecha:" + new Date(mLastLocation.getTime()).toString() + " " + mUbicacionListener;
                        ;
                        long lastTime = (long) mPrefs.getLong(KEY_TIME, 0);
                        if (save) {

                            Log.i(TAG, "startLocationUpdates, " + result + "lastTime" + lastTime);
                            saver.saveLocationtoUbicacion(mLastLocation, lastTime);
                        }
                        if (mUbicacionListener != null) {
                            for (Location location : locationResult.getLocations()) {
                                mUbicacionListener.nuevaUbicacionListener(locatioToUbicacionVO(location, lastTime));
                            }

                        }
                    }

                }
            }, Looper.myLooper());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    //
//	public void verificarWifi(Context ctx) {
//		WifiManager wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);
//		// preguntamos si esta activo , si lo esta le cambiamos el estado a
//		// desactiva do
//		if (!wifiManager.isWifiEnabled()) {
//			wifiManager.setWifiEnabled(true);
//		}
//	}
//
//	/**
//	 * A single Geofence object, defined by its center and radius.
//	 */
//

    public UbicacionVO getSaverUbicacionVo() {
        return saver.getSaveUbicacion();
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    protected UbicacionVO locatioToUbicacionVO(Location location, long timeLastLocationSaver) {
        UbicacionVO ubicacion = null;
        if (location != null) {
            ubicacion = new UbicacionVO(
                    location.getLatitude(),
                    location.getLongitude(),
                    location.getAccuracy(),
                    location.getTime(),
                    getVelocidad(location.getSpeed()),
                    location.getBearing(),
                    "", timeLastLocationSaver
            );
        }
        return ubicacion;
    }

    public Address getAddress(UbicacionVO uvo) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(uvo.getLatitude(), uvo.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public class SimpleUbicacionStore {
        // Keys for flattened geofences stored in SharedPreferences

        // The SharedPreferences object in which geofences are stored
        private final SharedPreferences mPrefs;

        // The name of the SharedPreferences
        // Create the SharedPreferences storage with private access only
        public SimpleUbicacionStore(Context context) {
            mPrefs = context.getSharedPreferences(Codigos.SP_SUITE.get(Codigos.SP_UBICACION), Context.MODE_PRIVATE);
        }


        public UbicacionVO getSaveUbicacion() {
            /*
             * Get the latitude for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
            double lat = mPrefs.getFloat(KEY_LATITUDE, INVALID_FLOAT_VALUE);
            /*
             * Get the longitude for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
            double lng = mPrefs.getFloat(KEY_LONGITUDE, INVALID_FLOAT_VALUE);
            /*
             * Get the radius for the geofence identified by id, or
             * INVALID_FLOAT_VALUE if it doesn't exist
             */
            float presision = mPrefs.getFloat(KEY_PRESISION, INVALID_FLOAT_VALUE);

            long time = (long) mPrefs.getLong(KEY_TIME, 0);
            long lasTime = (long) mPrefs.getLong(KEY_LAST_TIME, 0);

            String speed = mPrefs.getString(KEY_SPEED, INVALID_STRING_VALUE);
            String telephone = mPrefs.getString(KEY_TELEPHONE, INVALID_STRING_VALUE);
            float orientation = mPrefs.getFloat(KEY_ORIENTATION, INVALID_FLOAT_VALUE);
            if (lat != INVALID_FLOAT_VALUE && lng != INVALID_FLOAT_VALUE && presision != INVALID_FLOAT_VALUE) {
                // Return a true Geofence object
                return new UbicacionVO(lat, lng, presision, time, speed, orientation, telephone, lasTime);
                // Otherwise, return null.
            } else {
                return null;
            }

        }

        //
//		/*
//		 * Get the expiration duration for the geofence identified by id, or
//		 * INVALID_LONG_VALUE if it doesn't exist
//		 *
//		 *
//		 * /** Save a geofence.
//		 *
//		 * @param geofence The SimpleGeofence containing the values you want to
//		 * save in SharedPreferences
//		 */
        public void saveLocationtoUbicacion(Location location, long lastTime) {
            if (!location.isFromMockProvider()) {
                String speed = "0";

                String telefono = "";//;prefSEguimiento.getString("telefono", "");
                /*
                 * Get a SharedPreferences editor instance. Among other things,
                 * SharedPreferences ensures that updates are atomic and non-concurrent
                 */
                SharedPreferences.Editor editor = mPrefs.edit();
                // Write the Geofence values to SharedPreferences
                editor.putFloat(KEY_LATITUDE, (float) location.getLatitude());
                editor.putFloat(KEY_LONGITUDE, (float) location.getLongitude());
                editor.putFloat(KEY_PRESISION, location.getAccuracy());
                editor.putLong(KEY_TIME, location.getTime());
                editor.putString(KEY_SPEED, getVelocidad(location.getSpeed()));
                editor.putString(KEY_TELEPHONE, telefono);
                editor.putFloat(KEY_ORIENTATION, location.getBearing());
                editor.putLong(KEY_LAST_TIME, lastTime);
                // Commit the changes
                editor.commit();
            }
        }
    }
}