package tr.com.getir.getirfinalcase.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* tr.com.getir.getirfinalcase.controller.*.*(..))")
    public void controllerLayerPointcut() {
    }

    @Pointcut("execution(* tr.com.getir.getirfinalcase.service.impl.*.*(..))")
    public void serviceLayerPointcut() {
    }

    @Around("controllerLayerPointcut() || serviceLayerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String layer = className.endsWith("Controller") ? "Controller" : "Service";

        // Method start log
        logger.info("[{}] {}.{}() started - Parameters: {}",
                layer, className, methodName, formatParameters(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            // Successful result log
            logger.info("[{}] {}.{}() completed successfully - Duration: {}ms",
                    layer, className, methodName, executionTime);

            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;

            // Error log
            logger.error("[{}] {}.{}() failed - Duration: {}ms - Hata: {} - Detay: {}",
                    layer, className, methodName, executionTime, e.getClass().getSimpleName(), e.getMessage());

            throw e;
        }
    }

    private String formatParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            Object arg = args[i];
            if (arg == null) {
                sb.append("null");
            } else if (arg instanceof String) {
                sb.append("\"").append(arg).append("\"");
            } else {
                sb.append(arg);
            }
        }
        sb.append("]");
        return sb.toString();
    }
}