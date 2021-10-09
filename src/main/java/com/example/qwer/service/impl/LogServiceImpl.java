package com.example.qwer.service.impl;

import com.example.qwer.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class LogServiceImpl implements LogService {

    private static final String LOG_CONTENT = "[类名]:%s,[方法]:%s,[参数]:%s,[IP]:%s";

    private String username;

    public void put(JoinPoint joinPoint, String methodName, String module, String description, String userName, Long userId) {
        /*try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            OperationLog log = new OperationLog();
            if (StringUtils.isEmpty(userName)) {
                username = "未知用户";
            }

            String ip = IPUtil.getIpAddress(request);
            log.setUserId(userId);
            log.setUserName(userName);
            log.setModule(module);
            log.setCreateTime(new Date());
            log.setDescription(description);
            log.setIp(ip);
            log.setContent(operateContent(joinPoint, methodName, ip, request));
            operationLogService.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println(methodName);
        System.out.println(module);
        System.out.println(description);
        System.out.println("写入日志操作");
    }


    /**
     * 查询所有日志
     *
     * @param request
     * @return
     */


}
