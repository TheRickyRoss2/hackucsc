package com.beckoningtech.fastnsafe;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by William on 1/21/2017.
 */

public class UserActionInGridView extends LinearLayout {
    public ImageView imageView;
    public TextView textView;
    public Button button;


    public UserActionInGridView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.UserActionInGridViewOptions,
                0, 0);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_action_in_grid_view, this, true);


    }

    public UserActionInGridView(Context context, String titleText, Image image){
        super(context,null);
        imageView = (ImageView) getChildAt(0);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView = (TextView) getChildAt(1);

    }

    public UserActionInGridView(Context context, int columnWidth){
        super(context,null);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_action_in_grid_view, this, true);
        imageView = (ImageView) getChildAt(0);
        imageView.setMaxHeight(columnWidth);
        imageView.setMaxWidth(columnWidth);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView = (TextView) getChildAt(1);
        textView.setVisibility(textView.VISIBLE);

    }

    public void setUpListeners(){

    }
}
