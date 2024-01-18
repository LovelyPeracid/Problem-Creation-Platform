package com.lcl.controller;

import cn.hutool.core.util.StrUtil;
import com.lcl.annotation.*;
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
    @Role(com.lcl.enumeration.Role.visitor)
    public Result save(@PathVariable Long spaceId, @RequestBody ProblemCreateDTO problemCreateDTO, @ApiIgnore HttpServletRequest request) {
        problemService.save(problemCreateDTO, spaceId, request);
        return Result.success();
    }

    @DeleteMapping("/space/{spaceId}")
    @Role(com.lcl.enumeration.Role.admin)
    @ApiOperation("删除题目")
    public Result delete(@PathVariable Long spaceId, @RequestBody ProblemStartOrStopDTO problemStartOrStopDTO, @ApiIgnore HttpServletRequest request) {
        problemService.startOrStop(spaceId, problemStartOrStopDTO, request);
        return Result.success();
    }
    @Role(com.lcl.enumeration.Role.user)
    @ApiIgnore("更新题目信息")
    @PutMapping("/space/{spaceId}")
    public Result update(@PathVariable Long spaceId ,@RequestBody ProblemUpdateDTO problemUpdateDTO,@ApiIgnore HttpServletRequest request){
        problemService.update(spaceId,problemUpdateDTO,request);
        return Result.success();

   }
    @PutMapping("/{problemId}/space/{sourceSpaceId}/moveTo/{destinationSpaceId}")
    @ApiOperation("转移题目")
    @Role(com.lcl.enumeration.Role.admin)
    @UserInOtherSpace
    public Result transfer(@PathVariable Long sourceSpaceId,@PathVariable Long problemId,@PathVariable Long destinationSpaceId,@ApiIgnore HttpServletRequest request ){
        problemService.transfer(sourceSpaceId,problemId,destinationSpaceId,request);
        return Result.success();
    }
    @PutMapping("/{problemId}/clone/{newSpaceId}")
    @ApiOperation("克隆题目")
    @UserInOtherSpace
    @Role(com.lcl.enumeration.Role.user)
    public  Result clone(@PathVariable Long problemId,@PathVariable Long newSpaceId ,@ApiIgnore HttpServletRequest request){

      Long Id=  problemService.clone(problemId,request,newSpaceId);
      return Result.success(Id);
    }
    @Role(com.lcl.enumeration.Role.admin)
    @PutMapping("/{problemId}/quote/{newSpaceId}")
    @ApiOperation("引用题目")
    @UserInOtherSpace
    public Result quote(@PathVariable Long problemId,@PathVariable Long newSpaceId,@ApiIgnore HttpServletRequest request){
        Long id=problemService.quote(problemId,newSpaceId,request);
        return Result.success(id);
    }

    @Role(com.lcl.enumeration.Role.visitor)
    @ApiOperation("获取文件内容")
    @GetMapping("/fileContent")
    public Result getFileContent(@RequestParam Long projectId,@RequestParam String commitSha,@RequestParam String fileName) {
        //String
         List<String>  content =problemService.getContent(projectId,commitSha,fileName);
        return Result.success(content);
    }
    @Role(com.lcl.enumeration.Role.visitor)
    @ApiOperation("获取文件结构,不传commitSha 默认")
    @GetMapping("/{problemId}/space/{spaceId}/struct")
    public Result getStruct(@PathVariable Long spaceId,@PathVariable Long problemId,@RequestParam(required = false) String commitSha){
      List<String> list=  problemService.getStruct(problemId,commitSha);
      return  Result.success(list);
    }
    @Role(com.lcl.enumeration.Role.visitor)
    @ApiOperation("获取最新的commit信息")
    @VisitorAuth
    @GetMapping("/{problemId}/space/{spaceId}/laster")
    public  Result<CommitInfoVO> fetchLatest(@PathVariable Long spaceId,@PathVariable Long problemId){
            return problemService.getInfo(problemId);
    }
    @Role(com.lcl.enumeration.Role.user)
    @ApiOperation("更新文件内容")
    @PostMapping("/{problemId}/space/{spaceId}")
    public  Result pushContent(@PathVariable Long spaceId ,@PathVariable Long problemId,@RequestBody CommitDTO commitDTO,@ApiIgnore HttpServletRequest request) {
        System.out.println(commitDTO);
        problemService.pushContent(spaceId,problemId,commitDTO,request);
        return Result.success();
    }
    @Role(com.lcl.enumeration.Role.visitor)
    @ApiOperation("获取所有版本commit值")
    @GetMapping("/{problemId}/space/{spaceId}/commits")
    public Result<List<CommitInfoVO>> fetchCommitSha(@PathVariable Long spaceId,@PathVariable Long problemId){
       List<CommitInfoVO>  list =problemService.fetchCommitSha(problemId);
        return  Result.success(list);
    }

    @Role(com.lcl.enumeration.Role.visitor)
    @GetMapping("/{problemId}/space/{spaceId}/diff")
    public Result fetchDiff(@PathVariable Long spaceId,@PathVariable Long problemId){
        return problemService.getDiff(problemId);
    }
}
