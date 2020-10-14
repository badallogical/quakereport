package com.passion.quakereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String LOG = MainActivity.class.getName();

    /** Sample JSON response for a USGS query */
    private static final String URL_QUERY = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    ListView listView = null;
    EarthquakeAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get the Earthquake data form json response
        EarthQuakeAsyncRequest earthQuakeAsyncRequest = new EarthQuakeAsyncRequest();
        earthQuakeAsyncRequest.execute(URL_QUERY);
    }



    private class EarthQuakeAsyncRequest extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        @Override
        protected void onPreExecute() {
            listView = findViewById(R.id.list);
        }

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... jsonRequest) {
            ArrayList<EarthQuake> list = null;
            if( jsonRequest.length < 1 || jsonRequest[0] == null )
                return null;

            // make HttpRequest and get the EarthQuake data list
            try {
                list = QuakeUtils.makeRequestOfEarthQuakeData(jsonRequest[0]);
            }
            catch (IOException e){
                Log.v( LOG + "Exception" , e.getMessage() );
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            if( earthQuakes == null ){
                return;
            }

            // fill the earthQuake data to list
            updateUI(earthQuakes);
        }
    }

    private void updateUI( ArrayList<EarthQuake> list ){
        // put data into list
        adapter = new EarthquakeAdapter(MainActivity.this, list );
        listView.setAdapter( adapter );

        final ArrayList<EarthQuake> innerHelperList = list;
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentBrowser = new Intent( Intent.ACTION_VIEW);
                intentBrowser.setData( innerHelperList.get(i).getUrl() );
                startActivity(intentBrowser);
            }
        });
    }
}
