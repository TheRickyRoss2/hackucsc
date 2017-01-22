package com.beckoningtech.fastnsafe;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class PanicAction extends UserAction {

    String defaultMessage ="Hi. I need help.\n";
    String defaultTitle = "Panic";

    public PanicAction(String message, String title, boolean yourName,
                         boolean recipientNames, boolean location, boolean time,
                         boolean confirmationScreen, boolean showOnNotificationBar,
                         int positionInGrid, ArrayList<Recipient> recipients ){
        super( message,  title,  yourName, recipientNames, location, time, confirmationScreen,
                showOnNotificationBar, positionInGrid,  recipients);
        type =  UserAction.PANIC_ACTION;
    }
    public PanicAction(){
        super();
        type = UserAction.PANIC_ACTION;
        location = true;
        title = defaultTitle;
        message = defaultMessage;
    }
}
