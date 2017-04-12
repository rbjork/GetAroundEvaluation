package com.getaround.getaroundevalapp.authenticate;

import okhttp3.*;

import java.io.IOException;

/**
 * Created by ronaldbjork on 4/10/17.
 */

public class AuthenticationInterceptor implements Interceptor {
    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
