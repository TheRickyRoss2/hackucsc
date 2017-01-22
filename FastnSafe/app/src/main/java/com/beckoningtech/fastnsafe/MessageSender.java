package com.beckoningtech.fastnsafe;

import android.content.Context;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by William on 1/21/2017.
 */

public class MessageSender {
    SMSSender smsSender;
    public static MessageSender instance;
    Context context;
    protected MessageSender(Context context){
        smsSender = new SMSSender();
        this.context = context;

    }
    public static MessageSender getInstance(Context context){
        if (instance==null){
            instance = new MessageSender(context);
        }
        return  instance;
    }

    public void sendMessage(UserAction userAction){
        StringBuilder stringBuilder = new StringBuilder();
        if(userAction.recipientNames){
            stringBuilder.append("To: ").append(userAction.recipients.get(0).name).append('\n');
        }
        if(userAction.yourName){
            stringBuilder.append("From: ").append(GlobalSettings.getInstance(context).name).append('\n');
        }
        if(userAction.time){
            Calendar calendar = Calendar.getInstance();
            stringBuilder.append("Time Sent: ").append(calendar.getTime().toString()).append('\n');
        }
        if (userAction.location){
            AddressSender addressSender = new AddressSender(context);
            String location = addressSender.convertToAddress(addressSender.mLongitude,
                    addressSender.mLatitude);
            stringBuilder.append("Location: ").append(location).append('\n');
        }
        stringBuilder.append(userAction.message);
        System.out.println("Message: "+stringBuilder.toString());
        String number =userAction.recipients.get(0).numbers.get(0);
        System.out.println("Number: "+number);
        if (number!=""){
            smsSender.sendSMS(number,stringBuilder.toString());
        }
        String email =userAction.recipients.get(0).emails.get(0);
        if (email!=""){
            try {
                Socket client = new Socket("104.236.164.19",5050);

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: SEND EMAIL KEVIN STUFF
        }
    }

}
