package com.damavandit.apps.chair.webapis;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.damavandit.apps.chair.webapis.ApiService.API_BASE_URL;

public class ServiceGenerator {

    private static OkHttpClient okHttpClient = new OkHttpClient();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(240, TimeUnit.SECONDS);

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static NetworkConnectionInterceptor networkConnectionInterceptor =
            new NetworkConnectionInterceptor();

    private static AuthenticationInterceptor authenticationInterceptor =
            new AuthenticationInterceptor(null);

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(Class<S> serviceClass, String clientId, String clientSecret) {
        if (!TextUtils.isEmpty(clientId)
                && !TextUtils.isEmpty(clientSecret)) {
            String authToken = Credentials.basic(clientId, clientSecret);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!httpClient.interceptors().contains(networkConnectionInterceptor)) {
            httpClient.addInterceptor(networkConnectionInterceptor);
        }

        if (TextUtils.isEmpty(authToken)) {
            if (httpClient.interceptors().contains(authenticationInterceptor)) {
                httpClient.interceptors().remove(authenticationInterceptor);
            }
        } else {
            if (!httpClient.interceptors().contains(authenticationInterceptor)) {
                authenticationInterceptor.setAuthToken(authToken);
                httpClient.addInterceptor(authenticationInterceptor);
            }
        }

        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }
}
