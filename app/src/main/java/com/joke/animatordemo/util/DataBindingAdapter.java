package com.joke.animatordemo.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by SHF on 2016/12/14.
 */

public class DataBindingAdapter {

    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView view,int resource){
        view.setImageResource(resource);

    }
}
