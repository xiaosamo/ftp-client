package com.yuanshijia.ftpclient.controller;

import com.alibaba.fastjson.JSON;
import com.yuanshijia.ftpclient.Constants;
import com.yuanshijia.ftpclient.util.HttpClient;
import com.yuanshijia.ftpclient.entity.FileInfo;
import com.yuanshijia.ftpclient.util.OKHttp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author yuan
 * @date 2019/10/21
 * @description
 */
@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @GetMapping("/")
    public String index(Model model){
        // 获取所有的文件
        String json = HttpClient.doGet(Constants.get_url);
        List<FileInfo> fileList = JSON.parseArray(json, FileInfo.class);
        model.addAttribute("fileList", fileList);
        return "index";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return "文件为空";
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为:{}", fileName);
            log.info("开始请求服务...");
            String uuid = HttpClient.uploadFile(Constants.upload_url, file.getInputStream(), fileName);
            log.info("调用返回结果uuid:{}", uuid);

            if (!StringUtils.isEmpty(uuid)) {
                return "上传成功,文件信息:" + uuid;
            }

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    @GetMapping("/get")
    @ResponseBody
    public String get(String uuid) {
        return HttpClient.doGet(Constants.get_url + "?uuid=" + uuid);
    }

    @GetMapping("/download")
    public void download(String uuid, HttpServletResponse response) throws IOException {
        OKHttp.download(Constants.download_url + uuid, response);

    }
}
