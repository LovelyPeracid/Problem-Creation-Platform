package com.lcl.controller;

import com.lcl.dto.ProblemCreateDTO;
import com.lcl.dto.ProblemPageQueryDTO;
import com.lcl.dto.UserPageQueryDTO;
import com.lcl.entity.Problem;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.ProblemService;
import com.lcl.vo.ProblemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public Result<ProblemVO> getById(@PathVariable Long ProblemId){
       ProblemVO problem= problemService.getById(ProblemId);
        return  Result.success(problem);
    }
    @GetMapping("/space/{spaceId}")
    @ApiOperation("根据空间ID查找题目")
    public Result<List< ProblemVO>> getBySpaceId(@PathVariable Long spaceId){
       List< ProblemVO>  problemVO =problemService.getBySpaceId(spaceId);
        return Result.success(problemVO);
    }
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(ProblemPageQueryDTO problemPageQueryDTO){
        PageResult pageResult=problemService.page(problemPageQueryDTO);
        return  Result.success(pageResult);
    }
    @PostMapping("/space/{spaceId}")
    @ApiOperation("新建题目")
    public Result save(@PathVariable Long spaceId ,@RequestBody ProblemCreateDTO problemCreateDTO, @ApiIgnore HttpServletRequest request){
            problemService.save(problemCreateDTO,spaceId,request);
            return Result.success();
    }
  //  @DeleteMapping("/space/{spaceId}")
   // public  Result delete(@PathVariable Long spaceId ,@RequestBody )

}
