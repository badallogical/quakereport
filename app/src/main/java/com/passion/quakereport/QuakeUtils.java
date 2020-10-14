package com.passion.quakereport;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class QuakeUtils {

    private final static String LOG = QuakeUtils.class.getName();

   public static ArrayList<EarthQuake> makeRequestOfEarthQuakeData(String url_requst ) throws IOException{
       ArrayList<EarthQuake> list = new ArrayList<EarthQuake>();
       String jsonResponse = "";

       if(TextUtils.isEmpty( url_requst) ){
           return null;
       }

       //create URL
       URL url = createURL(url_requst);

       // make HTTP request and get Response
       jsonResponse = makeHttpRequest(url);

       // parse the ArrayList<EarthQuake> from jsonResponse
       list = extractEarthquakes(jsonResponse);

       return list;
   }

   public static String makeHttpRequest(URL url_request ) throws IOException {

       String jsonResponse = "";
       HttpURLConnection urlConnection = null;
       InputStream inputStream = null;

       if( url_request == null ){
           return jsonResponse;
       }


       try{
           // create HttpRequest
           urlConnection =  (HttpURLConnection) url_request.openConnection();
           urlConnection. setRequestMethod("GET");
           urlConnection.setReadTimeout(10000);
           urlConnection.setConnectTimeout(15000);
           urlConnection.connect();

           //check if successfull
           if( urlConnection.getResponseCode() == 200 ){
               // fetch data from inputStream
               inputStream = urlConnection.getInputStream();
               jsonResponse = fetchJsonResponseAsString(inputStream);
           }
           else{
               Log.v(LOG + "Problem" , "Url Connection response code is bad " + urlConnection.getResponseCode());
               return null;
           }

       }
       catch(IOException e){
           Log.v(LOG + "Exception", e.getMessage());
       }
       finally {
           if( urlConnection != null )
               urlConnection.disconnect();

           if( inputStream != null )
               inputStream.close();
       }

       return jsonResponse;
   }

    private static String fetchJsonResponseAsString(InputStream inputStream) {
        StringBuilder jsonResponse = new StringBuilder();

       if( inputStream == null )
           return jsonResponse.toString();

       try {

           // take the sequence of bytes to read byte by byte with InputStream reader which convert the binary to character
           InputStreamReader Ireader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

           // fetch the big chunch or read the big chunch of data from Ireader as sequence of words as Line.
           BufferedReader Breader = new BufferedReader(Ireader);

           String line = Breader.readLine();
           while( line != null ){
               jsonResponse.append(line);
               line = Breader.readLine();
           }
       }
       catch(IOException e){
           Log.v(LOG + "Exception" , e.getMessage());
       }

       return jsonResponse.toString();
    }

    public static URL createURL(String url_request){
        URL url = null;
        try{
            url = new URL(url_request);
        }
        catch( MalformedURLException e){
            Log.v(LOG + "Exception", e.getMessage() );
        }

        return url;
    }

    public static ArrayList<EarthQuake> extractEarthquakes(String jsonResponse){
        ArrayList<EarthQuake> list = new ArrayList<EarthQuake>();
        EarthQuake earthQuakeData = null;

        try {
            // json parsing
            JSONObject jsonResponceObj = new JSONObject(jsonResponse);

            // fetch features
            JSONArray features = jsonResponceObj.getJSONArray("features");

            // iterate to all features ( earthQuake data )
            for( int i = 0; i < features.length(); i++ ){
                // fetch the properties object
                JSONObject currentEarrthQuakeData = features.getJSONObject(i);
                JSONObject properties = currentEarrthQuakeData.getJSONObject("properties");

                //fetch mag, place and time
                Long time = properties.getLong("time");

                earthQuakeData = new EarthQuake(properties.getString("mag"), properties.getString("place"), time , Uri.parse( properties.getString("url")));

                // add parse data of earthQuake to list
                list.add( earthQuakeData );
            }
        }
        catch( JSONException e ){
            Log.v( LOG, e.getMessage() );
        }

        return list;
    }
}
