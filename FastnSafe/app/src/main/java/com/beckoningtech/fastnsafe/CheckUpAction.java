package com.beckoningtech.fastnsafe;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class CheckUpAction extends UserAction {


    String defaultMessage ="Hi. Are you okay?\n";
    String defaultTitle = "Check Up";

    public CheckUpAction(String message, String title, boolean yourName,
                       boolean recipientNames, boolean location, boolean time,
                       boolean confirmationScreen, boolean showOnNotificationBar,
                       int positionInGrid, ArrayList<Recipient> recipients ){
        super( message,  title,  yourName, recipientNames, location, time, confirmationScreen,
                showOnNotificationBar, positionInGrid,  recipients);
        type =  UserAction.CHECK_UP_ACTION;
    }
    public CheckUpAction(){
        super();
        type = UserAction.CHECK_UP_ACTION;
        location = false;
        title = defaultTitle;
        message = defaultMessage;
    }
}
