package com.gebeya.mobile.bidir.data.base.remote.backend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gebeya.mobile.bidir.data.user.User;
import com.gebeya.mobile.bidir.impl.util.Constants;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Concrete implementation for the {@link ConnectionProvider} interface
 */
public class BaseConnectionProvider implements ConnectionProvider {

    @Override
    public <T> T createService(@NonNull String scheme,
                               @NonNull String authority,
                               @NonNull String service,
                               @Nullable User user,
                               @NonNull Class<T> serviceClass) {

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {      // No more passing User as an argument to Retrofit!
                    final Request.Builder builder = chain.request().newBuilder();
                    if (user != null) {
                        builder.addHeader("Authorization", "Bearer " + user.token);
                        return chain.proceed(builder.build());
                    } else {
                        return chain.proceed(chain.request());
                    }
                })
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .baseUrl(String.format(Locale.getDefault(),
                        Constants.URL_FORMAT, scheme, authority, service
                ))
                .build();

        return retrofit.create(serviceClass);
    }
}