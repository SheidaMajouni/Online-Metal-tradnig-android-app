package com.damavandit.apps.chair.webapis;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.damavandit.apps.chair.application.App;
import com.damavandit.apps.chair.models.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.damavandit.apps.chair.constants.Const.grant_type.REFRESH_TOKEN;
import static com.damavandit.apps.chair.constants.Const.response_code.UNAUTHORIZED;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_REFRESH_TOKEN;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_TYPE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_VALUE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_USER_NAME;
import static com.damavandit.apps.chair.constants.Const.shared_pref.NAME_ACCESS_TOKEN;

public class AuthenticationInterceptor implements Interceptor {

    private String mAuthToken;

    public AuthenticationInterceptor(String authToken) {
        this.mAuthToken = authToken;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = makeRequest(chain);

        if (response.code() == UNAUTHORIZED) {
            SharedPreferences sharedPref = App.getContext()
                    .getSharedPreferences(NAME_ACCESS_TOKEN, Context.MODE_PRIVATE);

            ApiService webService = ServiceGenerator.createService(ApiService.class);
            retrofit2.Response<AccessToken> responseAccessToken = webService.getRefreshToken(
                    REFRESH_TOKEN,
                    sharedPref.getString(KEY_ACCESS_USER_NAME, ""),
                    sharedPref.getString(KEY_ACCESS_REFRESH_TOKEN, ""))
                    .execute();

            if (responseAccessToken.isSuccessful()) {
                AccessToken accessToken = responseAccessToken.body();
                if (accessToken != null) {
                    setAuthToken(accessToken.getAccessToken());
                    sharedPref
                            .edit()
                            .putString(KEY_ACCESS_TOKEN_VALUE, accessToken.getAccessToken())
                            .putString(KEY_ACCESS_TOKEN_TYPE, accessToken.getTokenType())
                            .putString(KEY_ACCESS_REFRESH_TOKEN, accessToken.getRefreshToken())
                            .putString(KEY_ACCESS_USER_NAME, accessToken.getUserName())
                            .apply();
                    return makeRequest(chain);
                }
            }
        }

        return response;
    }

    private Response makeRequest(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", TextUtils.isEmpty(mAuthToken) ? "" : mAuthToken);

        Request request = builder.build();

        return chain.proceed(request);
    }

    public void setAuthToken(String authToken) {
        this.mAuthToken = authToken;
    }
}
