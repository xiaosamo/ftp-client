package com.yuanshijia.ftpclient.util;

import java.io.*;
import java.net.*;
import java.util.UUID;

import static com.yuanshijia.ftpclient.Constants.PRIVATEKEY;

public class HttpClient {


    /**
     * 设置X-SID、X-Signature
     * @param connection
     * @throws Exception
     */
    private static void setHeader(HttpURLConnection connection) throws Exception {
        // 随机生成sid字符串
        String sid = UUID.randomUUID().toString();
        // 使用私钥对sid加密

        String signature = RSAUtils.encryptedByPrivateKey(sid, PRIVATEKEY);
        connection.setRequestProperty("X-SID", sid);
        connection.setRequestProperty("X-Signature", signature);
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");

            setHeader(connection);

            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }


    public static String uploadFile(String serverUrl, InputStream is, String fileName) {
        try {
            // 换行符
            final String newLine = "\r\n";
            // 服务器的上传地址
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            connection.setRequestMethod("POST");
            setHeader(connection);

            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            // 设置请求头参数
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryari0emH33oMihIU4");


            OutputStream out = new DataOutputStream(connection.getOutputStream());

            // 上传文件
            StringBuilder sb = new StringBuilder();
            sb.append(newLine);
            // 文件参数
            sb.append("------WebKitFormBoundaryari0emH33oMihIU4\n" +
                    "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"");
//            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(is);
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = "------WebKitFormBoundaryari0emH33oMihIU4--".getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        return "";
    }

}