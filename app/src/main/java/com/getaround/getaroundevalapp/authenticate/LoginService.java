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
