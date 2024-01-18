package com.lcl.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author LovelyPeracid
 */
@Data
public class ActionDTO {
    private  String filePath;
    @ApiModelProperty("每次请求文件内容的时候 会返回 修改文件记得传这个")
    private String lastCommitId;
    private String action; //后端封装一下
    @ApiModelProperty(value = "这是传最新内容 即便是修改文件名 也要传content",required = true)
    private String content;
    @ApiModelProperty(value = "如果是修改文件名字要传 否则不需要",required = false)
    private String previousPath;
}

