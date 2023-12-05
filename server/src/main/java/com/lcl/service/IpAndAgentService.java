package com.lcl.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author LovelyPeracid
 */
public interface IpAndAgentService {
    List<String> getInfo(HttpServletRequest request);
}
