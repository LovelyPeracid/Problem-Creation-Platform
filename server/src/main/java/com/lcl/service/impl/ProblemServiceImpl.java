package com.lcl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcl.constant.DeletedConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.OperationRecordConstant;
import com.lcl.context.BaseContext;
import com.lcl.dto.ProblemCreateDTO;
import com.lcl.dto.ProblemPageQueryDTO;
import com.lcl.entity.Problem;
import com.lcl.entity.SpaceProblem;
import com.lcl.exception.BaseException;
import com.lcl.exception.NameDuplicationException;
import com.lcl.mapper.ProblemMapper;
import com.lcl.mapper.SpaceProblemMapper;
import com.lcl.result.PageResult;
import com.lcl.service.IpAndAgentService;
import com.lcl.service.OperationRecordService;
import com.lcl.service.ProblemService;
import com.lcl.vo.ProblemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private IpAndAgentService ipAndAgentService;
    @Autowired
    private SpaceProblemMapper spaceProblemMapper;
    @Autowired
    private GitlabServiceImpl gitlabService;


    @Override
    public PageResult page(ProblemPageQueryDTO problemPageQueryDTO) {
        PageHelper.startPage(problemPageQueryDTO.getPage(),problemPageQueryDTO.getPageSize());
        Page<ProblemVO> page=problemMapper.page(problemPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult()) ;

    }

    @Override
    public ProblemVO getById(Long problemId) {
        ProblemVO problem= problemMapper.getById(problemId);
        return problem;
    }

    @Override
    public List<ProblemVO> getBySpaceId(Long spaceId) {
       List<ProblemVO> problem =problemMapper.getBySpaceId(spaceId);
        return problem;
    }
    @Override
    @Transactional
    public void save(ProblemCreateDTO problemCreateDTO, Long spaceId, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        Problem problem = problemMapper.getByTitle(problemCreateDTO.getTitle());
        if(problemCreateDTO.getForkedFrom()!=null){
            ProblemVO forked =problemMapper.getById(problemCreateDTO.getForkedFrom());
            if(forked==null||forked.getIsDeprecated())
               throw new BaseException(MessageConstant.FORKED_PROBLEM_NOT_EXIST);
        }
        if(problem!=null){
            throw new NameDuplicationException(MessageConstant.NAME_DUPLICATION);
        }
        Problem one =new Problem();
        BeanUtils.copyProperties(problemCreateDTO,one);
        one.setSpaceId(spaceId);
        one.setAuthor(BaseContext.getCurrentId());
        Long id= gitlabService.CreateProblem(one);
        one.setGitlabId(id);
        problemMapper.save(one);
        Problem byTitle = problemMapper.getByTitle(problemCreateDTO.getTitle());
        SpaceProblem spaceProblem = new SpaceProblem();
        spaceProblem.setProblemId(byTitle.getProblemId());
        spaceProblem.setSpaceId(spaceId);
        spaceProblem.setIsDeleted(DeletedConstant.DISDELETED);
        spaceProblemMapper.save(spaceProblem);

        operationRecordService.ProblemOperation(one,Ip, OperationRecordConstant.CREATE_QUESTION);

    }
}
