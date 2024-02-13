package ru.skypro.homework.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Аспект, отвечающий за логирование вызовов методов в контроллерах.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Логирует вызов метода перед его выполнением.
     *
     * @param joinPoint Точка присоединения, представляющая вызываемый метод.
     */
    @Before("execution(* ru.skypro.homework.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.debug("Вызов метода: {}", joinPoint.getSignature().toShortString());
        LOGGER.debug("Аргументы: {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * Логирует успешное выполнение метода и его результат.
     *
     * @param joinPoint Точка присоединения, представляющая вызываемый метод.
     * @param result Результат выполнения метода.
     */
    @AfterReturning(pointcut = "execution(* ru.skypro.homework.controller.*.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.debug("Метод {} выполнен успешно", joinPoint.getSignature().toShortString());
        LOGGER.debug("Результат: {}", result);
    }

    /**
     * Логирует исключение, возникшее при выполнении метода.
     *
     * @param joinPoint Точка присоединения, представляющая вызываемый метод.
     * @param exception Исключение, возникшее при выполнении метода.
     */
    @AfterThrowing(pointcut = "execution(* ru.skypro.homework.controller.*.*(..))",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("Ошибка при выполнении метода: {}", joinPoint.getSignature().toShortString());
        LOGGER.error("Исключение: {}", exception.getMessage());
    }
}
