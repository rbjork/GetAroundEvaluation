package com.getaround.getaroundevalapp.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by ronaldbjork on 4/12/17.
 */

public class Model {
    private ArrayList<Bitmap> photobitmaps;

    private ArrayList<PhotoPair> photoPairs;

    synchronized public ArrayList<Bitmap> getPhotobitmaps() {
        return photobitmaps;
    }

    synchronized public void addPhoto(Bitmap bmp){
        photobitmaps.add(bmp);
    }

    public ArrayList<PhotoPair> getPhotoPairs() {
        return photoPairs;
    }

    public void setPhotoPairs(ArrayList<PhotoPair> photoPairs) {
        this.photoPairs = photoPairs;
    }

    private static Model model;
    synchronized public static Model getInstance(){
        if(model == null){
            model = new Model();
            model.photobitmaps = new ArrayList<>();
        }
        return model;
    }
}
