package com.example.demo.userservice.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.demo.userservice.common.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@Api("文件管理")
@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip = "http://localhost";

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // 定义文件的唯一标识前缀
        String uuid = IdUtil.fastSimpleUUID();
        // 找到resources下的files路径,并把文件写到该目录下
        String rootFilePath = System.getProperty("user.dir") + "/springboot-project-demo/src/main/resources/files/" + uuid + "_" +fileName;
        System.out.println(rootFilePath);
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        // 返回url
        return R.ok().data("url", ip + ":" + port + "/files/" + uuid);
    }

    @CrossOrigin
    @ApiOperation("富文本文件上传")
    @PostMapping("/edit/upload")
    public JSON editUpload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // 定义文件的唯一标识前缀
        String uuid = IdUtil.fastSimpleUUID();
        // 找到resources下的files路径,并把文件写到该目录下
        String rootFilePath = System.getProperty("user.dir") + "/springboot-project-demo/src/main/resources/files/" + uuid + "_" +fileName;
        System.out.println(rootFilePath);
        FileUtil.writeBytes(file.getBytes(), rootFilePath);
        String url = ip + ":" + port + "/files/" + uuid;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        // 返回url
        return json;
    }


    @CrossOrigin
    @ApiOperation("文件下载")
    @GetMapping("/{uuid}")
    public void getFiles(@PathVariable String uuid, HttpServletResponse response) {
        OutputStream os;
        String basePath = System.getProperty("user.dir") + "/springboot-project-demo/src/main/resources/files/";
        List<String> fileNames = FileUtil.listFileNames(basePath);
        String fileName = fileNames.stream().filter(name -> name.contains(uuid)).findAny().orElse("");
        try{
            if(StrUtil.isNotEmpty((fileName))){
                //response.addHeader("Content-Disposition", "attachment:filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("文件下载失败");
        }
    }
}
