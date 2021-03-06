package com.grupoasv.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuditingAspect {

    @AfterReturning("execution(* com.grupoasv.repository.ServiceRepository+.findOne(..))")
    public void logServiceAccess(JoinPoint joinPoint) {
        System.out.println("User Access to service with id: " + joinPoint.getArgs()[0]);
    }
}
