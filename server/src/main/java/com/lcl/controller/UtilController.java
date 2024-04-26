package com.lcl.controller;

import cn.hutool.core.util.StrUtil;
import com.lcl.result.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author LovelyPeracid
 */
@RestController
@Slf4j
@Api(tags = "图片上传和获取")
public class  UtilController {
    /**
     *  上传图片
     */
    @PostMapping("/uploadImage")
    public Result uploadImage(@RequestBody MultipartFile image) {
        try {
            String originImageName = image.getName();
            String newImageName = createNewFileName(originImageName);
            image.transferTo(new File("D:\\dir", newImageName));
            return Result.success("http://localhost:8080/image" + newImageName);
        } catch (IOException e) {
            throw new RuntimeException("upload image exception");
        }
    }

    private String createNewFileName(String originFileName) {
        // 获取图片后缀
        String suffix = StrUtil.subAfter(originFileName, ".", true);
        // 给图片一个新的名字
        String name = UUID.randomUUID().toString();
        // 图片放哪个文件夹
        File dir = new File("D:\\dir", "chileDir");
        // 文件夹不存在就创建
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return String.format("/chileDir/{}.{}", name, suffix);
    }

    /**
     * 获取图片
     * @param httpServletRequest
     * @return
     * @throws IOException
     */
//    @GetMapping("/**")
    public ResponseEntity<InputStreamResource> showImage(HttpServletRequest httpServletRequest) throws IOException {
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        // 获取文件名部分
        String fileName = extractFileNameFromURL(requestURL.toString());
        // 拼接本地文件路径()
        String localFilePath = "D:\\dir\\chileDir" + fileName;
        // 读取文件内容
        File file = new File(localFilePath);
        InputStream inputStream = Files.newInputStream(file.toPath());

        // 设置响应头，告知浏览器文件类型
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // 根据实际文件类型设置 MediaType

        // 将文件内容作为 InputStreamResource 返回
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
    }

    private String extractFileNameFromURL(String url) {
        // 在这里解析出文件名，这里假设文件名是 URL 中最后一个斜杠后的部分
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1);
    }
}
