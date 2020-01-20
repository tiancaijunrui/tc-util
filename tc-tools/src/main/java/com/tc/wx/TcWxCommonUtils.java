package com.tc.wx;

import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class TcWxCommonUtils {

    private static OkHttpClient client = new OkHttpClient();

    public static String getWxAccessToken(String appId, String secretKey) throws IOException {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secretKey)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = Objects.requireNonNull(response.body()).string();
            JSONObject jsonObject = JSONObject.parseObject(json);
            return jsonObject.getString("access_token");
        }
    }




}
