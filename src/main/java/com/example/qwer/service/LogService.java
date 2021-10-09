package com.example.qwer.service;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
   void put(JoinPoint joinPoint, String methodName, String module, String description, String userName, Long userId);
}
