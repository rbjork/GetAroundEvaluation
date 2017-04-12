package com.getaround.getaroundevalapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;

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

    private ArrayAdapter adapter;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ListView list = (ListView)view.findViewById(R.id.photolist);
        /*list.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPair p = (PhotoPair)adapter.getItem(position);
                int i = view.getId();
            }
        });*/

        adapter = new photolistadapter(getActivity(),R.layout.photoitempair);
        list.setAdapter(adapter);

        new SearchImagesTask().execute();
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_launcher);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    // downloadBitmap code taken from outside resource
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }


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
            PhotoImageView leftimg = (PhotoImageView)row.findViewById(R.id.imageView);
            leftimg.setImage_url(pair.leftPhoto.getImage_url());
            leftimg.setPhotoId(String.valueOf(pair.leftPhoto.getId()));
            PhotoImageView rightimg = (PhotoImageView)row.findViewById(R.id.imageView2);
            rightimg.setImage_url(pair.rightPhoto.getImage_url());
            rightimg.setPhotoId(String.valueOf(pair.rightPhoto.getId()));

            leftimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoImageView pv = (PhotoImageView)v;
                    String photoid = String.valueOf(pv.getPhotoId());
                    showDetail(photoid);
                }
            });

            rightimg.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    PhotoImageView pv = (PhotoImageView)v;
                    String photoid = String.valueOf(pv.getPhotoId());
                    showDetail(photoid);
                }
            });


            new ImageDownloaderTask(leftimg).execute(pair.leftPhoto.getImage_url());
            if(pair.rightPhoto != null)new ImageDownloaderTask(rightimg).execute(pair.rightPhoto.getImage_url());


            return row;
        }
    }

    public class SearchImagesTask extends AsyncTask<String,Void,List<Photo>> {

        @Override
        protected void onPostExecute(List<Photo> photos) {

            int i = 0;
            ArrayList<PhotoPair> pairs = new ArrayList<>();
            int cnt = 2*(photos.size()/2);

            while(i < cnt){
                PhotoPair pair = new PhotoPair(photos.get(i),photos.get(i+1));
                pairs.add(pair);
                i = i + 2;
            }
            if(i < photos.size()-1){
                PhotoPair pair = new PhotoPair(photos.get(i-1),null);
                pairs.add(pair);
            }

            adapter.clear();
            adapter.addAll(pairs);
        }

        @Override
        protected List<Photo> doInBackground(String... params) {
            URL url = null;
            StringBuilder result = new StringBuilder();
            try {

                url = new URL("https://api.500px.com/v1/photos/?feature=popular&only=nature&consumer_key=UU6XQeziu01adhSANZo3J5gDsZD6gaFyJXomYlhz");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json");
                connection.setDoInput(true);
                connection.setConnectTimeout(20 * 1000);
                connection.setReadTimeout(20 * 1000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONArray ja = null;
            List<Photo> photos = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ja = new JSONObject(result.toString()).getJSONArray("photos");
                photos = mapper.readValue(ja.toString(), new TypeReference<List<Photo>>(){});
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return photos;
        }
    }
}
