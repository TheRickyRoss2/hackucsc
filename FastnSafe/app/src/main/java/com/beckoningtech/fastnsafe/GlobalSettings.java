package com.beckoningtech.fastnsafe;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class GlobalSettings {
    SharedPreferences sharedPreferences;
    int mUserID;
    String name;
    public static ArrayList<UserAction> userActions;
    int numUserActions;
    private static GlobalSettings instance = null;
    Context context;

    /* TODO: Need to make a shared preference file for every user action and then store the values
       TODO:    of each user action in that file
     */
    protected GlobalSettings(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("name",0);
        name = sharedPreferences.getString("myName","William Jung");
        name = "William Jung";
        sharedPreferences = context.getSharedPreferences("numUserAction",0);
        numUserActions = sharedPreferences.getInt("num",0);
        if (numUserActions == 0){
            userActions = new ArrayList<>();
        } else {
            userActions = new ArrayList<>();
            initializeArrayList();
        }

    }
    public static GlobalSettings getInstance(Context context){
        if (instance==null){
            instance = new GlobalSettings(context);
        }
        return  instance;
    }

    private void initializeArrayList(){
        for (int i =0; i < numUserActions; i++){
            sharedPreferences = context.getSharedPreferences("userAction"+i,0);
            /*
              *  Types:
              *   0 = CustomAction
              *   1 = CheckInAction
              *   2 = PanicAction
              *   3 = OnMyWayAction
              *   4 = CheckUpAction
             */
            int type = sharedPreferences.getInt("type",0);
            UserAction tmp;
            switch (type){
                case UserAction.CUSTOM_ACTION:
                    tmp = new UserAction();
                    break;
                case UserAction.CHECK_IN_ACTION:
                    tmp = new CheckInAction();
                    CheckInAction tmp2 = (CheckInAction)tmp;
                    tmp2.fakePassword = sharedPreferences.getString("fakePassword","");
                    tmp2.password = sharedPreferences.getString("password","");
                    tmp2.passwordOn = sharedPreferences.getBoolean("passwordOn",false);
                    tmp2.fakePasswordOn = sharedPreferences.getBoolean("fakePasswordOn",false);
                    break;
                case UserAction.PANIC_ACTION:
                    tmp = new PanicAction();
                    break;
                case UserAction.ON_MY_WAY_ACTION:
                    tmp = new OnMyWayAction();
                    break;
                case UserAction.CHECK_UP_ACTION:
                    tmp = new CheckUpAction();
                    break;
                default:
                    tmp = new UserAction();
                    break;
            }

            tmp.message = sharedPreferences.getString("message","Custom Message");
            tmp.title = sharedPreferences.getString("title","Custom Title");
            tmp.yourName = sharedPreferences.getBoolean("yourName",true);
            tmp.recipientNames = sharedPreferences.getBoolean("recipientNames",true);
            tmp.location = sharedPreferences.getBoolean("location",false);
            tmp.time = sharedPreferences.getBoolean("time",false);
            tmp.confirmationScreen = sharedPreferences.getBoolean("confirmationScreen",false);
            tmp.showOnNotificationBar = sharedPreferences.getBoolean("showOnNotificationBar",false);
            tmp.positionInGrid = sharedPreferences.getInt("positionInGrid",0);
            //tmp.image = image;
            int numRecipients = sharedPreferences.getInt("numRecipients",0);
            tmp.recipients = new ArrayList<Recipient>();
            if (numRecipients > 0) {
                for(int j =0; j <numRecipients; j++){
                    String name = sharedPreferences.getString("recipient"+j+"name","");
                    int numNumbers = sharedPreferences.getInt("recipient"+j+"numNumbers",0);
                    ArrayList<String> numbers = new ArrayList<>();
                    if (numNumbers>0){
                        for (int k = 0; k<numNumbers; k++){
                            numbers.add(sharedPreferences.getString("recipient"+j+"number"+k,""));
                        }
                    }
                    int numEmails = sharedPreferences.getInt("recipient"+j+"numEmails",0);
                    ArrayList<String> emails = new ArrayList<>();
                    if (numEmails>0){
                        for (int k = 0; k<numEmails; k++){
                            emails.add(sharedPreferences.getString("recipient"+j+"email"+k,""));
                        }
                    }
                    tmp.recipients.add(new Recipient(name,numbers,emails));
                }
            }
            userActions.add(tmp);
        }
    }

    public void saveUserAction (int index){

        UserAction tmp = userActions.get(index);
        sharedPreferences = context.getSharedPreferences("userAction"+index,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("message",tmp.message);
        editor.putString("title",tmp.title);
        editor.putBoolean("yourName",tmp.yourName);
        editor.putBoolean("recipientNames",tmp.recipientNames);
        editor.putBoolean("location",tmp.location);
        editor.putBoolean("time",tmp.time);
        editor.putBoolean("confirmationScreen",tmp.confirmationScreen);
        editor.putBoolean("showOnNotificationBar",tmp.showOnNotificationBar);
        editor.putInt("positionInGrid",tmp.positionInGrid);
        editor.putInt("numRecipients",tmp.recipients.size());
        for (int j =0 ;j <tmp.recipients.size(); j++){
            Recipient recipient = tmp.recipients.get(j);
            editor.putString("recipient"+j+"name",recipient.name);
            editor.putInt("recipient"+j+"numNumbers",recipient.numbers.size());
            for (int k = 0; k<recipient.numbers.size();k++){
                editor.putString("recipient"+j+"number"+k,recipient.numbers.get(k));
            }
            editor.putInt("recipient"+j+"numEmails",recipient.emails.size());
            for (int k = 0; k<recipient.emails.size();k++){
                editor.putString("recipient"+j+"email"+k,recipient.emails.get(k));
            }
        }
        editor.putInt("type",tmp.getType());
        switch (tmp.getType()){

            case UserAction.CUSTOM_ACTION:
                break;
            case UserAction.CHECK_IN_ACTION:
                CheckInAction tmp2 = (CheckInAction) tmp;
                editor.putBoolean("passwordOn",tmp2.passwordOn);
                editor.putBoolean("fakePasswordOn",tmp2.fakePasswordOn);
                editor.putString("password",tmp2.password);
                editor.putString("fakePassword",tmp2.fakePassword);
                break;
            case UserAction.PANIC_ACTION:
                break;
            case UserAction.CHECK_UP_ACTION:
                break;
            case UserAction.ON_MY_WAY_ACTION:
                break;
            default:
                break;

        }
        editor.commit();

        sharedPreferences = context.getSharedPreferences("numUserAction",0);
        editor = sharedPreferences.edit();
        editor.putInt("num",userActions.size());
        editor.commit();

    }

    public void saveAllUserActions(){
        for (int i = 0; i<userActions.size();i++){
            saveUserAction(i);
        }
    }
}
