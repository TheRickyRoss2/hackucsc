package com.beckoningtech.fastnsafe;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

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

//    public void sendSMS(Context mContext, String phoneNumber, String message) {
//        if (smsManager == null){
//            smsManager = SmsManager.getDefault();
//            if (smsManager == null){
//                Toast.makeText(mContext, "SMS sending failed. Please try again.",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        this.phoneNumber = phoneNumber;
//        System.out.println("Sending text to: "+phoneNumber);
//        this.message = message;
//
//        ArrayList<String> parts = smsManager.divideMessage(message);
//        ArrayList<PendingIntent> sentIntents=new ArrayList<>();
//        Intent tmpIntent = new Intent(mContext, SmsSentReceiver.class);
//        tmpIntent.putExtra("phoneNumber",phoneNumber);
//        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
//                tmpIntent, 0);
//        for (String str : parts){
//            sentIntents.add(sentPI);
//        }
//        smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message),
//                sentIntents, null);
//    }


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
            if (smsManager == null){
                smsManager = SmsManager.getDefault();
                if (smsManager == null){
                    Toast.makeText(activity, "SMS sending failed. Please try again.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            this.phoneNumber = phoneNumber;
            Toast.makeText(activity,
                    "Sending SMS to: " + phoneNumber,
                    Toast.LENGTH_SHORT).show();
            this.message = message;

            ArrayList<String> parts = smsManager.divideMessage(message);
            ArrayList<PendingIntent> sentIntents=new ArrayList<>();
            Intent tmpIntent = new Intent(activity, SmsSentReceiver.class);
            tmpIntent.putExtra("phoneNumber",phoneNumber);
            PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0,
                    tmpIntent, 0);
            for (String str : parts){
                sentIntents.add(sentPI);
            }
            smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message),
                    sentIntents, null);

        }
    }

    /*
    // This method is part of Activity.
    @Override
    public void onRequestPermissionResult(int requestCode, String permission[], int[]grantResults){

    }*/
}

