package com.task_organizer.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
@Component
public class AspectConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper mapper;

    @Pointcut("@annotation(com.task_organizer.annotations.Log)")
    public void loggingPointCut(){
        //from here the aop logging will be invoked for all the methods
    }

    @Around("loggingPointCut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().getName();
        Object[] args = pjp.getArgs();

        logMethodInvocation(className, methodName, args);

        Object result = pjp.proceed();

        logMethodResponse(className, methodName, result);

        return result;
    }

    private void logMethodInvocation(String className, String methodName, Object[] args) throws JsonProcessingException {
        String argsString = mapper.writeValueAsString(args);
        logger.info("Method invoked {} : {}() arguments : {}", className, methodName, argsString);
    }

    private void logMethodResponse(String className, String methodName, Object response) throws JsonProcessingException {
        String responseString = mapper.writeValueAsString(response);
        logger.info("{} : {}() Response : {}", className, methodName, responseString);
    }
}
