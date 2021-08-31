package org.kalla.enterprise.movil.gps.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.kalla.enterprise.movil.gps.com.vo.TrackVO;
import org.kalla.enterprise.movil.gps.util.Utilidades;

import java.util.Date;

public class UbicacionVO implements Parcelable {
    // Instance variables

    private final double mLatitude;
    private final double mLongitude;
    private final float mPresision;
    private final long mfecha;
    private final String mVelocidad;
    private final float mSentido;
    private final String mTelefono;
    private final long mfechaUltimaUbicacion;
    private long id;

    /**
     * @param latitude  Latitude of the Geofence's center.
     * @param longitude Longitude of the Geofence's center.
     *                  <p>
     *                  expiration
     *                  Geofence expiration duration
     *                  transition
     *                  Type of Geofence transition.
     */

    public UbicacionVO(double latitude, double longitude) {
        // Set the instance fields from the constructor
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mPresision = 0;
        this.mfecha = 0;
        this.mVelocidad = null;
        this.mSentido = 0;
        this.mTelefono = "";
        this.mfechaUltimaUbicacion = 0;
    }

    public UbicacionVO(double latitude, double longitude, float presision, long fecha, String velocidad, float sentido, String telefono, long mfechaUltimaUbicacion) {
        // Set the instance fields from the constructor

        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mPresision = presision;
        this.mfecha = fecha;
        this.mVelocidad = velocidad;
        this.mSentido = sentido;
        this.mTelefono = telefono;
        this.mfechaUltimaUbicacion = mfechaUltimaUbicacion;
    }

    public long getFecha() {
        return mfecha;
    }

    // Instance field getters

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public float getmPresision() {
        return mPresision;
    }

    public String getmVelocidad() {
        return mVelocidad;
    }


    public float getmSentido() {
        return mSentido;
    }


    public long getMfechaUltimaUbicacion() {
        return mfechaUltimaUbicacion;
    }

    /**
     * Creates a Location Services Geofence object from a SimpleGeofence.
     *
     * @return A Geofence object
     */

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        // private final double mLatitude;
        // private final double mLongitude;
        // private final float mPresision;
        // private final long mfecha;
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeFloat(mPresision);
        dest.writeLong(mfecha);
        dest.writeString(mVelocidad);
        dest.writeFloat(mSentido);
        dest.writeString(mTelefono);
    }

    public static final Parcelable.Creator<UbicacionVO> CREATOR = new Parcelable.Creator<UbicacionVO>() {
        public UbicacionVO createFromParcel(Parcel in) {
            double mLatitude = in.readDouble();
            double mLongitude = in.readDouble();
            float mPresision = in.readFloat();
            long mfecha = in.readLong();
            String mVelocidad = in.readString();
            float mSentido = in.readFloat();
            String mTelefono = in.readString();
            long mfechaUltimaUbicacion = in.readLong();
            return new UbicacionVO(mLatitude, mLongitude, mPresision, mfecha, mVelocidad, mSentido, mTelefono, mfechaUltimaUbicacion);
        }

        public UbicacionVO[] newArray(int size) {
            return new UbicacionVO[size];
        }
    };

    public String toString() {
        return "Lat:" + mLatitude + "Long:" + mLongitude + "Presision:" + mPresision + "date:" + (new Date(mfecha)).toLocaleString() + "vel:" + mVelocidad + "sent:" + mSentido + "tel:" + mSentido;
    }

    public TrackVO toTrackVO() {
        TrackVO trackVO = new TrackVO();
        trackVO.setLatitudPosicion(mLatitude);
        trackVO.setLongitudPosicion(mLongitude);
        trackVO.setPresicion((double) mPresision);
        trackVO.setFechaCapturaGps(Utilidades.dateFormatSeguimiento.format(new Date(mfecha)));
        trackVO.setOrientacion((double) mSentido);
        return trackVO;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


}
