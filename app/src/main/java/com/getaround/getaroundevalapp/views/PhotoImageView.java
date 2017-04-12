package com.getaround.getaroundevalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ronaldbjork on 4/11/17.
 */

public class PhotoImageView extends ImageView {
    private String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public PhotoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
