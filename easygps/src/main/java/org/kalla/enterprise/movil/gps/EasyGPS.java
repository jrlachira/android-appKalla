package org.kalla.enterprise.movil.gps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kalla.suite_easygps.R;

import org.kalla.enterprise.movil.data.BoxStoreDB;
import org.kalla.enterprise.movil.gps.com.vo.TipoTrackVO;
import org.kalla.enterprise.movil.gps.com.vo.TrackVO;
import org.kalla.enterprise.movil.gps.util.Utilidades;
import org.kalla.enterprise.movil.gps.vo.UbicacionVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
public class EasyGPS implements IUbicacionListener {

    private final static int PLAY_SERVICES_REQUEST = 1000;
    private static final int REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 2000;
    private static final String TAG = "EasyGPS";
    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;
    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;
    /*
     * Note if updates have been turned on. Starts out as "false"; is set to
     * "true" in the method handleRequestSuccess of LocationUpdateReceiver.
     */
    boolean mUpdatesRequested = false;
    private Activity activity;
    private ArrayList<String> permissions = new ArrayList<>();
    private LocationRequest mLocationRequest;
    private LocationManager mLocationClient;
    private IUbicacionListener ubicacionListener;
    private IUbicacionListener ubicacionListenerForService;
    private boolean isPermissionGranted;


