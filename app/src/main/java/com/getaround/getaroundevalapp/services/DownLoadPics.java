package com.getaround.getaroundevalapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getaround.getaroundevalapp.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownLoadPics extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private static final String ACTION_DOWNLOAD_GALLERY = "com.getaround.getaroundevalapp.services.action.GALLERY";
    private static final String ACTION_DOWNLOAD_DETAIL = "com.getaround.getaroundevalapp.services.action.DETAIL";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_KEY = "com.getaround.getaroundevalapp.services.extra.KEY";
    private static final String EXTRA_PARAM_PAGE = "com.getaround.getaroundevalapp.services.extra.PAGE";

    public static final String POPULAR_AND_NATURE = "?feature=popular&only=nature";

    private RequestQueue mQueue;

    public DownLoadPics() {
        super("DownLoadPics");
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownloadGallery(Context context, String key, String page) {

        Intent intent = new Intent(context, DownLoadPics.class);
        intent.setAction(ACTION_DOWNLOAD_GALLERY);
        intent.putExtra(EXTRA_PARAM_KEY, key);
        intent.putExtra(EXTRA_PARAM_PAGE, page);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownloadDetail(Context context, String key, String page) {
        Intent intent = new Intent(context, DownLoadPics.class);
        intent.setAction(ACTION_DOWNLOAD_DETAIL);
        intent.putExtra(EXTRA_PARAM_KEY, key);
        intent.putExtra(EXTRA_PARAM_PAGE, page);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_GALLERY.equals(action)) {
                final String key = intent.getStringExtra(EXTRA_PARAM_KEY);
                final String page = intent.getStringExtra(EXTRA_PARAM_PAGE);
                handleActionGallery(key, page);
            } else if (ACTION_DOWNLOAD_DETAIL.equals(action)) {
                final String key = intent.getStringExtra(EXTRA_PARAM_KEY);
                final String page = intent.getStringExtra(EXTRA_PARAM_PAGE);
                handleActionDetail(key, page);
            }
        }
    }

    private String getBaseUrl() {
        return "https://api.500px.com/v1/photos/";
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGallery(String key, String page) {
        // TODO: Handle action Foo
        Uri uri = Uri.parse(getBaseUrl()).buildUpon()
                .appendEncodedPath(POPULAR_AND_NATURE)
                .appendQueryParameter("page", page)
                .appendQueryParameter("consumer_key", key)
                .build();

        StringRequest request = new StringRequest(StringRequest.Method.GET, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    JSONArray ja = new JSONObject(response).getJSONArray("photos");
                    try {
                        List<Photo> photos = mapper.readValue(ja.toString(), new TypeReference<List<Photo>>() {});
                        for(Photo p : photos){

                            ImageLoader loader = new ImageLoader(mQueue,new ImageLoader.ImageCache(){

                                @Override
                                public Bitmap getBitmap(String url) {
                                    return null;
                                }

                                @Override
                                public void putBitmap(String url, Bitmap bitmap) {

                                }
                            });

                        }
                    } catch (IOException e) {
                        Log.e("DOWNLOADPICS", "download and parse failed", e);
                    }
                } catch (JSONException e) {
                    Log.e("DOWNLOADPICS", "download failed:", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DOWNLOADPICS", "Error", error);
            }
        });
        //request.setRetryPolicy(mRetryPolicy);
        mQueue.add(request);
    }





    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDetail(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
