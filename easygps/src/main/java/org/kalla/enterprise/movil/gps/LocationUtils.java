package org.kalla.enterprise.movil.gps;



import android.content.Context;
import android.location.Location;

import com.kalla.suite_easygps.R;

import java.util.Calendar;
import java.util.Date;


/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils {

    // Debugging tag for the application
    public static final String APPTAG = "LocationSample";

    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "com.example.android.location.SHARED_PREFERENCES";

    public static final String PREFERENCES_SEGUIMIENTO =
            "org.kalla.enterprise.movil.gps.SEGUIMIENTO_PREFERENCES";

    public static final String PREFERENCES_SEGUIMIENTO_TRIGER =
            "org.kalla.enterprise.movil.gps.SEGUIMIENTO_TRIGER_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED =
            "com.example.android.location.KEY_UPDATES_REQUESTED";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 30;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;

    public static final long UPDATE_INTERVAL_IN_ONE_MINUTE = MILLISECONDS_PER_SECOND*60;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // Create an empty string for initializing strings
    public static final String EMPTY_STRING = new String();

    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
    public static String getLatLng(Context context, Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {

            // Return the latitude and longitude as strings
            return context.getString(R.string.latitude_longitude,
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
        } else {

            // Otherwise, return the empty string
            return EMPTY_STRING;
        }
    }

    /**
     * Este metodo obtiene la fecha sin tener en cuenta los milisegundos para efectos de comparaciones
     * @author Eliana Andrea Concha Agredo
     * @param date - Fecha a formatear
     * @return long     *
     */
    public static long getDateWithoutMilliseconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

}
