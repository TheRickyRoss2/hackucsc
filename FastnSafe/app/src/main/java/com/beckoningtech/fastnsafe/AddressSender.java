package com.beckoningtech.fastnsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.beckoningtech.fastnsafe.SocketClient.sendMessage;

// AddressSender.class
// Desc: Used as an asynctask to send location data to server
// Req: Requires an instant to run .execute()
//      Requires a context to access user's data

public class AddressSender extends AsyncTask<Void, Void, Void> implements
        ConnectionCallbacks, OnConnectionFailedListener {


    // Need a private class that extends broadcastreceiver so it can shut off the connection when
    // when the user disconnects
    private class TestForDC extends BroadcastReceiver{
        // client to disconnect and connect
        GoogleApiClient googleClient;
        // Should connect us when the bg activity starts
        TestForDC(GoogleApiClient input){
            googleClient = input;
            googleClient.connect();
        }

        // Disconnect us when onStop in non bg activity happens
        @Override
        public void onReceive(Context context, Intent intent) {
            googleClient.disconnect();
        }
    }

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;
    public static double mLatitude;
    public static double mLongitude;
    private Boolean failed = false;
    private static Boolean alreadyRan = false; // determine if initialization has already been done

    AddressSender(Context context) {
        mContext = context;
        failed = false;
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } catch (SecurityException e) {
            failed = true;
        }
        if (mLastLocation != null) {
            mLatitude = mLastLocation.getLatitude();
            mLongitude = mLastLocation.getLongitude();
        }
//        String out = convertToAddress(mLongitude,mLatitude);
        String out = "pushLoc;5555234259;" + convertToAddress(mLongitude,mLatitude) + ";\n";
        // For demo purposes instead of using convertToAddr we shall use the string above
        Log.d("HackUCSC", out);
        sendMessage(out, false, mContext);
    }

    public String convertToAddress(double longitude, double latitude) {

        Log.d("convertToAddress!", "entered");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            return new String("Couldn't find close address, user at longitude: " + longitude + " latitude: " + latitude);

        }
        if (addresses.size()==0){
            return new String("Couldn't find close address, user at longitude: " + longitude + " latitude: " + latitude);
        }
        return addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    }

    @Override
    public void onConnectionSuspended(int cause) {
        failed = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        failed = true;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        if(!alreadyRan) {
            // Initialize all the variables
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            new TestForDC(mGoogleApiClient);
            alreadyRan=true;
        }
        return null;
    }
}

