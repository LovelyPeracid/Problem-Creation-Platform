package com.lcl.service;

import com.lcl.dto.ProblemCreateDTO;
import com.lcl.dto.ProblemPageQueryDTO;
import com.lcl.dto.ProblemStartOrStopDTO;
import com.lcl.dto.ProblemUpdateDTO;
import com.lcl.result.PageResult;
import com.lcl.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface ProblemService {


    PageResult page(ProblemPageQueryDTO problemPageQueryDTO);

    ProblemVO getById(Long problemId);

    List<ProblemVO> getBySpaceId(Long spaceId);

    void save(ProblemCreateDTO problemCreateDTO, Long spaceId, HttpServletRequest request);

    void startOrStop(Long spaceId, ProblemStartOrStopDTO problemStartOrStopDTO, HttpServletRequest request);

    void update(Long spaceId, ProblemUpdateDTO problemUpdateDTO, HttpServletRequest request);

    void pushContent(Long spaceId, Long problemId, String markdownContent, HttpServletRequest request);
}
