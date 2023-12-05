package com.lcl.controller;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lcl.dto.SpaceCreateDTO;
import com.lcl.dto.SpaceDTO;
import com.lcl.dto.SpaceQueryDTO;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.SpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@RestController
@RequestMapping("/space")
@Api(tags = "空间接口")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;

    @GetMapping("/{id}")
    @ApiOperation("查询id")
    public Result<Space> getSpace(@PathVariable Long id) {
      Space space= spaceService.getById(id);
        return  Result.success(space);
    }

    @GetMapping
    @ApiOperation("批量查询")
    public Result<List<Space>> queryList(@RequestParam List<Long> ids) {
        return spaceService.getByIds(ids);
    }
//    @GetMapping("/like")
//    @ApiOperation("模糊查询")
//    public Result FuzzyQuery(@RequestParam SpaceQueryDTO spaceQueryDTO) {
//        List<Space> one = spaceService.list(spaceQueryDTO);
//        return Result.success(one);
//    }
    //@GetMapping("/page")
    //@ApiOperation("空间分页查询")
    //public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
    //    PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
    //
    //    return  Result.success(pageResult);
    //}

    @PostMapping("/space")
    @ApiOperation("创建空间")
    public Result createProject(@Valid @RequestBody SpaceCreateDTO space, @ApiIgnore HttpServletRequest request) {
        spaceService.save(space,request);
        return Result.success();
    }

}
