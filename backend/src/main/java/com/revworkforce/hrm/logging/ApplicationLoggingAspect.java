package com.revworkforce.hrm.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class ApplicationLoggingAspect {

    private static final Logger log = LogManager.getLogger(ApplicationLoggingAspect.class);

    @Around("execution(* com.revworkforce.hrm.controller..*(..)) || " +
            "execution(* com.revworkforce.hrm.service..*(..)) || " +
            "execution(* com.revworkforce.hrm.repository..*(..))")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String method = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String argTypes = Arrays.stream(signature.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));

        log.debug("ENTER {} args=[{}]", method, argTypes);
        try {
            Object result = pjp.proceed();
            long elapsed = System.currentTimeMillis() - start;
            if (signature.getDeclaringTypeName().contains(".controller.")) {
                log.info("EXIT {} success durationMs={}", method, elapsed);
            } else {
                log.debug("EXIT {} success durationMs={}", method, elapsed);
            }
            return result;
        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("EXIT {} failure durationMs={} errorType={} message={}",
                    method, elapsed, ex.getClass().getSimpleName(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
