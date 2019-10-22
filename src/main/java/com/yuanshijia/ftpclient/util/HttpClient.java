package com.yuanshijia.ftpclient.util;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HttpClient {

    /**
     * 私钥
     */
    public static final String PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIEavWMNFjYw7WCmJk3pH6gs+UzP1lT7DMEYDpYzWqBsjqDQAB03kC18ex1PZIGbXgTZe3/1/kmMS3kNUnN4PWuacCEKOGUt/JaRK6sK0b6HevnfllBeyQXvHIyOiGezr3qWF32kfYk1EcyhWhA2/9jp2ETgEEpztje4Eo0rMxuVAgMBAAECgYB865e24PHOC7eUXhAQMPMtsh6jQTN9VNF5gUKT+SrGsWOsnLaPjrTWHm2t5aU1d1UY0isanLapCbP5dHeEPSMHqJT06dKOcNKyO+d4tAUifp4X6SRxQM451fTlH3b9DyBOCzj8cWCV3G5yDGw8r0UvgRaZtETGLQtEiU481tJGPQJBAP2SOLbYCfbRJJfNuygJPVLsb/Udvhw+AhTBxON4AeOCVpzbHuTOo4ctJpBv7mQr/jZaC+lD6EC2xVRwXn2peTcCQQCCV1Co1ZYjb6ib2bOnzYt9FWnmvUWYhY3tNnA1Z4nyuk+F3ItnptOCL+OM71fvDhK75dFIGBaR/cf+/tHE/geTAkEAmMDVWZCbsAwdtzVAYcXvI107tLXOKMVSC58PNNi+ioeiFawK2FuDN5ODf89uFPpLkgJ82nEGILX5+fwXcCv/0QJAKyS1yTzxtocWO4hDJG8wFdZJuJu79rF2eieD2tmL2vF6syd+aW/aIQSzAIqVA8cXwmRe8ssuZWKomapFSzx+ywJAP0CHKzBRU6C3ixJDoaZAKi5mYuGEa5im1icpoiadM9uOzMA62ZXdNAHQamzOBuA2zvyM0yHKAUfHark+p9iiyA==";


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

    public static String doPost(String httpUrl, String param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            setHeader(connection);

            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);


            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
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
            if (null != os) {
                try {
                    os.close();
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
            // 断开与远程地址url的连接
            connection.disconnect();
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