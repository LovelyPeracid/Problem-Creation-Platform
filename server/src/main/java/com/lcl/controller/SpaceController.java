package com.lcl.controller;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lcl.annotation.Authenticate;
import com.lcl.dto.*;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceProblem;
import com.lcl.entity.SpaceUser;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.SpaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.POST;
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
    /**
     * @param space:
     * @param request:
     * @return Result
     * @author LovelyPeracid
     * &#064;description  创建
     * &#064;date  2023/12/5 9:55
     */

    @PostMapping("/space")
    @ApiOperation("创建空间")
    public Result createProject(@Valid @RequestBody SpaceCreateDTO space, @ApiIgnore HttpServletRequest request) {
        spaceService.save(space,request);
        return Result.success();
    }
    @Authenticate
    @DeleteMapping("/{id}")
    @ApiOperation("删除空间")
    public  Result delete(@PathVariable Long id,@ApiIgnore HttpServletRequest httpServletRequest){
        spaceService.deleteById(id,httpServletRequest);
        return  Result.success();

    }
    @PutMapping("/{id}")
    @ApiOperation("更新空间")
    public  Result update(@PathVariable Long id ,@Valid @RequestBody SpaceUpdateDTO spaceUpdateDTO, @ApiIgnore HttpServletRequest request){
        spaceUpdateDTO.setSpaceId(id);
        spaceService.update(spaceUpdateDTO,request);
        return  Result.success();
    }
    @PostMapping("/{spaceId}/member")
    @ApiOperation("添加空间成员")
    public  Result addMember(@PathVariable Long spaceId ,@RequestBody SpaceAddMemberDTO spaceAddMemberDTO , @ApiIgnore HttpServletRequest request){
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(spaceAddMemberDTO,spaceUser);
        spaceUser.setSpaceId(spaceId);
        spaceService.addMember(spaceUser,request);
        return  Result.success();
    }
    @PutMapping("/{spaceId}/member")
    @ApiOperation("更新空间成员")
    public  Result update(@PathVariable Long spaceId ,@RequestBody SpaceUserUpdateDTO spaceUserUpdateDTO,@ApiIgnore HttpServletRequest request){
            SpaceUser spaceUser = new SpaceUser();
            BeanUtils.copyProperties(spaceUserUpdateDTO,spaceUser);
            spaceUser.setSpaceId(spaceId);
            spaceService.updaeSpaceUser(spaceUser,request);
            return Result.success();

    }
//    @PostMapping("/{spaceId}/problem")
//    @ApiOperation("添加题目")
//    public Result addProblem(@PathVariable Long spaceId, @RequestBody SpaceProblemDTO spaceProblemDTO,@ApiIgnore HttpServletRequest request){
//
//
//    }
}
