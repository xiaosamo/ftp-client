# ftp客户端
需要JDK8+，客户端端口为8088

## 运行
启动后，访问首页http://localhost:8088

### 首页：

![首页](doc/img1.png)

### 上传文件:

![上传](doc/img2.png)

### 返回文件uuid信息
![显示uuid](doc/img3.png)

### 查看文件信息
![查看文件信息](doc/img4.png)

### 下载文件
![下载文件](doc/img5.png)



### 说明：
* 项目使用springboot
* 客户端首页地址:http://localhost:8088
* 页面使用thymeleaf模板
* 使用了Okhttp请求服务器下载文件
* fastjson解析json
* 默认服务端url为http://localhost:8080/ftp-server

