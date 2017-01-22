package com.beckoningtech.fastnsafe;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class CheckInAction extends UserAction {
    String defaultMessage ="Hi. I am okay.\n";
    String defaultTitle = "Check In";
    public String password, fakePassword;
    public boolean passwordOn, fakePasswordOn;

    public CheckInAction(String message, String title, boolean yourName,
                      boolean recipientNames, boolean location, boolean time,
                      boolean confirmationScreen, boolean showOnNotificationBar,
                      int positionInGrid, ArrayList<Recipient> recipients,boolean passwordOn,
                      boolean fakePasswordOn){
        super( message,  title,  yourName, recipientNames, location, time, confirmationScreen,
                showOnNotificationBar, positionInGrid,  recipients);
        this.passwordOn = passwordOn;
        this.fakePasswordOn = fakePasswordOn;
        type =  UserAction.CHECK_IN_ACTION;

    }

    public CheckInAction(){
        super();
        this.message = defaultMessage;
        this.title = defaultTitle;
        passwordOn = false;
        fakePasswordOn = false;
        password = "";
        fakePassword ="";
        type =  UserAction.CHECK_IN_ACTION;
        location = false;

    }
}
