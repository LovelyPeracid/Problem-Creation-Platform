package com.lcl.controller;

import cn.hutool.core.util.StrUtil;
import com.lcl.annotation.Admin;
import com.lcl.annotation.UserAuth;
import com.lcl.annotation.UserInOtherSpace;
import com.lcl.dto.*;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.ProblemService;
import com.lcl.vo.CommitInfoVO;
import com.lcl.vo.ProblemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * @author LovelyPeracid
 */
@Api(tags = "题目操作")
@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @ApiOperation("查找题目")
    @GetMapping("/{problemId}")
    public Result<ProblemVO> getById(@PathVariable Long ProblemId) {
        ProblemVO problem = problemService.getById(ProblemId);
        return Result.success(problem);
    }

    @GetMapping("/space/{spaceId}")
    @ApiOperation("根据空间ID查找题目")
    public Result<List<ProblemVO>> getBySpaceId(@PathVariable Long spaceId) {
        List<ProblemVO> problemVO = problemService.getBySpaceId(spaceId);
        return Result.success(problemVO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(ProblemPageQueryDTO problemPageQueryDTO) {
        PageResult pageResult = problemService.page(problemPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/space/{spaceId}")
    @ApiOperation("新建题目")
    @UserAuth
    public Result save(@PathVariable Long spaceId, @RequestBody ProblemCreateDTO problemCreateDTO, @ApiIgnore HttpServletRequest request) {
        problemService.save(problemCreateDTO, spaceId, request);
        return Result.success();
    }

    @DeleteMapping("/space/{spaceId}")
    @Admin
    @ApiOperation("删除题目")
    public Result delete(@PathVariable Long spaceId, @RequestBody ProblemStartOrStopDTO problemStartOrStopDTO, @ApiIgnore HttpServletRequest request) {
        problemService.startOrStop(spaceId, problemStartOrStopDTO, request);
        return Result.success();
    }
    @UserAuth
    @ApiIgnore("更新题目")
    @PutMapping("/space/{spaceId}")
    public Result update(@PathVariable Long spaceId ,@RequestBody ProblemUpdateDTO problemUpdateDTO,@ApiIgnore HttpServletRequest request){
        problemService.update(spaceId,problemUpdateDTO,request);
        return Result.success();

   }
//    @PutMapping("/{problemId}/space/{spaceId}/push")
//    @ApiOperation("更新题面")
//    @UserAuth
//    public Result commit(@PathVariable Long spaceId, @PathVariable Long problemId ,@RequestBody String markdownContent ,@ApiIgnore HttpServletRequest request){
//        problemService.pushContent(spaceId,problemId,markdownContent,request);
//        return null;
//        //problemService
//    }
    //@Admin
    @PutMapping("/{problemId}/space/{sourceSpaceId}/moveTo/{destinationSpaceId}")
    @ApiOperation("转移题目")
    @Admin
    @UserInOtherSpace
    public Result transfer(@PathVariable Long sourceSpaceId,@PathVariable Long problemId,@PathVariable Long destinationSpaceId,@ApiIgnore HttpServletRequest request ){
        problemService.transfer(sourceSpaceId,problemId,destinationSpaceId,request);
        return Result.success();
    }
    @PutMapping("/{problemId}/clone/{newSpaceId}")
    @ApiOperation("克隆题目")
    @UserInOtherSpace
    //@UserInOtherSapce
    @UserAuth
    public  Result clone(@PathVariable Long problemId,@PathVariable Long newSpaceId ,@ApiIgnore HttpServletRequest request){

      Long Id=  problemService.clone(problemId,request,newSpaceId);
      return Result.success(Id);
    }
    @PutMapping("/{problemId}/quote/{newSpaceId}")
    @ApiOperation("引用题目")
    @Admin
    @UserInOtherSpace
    public Result quote(@PathVariable Long problemId,@PathVariable Long newSpaceId,@ApiIgnore HttpServletRequest request){
        Long id=problemService.quote(problemId,newSpaceId,request);
        return Result.success(id);
    }
    /**
     * 从磁盘中获取md文件名称
     * @return
     */
    @GetMapping("/getProblemList")
    public Result findFileInDir() {
        // 假设文件在F盘的MD文件夹中
        File dir = new File("D:\\MD");
        if(dir.exists() && dir.isDirectory()) {
            List<String> names = new ArrayList<>();
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                String name = file.getName();
                names.add(name);
            }
            return Result.success(names);
        }
        return Result.error("not found dir");
    }

    /**
     * 读取md文件的内容
     */
    @GetMapping("/{fileName}")
    public Result getContentInMdFile(@PathVariable String fileName) {
        // 假设文件在F盘的MD文件夹中
        String filePath = "D:\\MD" + fileName;
        Path path = Paths.get(filePath);
        try {
            String content = String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
            return Result.success(content);
        } catch (IOException e) {
            throw new RuntimeException("file is not exist");
        }
    }

    /**
     *  文件内容替换
     * @param content
     * @param fileName
     * @return
     */
    @PostMapping()
    public Result updateMdFileContent(@RequestBody String content, @RequestBody String fileName) {
        // 指定MD文件路径
        String filePath = "D:\\MD\\" +fileName;
        // 使用Paths.get()方法创建Path对象
        Path path = Paths.get(filePath);
        try {
            // 将新的内容写入文件，覆盖原有内容
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
            return Result.success("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


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
    @GetMapping("/**")
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

    /**
     * 获取文件名
     * @param url
     * @return
     */
    private String extractFileNameFromURL(String url) {
        // 在这里解析出文件名，这里假设文件名是 URL 中最后一个斜杠后的部分
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1);
    }
    @ApiOperation("获取文件内容")
    @GetMapping("/fileContent")
    public Result getFileContent(@RequestParam Long projectId,@RequestParam String commitSha,@RequestParam String fileName) {
        //String
         List<String>  content =problemService.getContent(projectId,commitSha,fileName);
        return Result.success(content);
    }
    @ApiOperation("获取文件结构")
    @GetMapping("/{problemId}/struct")
    public Result getStruct(@PathVariable Long problemId){
      List<String> list=  problemService.getStruct(problemId);
      return  Result.success(list);
    }

    @ApiOperation("获取最新内容")
    @GetMapping("/{problemId}/laster")
    public  Result<CommitInfoVO> fetchLatest(@PathVariable Long problemId){
            return problemService.getInfo(problemId);
    }
    @ApiOperation("更新文件内容")
    @PostMapping("/{problemId}/space/{spaceId}")
    public  Result pushContent(@PathVariable Long problemId,@PathVariable Long spaceId ,@RequestBody CommitDTO commitDTO,@ApiIgnore HttpServletRequest request) {
        System.out.println(commitDTO);
        problemService.pushContent(spaceId,problemId,commitDTO,request);
        return Result.success();
    }
    @ApiOperation("获取所有版本commit值")
    @GetMapping("/{problemId}/space/{spaceId}/commits")
    public Result fetchCommitSha(@PathVariable Long problemId, @PathVariable Long spaceId){
        //problemService.fetchCommitSha(problemId);
        return  null;
    }
}
