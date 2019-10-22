package com.yuanshijia.ftpclient.util;

import okhttp3.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.yuanshijia.ftpclient.Constants.PRIVATEKEY;

/**
 * @author yuan
 * @date 2019/10/22
 * @description
 */
public class OKHttp {
    private static final OkHttpClient client = new OkHttpClient();


    public static void download(final String url, HttpServletResponse servletResponse) throws IOException {
        // 随机生成sid字符串
        String sid = UUID.randomUUID().toString();
        // 使用私钥对sid加密
        String signature = RSAUtils.encryptedByPrivateKey(sid, PRIVATEKEY);

        final Request request = new Request.Builder()
                .get()
                .addHeader("X-SID", sid)
                .addHeader("X-Signature", signature)
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();


        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.addHeader("Content-Disposition", response.header("Content-Disposition"));
        servletResponse.addHeader("Content-Type", "application/octet-stream");
        InputStream is = response.body().byteStream();
        ServletOutputStream os = servletResponse.getOutputStream();

        int len = 0;
        byte[] data = new byte[1024];
        while ((len = is.read(data)) != -1) {
            os.write(data, 0, len);
        }
        os.flush();
        is.close();
        os.close();
    }
}
