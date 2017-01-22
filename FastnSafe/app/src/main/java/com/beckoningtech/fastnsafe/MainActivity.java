package com.beckoningtech.fastnsafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AddressSender(this).execute();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        Intent intent = new Intent();
        intent.setAction("com.beckoningtech.fastnsafe.STOPPING");
        sendBroadcast(intent);
        super.onDestroy();
    }
}
