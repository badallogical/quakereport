package com.passion.quakereport;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

public class EarthquakeAdapter extends ArrayAdapter<EarthQuake> {


    public EarthquakeAdapter(Context context, ArrayList<EarthQuake> list) {
        super(context, 0, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create the layout of the item
        if( convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false );
        }

        // fill it with data
        EarthQuake currentEarthquake = getItem(position);

        // Magnitude
        TextView magnitude = (TextView) convertView.findViewById(R.id.magnitude);

        // formatter decimal places to one only
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        magnitude.setText(  decimalFormatter.format( Double.parseDouble( currentEarthquake.getMagnitude() ) ) );

        // set the relevant color on the magnitude background based on its value
        GradientDrawable magBackground = (GradientDrawable)magnitude.getBackground();
        magBackground.setColor( getMagnitudeColor( Double.parseDouble( currentEarthquake.getMagnitude())));

        // Show place by, splitting the title in two lines with offset and primary string
        String place = currentEarthquake.getTitle();
        String [] places_byPart = place.split("of");

        TextView offset_place = (TextView) convertView.findViewById(R.id.offset);
        TextView primary_location = (TextView) convertView.findViewById(R.id.primary_location);

        if( places_byPart.length == 1 ){
            offset_place.setText( "Near the " );
            primary_location.setText(places_byPart[0].trim());
        }
        else{
            offset_place.setText( places_byPart[0] );
            primary_location.setText( places_byPart[1].trim());
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        // get formatter data time
        Date date_time = new Date( currentEarthquake.getTimeInMillis() );
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

        // set the formatted data and time using data object with formatter
        date.setText( formatter.format(date_time)  );

        // set time
        TextView time = (TextView) convertView.findViewById(R.id.time);
        formatter = new SimpleDateFormat("hh:mm a");
        time.setText( formatter.format(date_time) );

        return convertView;
    }

    private int getMagnitudeColor(double mag) {
        // color id
        int colorId;

        // upper limit exclusive
        switch( (int)Math.floor( mag ) ){

            case 0:
            case 1: colorId = R.color.magnitude1;
            break;
            case 2: colorId = R.color.magnitude2;
            break;
            case 3: colorId = R.color.magnitude3;
            break;
            case 4: colorId = R.color.magnitude4;
            break;
            case 5: colorId = R.color.magnitude5;
            break;
            case 6: colorId = R.color.magnitude6;
            break;
            case 7: colorId = R.color.magnitude7;
            break;
            case 8: colorId = R.color.magnitude8;
            break;
            case 9: colorId = R.color.magnitude9;
            break;
            case 10:
            default:colorId = R.color.magnitude10plus;
        }

        // fetch the color by id
        return ContextCompat.getColor( getContext(), colorId );
    }
}
