package com.lcl.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lcl.constant.DeletedConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.OperationRecordConstant;
import com.lcl.context.BaseContext;
import com.lcl.dto.*;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.entity.SpaceProblem;
import com.lcl.exception.BaseException;
import com.lcl.exception.NameDuplicationException;
import com.lcl.mapper.ProblemMapper;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceProblemMapper;
import com.lcl.result.PageResult;
import com.lcl.result.Result;
import com.lcl.service.IpAndAgentService;
import com.lcl.service.OperationRecordService;
import com.lcl.service.ProblemMigrationService;
import com.lcl.service.ProblemService;
import com.lcl.vo.CommitInfoVO;
import com.lcl.vo.ProblemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private ProblemMigrationService problemMigrationService;
    @Autowired
    private SpaceMapper  spaceMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
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
        //TODO 一会把 save 改了
        //Problem byTitle = problemMapper.getByTitle(problemCreateDTO.getTitle());
        Problem byTitle=problemMapper.getById(one.getProblemId());
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
        problem.setSpaceId(spaceId);
        operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.UPDATE_PROBLEM);
        problemMapper.update(problem);


    }

//    @Override
//    public void pushContent(Long spaceId, Long problemId, String markdownContent, HttpServletRequest request) {
//        String key = "problem:" + problemId;
//        // 尝试设置key，如果key已经存在，就返回false
//        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "true", 1, TimeUnit.MINUTES);
//        if (success == null || !success) {
//            // 如果设置失败，就直接返回，不处理这个请求
//            throw new RuntimeException("Another request is processing the same problem.");
//        }
//        try {
//            // TODO: 进行你的业务处理，例如更新题面
//            List<String> Ip = ipAndAgentService.getInfo(request);
//            Problem byId = problemMapper.getById(problemId);
//            Space spaceById = spaceMapper.getById(spaceId);
//            gitlabService.pushContent(spaceById.getGitlabId(),byId.getGitlabId() ,markdownContent);
//
//        } finally {
//            // 无论业务处理是否成功，最后都删除这个key
//            redisTemplate.delete(key);
//        }
//
//
//
//    }



    @Override
    @Transactional
    public void transfer(Long sourceSpaceId, Long problemId, Long destinationSpaceId, HttpServletRequest request) {
        String key = "problem:" + problemId;
        // 尝试设置key，如果key已经存在，就返回false
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "true", 1, TimeUnit.MINUTES);
        if (success == null || !success) {
            // 如果设置失败，就直接返回，不处理这个请求
            throw new RuntimeException("Another request is processing the same problem.");
        }
        try {
            Space byId1 = spaceMapper.getById(destinationSpaceId);
            Space byId2 = spaceMapper.getById(sourceSpaceId);
            if(byId2==null|| byId1==null){
                throw  new BaseException(MessageConstant.SPACE_NOT_EXIST);
            }
            Problem byId3 = problemMapper.getById(problemId);
            if(byId3==null){
                throw  new BaseException(MessageConstant.PROBLEM_NOT_EXIST);
            }
            List<String> Ip = ipAndAgentService.getInfo(request);
            Problem byId = problemMapper.getById(problemId);
            byId.setSpaceId(destinationSpaceId);
            problemMapper.update(byId);
            Problem problem = new Problem();
            problem.setProblemId(problemId);
            problemMigrationService.transfer(sourceSpaceId,destinationSpaceId,problemId);
            operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.TRANSFER_PROBLEM);
        } finally {
            // 无论业务处理是否成功，最后都删除这个key
            redisTemplate.delete(key);
        }
        return;
    }

    @Override
    @Transactional
    public Long clone(Long problemId, HttpServletRequest request, Long newSpaceId) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        Problem byId = problemMapper.getById(problemId);
        if(byId==null){
            throw  new BaseException(MessageConstant.PROBLEM_NOT_EXIST);
        }
        Space space = spaceMapper.getById(newSpaceId);
        if(space==null){
                throw  new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }
        Long id= gitlabService.fork(byId.getGitlabId(),newSpaceId);
        Problem problem = new Problem();
        problem.setGitlabId(id);
        problem.setAuthor(BaseContext.getCurrentId());
        problem.setTitle(byId.getTitle());
        problem.setIsDeprecated(DeletedConstant.DISDELETED);
        problem.setForkedFrom(problemId);
        problem.setSpaceId(newSpaceId);
        problemMapper.save(problem);
        SpaceProblem spaceProblem = new SpaceProblem();
        spaceProblem.setIsDeleted(DeletedConstant.DISDELETED);
        spaceProblem.setProblemId(problem.getProblemId());
        spaceProblem.setIsReference(true);
        spaceProblem.setSpaceId(newSpaceId);
        spaceProblemMapper.save(spaceProblem);
        operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.CLONE_PROBLEM);
        return null;
    }

    @Override
    @Transactional
    public Long quote(Long problemId, Long newSpaceId, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
        Problem byId = problemMapper.getById(problemId);
        if(byId==null){
            throw  new BaseException(MessageConstant.PROBLEM_NOT_EXIST);
        }
        Space space = spaceMapper.getById(newSpaceId);
        if(space==null){
            throw  new BaseException(MessageConstant.SPACE_NOT_EXIST);
        }
        Problem problem = new Problem();
       problem.setIsDeprecated(byId.getIsDeprecated());
       problem.setGitlabId(byId.getGitlabId());
       problem.setSpaceId(newSpaceId);
       problem.setAuthor(byId.getAuthor());
       problem.setForkedFrom(byId.getForkedFrom());
        problemMapper.save(problem);
        operationRecordService.ProblemOperation(problem,Ip,OperationRecordConstant.REFERENCE_PROBLEM);
        return problem.getProblemId();
    }

    @Override
    public List<String> getContent(Long projectId, String commitSha, String fileName) {
        Problem byId = problemMapper.getById(projectId);
       return gitlabService.getContent(byId.getGitlabId(),commitSha,fileName);
    }

    @Override
    public List<String> getStruct(Long problemId) {
        Problem byId = problemMapper.getById(problemId);
        return  gitlabService.getStruct(byId.getGitlabId());
    }

    @Override
    @Transactional
    public void pushContent(Long spaceId, Long problemId, CommitDTO list, HttpServletRequest request) {
        List<String> Ip = ipAndAgentService.getInfo(request);
         Long currentId = BaseContext.getCurrentId();
         Problem byId = problemMapper.getById(problemId);
         gitlabService.pushContent(list.getActions(),currentId,list.getMessage(),byId.getGitlabId());
         problemMapper.update(byId);
        operationRecordService.ProblemOperation(byId,Ip,OperationRecordConstant.UPDATE_PROBLEM_CONTENT);
    }

    @Override
    public Result<CommitInfoVO> getInfo(Long problemId) {
        Problem byId = problemMapper.getById(problemId);
        return gitlabService.getInfo(byId.getGitlabId());
    }

}
