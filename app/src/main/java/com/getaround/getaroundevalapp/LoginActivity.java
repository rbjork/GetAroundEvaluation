package com.getaround.getaroundevalapp;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getaround.getaroundevalapp.authenticate.AccessToken;
import com.getaround.getaroundevalapp.authenticate.LoginService;
import com.getaround.getaroundevalapp.authenticate.ServiceGenerator;
//import com.squareup.okhttp.*;
import okhttp3.*;
import retrofit2.Call;
import java.io.IOException;

import okio.BufferedSink;

// Authentication wasn't necessary having the api key.  So this part of app wasn't incorporated.
// The app launches instead directly into MainActivity
public class LoginActivity extends AppCompatActivity {

    private final String clientId = "grbtxtmsg@gmail.com";
    private final String clientSecret = "5CZl9ZYnOwvOn35Jg3sKFFDR6nKZC4DK2NctRPUc";
    private final String redirectUri = "https://api.500px.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(ServiceGenerator.API_BASE_URL + "/login" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                LoginService loginService = ServiceGenerator.createService(LoginService.class, clientId, clientSecret);
                Call<AccessToken> call = loginService.getAccessToken(code, "authorization_code");
                try {
                    AccessToken accessToken = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    public class LoginTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url("https://api.yourapi...")
                    .header("ApiKey", "UU6XQeziu01adhSANZo3J5gDsZD6gaFyJXomYlhz")
                    .build();

            String result = null;
            try {
                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
