package com.getaround.getaroundevalapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getaround.getaroundevalapp.model.Model;
import com.getaround.getaroundevalapp.model.Photo;
import com.getaround.getaroundevalapp.model.PhotoPair;
import com.getaround.getaroundevalapp.views.GalleryFragment;
import com.getaround.getaroundevalapp.views.PhotoDetailFragment;

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


public class MainActivity extends AppCompatActivity implements GalleryFragment.OnFragmentGalleryInteractionListener, PhotoDetailFragment.onFragmentDetailListener {

    private FragmentManager fm;
    private GalleryFragment gf;
    private PhotoDetailFragment pd;
    private List<Photo> photos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        gf = GalleryFragment.newInstance();
        ft.add(R.id.framecontainer, gf);
        ft.commit();

        countloaded = 0;
        new SearchImagesTask().execute();
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
        gf.renderImages(Model.getInstance().getPhotoPairs());
    }


    private int countloaded;
    private void loadBitmaps(List<Photo> photos){
        this.photos = photos;
        for(Photo p : photos){
            String url = p.getImage_url();
            new ImageDownloaderTask(p).execute(url);
        }
    }

    private void displayGallery(){
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
        PhotoPair more = new PhotoPair(null,null);
        more.needmore = true;
        pairs.add(more);

        Model.getInstance().setPhotoPairs(pairs);
        gf.renderImages(pairs);
    }



    // ImageDownloaderTask code taken from outside resource
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private Photo photo;
        public ImageDownloaderTask(Photo p) {
            photo = p;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = downloadBitmap(params[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            photo.setPhotoBitmap(bitmap);
            countloaded++;
            double progress = countloaded/(1.0*photos.size());
            Log.d("MainActivity","progress = "+progress);
            gf.setProgress(progress);
            if(progress > 0.99){
                displayGallery();
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

    public class SearchImagesTask extends AsyncTask<String,Void,List<Photo>> {

        @Override
        protected void onPostExecute(List<Photo> photos) {
            loadBitmaps(photos);
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
