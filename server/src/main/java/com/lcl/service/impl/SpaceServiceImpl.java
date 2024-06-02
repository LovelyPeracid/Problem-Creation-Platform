package com.lcl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.util.StringUtil;
import com.lcl.constant.*;
import com.lcl.context.BaseContext;
import com.lcl.dto.SpaceCreateDTO;
import com.lcl.dto.SpaceUpdateDTO;
import com.lcl.entity.ExtUser;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.enumeration.ErrorCode;
import com.lcl.exception.BaseException;
import com.lcl.exception.BusinessException;
import com.lcl.exception.NameDuplicationException;
import com.lcl.exception.SpaceAddMemberException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceUserMapper;
import com.lcl.mapper.UserMapper;
import com.lcl.result.Result;
import com.lcl.service.GitlabService;
import com.lcl.service.IpAndAgentService;
import com.lcl.service.OperationRecordService;
import com.lcl.service.SpaceService;
import com.lcl.utils.RedisUtils;
import com.lcl.utils.UserHolder;
import com.lcl.vo.SpaceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class SpaceServiceImpl implements SpaceService {
    @Autowired
    private SpaceMapper spaceMapper;
    @Autowired
    private IpAndAgentService ipAndAgentService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SpaceUserMapper spaceUserMapper;
    @Autowired
    private GitlabService gitlabService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public SpaceVO getById(Long id) {
        Space space = spaceMapper.getById(id);
        SpaceVO spaceVO = new SpaceVO();
        BeanUtils.copyProperties(space, spaceVO);
        return spaceVO;
    }

    @Override
    public Result<List<Space>> getByIds(List<Long> ids) {
        List<Space> list = spaceMapper.getByIds(ids);
        System.out.println(list.get(0));
        return Result.success(list);
    }

    @Override
    @Transactional
    public Result save(SpaceCreateDTO spaceDTO, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        Space one = spaceMapper.getByName(spaceDTO.getSpaceName());
        if (one != null) {
            throw new NameDuplicationException(MessageConstant.NAME_DUPLICATION);
        }
        Space space = new Space();
        BeanUtils.copyProperties(spaceDTO, space);
        space.setIsDeleted(DeletedConstant.DISDELETED);
        //Long currentId = BaseContext.getCurrentId();
        // UserDTO user = UserHolder.getUser();
        Long currentId = UserHolder.getUser().getUserId();
//        Long currentId= UserHolder.getUser().getUserId();
//        Long id = spaceMapper.getPrivateSpaceByUserId(currentId);
//        if(id!=null){
//            throw new BaseException(MessageConstant.EXIST_PERSONAL_SPACE);
//        }
        Long GitlabId = gitlabService.CreateSpace(space);
        //TODO 测试
        // Long GitlabId=5L;
        space.setGitlabId(GitlabId);
//        space.setOwner(currentId);
        Boolean save = spaceMapper.save(space);
        if (save == null || save == false) {
            throw new BusinessException(ErrorCode.DATABASE_ERROR);
        }
        //  Space byName = spaceMapper.getByName(space.getSpaceName());
        SpaceUser spaceUser = new SpaceUser();
        spaceUser.setSpaceId(space.getSpaceId());
        spaceUser.setUserId(currentId);
        spaceUser.setRole(RoleConstant.ROOT);
        spaceUser.setIsSuspended(false);
        spaceUserMapper.addMember(spaceUser);
        operationRecordService.SpaceOperation(space, Ip, OperationRecordConstant.CREATE_SPACE);
        SpaceVO spaceVO = SpaceVO.ObjToVo(space);
        return Result.success(spaceVO);
        //return null;
    }

    @Override
    @Transactional
    public void deleteById(Long id, HttpServletRequest httpServletRequest) {
        List<String> Ip = ipAndAgentService.getInfo(httpServletRequest);
        Space space = new Space();
        space.setSpaceId(id);
        space.setIsDeleted(DeletedConstant.DELETED);
        spaceMapper.update(space);
        operationRecordService.SpaceOperation(space, Ip, OperationRecordConstant.DELETE_SPACE);
    }

    @Override
    @Transactional
    public void update(SpaceUpdateDTO spaceUpdateDTO, HttpServletRequest request) {
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateDTO, space);
        List<String> Ip = ipAndAgentService.getInfo(request);
        Space byName = spaceMapper.getByName(space.getSpaceName());
        if (byName != null) {
            throw new BaseException(MessageConstant.NAME_DUPLICATION);
        }
        spaceMapper.update(space);
        Space byId = spaceMapper.getById(space.getSpaceId());
        space.setGitlabId(byId.getGitlabId());
        if (StringUtil.isNotEmpty(space.getSpaceName())) {
            gitlabService.updateSpaceTitle(space);
        }
        operationRecordService.SpaceOperation(space, Ip, OperationRecordConstant.UPDATE_SPACE);
    }

    @Override
    @Transactional
    public void addMember(SpaceUser spaceUser, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        if (spaceUser.getRole() <= 1) {
            throw new BusinessException(MessageConstant.ROLE_ERROR);
        }
        ExtUser byId = userMapper.getById(spaceUser.getUserId());
        if (byId == null || byId.getIsSuspended()) {
            throw new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }

        SpaceUser one = spaceUserMapper.getByUserIdAndSpace(spaceUser.getSpaceId(), spaceUser.getUserId());
        if (one != null) {
            if (one.getIsSuspended()) {
                one.setIsSuspended(true);
                one.setRole(RoleConstant.VISITOR);
                spaceUserMapper.update(one);
                operationRecordService.SpaceUserOperation(one, Ip, OperationRecordConstant.ADD_SPACE_MEMBER);
                return;
            }
            throw new SpaceAddMemberException(MessageConstant.MEMBER_ALREADY_EXIST);
        }
        spaceUser.setIsSuspended(DeletedConstant.DISDELETED);
        //spaceUser.setRole(spaceUser);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //这里会多次添加
        String key = RedisConstants.SPACE_USER_LOCK + spaceUser.getSpaceId() + ":" + spaceUser.getUserId();
        boolean success = redisUtils.tryLock(key);

        if (!success) {
            throw new BusinessException(MessageConstant.REPEATED_SUBMISSION);
        }
        try {
            SpaceUser again = spaceUserMapper.getByUserIdAndSpace(spaceUser.getSpaceId(), spaceUser.getUserId());
            if (again != null) {
                throw new BusinessException(MessageConstant.USER_EXITST);
            }
            spaceUserMapper.addMember(spaceUser);
            operationRecordService.SpaceUserOperation(spaceUser, Ip, OperationRecordConstant.ADD_SPACE_MEMBER);
        } finally {
            redisUtils.unlock(key);
        }
    }

    /**
     * @param spaceUser:
     * @param request:
     * @return void
     * @author LovelyPeracid
     * @description
     * @date 2023/12/6 9:34
     */

    @Transactional
    @Override
    public void updaeSpaceUser(SpaceUser spaceUser, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        spaceUserMapper.update(spaceUser);
        operationRecordService.SpaceUserOperation(spaceUser, Ip, OperationRecordConstant.CHANGE_MEMBER_PERMISSION);
    }

    @Override
    @Transactional
    public void transference(SpaceUser spaceUser, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        spaceUserMapper.update(spaceUser);
        SpaceUser currendUser = new SpaceUser();
        currendUser.setUserId(BaseContext.getCurrentId());
        currendUser.setUserSpaceId(spaceUser.getUserSpaceId());
        currendUser.setRole(2);
        Space byId = spaceMapper.getById(spaceUser.getSpaceId());
        //byId.setOwner(spaceUser.getUserId());
        spaceMapper.update(byId);
        spaceUserMapper.update(currendUser);
        operationRecordService.SpaceUserOperation(spaceUser, Ip, OperationRecordConstant.CHANGE_MEMBER_PERMISSION);
        operationRecordService.SpaceUserOperation(currendUser, Ip, OperationRecordConstant.TRANSFER_ROOT);
    }

    @Override
    public Long getPrivateSpaceByUserId(Long userId) {
        return spaceMapper.getPrivateSpaceByUserId(userId);
    }

    @Override
    public List<SpaceVO> getByUserId(Long userId) {
        List<Space> list = spaceUserMapper.getByUserId(userId);
        List<SpaceVO> spaceVOS = new ArrayList<>();
        for (Space space : list) {
            SpaceVO spaceVO = new SpaceVO();
            BeanUtil.copyProperties(space, spaceVO);
            spaceVOS.add(spaceVO);
            System.out.println(spaceVO);
        }
        //List<Space>
        return spaceVOS;
    }

//    @Override
//    public void updaeSpaceUser(SpaceUserUpdateDTO spaceUserUpdateDTO, HttpServletRequest request) {
//        List<String> Ip = ipAndAgentService.getInfo(request);
//
//    }


}
