package com.lcl.service;

import com.lcl.dto.SpaceCreateDTO;
import com.lcl.entity.Space;
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
}
