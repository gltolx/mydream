package com.lin.mydream.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Created on Milky Way Galaxy.
 * SLOGAN: With the future coming, let's dream it.
 *
 * @author <a href="mailto:linfeng.gdlk@gmail.com">Lin Xiao</a> 2021/11/10.
 * @desc OK Http Client 工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class OkHttpUtil {

    /**
     * 全局唯一 okHttpClient
     */
    private static final OkHttpClient okHttpClient =
            new OkHttpClient
                    .Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)   // 设置连接超时
                    .readTimeout(60, TimeUnit.SECONDS)      // 设置读超时
                    .writeTimeout(60, TimeUnit.SECONDS)     // 设置写超时
                    .retryOnConnectionFailure(true)                 // 是否自动重连
                    // 设置连接池 最大连接数4 生命周期7min
                    .connectionPool(new ConnectionPool(3, 7, TimeUnit.MINUTES))
                    .build();

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return _callReturn(request);
    }

    public static String post(String url, String body) {
        return post(url, body, null, null);
    }

    public static String post(String url, String body, Map<String, String> headers) {
        return post(url, body, headers, null);
    }

    public static String post(String url, String body, Map<String, String> headers, Consumer<String> lineConsumer) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Headers.Builder headersBuilder = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(headersBuilder::add);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, body))
                .headers(headersBuilder.build())
                .build();
        if (lineConsumer != null) {
            _callReturn(request, lineConsumer);
            return Strings.EMPTY;
        } else {
            return _callReturn(request);
        }
    }



    /**
     * ok http call return result method
     *
     * @param request OkRequest
     * @return response.body
     */
    public static String _callReturn(Request request) {
        Objects.requireNonNull(request);
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                log.info("ok http success. result:{}", result);
                return result;
            }
        } catch (IOException e) {
            log.error("ok http exception", e);
        }
        log.error("ok http failed. request:{}", request);
        return StringUtils.EMPTY;
    }

    /**
     * ok http call return result method
     *
     * @param request OkRequest
     * @return response.body
     */
    public static void _callReturn(Request request, Consumer<String> lineConsumer) {
        Objects.requireNonNull(request);
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("OK http failed, Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response body" + response);
            }
            BufferedSource source = responseBody.source();
            String line;
            while ((line = source.readUtf8Line()) != null) {
                lineConsumer.accept(line);
            }

        } catch (IOException e) {
            log.error("OK http exception", e);
        }
    }



    /**
     * ok http call async
     *
     * @param request OkRequest
     */
    public static void _callAsync(Request request) {
        Objects.requireNonNull(request);
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("ok http exception, async call failed, request:{}", call.request().toString(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });

    }
}
