package com.passion.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthQuake>> {

    private final String LOG = EarthquakeLoader.class.getName();
    String url;

    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList<EarthQuake> loadInBackground() {
        ArrayList<EarthQuake> list  = null;

        try{
            list = QuakeUtils.makeRequestOfEarthQuakeData(url);
        }
        catch(IOException e){
            Log.v(LOG + "Exception" , e.getMessage());
        }

        return list;
    }
}
