package com.cecs453.my_maps;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Michael on 11/18/2015.
 */
public class LocationContentProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.cecs453.my_maps";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/locations");
    private static final int LOCATIONS = 1;
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
    }

    @Override
    public boolean onCreate() {
        db = new LocationDB(getContext());
        return true;
    }

    LocationDB db;

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(uriMatcher.match(uri) == LOCATIONS) return db.getAllLocations();
        return null;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insertLocation(values);
        Uri insertUri = null;
        if(id > 0) insertUri = ContentUris.withAppendedId(CONTENT_URI, id);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.deleteAll();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }
}
