package com.beckoningtech.fastnsafe;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class EditUserActionActivity extends AppCompatActivity {

    UserAction current;
    int position;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_action);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("currentUserAction",0);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        current = GlobalSettings.getInstance(this).userActions.get(position);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        registerForContextMenu(scrollView);
        EditText tmp;
        if (current.title != ""){
            tmp = (EditText) findViewById(R.id.user_action_title);
            tmp.setText(current.title);
        }

        if (current.recipients.size() >0){
            tmp = (EditText) findViewById(R.id.recipient);
            tmp.setText(current.recipients.get(0).name);
            tmp = (EditText) findViewById(R.id.recipient_email);
            tmp.setText(current.recipients.get(0).emails.get(0));
            tmp = (EditText) findViewById(R.id.recipient_number);
            tmp.setText(current.recipients.get(0).numbers.get(0));
        } else{
            current.recipients = new ArrayList<>();
            ArrayList<String> number = new ArrayList<>();
            number.add("");
            ArrayList<String> email = new ArrayList<>();
            email.add("");
            current.recipients.add(new Recipient("",number,email));
        }
        CheckBox checkBox;
        checkBox = (CheckBox) findViewById(R.id.your_name);
        checkBox.setChecked(current.yourName);
        checkBox = (CheckBox) findViewById(R.id.recipient_name);
        checkBox.setChecked(current.recipientNames);
        checkBox = (CheckBox) findViewById(R.id.time_date);
        checkBox.setChecked(current.time);
        checkBox = (CheckBox) findViewById(R.id.location);
        checkBox.setChecked(current.location);
        checkBox = (CheckBox) findViewById(R.id.show_on_notification_bar);
        checkBox.setChecked(current.showOnNotificationBar);

        checkBox = (CheckBox) findViewById(R.id.confirmation_screen);
        checkBox.setChecked(current.confirmationScreen);
        if(current.message!="") {
            tmp = (EditText) findViewById(R.id.user_message);
            tmp.setText(current.message);
        }
    }

    @Override
    public void onBackPressed()
    {
        scrollView.showContextMenu();
        // code here to show dialog
        //super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_edit_user_action, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent resultIntent;
        switch (item.getItemId()) {
            case R.id.discard_changes:
                resultIntent = new Intent();
                setResult(1, resultIntent);
                finish();

                return  true;
            case R.id.save_changes:
                saveChanges();
                resultIntent = new Intent();
                setResult(2, resultIntent);
                finish();
                return  true;
            case R.id.cancel:
                super.onBackPressed();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void saveChanges(){
        EditText tmp;
        tmp = (EditText) findViewById(R.id.user_action_title);
        current.title=tmp.getText().toString();

        tmp = (EditText) findViewById(R.id.recipient);
        current.recipients.get(0).name=tmp.getText().toString();
        tmp = (EditText) findViewById(R.id.recipient_email);
        current.recipients.get(0).emails.set(0,tmp.getText().toString());
        tmp = (EditText) findViewById(R.id.recipient_number);
        current.recipients.get(0).numbers.set(0,tmp.getText().toString());

        CheckBox checkBox;
        checkBox = (CheckBox) findViewById(R.id.your_name);
        current.yourName = checkBox.isChecked();
        checkBox = (CheckBox) findViewById(R.id.recipient_name);
        current.recipientNames=checkBox.isChecked();
        checkBox = (CheckBox) findViewById(R.id.time_date);
        current.time=checkBox.isChecked();
        checkBox = (CheckBox) findViewById(R.id.location);
        current.location=checkBox.isChecked();
        checkBox = (CheckBox) findViewById(R.id.show_on_notification_bar);
        current.showOnNotificationBar = checkBox.isChecked();

        checkBox = (CheckBox) findViewById(R.id.confirmation_screen);
        current.confirmationScreen=checkBox.isChecked();
        tmp = (EditText) findViewById(R.id.user_message);
        current.message=tmp.getText().toString();
        GlobalSettings.getInstance(this).saveUserAction(position);
    }
}
