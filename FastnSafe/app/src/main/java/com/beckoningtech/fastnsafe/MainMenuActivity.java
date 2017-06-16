package com.beckoningtech.fastnsafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static com.beckoningtech.fastnsafe.AddressSender.mLatitude;
import static com.beckoningtech.fastnsafe.AddressSender.mLongitude;
import static com.beckoningtech.fastnsafe.SocketClient.sendMessage;

public class MainMenuActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    GridView gridView;
    ArrayList<UserAction> userActions;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mContext=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_menu_toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.main_menu_grid_view);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int columnWidth = Math.min(width/2, height/2) - 20;
        gridView.setColumnWidth(columnWidth );

        userActions = GlobalSettings.getInstance(this).userActions;

        gridView.setAdapter(new MainMenuUserActionAdapter(this,
                userActions,columnWidth));
        registerForContextMenu(gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position==userActions.size()){
                    v.showContextMenu();
                } else{
                    sendMessage(position);
                    UserAction userAction = getUserAction(position);
                    if (userAction instanceof CheckInAction){
                        //TODO: checkin pushes location
                        AddressSender addressSender = new AddressSender(mContext);
                        String location = addressSender.convertToAddress(mLongitude, mLatitude);
                        SocketClient.sendMessage("pushLoc;" + userAction.recipients.get(0).numbers.get(0)+";" + location + ";\n", true, mContext);
                    }
                    if (userAction instanceof CheckUpAction){
                        //TODO: checkup retrieve friend last location
                        SocketClient.sendMessage("getLoc;" + userAction.recipients.get(0).numbers.get(0) + ";\n", true, mContext);
                    }
                }
            }
        });


    }

    public UserAction getUserAction(int position){
        return GlobalSettings.getInstance(this).userActions.get(position);
    }

    void sendMessage(int position){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting ready...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        UserAction userAction = GlobalSettings.getInstance(this).userActions.get(position);
        MessageSender.getInstance(this, this).sendMessage(userAction);
        System.out.println("Sending Text");
        progressDialog.dismiss();

    }



    void openEditUserActionActivity(int position){
        Intent intent = new Intent(this, EditUserActionActivity.class);
        intent.putExtra("position",position);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 2){
            gridView.invalidateViews();
            Log.d("HackUCSC","onActivityResult mainmenuactivy.java");
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_main, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("HackUCSC","onContextItemSelected mainmenuactivy.java");
        switch (item.getItemId()) {
            case R.id.context_menu_custom:
                userActions.add(new UserAction());
                openEditUserActionActivity(userActions.size()-1);
                break;
            case R.id.context_menu_check_in:
                userActions.add(new CheckInAction());
                openEditUserActionActivity(userActions.size()-1);
                break;
            case R.id.context_menu_panic:
                userActions.add(new PanicAction());
                openEditUserActionActivity(userActions.size()-1);
                break;
            case R.id.context_menu_on_my_way:
                userActions.add(new OnMyWayAction());
                openEditUserActionActivity(userActions.size()-1);
                break;
            case  R.id.context_menu_check_out:
                userActions.add(new CheckUpAction());
                openEditUserActionActivity(userActions.size()-1);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("HackUCSC","onoptionsitemsslected mainmenuactivy.java");
        switch (item.getItemId()) {
            case R.id.action_settings:
                // TODO: User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_add_friend:
                // TODO: add friend menu should pop up
                return true;

            case R.id.action_open_select:
                Intent intent = new Intent(this, SelectMenuActivity.class);
                startActivity(intent);
                return  true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Log.d("HackUCSC","onoptionsitemsslected mainmenuactivy.java defaulted");
                return super.onOptionsItemSelected(item);

        }

    }

}
