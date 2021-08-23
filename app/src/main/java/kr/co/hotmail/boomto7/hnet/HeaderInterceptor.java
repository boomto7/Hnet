package kr.co.hotmail.boomto7.hnet;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private boolean isUseAuthToken = false;
    private final String HEADER_FIELD_JWT = "x-auth-token";
    private SpManager spManager;

    public HeaderInterceptor(boolean isUseAuthToken, Context ctx) {
        spManager = SpManager.getInstance(ctx);
        this.isUseAuthToken = isUseAuthToken;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if ( isUseAuthToken) {
            if (request.header(HEADER_FIELD_JWT)==null) {
                if (!TextUtils.isEmpty(spManager.getCurrentJwt())){
                    request = request.newBuilder().addHeader(HEADER_FIELD_JWT,spManager.getCurrentJwt()).build();
                }
            }
        }
        return chain.proceed(request);
    }
}
