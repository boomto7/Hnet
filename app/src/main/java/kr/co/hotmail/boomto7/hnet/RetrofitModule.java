package kr.co.hotmail.boomto7.hnet;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitModule<E> {
    private Map<String, Object> counter = new HashMap<String, Object>();

    @SuppressLint("StaticFieldLeak")
    private static final RetrofitModule instance = new RetrofitModule();
    private String mBaseUrl="";

    private boolean isUseHeaderInterceptor = false;
    private Context mCtx;

    public static void init(Context ctx, String _baseUrl, boolean _isUseHeaderInterceptor) {
        instance.mCtx = ctx;
        instance.mBaseUrl = _baseUrl;
        instance.isUseHeaderInterceptor = _isUseHeaderInterceptor;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getInstance(Class<T> tClass) throws InstantiationException, IllegalAccessException {
        synchronized (instance) {
            T singleton = (T) instance.counter.get(tClass.getName());
            if (singleton == null) {
                singleton = new Retrofit.Builder()
                        .baseUrl(instance.mBaseUrl)
                        .addCallAdapterFactory(provideRxJAvaAdapterFactory())
                        .addConverterFactory(provideConverterFactory())
                        .client(provideOkHttpClient())
                        .build()
                        .create(tClass);
                instance.counter.put(tClass.getName(), singleton);
            }

            return singleton;
        }
    }


    private static OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = provideLoggingInterceptor();
        HeaderInterceptor headerInterceptor = provideHeaderInterceptor();
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(headerInterceptor).build();
    }

    private static HttpLoggingInterceptor provideLoggingInterceptor() {
        if (BuildConfig.DEBUG) {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return new HttpLoggingInterceptor();
        }
    }

    private static HeaderInterceptor provideHeaderInterceptor() {
        return new HeaderInterceptor(instance.isUseHeaderInterceptor, instance.mCtx);
    }

    private static Converter.Factory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    private static RxJava2CallAdapterFactory provideRxJAvaAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }


}
