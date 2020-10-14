package com.passion.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>> {

    private static final String LOG = MainActivity.class.getName();

    /** Sample JSON response for a USGS query */
    private static final String URL_QUERY = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=15";

    ListView listView = null;
    EarthquakeAdapter adapter =null;
    TextView emptyView = null;



    private final int Loader_Id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // configure list and adapter
        listView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        adapter = new EarthquakeAdapter(this, new ArrayList<EarthQuake>());
        listView.setAdapter( adapter );

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(this.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if( networkInfo != null && networkInfo.isConnected() ){



            // get the Earthquake data form json response
            getSupportLoaderManager().initLoader(Loader_Id, null, this );
        }
        else{

            emptyView.setText("No Internet Connection");

        }


    }

    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int id,  Bundle args) {
        return new EarthquakeLoader(this, URL_QUERY);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> earthQuakes) {

        // progress
        View progressIndicator = findViewById(R.id.progress_bar);
        progressIndicator.setVisibility(View.GONE);



        // clear the adapter data
        adapter.clear();

        if( earthQuakes != null ){
            adapter.addAll( earthQuakes );
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent browserIntent = new Intent( Intent.ACTION_VIEW);
                    browserIntent.setData( adapter.getItem(i).getUrl() );
                    startActivity(browserIntent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<EarthQuake>> loader) {
        adapter.clear();
    }
}
