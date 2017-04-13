package com.getaround.getaroundevalapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ronaldbjork on 4/11/17.
 */

public class PhotoPair {

    public Photo leftPhoto;
    public Photo rightPhoto;
    public Boolean needmore = false;


    public PhotoPair(Photo left, Photo right) {
        leftPhoto = left;
        rightPhoto = right;
    }
}
