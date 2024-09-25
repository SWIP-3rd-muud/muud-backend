package com.muud.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "ExecutionTimeLogger")
public class ExecutionTimeAspect {

    @Around("execution(* com.muud..controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("실행 메서드: {}, 실행 시간: {}ms", methodName, executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("실행 메서드: {}, 예외 발생: {}, 실행 시간: {}ms", methodName, e.getMessage(), executionTime);
            throw e;
        }
    }
}
