package com.example.demo.jws_session12_bai5.aop;

import com.example.demo.jws_session12_bai5.exception.StudentNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("within(com.example.demo.jws_session12_bai5.controller..*)")
    public void logControllerEntry(JoinPoint joinPoint) {
        logger.info("Controller request: method={}, args={}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterThrowing(
            pointcut = "within(com.example.demo.jws_session12_bai5.service..*)",
            throwing = "exception"
    )
    public void logServiceException(JoinPoint joinPoint, Throwable exception) {
        if (exception instanceof StudentNotFoundException) {
            logger.warn("Service exception in {}: {}",
                    joinPoint.getSignature().getName(),
                    exception.getMessage());
            return;
        }

        logger.error("Service exception in {}: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage(),
                exception);
    }

    @Around("within(com.example.demo.jws_session12_bai5.controller..*)")
    public Object measureControllerExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            logger.info("Controller method {} executed in {} ms",
                    joinPoint.getSignature().getName(),
                    elapsedTime);
        }
    }
}
