package com.beckoningtech.fastnsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by William on 1/21/2017.
 */

public class MainMenuUserActionAdapter extends BaseAdapter {
    private Context mContext;
    public static ArrayList<UserAction> userActions;
    int columnWidth;
    private static LayoutInflater inflater=null;

    public MainMenuUserActionAdapter(Context context){
        mContext = context;
        userActions = new ArrayList<>();
    }
    public MainMenuUserActionAdapter(Context context, ArrayList<UserAction> userActions,
                                     int columnWidth){

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        this.userActions = userActions;
        this.columnWidth = columnWidth;
    }

    @Override
    public int getCount() {
        return GlobalSettings.getInstance(mContext).userActions.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position < GlobalSettings.getInstance(mContext).userActions.size()){
            return GlobalSettings.getInstance(mContext).userActions.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View view;
        if(convertView == null){

            view = inflater.inflate(R.layout.user_action_in_grid_view,null);
        } else {
            view= convertView;
            view = inflater.inflate(R.layout.user_action_in_grid_view,null);
        }
        holder.tv=(TextView) view.findViewById(R.id.grid_text_view);
        holder.img=(ImageView) view.findViewById(R.id.grid_image_view);
        System.out.println(columnWidth);
        if (position == userActions.size()){
            holder.img.setImageResource(R.drawable.add_new_image_grid);
            holder.tv.setText("Add New Action");
            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,
                    columnWidth+holder.tv.getHeight()+30));
            /*view.setLayoutParams(new LinearLayout.LayoutParams(columnWidth,
                    columnWidth+holder.tv.getHeight()));*/
            return view;
        }

        UserAction tmp = GlobalSettings.getInstance(mContext).userActions.get(position);
        switch(tmp.getType()){
            case UserAction.CUSTOM_ACTION:
                holder.img.setImageResource(R.drawable.custom_image_grid);
                break;
            case UserAction.CHECK_IN_ACTION:
                holder.img.setImageResource(R.drawable.check_in_image_grid);
                break;
            case UserAction.PANIC_ACTION:
                holder.img.setImageResource(R.drawable.panic_image_grid);
                break;
            case UserAction.ON_MY_WAY_ACTION:
                holder.img.setImageResource(R.drawable.on_my_way_image_grid);
                break;
            case UserAction.CHECK_UP_ACTION:
                holder.img.setImageResource(R.drawable.check_up_image_grid);
                break;
            default:
                holder.img.setImageResource(R.drawable.custom_image_grid);
                break;
        }
        holder.tv.setText(tmp.title);

        view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,
                columnWidth+holder.tv.getHeight()+30));
        return view;
        /*
        UserActionInGridView view;
        ImageView imageView;
        if (convertView == null || !(convertView instanceof UserActionInGridView) ) {
            // if it's not recycled, initialize some attributes
            view = new UserActionInGridView(mContext,columnWidth);
            view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,
                    GridView.AUTO_FIT));

            //imageView.setPadding(8, 8, 8, 8);
        } else {
            view = (UserActionInGridView) convertView;
        }

        if (position == userActions.size()){
            view.imageView.setImageResource(R.drawable.add_new_image_grid);
            view.textView.setText("Add New Action");
            return view;
        }

        UserAction tmp = userActions.get(position);
        switch(tmp.getType()){
            case UserAction.CUSTOM_ACTION:
                view.imageView.setImageResource(R.drawable.custom_image_grid);
                break;
            case UserAction.CHECK_IN_ACTION:
                view.imageView.setImageResource(R.drawable.check_in_image_grid);
                break;
            case UserAction.PANIC_ACTION:
                view.imageView.setImageResource(R.drawable.panic_image_grid);
                break;
            case UserAction.ON_MY_WAY_ACTION:
                view.imageView.setImageResource(R.drawable.on_my_way_image_grid);
                break;
            default:
                view.imageView.setImageResource(R.drawable.custom_image_grid);
                break;
        }
        view.textView.setText(tmp.title);
        return view;
        */
    }
}
