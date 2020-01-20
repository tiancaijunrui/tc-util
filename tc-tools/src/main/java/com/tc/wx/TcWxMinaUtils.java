package com.tc.wx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.Base64;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TcWxMinaUtils {
    private static OkHttpClient client = new OkHttpClient();

    public static InputStream getQrCodeUrl(String scene, String appId, String secretKey, File temFile, String fileUploadUrl) throws IOException {
        if (StringUtils.isBlank(scene) || StringUtils.isBlank(appId) || StringUtils.isBlank(secretKey)) {
            throw new IllegalArgumentException("params is not be blank~~");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("scene", scene);
        param.put("width", 430);
        param.put("auto_color", false);
        Map<String, Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        param.put("line_color", line_color);
        RequestBody requestBody = RequestBody.create(JSONObject.toJSONString(param), MediaType.get("application/json; charset=utf-8"));
        String accessToken = TcWxCommonUtils.getWxAccessToken(appId, secretKey);
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken)
                .post(requestBody).build();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).byteStream();
        }
    }

    public static JSONObject getSessionAndOpenId(String code, String appId, String secret) throws IOException {
        String params = "appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/jscode2session?" + params)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = Objects.requireNonNull(response.body()).string();
            return JSONObject.parseObject(json);
        }
    }

    public static JSONObject getUserInfo(String sessionKey, String encryptedData, String iv) throws Exception {
        // 被加密的数据
        byte[] dataByte = Base64.decodeFast(encryptedData);
        // 加密秘钥
        byte[] keyByte = com.alibaba.fastjson.util.Base64.decodeFast(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeFast(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, StandardCharsets.UTF_8);
                return JSONObject.parseObject(result);
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
