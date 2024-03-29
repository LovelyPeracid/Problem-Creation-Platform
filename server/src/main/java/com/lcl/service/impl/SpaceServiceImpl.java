package com.lcl.service.impl;

import com.lcl.constant.*;
import com.lcl.context.BaseContext;
import com.lcl.dto.SpaceCreateDTO;
import com.lcl.dto.SpaceUpdateDTO;
import com.lcl.dto.SpaceUserUpdateDTO;
import com.lcl.entity.ExtUser;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.exception.BaseException;
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
import com.lcl.vo.SpaceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Override
    public SpaceVO getById(Long id) {
       Space space = spaceMapper.getById(id);
       SpaceVO spaceVO = new SpaceVO();
        BeanUtils.copyProperties(space,spaceVO);
        return spaceVO;
    }

    @Override
    public Result<List<Space>> getByIds(List<Long> ids) {
       List<Space> list= spaceMapper.getByIds(ids);
        System.out.println(list.get(0));
        return Result.success(list);
    }

    @Override
    @Transactional
    public void save( SpaceCreateDTO spaceDTO, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        Space one = spaceMapper.getByName(spaceDTO.getSpaceName());
        if (one != null) {
            throw  new NameDuplicationException(MessageConstant.NAME_DUPLICATION);
        }
        //System.out.println(spaceDTO.getOwner());
        Space space = new Space();
        BeanUtils.copyProperties(spaceDTO,space);
        space.setIsDeleted(DeletedConstant.DISDELETED);

        Long GitlabId  = gitlabService.CreateSpace(space);
        space.setGitlabId(GitlabId);
        spaceMapper.save(space);
        Space byName = spaceMapper.getByName(space.getSpaceName());
        operationRecordService.SpaceOperation(byName,Ip, OperationRecordConstant.CREATE_SPACE);

       // spaceUserMapper.addMember();
    }

    @Override
    @Transactional
    public void deleteById(Long id, HttpServletRequest httpServletRequest) {
        List<String> Ip = ipAndAgentService.getInfo(httpServletRequest);
        Space space = new Space();
        space.setSpaceId(id);
        space.setIsDeleted(DeletedConstant.DELETED);
        spaceMapper.update(space);
        operationRecordService.SpaceOperation(space,Ip, OperationRecordConstant.DELETE_SPACE);
    }

    @Override
    @Transactional
    public void update(SpaceUpdateDTO spaceUpdateDTO, HttpServletRequest request) {
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateDTO,space);
        List<String> Ip = ipAndAgentService.getInfo(request);
        spaceMapper.update(space);
        operationRecordService.SpaceOperation(space,Ip, OperationRecordConstant.UPDATE_SPACE);
    }

    @Override
    @Transactional
    public void addMember(SpaceUser spaceUser, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        SpaceUser one= spaceUserMapper.getByUserId(spaceUser.getSpaceId(),spaceUser.getUserId());
        ExtUser byId = userMapper.getById(spaceUser.getUserId());
        if(byId==null||byId.getIsSuspended()){
            throw  new BaseException(MessageConstant.ACCOUNT_LOCKED);
        }
        if(one!=null){
            if(one.getIsSuspended()){
                one.setIsSuspended(true);
                one.setRole(RoleConstant.VISITOR);
                spaceUserMapper.update(one);
                operationRecordService.SpaceUserOperation(one,Ip,OperationRecordConstant.ADD_SPACE_MEMBER);
                return;
            }
            throw new SpaceAddMemberException(MessageConstant.MEMBER_ALREADY_EXIST);
        }
        spaceUser.setIsSuspended(DeletedConstant.DISDELETED);
        spaceUserMapper.addMember(spaceUser);
        operationRecordService.SpaceUserOperation(spaceUser,Ip,OperationRecordConstant.ADD_SPACE_MEMBER);
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
        operationRecordService.SpaceUserOperation(spaceUser,Ip,OperationRecordConstant.CHANGE_MEMBER_PERMISSION);
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
        byId.setOwner(spaceUser.getUserId());
        spaceMapper.update(byId);
        spaceUserMapper.update(currendUser);
        operationRecordService.SpaceUserOperation(spaceUser,Ip,OperationRecordConstant.CHANGE_MEMBER_PERMISSION);
        operationRecordService.SpaceUserOperation(currendUser,Ip,OperationRecordConstant.TRANSFER_ROOT);
    }

    @Override
    public Long getPrivateSpaceByUserId(Long userId) {
       return spaceMapper.getPrivateSpaceByUserId(userId);
    }

//    @Override
//    public void updaeSpaceUser(SpaceUserUpdateDTO spaceUserUpdateDTO, HttpServletRequest request) {
//        List<String> Ip = ipAndAgentService.getInfo(request);
//
//    }


}
