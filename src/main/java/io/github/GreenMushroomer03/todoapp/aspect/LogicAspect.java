package io.github.GreenMushroomer03.todoapp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogicAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);
    private final Timer projectCreateGroupTimer;

    public LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.product.create.group");
    }

    // Możemy używać tej metody zamiast wrzucania Stringa do każdego aspektu.
    @Pointcut("execution(* io.github.GreenMushroomer03.todoapp.logic.ProjectService.createGroup(..))")
    static void projectServiceCreateGroup() {

    }

    @Before("projectServiceCreateGroup()")
    void logMethodCall(JoinPoint joinPoint) {
        logger.info("Before {} with {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    //Zapinamy się dookoła metody createGroup(ProjectService), joinPoint to punkt połączenia (pozwala nam to na chwilę zatrzymać wywołanie metdoy)
    @Around("projectServiceCreateGroup()")
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}
