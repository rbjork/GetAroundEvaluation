package com.getaround.getaroundevalapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getaround.getaroundevalapp.MainActivity;
import com.getaround.getaroundevalapp.R;
import com.getaround.getaroundevalapp.model.Photo;
import com.getaround.getaroundevalapp.model.PhotoPair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GalleryFragment.OnFragmentGalleryInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    private ArrayAdapter adapter;
    private ListView list;
    private ProgressBar progressBar;
    private List<PhotoPair> pairs;

    private OnFragmentGalleryInteractionListener mListener;

    public GalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment GalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        list = (ListView)view.findViewById(R.id.photolist);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        adapter = new photolistadapter(getActivity(),R.layout.photoitempair);
        if(pairs != null)adapter.addAll(pairs);
        list.setAdapter(adapter);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentGalleryInteractionListener) {
            mListener = (OnFragmentGalleryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentGalleryInteractionListener");
        }
    }

    public void setProgress(double progress){
        int prg = (int)(100*progress);
        Log.d("Gallery progress",String.valueOf(prg));
        if(prg == 0){
            progressBar.setVisibility(View.VISIBLE);
        }else if(prg >= 90){
            progressBar.setVisibility(View.INVISIBLE);
        }
        progressBar.setProgress(prg);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void renderImages(List<PhotoPair> pairs){
        Log.d(TAG,"renderImage");
        this.pairs = pairs;
        adapter.clear();
        list.invalidateViews();
        list.setAdapter(adapter);
        adapter.addAll(pairs);
        adapter.notifyDataSetChanged();
    }

    public void showDetail(String id){
        mListener.OnFragmentGalleryInteractionListener(id);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentGalleryInteractionListener {
        // TODO: Update argument type and name
        void OnFragmentGalleryInteractionListener(String url);
    }


    // ImageDownloaderTask code taken from outside resource



    private class photolistadapter extends ArrayAdapter<PhotoPair> {

        public photolistadapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if(row == null){
                row = LayoutInflater.from(getContext()).inflate(R.layout.photoitempair,parent, false);
            }

            PhotoPair pair = getItem(position);
            if(pair.needmore){

            }else {
                PhotoImageView leftimg = (PhotoImageView) row.findViewById(R.id.imageView);
                leftimg.setImageBitmap(pair.leftPhoto.getPhotoBitmap());
                leftimg.setPhotoId(String.valueOf(pair.leftPhoto.getId()));

                leftimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoImageView pv = (PhotoImageView) v;
                        String photoid = String.valueOf(pv.getPhotoId());
                        showDetail(photoid);
                    }
                });
                if(pair.rightPhoto != null) {
                    PhotoImageView rightimg = (PhotoImageView) row.findViewById(R.id.imageView2);
                    rightimg.setImageBitmap(pair.rightPhoto.getPhotoBitmap());
                    rightimg.setPhotoId(String.valueOf(pair.rightPhoto.getId()));

                    rightimg.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            PhotoImageView pv = (PhotoImageView) v;
                            String photoid = String.valueOf(pv.getPhotoId());
                            showDetail(photoid);
                        }
                    });
                }
            }

            return row;
        }
    }


}
