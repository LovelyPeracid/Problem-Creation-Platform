package com.lcl.service.impl;

import com.lcl.service.IpAndAgentService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LovelyPeracid
 */
@Service
public class IpAndAgentImpl implements IpAndAgentService {
    @Override
    public List<String> getInfo(HttpServletRequest request) {
        String remoteAddr = "";
        String userAgent = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
            userAgent = request.getHeader("User-Agent");
        }
        List<String> list= new ArrayList<>();
        list.add(remoteAddr);
        list.add(userAgent);
        return  list;
    }
}
