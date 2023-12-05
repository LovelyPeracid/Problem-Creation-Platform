package com.lcl.service.impl;

import com.lcl.constant.MessageConstant;
import com.lcl.constant.OperationRecordConstant;
import com.lcl.dto.SpaceCreateDTO;
import com.lcl.entity.Space;
import com.lcl.exception.NameDuplicationException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.result.Result;
import com.lcl.service.IpAndAgentService;
import com.lcl.service.OperationRecordService;
import com.lcl.service.SpaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Space getById(Long id) {
        return  spaceMapper.getById(id);
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
        System.out.println(spaceDTO.getOwner());
        Space space = new Space();
        BeanUtils.copyProperties(spaceDTO,space);
        spaceMapper.save(space);
        Space byName = spaceMapper.getByName(space.getSpaceName());
       operationRecordService.SpaceOperation(byName,Ip, OperationRecordConstant.CREATE_SPACE);
    }
}
