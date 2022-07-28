package com.damavandit.apps.chair.controllers;

import android.content.Intent;

import com.damavandit.apps.chair.application.App;
import com.damavandit.apps.chair.models.AccessToken;
import com.damavandit.apps.chair.webapis.ApiService;
import com.damavandit.apps.chair.webapis.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.grant_type.PASSWORD;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_REFRESH_TOKEN;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_TYPE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_TOKEN_VALUE;
import static com.damavandit.apps.chair.constants.Const.shared_pref.KEY_ACCESS_USER_NAME;
import static com.damavandit.apps.chair.constants.Const.shared_pref.NAME_ACCESS_TOKEN;

public class TokenManager {

    private static int n = 0;

    public static void getAccessToken(final String userName, final String passwordOrRefreshToken) {

        ApiService webService = ServiceGenerator.createService(ApiService.class);
        Call<AccessToken> callToGetAccessToken = webService.getAccessToken(PASSWORD, userName, passwordOrRefreshToken);

        if (callToGetAccessToken != null) {
            callToGetAccessToken.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Intent intent = new Intent();

                    AccessToken accessToken = response.body();
                    if (accessToken != null) {
                        App.getContext()
                                .getSharedPreferences(NAME_ACCESS_TOKEN, MODE_PRIVATE)
                                .edit()
                                .putString(KEY_ACCESS_TOKEN_VALUE, accessToken.getAccessToken())
                                .putString(KEY_ACCESS_TOKEN_TYPE, accessToken.getTokenType())
                                .putString(KEY_ACCESS_REFRESH_TOKEN, accessToken.getRefreshToken())
                                .putString(KEY_ACCESS_USER_NAME, accessToken.getUserName())
                                .apply();

                        intent.setAction(ACCESS_TOKEN_RECEIVED);
                    } else {
                        if (n!=0){
                            getAccessToken(userName,passwordOrRefreshToken);
                            n++;
                        }else {
                            intent.setAction(ACCESS_TOKEN_FAILED);
                            n=0;
                        }
                    }
                    App.getContext().sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }
}

