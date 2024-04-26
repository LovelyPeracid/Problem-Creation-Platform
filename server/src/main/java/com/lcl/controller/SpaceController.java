package com.lcl.controller;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lcl.annotation.*;
import com.lcl.constant.MessageConstant;
import com.lcl.dto.*;
import com.lcl.entity.OperationRecord;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceProblem;
import com.lcl.entity.SpaceUser;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.SpaceService;
import com.lcl.vo.SpaceVO;
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
import java.util.Objects;

/**
 * @author LovelyPeracid
 */
@RestController
@RequestMapping("/space")
@Api(tags = "空间接口")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;

    @GetMapping("/private/{userId}")
    @ApiOperation("根据用户Id 查询是否拥有个人空间")
    public Result getPrivateSpace(@PathVariable Long userId){
       Long id= spaceService.getPrivateSpaceByUserId(userId);
       return  Result.success(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id获取信息")
    public Result<SpaceVO> getSpace(@PathVariable Long id) {
      SpaceVO space= spaceService.getById(id);
        return  Result.success(space);
    }
    @GetMapping(" /users/{userId}/spaces")
    @ApiOperation("根据用户id获取关联空间")
    public Result<List<SpaceVO>> getByUserId(@PathVariable Long userId){
        List<SpaceVO> list=spaceService.getByUserId(userId);
        return Result.success(list);
    }

    @GetMapping
    @ApiOperation("批量查询")
    public Result<List<SpaceVO>> queryList(@RequestParam List<Long> ids) {
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
   // @Role(com.lcl.enumeration.Role.admin)
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
    @Admin
    @ApiOperation("更新空间,第一个参数也传的是spaceId 是为了统一鉴权方便 ")
    public  Result update(@PathVariable Long id ,@Valid @RequestBody SpaceUpdateDTO spaceUpdateDTO, @ApiIgnore HttpServletRequest request){
        spaceUpdateDTO.setSpaceId(id);
        spaceService.update(spaceUpdateDTO,request);
        return  Result.success();
    }
    //@Admin
    @Role(com.lcl.enumeration.Role.admin)
    @PostMapping("/{spaceId}/member/{userId}")
    @ApiOperation("添加空间成员")
    public  Result addMember(@PathVariable Long spaceId, Long userId,@RequestBody SpaceAddMemberDTO spaceAddMemberDTO , @ApiIgnore HttpServletRequest request){
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(spaceAddMemberDTO,spaceUser);
        spaceUser.setSpaceId(spaceId);
        spaceUser.setUserId(userId);
        spaceService.addMember(spaceUser,request);
        return  Result.success();
    }
    //@UserAuth
    /**
     * @param spaceId:
     * @param userId:
     * @param spaceUserUpdateDTO:
     * @param request:
     * @return Result
     * @author LovelyPeracid
     * @description
     * @date 2023/12/19 10:55
     */

    @Admin
    @HigherRole
    @PutMapping("/{spaceId}/member/{userId}")
    @ApiOperation("更新空间成员")
    public  Result update(@PathVariable Long spaceId ,Long userId ,@RequestBody SpaceUserUpdateDTO spaceUserUpdateDTO,@ApiIgnore HttpServletRequest request){
            SpaceUser spaceUser = new SpaceUser();
            BeanUtils.copyProperties(spaceUserUpdateDTO,spaceUser);
            spaceUser.setSpaceId(spaceId);
            spaceUser.setUserId(userId);
            spaceService.updaeSpaceUser(spaceUser,request);
            return Result.success();

    }
    @Authenticate
    @Admin
    @PutMapping("/{spaceId}/transference")
    @ApiOperation("转让空间所有权")
    public Result transference(@PathVariable Long spaceId,@RequestBody SpaceUserUpdateDTO spaceUserUpdateDTO,@ApiIgnore HttpServletRequest request){
        //spaceService.transference()
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(spaceUserUpdateDTO,spaceUser);
        spaceUser.setSpaceId(spaceId);
        spaceService.transference(spaceUser,request);
        return Result.success();
    }
}
