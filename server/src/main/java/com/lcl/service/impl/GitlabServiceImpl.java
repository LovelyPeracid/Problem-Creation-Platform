package com.lcl.service.impl;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.ServiceException;
import com.lcl.constant.GitlabConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.exception.BaseException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.mapper.SpaceProblemMapper;
import com.lcl.result.Result;
import com.lcl.service.GitlabService;
import com.lcl.service.SpaceService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.GroupApi;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupParams;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LovelyPeracid
 */
@Service
public class GitlabServiceImpl implements GitlabService {
    private  String baseUrl=GitlabConstant.baseUrl;
    private  String token=GitlabConstant.token;
    @Autowired
    private SpaceMapper spaceMapper;
    @Override
    public Long CreateSpace(Space space) {
        try {
            // 创建一个连接到 GitLab 服务器的 GitLabApi 实例
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            GroupParams group = new GroupParams();
            group.withName(space.getSpaceName());
            String id = IdUtil.getSnowflakeNextIdStr();
            group.withPath(id);
            Group newGroup = gitLabApi.getGroupApi().createGroup(group);
            System.out.println("New group created with ID: " + newGroup.getId());
            return newGroup.getId();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new  BaseException(MessageConstant.UNKNOWN_ERROR);
        }
    }

    @Override
   // @Transactional
    public Long CreateProblem(Problem problem) {
        try {
            Long problemId = problem.getProblemId();
            Space byId = spaceMapper.getById(problem.getSpaceId());
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            String path = IdUtil.getSnowflakeNextIdStr();
            Project project = new Project()
                    .withName(path)
                    .withNamespaceId(byId.getGitlabId())
                    .withVisibility(Visibility.PUBLIC)
                    .withApprovalsBeforeMerge(1);
            // 使用GitLabApi创建新的项目
            Project newProject = gitLabApi.getProjectApi().createProject(project);
            return newProject.getId();

        } catch (Exception e) {
            e.printStackTrace();
            throw  new BaseException(MessageConstant.UNKNOWN_ERROR);
        }
    }

    @Override
    public void commit() {

    }


}
