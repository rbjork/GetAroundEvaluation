package com.getaround.getaroundevalapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getaround.getaroundevalapp.R;
import com.getaround.getaroundevalapp.model.Photo;

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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoDetailFragment.onFragmentDetailListener} interface
 * to handle interaction events.
 * Use the {@link PhotoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoDetailFragment extends Fragment {
    private static final String TAG = PhotoDetailFragment.class.getName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static String IMAGE_URL = "param1";
    // TODO: Rename and change types of parameters

    private String img_url;
    private ImageView photoView;
    private TextView phototitle;
    private Button returnButton;
    private ProgressBar progressBar;

    private onFragmentDetailListener mListener;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    public void replacePhoto(String photoid){
        Log.d(TAG, "photoid="+photoid);
        new DetailAsyncTask().execute(photoid);
    }

    // TODO: Rename and change types and number of parameters
    public static PhotoDetailFragment newInstance(String photoid) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Log.d(TAG, "new Instance photoid="+photoid);
        fragment.replacePhoto(photoid);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //img_url = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        photoView = (ImageView) v.findViewById(R.id.photoview);
        phototitle = (TextView)v.findViewById(R.id.phototxt);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        returnButton = (Button)v.findViewById(R.id.returnbtn);
        returnButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mListener.onFragmentDetailListener();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentDetailListener) {
            mListener = (onFragmentDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface onFragmentDetailListener {
        // TODO: Update argument type and name
        void onFragmentDetailListener();
    }

    // Modified ImageDownloaderTask code taken from outside resource
    class ImageDownloaderTask extends AsyncTask<String, Integer, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            return downloadBitmap(params[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("PhotoDetailFragment", "progress "+ String.valueOf(values[0]));
            progressBar.setProgress(values[0]);
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

    private class DetailAsyncTask extends AsyncTask<String,Integer,Photo>{
        @Override
        protected void onPostExecute(Photo photo) {
            if(photo != null){
                new ImageDownloaderTask(photoView).execute(photo.getImage_url());
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("PhotoDetailFragment", "progress "+ String.valueOf(values[0]));
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Photo doInBackground(String... params) {
            String photoId = params[0];
            StringBuilder result = new StringBuilder();
            Photo photo = null;
            try {

                URL url = new URL("https://api.500px.com/v1/photos/" + photoId + "/?image_size=3&consumer_key=UU6XQeziu01adhSANZo3J5gDsZD6gaFyJXomYlhz");
              //  URL url = new URL("https://api.500px.com/v1/photos/207340747/?image_size=3&consumer_key=UU6XQeziu01adhSANZo3J5gDsZD6gaFyJXomYlhz");
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
                    JSONObject ja = null;

                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        JSONObject jr = new JSONObject(result.toString());
                        ja = jr.getJSONObject("photo");
                        photo = mapper.readValue(ja.toString(), new TypeReference<Photo>(){});
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return photo;
        }
    }


}
