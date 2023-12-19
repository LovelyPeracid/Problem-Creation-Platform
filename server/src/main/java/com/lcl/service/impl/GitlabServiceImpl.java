package com.lcl.service.impl;

import cn.hutool.core.util.IdUtil;
import com.lcl.constant.GitlabConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.ProjectStructConstant;
import com.lcl.context.BaseContext;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.exception.BaseException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.service.GitlabService;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Commit pushContent(Long spaceId, Long problemId, String markdownContent){
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            CommitAction action = new CommitAction();
            action.setAction(CommitAction.Action.CREATE);
            action.setFilePath(ProjectStructConstant.ZH_MD);
            action.setContent(markdownContent);
            // 提交文件到项目
            Commit commitMessage = gitLabApi.getCommitsApi().createCommit(problemId, ProjectStructConstant.BASE_BRANCH, "Commit message", null, null, null, action);
            return  commitMessage;
        } catch (GitLabApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    @Override
    public Commit updateContent(Long spaceId, Long problemId, String markdownContent){
        try {
            // 创建一个GitLabApi实例
            // GitLabApi gitLabApi = new GitLabApi("https://your.gitlab.server", "YOUR_PRIVATE_TOKEN");
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            // 创建一个CommitAction对象
            CommitAction action = new CommitAction();
            action.setAction(CommitAction.Action.UPDATE);
            action.setFilePath(ProjectStructConstant.ZH_MD);
            action.setContent(markdownContent);
            // 提交文件到项目
            Commit commitMessage = gitLabApi.getCommitsApi().createCommit(problemId, ProjectStructConstant.BASE_BRANCH, "Commit message", null, null, null, action);
            return commitMessage;

        } catch (GitLabApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
  //  @Override
    public void  revert(Long problemId,String commitSha){
        try {
            // 创建一个GitLabApi实例
            // GitLabApi gitLabApi = new GitLabApi("https://your.gitlab.server", "YOUR_PRIVATE_TOKEN");
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            // 创建一个CommitAction对象
            CommitsApi commitsApi = gitLabApi.getCommitsApi();
            Commit commit = commitsApi.revertCommit(problemId, commitSha, ProjectStructConstant.BASE_BRANCH);

        } catch (GitLabApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
      //  return null;
    }

    //@Override
    public void GetCommitById(Long projectId) throws GitLabApiException {
        GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
        List<Commit> commits = gitLabApi.getCommitsApi().getCommits(projectId);
// 打印每个commit的SHA值
        for (Commit commit : commits) {
            System.out.println("Commit SHA: " + commit.getId()+"message"+commit.getMessage());
        }
    }
//    @Override
//    public String  GetContent(Long projectId){
//        try {
//            // 创建一个GitLabApi实例
//            //  String gitLabUrl = "http://your.gitlab.server";
//            //     String privateToken = "YOUR_PRIVATE_TOKEN";
//            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
//
//            // 获取项目
//           // Long  projectId = 31L; // 你的项目ID
//            Project project = gitLabApi.getProjectApi().getProject(projectId);
//
//            // 获取分支名为"master"的文件列表
//            RepositoryFile file = gitLabApi.getRepositoryFileApi().getFile("content", projectId, "master");
//            String content = file.getDecodedContentAsString();
//            System.out.println("File content: " + content);
//            return content;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }



}
