package com.dickies.android.relationbn.utils;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

/**
 * Created by Phil on 28/09/2018.
 */

public class UserLocation {


    MarkerOptions marker;
    Marker mark;

    public UserLocation() {

    }

    public MarkerOptions getMarker() {
        return marker;
    }

    public void setMarker(MarkerOptions marker) {
        this.marker = marker;
    }

    public Marker getMark() {
        return mark;
    }

    public void setMark(Marker mark) {
        this.mark = mark;
    }




}
