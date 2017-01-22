package com.beckoningtech.fastnsafe;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

/**
 * Created by William on 1/21/2017.
 */

public class SMSSender {
    String phoneNumber, message;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    SmsManager smsManager;

    public SMSSender() {
        smsManager = SmsManager.getDefault();
    }

    public void sendSMS(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        System.out.println("Sending text to: "+phoneNumber);
        this.message = message;

        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message),
                null, null);
    }


    // This should only be called when the activity can handle onRequestPermissionResult().
    public void sendSMS(Activity activity, String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message),
                    null, null);
        }
    }

    /*
    // This method is part of Activity.
    @Override
    public void onRequestPermissionResult(int requestCode, String permission[], int[]grantResults){

    }*/
}
