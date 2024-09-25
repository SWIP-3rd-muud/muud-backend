package com.muud.global.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExecutionTimeAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature methodSignature;

    @InjectMocks
    private ExecutionTimeAspect executionTimeAspect;

    @Test
    @DisplayName("로깅 메서드 호출 성공")
    void execute_success() throws Throwable {
        // given
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(proceedingJoinPoint.proceed()).thenReturn("test result");

        // when
        Object result = executionTimeAspect.execute(proceedingJoinPoint);

        // then
        assertEquals("test result", result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    @DisplayName("로깅 메서드 호출 실패 - 예외 던지기")
    void testExecuteMethodWithException() throws Throwable {
        // given
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("Test exception"));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            executionTimeAspect.execute(proceedingJoinPoint);
        });

        // then
        assertEquals("Test exception", exception.getMessage());
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}
