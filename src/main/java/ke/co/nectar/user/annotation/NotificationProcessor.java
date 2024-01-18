package ke.co.nectar.user.annotation;

import ke.co.nectar.user.annotation.exception.InvalidTemplateException;
import ke.co.nectar.user.annotation.exception.UnImplementedAnnotationException;
import ke.co.nectar.user.controllers.utils.ActivityLog;
import ke.co.nectar.user.service.user.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationProcessor {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationUtils notificationUtils;

    public String process(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> params = getNotificationParams(joinPoint);
        return saveActivityLog(params);
    }

    private Map<String, Object> getNotificationParams(ProceedingJoinPoint joinPoint)
            throws InvalidTemplateException, UnImplementedAnnotationException {
        Method method = getMethod(joinPoint);
        Notify notify = method.getAnnotation(Notify.class);

        if (notify != null) {
            String category = notify.category();
            String description = notify.description();

            Map<String, Object> functionParams = getFunctionParams(joinPoint);
            description = template(description, functionParams);

            Map<String, Object> params = new HashMap<>();
            params.put("category", category);
            params.put("description", description);
            params.put("user_ref", functionParams.get("userRef"));
            params.put("request_id", functionParams.get("requestId"));
            return params;
        }
        throw new UnImplementedAnnotationException(
                "Annotation @Notify not implemented"
        );
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private Map<String, Object> getFunctionParams(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> functionParams = new HashMap<>();
        for (int c = 0; c < parameters.length; c++) {
            functionParams.put(parameters[c].getName(),
                    args[c]);
        }
        return functionParams;
    }

    private String template(String description,
                            Map<String, Object> functionParams)
            throws InvalidTemplateException {
        return notificationUtils.template(description, functionParams);
    }

    private String saveActivityLog(Map<String, Object> params)
            throws Exception {
        ActivityLog activityLog = new ActivityLog(
                params.get("category").toString(),
                params.get("description").toString());
        return userService.setUserActivityLog(activityLog, params.get("user_ref").toString());
    }
}
