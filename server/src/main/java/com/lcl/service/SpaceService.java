package com.lcl.service;

import com.lcl.dto.SpaceCreateDTO;
import com.lcl.dto.SpaceUpdateDTO;
import com.lcl.dto.SpaceUserUpdateDTO;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceUser;
import com.lcl.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface SpaceService {
    Space getById(Long id);

    Result getByIds(List<Long> ids);

    void save( SpaceCreateDTO space, HttpServletRequest request);

    void deleteById(Long id, HttpServletRequest httpServletRequest);

    void update(SpaceUpdateDTO spaceUpdateDTO, HttpServletRequest request);


    void addMember(SpaceUser spaceUser, HttpServletRequest request);

    void updaeSpaceUser(SpaceUser spaceUser, HttpServletRequest request);

    // void updaeSpaceUser(SpaceUserUpdateDTO spaceUserUpdateDTO, HttpServletRequest request);
}
