package com.beckoningtech.fastnsafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new AddressSender(this).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("MainActivity", "created AddressSender");
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        Log.d("MainActivity","started MainMenuActivity");
    }

    @Override
    public void onDestroy(){
        Intent intent = new Intent();
        intent.setAction("com.beckoningtech.fastnsafe.STOPPING");
        sendBroadcast(intent);
        super.onDestroy();
    }
}
