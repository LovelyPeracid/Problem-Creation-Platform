package com.lcl.service;

import com.lcl.dto.SpaceCreateDTO;
import com.lcl.dto.SpaceUpdateDTO;
import com.lcl.entity.SpaceUser;
import com.lcl.result.Result;
import com.lcl.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface SpaceService {
    SpaceVO getById(Long id);

    Result getByIds(List<Long> ids);

    Result save(SpaceCreateDTO space, HttpServletRequest request);

    void deleteById(Long id, HttpServletRequest httpServletRequest);

    void update(SpaceUpdateDTO spaceUpdateDTO, HttpServletRequest request);


    void addMember(SpaceUser spaceUser, HttpServletRequest request);

    void updaeSpaceUser(SpaceUser spaceUser, HttpServletRequest request);

    void transference(SpaceUser spaceUser, HttpServletRequest request);

    Long getPrivateSpaceByUserId(Long userId);

    List<SpaceVO> getByUserId(Long userId);

    // void updaeSpaceUser(SpaceUserUpdateDTO spaceUserUpdateDTO, HttpServletRequest request);
}
