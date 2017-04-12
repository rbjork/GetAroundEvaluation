package com.getaround.getaroundevalapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.getaround.getaroundevalapp.views.GalleryFragment;
import com.getaround.getaroundevalapp.views.PhotoDetailFragment;


public class MainActivity extends AppCompatActivity implements GalleryFragment.OnFragmentGalleryInteractionListener, PhotoDetailFragment.onFragmentDetailListener {

    private FragmentManager fm;
    private GalleryFragment gf;
    private PhotoDetailFragment pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        gf = GalleryFragment.newInstance();
        ft.add(R.id.framecontainer, gf);
        ft.commit();
    }

    @Override
    public void OnFragmentGalleryInteractionListener(String id) {
        FragmentTransaction ft = fm.beginTransaction();
        if(pd == null){
            pd = PhotoDetailFragment.newInstance(id);
        }else{
            pd.replacePhoto(id);
        }
        ft.replace(R.id.framecontainer,pd).commit();
    }

    @Override
    public void onFragmentDetailListener() {
        FragmentTransaction ft = fm.beginTransaction();
        if(gf == null){
            gf = GalleryFragment.newInstance();
        }
        ft.replace(R.id.framecontainer,gf).commit();
    }

}
