package com.passion.quakereport;

import android.net.Uri;

class EarthQuake {

    String magnitude;
    String title;
    Long time;
    Uri url;

    public EarthQuake( String magnitude, String title, Long time, Uri url ){
        this.magnitude = magnitude;
        this.title = title;
        this.time = time;
        this.url = url;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimeInMillis() {
        return time;
    }

    public Uri getUrl() {
        return url;
    }
}

