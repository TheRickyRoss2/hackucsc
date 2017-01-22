package com.beckoningtech.fastnsafe;

/**
 * Created by User Account on 1/21/2017.
 */

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import static com.beckoningtech.fastnsafe.NotificationAdder.addNotification;

public class SocketClient {
    private Socket socket;
    private static final int PORTNUMBER = 9090;
    private static final String HOSTIP = "130.211.136.126";

    public static String serverResponse;
    private static String test;
    private static Boolean fail;
    private static Boolean wantLocation;
    private static Context mContext;

    // returns true if message sent successfully, false if not
    public static boolean sendMessage(String in, Boolean promptLocation, Context context) {
        mContext = context;
        wantLocation = promptLocation;
        test = in;
        fail = false;
        new Thread(new Runnable(){
            public void run(){
                Socket clientSocket = null;
                try {
                    clientSocket = new Socket(HOSTIP, PORTNUMBER);
                    Log.d("socketcliententered","afterSocket");
                } catch (IOException e1) {
                    try {clientSocket.close();} catch(IOException e) {}
                    Log.d("socketcliententered","oops server nonexistant");
                }

                String modifiedSentence = new String("");
                try {
                    BufferedReader inFromUser = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
                } catch (IOException e) {
                    Log.d("socketcliententered","Fail1");
                    e.printStackTrace();
                }
                DataOutputStream outToServer = null;
                try {
                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
                } catch (IOException e) {
                    Log.d("socketcliententered","Fail2");
                    e.printStackTrace();
                }
                BufferedReader inFromServer = null;
                try {
                    inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                } catch (IOException e) {
                    Log.d("socketcliententered","Fail3");
                    e.printStackTrace();
                }

                try {
                    outToServer.writeBytes(test);
                } catch (IOException e) {
                    Log.d("socketcliententered","Fail4");
                    e.printStackTrace();
                }
                try {
                    modifiedSentence = new String(inFromServer.readLine());
                    serverResponse = modifiedSentence;
                    clientSocket.close();
                    // if the user wants the response cuz it contains desired info show in noti
                    if (wantLocation) {
                        addNotification(MainActivity.class,12,mContext,"Kid is here:", serverResponse);
                    }
                } catch (IOException e) {
                    Log.d("socketcliententered","Fail5");
                    e.printStackTrace();
                }

            }
        }).start();

        if (fail == true){
            return false;
        } else {
            return true;
        }
    }

}
