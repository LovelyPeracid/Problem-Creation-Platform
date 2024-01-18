package com.lcl.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.util.StringUtil;
import com.lcl.constant.GitlabConstant;
import com.lcl.constant.MessageConstant;
import com.lcl.constant.ProjectStructConstant;
import com.lcl.dto.ActionDTO;
import com.lcl.entity.Problem;
import com.lcl.entity.Space;
import com.lcl.exception.BaseException;
import com.lcl.mapper.SpaceMapper;
import com.lcl.result.Result;
import com.lcl.service.GitlabService;
import com.lcl.vo.CommitInfoVO;
import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class GitlabServiceImpl implements GitlabService {
    private String baseUrl = GitlabConstant.baseUrl;
    private String token = GitlabConstant.token;
    @Autowired
    private SpaceMapper spaceMapper;

    @Override
    public Long CreateSpace(Space space) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            GroupParams group = new GroupParams();
          //  group.withName(space.getSpaceName());
            String id=space.getSpaceName();
            if(space.isType()){
                id=id+"-personl";
            }
            group.withName(id);
            group.withPath(space.getSpaceName());
            Group newGroup = gitLabApi.getGroupApi().createGroup(group);
            //System.out.println("New group created with ID: " + newGroup.getId());
            return newGroup.getId();
        } catch (Exception e) {
          ///  System.out.println("Error: " + e.getMessage());
            throw new BaseException(e.getMessage());
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
            CommitAction action = new CommitAction();
            action.setAction(CommitAction.Action.CREATE);
            action.setFilePath(ProjectStructConstant.ZH_MD);
            action.setContent("此处开始编辑");
            // 提交文件到项目
            Commit commitMessage = gitLabApi.getCommitsApi().createCommit(newProject.getId(), ProjectStructConstant.BASE_BRANCH, "Commit message", null, null, null, action);

            return newProject.getId();

        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(MessageConstant.UNKNOWN_ERROR);
        }
    }

    @Override
    public void commit() {

    }

    @Override
    public Commit pushContent(Long spaceId, Long problemId, String markdownContent) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            CommitAction action = new CommitAction();
            action.setAction(CommitAction.Action.CREATE);
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

    @Override
    public Commit updateContent(Long spaceId, Long problemId, String markdownContent) {
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

    @Override
    public List<String> getContent(Long projectId, String commitSha, String fileName) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            //System.out.println(commitSha);
            RepositoryFile file = gitLabApi.getRepositoryFileApi().getFile(fileName, projectId, commitSha);
            String text = file.getContent();
            List<String> list = new ArrayList<>();
            list.add(text);
            //file.getEncoding()
            list.add(file.getLastCommitId());
            return list;
        } catch (GitLabApiException e) {
            throw new BaseException(e.getMessage());
        }
    }

    public void revert(Long problemId, String commitSha) {
        //  @Override
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
            System.out.println("Commit SHA: " + commit.getId() + "message" + commit.getMessage());
        }
    }

    public Long fork(Long gitlabId, Long spaceId) {
        try {
            // 创建一个 GitLabApi 实例
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);

            // 获取你想要 fork 的项目
            // projectId = 33;
            Project originalProject = gitLabApi.getProjectApi().getProject(gitlabId);

            // 在指定的 group 下 fork 一个项目
            //Integer groupId = 3;
            Project forkedProject = gitLabApi.getProjectApi().forkProject(originalProject, spaceId);
            return forkedProject.getId();
            //  System.out.println("Forked project ID: " + forkedProject.getId());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            /// throw new BaseException(MessageConstant)
            return null;
        }
        //    return null;
    }

    @Override
    public List<String> getStruct(Long projectId) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            List<TreeItem> items = gitLabApi.getRepositoryApi().getTree(projectId, null, null);
            List<String> fileNames = getFiles(gitLabApi, projectId, items);
            return fileNames;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public List<String> getStruct(Long projectId, String commitSha) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            List<TreeItem> items = gitLabApi.getRepositoryApi().getTree(projectId, null, commitSha);
            List<String> fileNames = getFiles(gitLabApi, projectId, items);
            return fileNames;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public Result<CommitInfoVO> getInfo(Long problemId) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            String branchName = GitlabConstant.DEFAULT_BRANCH;
            Branch branch = gitLabApi.getRepositoryApi().getBranch(problemId, branchName);
            CommitInfoVO commitInfoVO = new CommitInfoVO();
            commitInfoVO.setCommitId(branch.getCommit().getId());
            commitInfoVO.setMessage(branch.getCommit().getMessage());
            commitInfoVO.setUpdatedAt(branch.getCommit().getCreatedAt());
            return Result.success(commitInfoVO);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public Commit pushContent(List<ActionDTO> actions, String author, String message, Long problemId) {
        try {
            // 创建一个 GitLabApi 实例来连接到你的 GitLab 服务器
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            List<CommitAction> list = new ArrayList<>();
            for (ActionDTO action : actions) {
                CommitAction addAction = new CommitAction()
                        //    .withAction(CommitAction.Action.CREATE)
                        .withFilePath(action.getFilePath())
                        .withContent(action.getContent());
                switch (action.getAction()) {
                    case "update":
                        addAction.setAction(CommitAction.Action.UPDATE);
                        break;
                    case "create":
                        addAction.setAction(CommitAction.Action.CREATE);
                        break;
                    case "move":
                        addAction.setPreviousPath(action.getPreviousPath());
                        addAction.setAction(CommitAction.Action.MOVE);
                    case "delete":
                        addAction.setAction(CommitAction.Action.DELETE);
                    default:
                        // 如果没有匹配的action，执行默认的代码
                        throw new BaseException("action 操作字段不合法");
                }
                list.add(addAction);
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String formatDateTime = now.format(formatter);
            System.out.println(formatDateTime);
            String commitMessage = "author: " + author + " " + formatter + " " + message;
            // 创建一个 CommitPayload 对象来描述提交
            CommitPayload commitPayload = new CommitPayload()
                    .withBranch(GitlabConstant.DEFAULT_BRANCH)  // 提交到哪个分支
                    .withCommitMessage(commitMessage)  // 提交信息
                    .withActions(list);  // 提交的更改
            // 创建新的提交
            Commit commit = gitLabApi.getCommitsApi().createCommit(problemId, commitPayload);
            System.out.println(commit.getId());
            return commit;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public List<CommitInfoVO> fetchCommits(Long gitlabId) {
        // GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
        List<CommitInfoVO> list = new ArrayList<>();
        List<Commit> commits = null;
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            commits = gitLabApi.getCommitsApi().getCommits(40L);
        } catch (GitLabApiException e) {
            throw new BaseException(e.getMessage());
        }
// 打印每个commit的SHA值
        for (Commit commit : commits) {
            //System.out.println(commit);\
            CommitInfoVO commitInfoVO = new CommitInfoVO();
            commitInfoVO.setMessage(commit.getMessage());
            commitInfoVO.setCommitId(commit.getId());
            commitInfoVO.setUpdatedAt(commit.getCreatedAt());
            list.add(commitInfoVO);
        }
        return list;
    }

    @Override
    public void updateProjectTitle(Long projectId,String title) {
        try {
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            ProjectApi projectApi = gitLabApi.getProjectApi();
            Project project = projectApi.getProject(projectId);
            project.setName(title);
            Project updatedProject = projectApi.updateProject(project);
        } catch (GitLabApiException e) {
            throw  new BaseException(e.getMessage());
        }
    }

    @Override
    public Result getDiff(Long gitlabId) {
        try {
            // 创建一个GitLabApi实例
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            // 获取项目ID和commit SHA
            // Integer projectId = 40;
            String commitSha = "5b7dda2d6e1d6b337109a8d2b7c6421506b6a71d";

            // 获取特定commit的diff
            List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(40L, commitSha);

            // 打印diffs
            for (Diff diff : diffs) {
                System.out.println(diff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void updateSpaceTitle(Space space) {
        try {
            // 创建一个GitLabApi实例
            GitLabApi gitLabApi = new GitLabApi(baseUrl, token);
            Group group = gitLabApi.getGroupApi().getGroup(space.getGitlabId());
            group.setName(space.getSpaceName());
            // 更新组
            gitLabApi.getGroupApi().updateGroup(group);

        } catch (Exception e) {
            throw  new BaseException(e.getMessage());
        }
    }

    private static List<String> getFiles(GitLabApi gitLabApi, Long projectId, List<TreeItem> items) throws Exception {
        List<String> fileNames = new ArrayList<>();
        for (TreeItem item : items) {
            if (item.getType() == TreeItem.Type.BLOB) {
                fileNames.add(item.getPath());
            } else if (item.getType() == TreeItem.Type.TREE) {
                List<TreeItem> subItems = gitLabApi.getRepositoryApi().getTree(projectId, item.getPath(), null);
                fileNames.addAll(getFiles(gitLabApi, projectId, subItems));
            }
        }
        return fileNames;
    }

}