    public EasyGPS(Context ctx) {

        this(ctx, 1000, LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }


    public EasyGPS(Context ctx, long intervalo, int prioridad) {


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(intervalo <= 1000 ? 1000 : intervalo);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(prioridad);
        mLocationClient = new LocationManager(mLocationRequest, this, ctx, true);
        try {

            // Open Shared Preferences
            mPrefs = ctx.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            // Get an editor
            mEditor = mPrefs.edit();


            activity = (Activity) ctx;


            if (activity != null) {
                checkPlayServices(ctx);
                checkForLocationSettings(ctx);
            }


            // Create a new global location parameters object
//		mLocationRequest = LocationRequest.create();
//
//		/*
//		 * Set the update interval
//		 */
//		if (intervalo >= LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS) {
//			mLocationRequest.setInterval(intervalo);
//		} else {
//			mLocationRequest
//					.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
//		}
//
//		// Use high accuracy
//		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//		mLocationClient = new LocationManager(mLocationRequest, this, ctx, true);


            mLocationClient.setLastLocation();


        } catch (java.lang.ClassCastException ex) {

        }

    }


    public void iniciarSeguimientoPeriodico() {
        Log.i(TAG, "iniciarSeguimientoPeriodico");
        mLocationClient.startLocationUpdates();
    }

    public void detenerSeguimientoPeriodico() {

        Log.i(TAG, "detenerSeguimientoPeriodico");
        mLocationClient.stopLocationUpdates();

    }
//		// If the app already has a setting for getting location updates, get it
//		if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
//			mUpdatesRequested = mPrefs.getBoolean(
//					LocationUtils.KEY_UPDATES_REQUESTED, false);
//
//			// Otherwise, turn off location updates until requested
//		} else {
//			mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
//			mEditor.commit();
//		}


    @Override
    public void nuevaUbicacionListener(UbicacionVO uVO) {

        if (ubicacionListener != null) {
            if (uVO != null) {
                long timeW = LocationUtils.getDateWithoutMilliseconds(new Date(uVO.getMfechaUltimaUbicacion()));
                long fechaUbicActual = LocationUtils.getDateWithoutMilliseconds(new Date(uVO.getFecha()));
                //Comparacion de fecha de ubicacion actual con respecto a la anterior para garantizar ubicaciones no repetidas
                //@author Eliana Andrea Concha Agredo
                Log.i(TAG, "nuevaUbicacionListener: " + uVO + ", " + fechaUbicActual + ", timeW: " + timeW);
                if (fechaUbicActual > timeW) {
                    ubicacionListener.nuevaUbicacionListener(uVO);
                }
            }
        }

        if (ubicacionListenerForService != null) {

            ubicacionListenerForService.nuevaUbicacionListener(uVO);
        }
        // TODO Auto-generated method stub

    }

    public void setUbicacionListener(IUbicacionListener ubicacionListener) {
        this.ubicacionListener = ubicacionListener;
    }

    public void setUbicacionListenerForService(
            IUbicacionListener ubicacionListenerForService) {
        this.ubicacionListenerForService = ubicacionListenerForService;
    }


    public Location getLastLocationOnLine() {
        return mLocationClient.getLastLocation();
    }

    public UbicacionVO getUltimaUbicacion(long ttl, long esperar) {
        mLocationClient.setLastLocation();
        try {
            Thread.sleep(esperar);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getUltimaUbicacion(ttl);
    }

    /***/

    public UbicacionVO getUltimaUbicacionConTrack(long ttl, TipoTrackVO tipoTrackVO, Context ctx) {
        UbicacionVO uVO = getUltimaUbicacion(ttl);
        if (uVO != null) {
            BoxStore box = BoxStoreDB.getInstance(ctx).getBox();
            if (box == null) {
                try {
                    box = obtenerInstanciaDB(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (box != null) {
                Box<TrackVO> trackBox = box.boxFor(TrackVO.class);
                TrackVO myTrack = uVO.toTrackVO();
                myTrack.setTipoTrack(tipoTrackVO);
                myTrack.setFechaMovil(Utilidades.dateFormatSeguimiento.format(new Date()));
                myTrack.setBateria((double) org.kalla.enterprise.movil.util.Utilidades.getNivelBateria(ctx));
                myTrack.setDireccion(getDireccion(uVO));
                trackBox.put(myTrack);

                Log.i(TAG, "getUltimaUbicacionConTrack, id: " + myTrack.getId());
                uVO.setId(myTrack.getId());
            } else {
                Log.i(TAG, "No fue posible registrar ubicación");
                try {
                    Toast.makeText(ctx, "No fue posible registrar ubicación", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return uVO;
    }

    private BoxStore obtenerInstanciaDB(Context ctx) {
        BoxStore boxStore = BoxStoreDB.getInstance(ctx).getBox();
        if (boxStore != null) {
            return boxStore;
        } else {
            return obtenerInstanciaDB(ctx);
        }
    }

    public UbicacionVO getUltimaUbicacion(long ttl) {

        UbicacionVO uvo = mLocationClient.getSaverUbicacionVo();
        Log.i(TAG, "getUltimaUbicacion(ttl:" + ttl + ")" + uvo);
        if (ttl == -1) {
            return uvo;
        } else {
            mLocationClient.setLastLocation();

            if (uvo == null || ttl == 0) {
                Location l = mLocationClient.getLastLocation();
                uvo = mLocationClient.locatioToUbicacionVO(l, 0);
            } else {
                if (uvo != null && (uvo.getFecha() > (System.currentTimeMillis() - ttl))) {

                } else {

                    uvo = null;
                }
            }
        }
        Log.i(TAG, "getUltimaUbicacion return:" + uvo);
        return uvo;
    }

    //Check for location settings. if the location disabled prompt an alert dialog to redirect user.
    public void checkForLocationSettings(final Context ctx) {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);
            SettingsClient settingsClient = LocationServices.getSettingsClient(ctx);

            settingsClient.checkLocationSettings(builder.build())
                    .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            //Setting is success...
                            //Toast.makeText(ctx, "Enabled the Location successfully. Now you can press the buttons..", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(activity, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(activity, REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE);
                                    } catch (IntentSender.SendIntentException sie) {
                                        sie.printStackTrace();
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    Toast.makeText(ctx, R.string.configuraciones_no_diponibles, Toast.LENGTH_LONG).show();
                            }

                        }
                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkPlayServices(final Context ctx) {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(ctx);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(activity, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(ctx, ("This device is not supported."), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        mLocationClient.setLastLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        break;
                    default:
                        break;
                }
                break;

            case PLAY_SERVICES_REQUEST:

                break;
        }
    }


    public List<TrackVO> getTracksSinEnviar(Context context) {
        Box<TrackVO> trackBox = BoxStoreDB.getInstance(context).getBox().boxFor(TrackVO.class);
        Log.i(TAG, "getTracksSinEnviar, tracks BOX" + trackBox.count());
        return trackBox.getAll();
    }

    @Deprecated
    public void onStart() {
        iniciarSeguimientoPeriodico();
    }

    @Deprecated
    public void onStop() {
    }

    @Deprecated
    public void onResume() {
        detenerSeguimientoPeriodico();

    }

    @Deprecated
    public void onPause() {

    }

    public String getDireccion(UbicacionVO uvo) {
        Address locationAddress = mLocationClient.getAddress(uvo);
        String currentLocation = "";
        if (locationAddress != null) {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            if (!TextUtils.isEmpty(address)) {
                currentLocation = address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;

                if (!TextUtils.isEmpty(city)) {
                    currentLocation += "\n" + city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += " - " + postalCode;
                } else {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation += "\n" + postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation += "\n" + state;

                if (!TextUtils.isEmpty(country))
                    currentLocation += "\n" + country;


            }

        }

        return currentLocation;
    }
}
