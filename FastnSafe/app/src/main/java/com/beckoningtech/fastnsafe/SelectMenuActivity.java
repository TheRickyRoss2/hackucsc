package com.beckoningtech.fastnsafe;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class SelectMenuActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<UserAction> userActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

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
                    openEditUserActionActivity(position);
                }
            }
        });


    }

    public UserAction getUserAction(int position){
        return GlobalSettings.getInstance(this).userActions.get(position);
    }

//    void sendMessage(int position){
//        UserAction userAction = GlobalSettings.getInstance(this).userActions.get(position);
//        MessageSender.getInstance(this).sendMessage(userAction);
//        System.out.println("Sending Text");
//    }

    void openEditUserActionActivity(int position){
        Intent intent = new Intent(this, EditUserActionActivity.class);
        intent.putExtra("position",position);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 2){
            gridView.invalidateViews();
        }
        finish();
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


}
