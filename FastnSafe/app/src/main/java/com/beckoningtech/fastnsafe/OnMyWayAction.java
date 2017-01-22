package com.beckoningtech.fastnsafe;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class OnMyWayAction extends UserAction {

    String defaultMessage ="Hi. I am on my way.\n";
    String defaultTitle = "On My Way";

    public OnMyWayAction(String message, String title, boolean yourName,
                       boolean recipientNames, boolean location, boolean time,
                       boolean confirmationScreen, boolean showOnNotificationBar,
                       int positionInGrid, ArrayList<Recipient> recipients ){
        super( message,  title,  yourName, recipientNames, location, time, confirmationScreen,
                showOnNotificationBar, positionInGrid,  recipients);
        type =  UserAction.ON_MY_WAY_ACTION;
    }
    public OnMyWayAction(){
        super();
        type = UserAction.ON_MY_WAY_ACTION;
        location = true;
        message = defaultMessage;
        title = defaultTitle;
    }
}
