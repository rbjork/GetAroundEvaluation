/*
Code created by Marcus Pöhls on December 15 2014, tagged in Development, Android, Retrofit,
Website: Future Studio , Title: Retrofit - Oauth on Android
https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
 */
package com.getaround.getaroundevalapp.authenticate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ronaldbjork on 4/10/17.
 */

public interface LoginService {
    @FormUrlEncoded
    @POST("/token")
    Call<AccessToken> getAccessToken(
            @Field("code") String code,
            @Field("grant_type") String grantType);

}
