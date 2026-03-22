package com.awbd.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.awbd.demo.service..*(..))")
    public void logBeforeServiceMethods(JoinPoint joinPoint) {
        log.info("Intrare in metoda: {}.{} cu argumente: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(
            pointcut = "execution(* com.awbd.demo.service..*(..))",
            returning = "result"
    )
    public void logAfterReturningServiceMethods(JoinPoint joinPoint, Object result) {
        log.info("Iesire din metoda: {}.{} cu rezultat: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(
            pointcut = "execution(* com.awbd.demo.service..*(..))",
            throwing = "ex"
    )
    public void logAfterThrowingServiceMethods(JoinPoint joinPoint, Throwable ex) {
        log.error("Eroare in metoda: {}.{} - mesaj: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage(),
                ex);
    }
}