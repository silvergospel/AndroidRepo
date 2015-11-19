package com.cecs453.my_maps;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final LatLng LOCATION_UNIV = new LatLng(33.783768,-118.114336);
    private final LatLng LOCATION_ECS = new LatLng(33.783570, -118.110241);
    private static final int LOADER_ID = 1;

    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeMap();
        getLoaderManager().initLoader(LOADER_ID, null, this);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_ECS, 15));

    }

    private void initializeMap(){
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_ECS, 16);
        map.animateCamera(update);

        map.setMyLocationEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                drawMarker(latLng);
                new LocationInsertAsync().execute(generateContentValue(latLng));
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                map.clear();
                new LocationDeleteAsync().execute();
                Toast.makeText(MainActivity.this, "All markers cleared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ContentValues generateContentValue(LatLng latLng){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LocationDB.FIELD_LAT, latLng.latitude);
        contentValues.put(LocationDB.FIELD_LNG, latLng.longitude);
        contentValues.put(LocationDB.FIELD_ZOOM, map.getCameraPosition().zoom);
        return contentValues;
    }

    private void drawMarker(LatLng latLng){
        map.addMarker(new MarkerOptions().position(latLng));
    }

    public void onClick_ECS(View v){
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_ECS, 16);
        map.animateCamera(update);
    }

    public void onClick_Univ(View v){
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        map.animateCamera(update);
    }

    public void onClick_City(View v){
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 9);
        map.animateCamera(update);
    }

    private class LocationInsertAsync extends AsyncTask<ContentValues, Void, Void>
    {
        @Override
        protected Void doInBackground(ContentValues... params) {
            getContentResolver().insert(LocationContentProvider.CONTENT_URI, params[0]);
            return null;
        }
    }

    private class LocationDeleteAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            getContentResolver().delete(LocationContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    //LOADER IMPLEMENTATION
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = LocationContentProvider.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;

        // Number of locations available in the SQLite database table
        locationCount = data.getCount();

        // Move the current record pointer to the first row of the table
        data.moveToFirst();

        for(int i=0;i<locationCount;i++){

            // Get the latitude
            lat = data.getDouble(data.getColumnIndex(LocationDB.FIELD_LAT));

            // Get the longitude
            lng = data.getDouble(data.getColumnIndex(LocationDB.FIELD_LNG));

            // Get the zoom level
            zoom = data.getFloat(data.getColumnIndex(LocationDB.FIELD_ZOOM));

            // Creating an instance of LatLng to plot the location in Google Maps
            LatLng location = new LatLng(lat, lng);

            // Drawing the marker in the Google Maps
            drawMarker(location);

            // Traverse the pointer to the next row
            data.moveToNext();
        }
        //data.moveToFirst();
        //iterate through result set
        //do
        //{
        //    drawMarker(new LatLng(data.getColumnIndex(LocationDB.FIELD_LAT), data.getColumnIndex(LocationDB.FIELD_LNG)));
        //}while(data.moveToNext());
        //move to first entries location
        if(data.getCount() > 0){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
            Toast.makeText(MainActivity.this, "Number of entries = " + data.getCount(), Toast.LENGTH_SHORT).show();
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_ECS, 10));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
