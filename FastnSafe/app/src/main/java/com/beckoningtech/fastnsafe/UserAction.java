package com.beckoningtech.fastnsafe;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class UserAction{


    public String message;
    public String title;
    public boolean yourName;
    public boolean recipientNames;
    public boolean location;
    public boolean time;
    public boolean confirmationScreen;
    public boolean showOnNotificationBar;
    public int positionInGrid;
    public Image image;
    public ArrayList<Recipient> recipients;
    int type = 0;

    /*
              *  Types:
              *   0 = UserAction
              *   1 = CheckInAction
              *   2 = PanicAction
              *   3 = OnMyWayAction
             */
    public final static int CUSTOM_ACTION = 0;
    public final static int CHECK_IN_ACTION = 1;
    public final static int PANIC_ACTION = 2;
    public final static int ON_MY_WAY_ACTION = 3;
    public final static int CHECK_UP_ACTION = 4;

    public UserAction(){
        message = "Custom Message";
        title = "Custom Title";
        location = false;
        yourName = true;
        recipientNames = true;
        time = false;
        confirmationScreen = false;
        showOnNotificationBar = false;
        recipients = new ArrayList<>();

    }

    public UserAction(String message, String title, boolean yourName,
                      boolean recipientNames, boolean location, boolean time,
                      boolean confirmationScreen, boolean showOnNotificationBar,
                      int positionInGrid,  ArrayList<Recipient> recipients){
        this.message = message;
        this.title = title;
        this.yourName = yourName;
        this.recipientNames = recipientNames;
        this.location = location;
        this.time = time;
        this.confirmationScreen = confirmationScreen;
        this.showOnNotificationBar = showOnNotificationBar;
        this.positionInGrid = positionInGrid;
        this.recipients = recipients;
    }

    public void addRecipient(Recipient recipient){
        recipients.add(recipient);
    }

    public void removeRecipient(int index){
        recipients.remove(index);
    }

    public int getType(){
        return type;
    }
}