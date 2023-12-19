package com.lcl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcl.constant.DeletedConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.OperationRecordConstant;
import com.lcl.context.BaseContext;
import com.lcl.dto.ProblemCreateDTO;
import com.lcl.dto.ProblemPageQueryDTO;
import com.lcl.dto.ProblemStartOrStopDTO;
import com.lcl.dto.ProblemUpdateDTO;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceProblem;
import com.lcl.exception.BaseException;
import com.lcl.exception.NameDuplicationException;
import com.lcl.mapper.ProblemMapper;
import com.lcl.mapper.SpaceMapper;
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

    @Autowired
    private SpaceMapper  spaceMapper;

    @Override
    public PageResult page(ProblemPageQueryDTO problemPageQueryDTO) {
        PageHelper.startPage(problemPageQueryDTO.getPage(),problemPageQueryDTO.getPageSize());
        Page<ProblemVO> page=problemMapper.page(problemPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult()) ;

    }

    @Override
    public ProblemVO getById(Long problemId) {
        Problem problem= problemMapper.getById(problemId);
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problem,problemVO);
        return problemVO;
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
            Problem forked =problemMapper.getById(problemCreateDTO.getForkedFrom());
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
        operationRecordService.ProblemOperation(one,Ip, OperationRecordConstant.CREATE_PROBLEM);

    }

    @Override
    @Transactional
    public void startOrStop(Long spaceId, ProblemStartOrStopDTO problemStartOrStopDTO, HttpServletRequest request) {
        Problem problem = new Problem();
        List<String> Ip = ipAndAgentService.getInfo(request);
        BeanUtils.copyProperties(problemStartOrStopDTO,problem);
        problem.setSpaceId(spaceId);
        problemMapper.update(problem);
        String type=null;
        if(problem.getIsDeprecated()){
            type=OperationRecordConstant.PROBLEM_STATUS_DISABLED;
        }
        else{
            type=OperationRecordConstant.PROBLEM_STATUS_ENABLED;
        }
        operationRecordService.ProblemOperation(problem,Ip,type);
    }

    @Override
    @Transactional
    public void update(Long spaceId, ProblemUpdateDTO problemUpdateDTO, HttpServletRequest request) {
        Problem problem = new Problem();
        List<String> Ip = ipAndAgentService.getInfo(request);
        BeanUtils.copyProperties(problemUpdateDTO,problem);
        if(problemUpdateDTO.getSpaceId()!=null){
            SpaceProblem spaceProblem=   spaceProblemMapper.getBySpaceIdAndProblemId(problem);
            spaceProblemMapper.update(spaceProblem);
            operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.TRANSFER_PROBLEM);
        }
        else {
        problem.setSpaceId(spaceId);
        operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.UPDATE_PROBLEM);
        }
        problemMapper.update(problem);
    }

    @Override
    public void pushContent(Long spaceId, Long problemId, String markdownContent, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);

        Problem byId = problemMapper.getById(problemId);
        Space spaceById = spaceMapper.getById(spaceId);
        gitlabService.pushContent(spaceById.getGitlabId(),byId.getGitlabId() ,markdownContent);
    }
}
