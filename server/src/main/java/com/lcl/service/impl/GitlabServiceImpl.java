package com.lcl.service.impl;

import com.aliyun.oss.ServiceException;
import com.lcl.constant.GitlabConstant;
import com.lcl.entity.Space;
import com.lcl.result.Result;
import com.lcl.service.GitlabService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.GroupApi;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupParams;
import org.springframework.stereotype.Service;

/**
 * @author LovelyPeracid
 */
@Service
public class GitlabServiceImpl implements GitlabService {

    @Override
    public Space CreateSpace(Space space) {
//        String token = GitlabConstant.token;
//        String baseUrl = GitlabConstant.baseUrl;
//        String suffix=null;
//      //  if(flag==0) suffix="x";
//
//        GitLabApi gitLabApi = null;
//        try {
//
//            gitLabApi = new GitLabApi(baseUrl, token);
//        } catch (Exception e) {
//            System.out.println(e);
//            String message = "token过期或者gitlab 服务器变动";
//            //throw new ServiceException(message,400);
//        }
//        // gitLabApi = new GitLabApi(baseUrl, token);
//
//        String projectName = space.getSpaceName();
//
//        GroupApi groupApi = gitLabApi.getGroupApi();
//        GroupParams params= new GroupParams();
//        params.withName(projectName+"space"+suffix);//避免前人把后人的个人空间创建掉
//        params.withPath(projectName+"space"+suffix);
//        Group group=null;
//
//        try {
//            group = groupApi.createGroup(params);
//        } catch (GitLabApiException e) {
//            //throw new RuntimeException(e);
//            System.out.println(e);
//            String message = "网络波动或创建失败";
//            throw new ServiceException(message,400);
//        }
        return  null;
    }
}
