
/*
Code created by Marcus Pöhls on December 15 2014, tagged in Development, Android, Retrofit,
Website: Future Studio , Title: Retrofit - Oauth on Android
https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
 */

package com.getaround.getaroundevalapp.authenticate;

/**
 * Created by ronaldbjork on 4/8/17.
 */

public class AccessToken {
    private String accessToken;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if (! Character.isUpperCase(tokenType.charAt(0))) {
            tokenType = Character.toString(tokenType.charAt(0)).toUpperCase() + tokenType.substring(1);
        }
        return tokenType;
    }
}
