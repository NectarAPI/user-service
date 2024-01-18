package ke.co.nectar.user.annotation.aspect;


import ke.co.nectar.user.annotation.NotificationProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotifyAspect {

    @Autowired
    private NotificationProcessor notificationProcessor;

    @Around("@annotation(ke.co.nectar.user.annotation.Notify)")
    public Object notify(ProceedingJoinPoint joinPoint) throws Throwable {
        notificationProcessor.process(joinPoint);
        return joinPoint.proceed();
    }
}
