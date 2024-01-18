package ke.co.nectar.user.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.constant.StringConstants;
import ke.co.nectar.user.entity.Notification;
import ke.co.nectar.user.entity.User;
import ke.co.nectar.user.entity.notifications.NotificationUser;
import ke.co.nectar.user.response.ApiResponse;
import ke.co.nectar.user.service.notification.NotificationService;
import ke.co.nectar.user.service.notification.impl.exceptions.InvalidNotificationRefException;
import ke.co.nectar.user.service.user.UserService;
import ke.co.nectar.user.service.user.impl.exceptions.InvalidUserRefException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1", produces = "application/json")
public class NotificationsController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/notifications")
    public ApiResponse getNotifications(@RequestParam(value = "request_id") @NotNull String requestId,
                                        @RequestParam(value = "user_ref") @NotNull String userRef) {
        ApiResponse apiResponse;
        try {
            if (userRef != null && !userRef.isBlank()) {
                List<MessageNotification>
                        notifications = notificationService.getNotifications(userRef);
                Map<String, Object> output = new LinkedHashMap<>();
                output.put("notifications", notifications);
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                                                StringConstants.SUCCESS_NOTIFICATIONS_DETAILS,
                                                requestId,
                                                output);
            } else {
                apiResponse = new ApiResponse(StringConstants.SUCCESS_CODE,
                        StringConstants.EMPTY_REF_VALUE,
                        requestId);
            }

        } catch (Exception e) {
            apiResponse = new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
        return apiResponse;
    }

    @PostMapping(value = "/notifications")
    public ApiResponse addNotification(@RequestParam(value = "request_id") @NotNull String requestId,
                                       @NotNull @Valid @RequestBody MessageNotification messageNotification) {
        try {
            String notificationRef = processNotification(messageNotification);
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("notification_ref", notificationRef);
            return new ApiResponse(StringConstants.SUCCESS_CODE,
                    StringConstants.SUCCESS_NOTIFICATION_SAVED,
                    requestId,
                    output);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    @PutMapping(value = "/notifications")
    public ApiResponse setNotificationReadStatus(@RequestParam(value = "request_id") @NotNull String requestId,
                                                 @NotNull @RequestBody List<NotificationBundle> notificationsBundle) {
        try {
            String userRef, notificationRef;
            boolean status;
            Instant readTimestamp;

            for (NotificationBundle bundle : notificationsBundle) {
                userRef = bundle.getUserRef();
                notificationRef = bundle.getNotificationRef();
                status = bundle.isStatus();
                readTimestamp = Instant.ofEpochSecond(bundle.getReadTimestamp());

                if (userService.findByRef(userRef) != null) {
                    if (notificationService.findByRef(notificationRef) != null) {
                        notificationService.setNotificationStatus(userRef, notificationRef, status, readTimestamp);
                    } else {
                        throw new InvalidNotificationRefException(
                                String.format(StringConstants.INVALID_NOTIFICATION_REF));
                    }
                } else {
                    throw new InvalidUserRefException(StringConstants.INVALID_USER_REF);
                }

            }

            return new ApiResponse(StringConstants.SUCCESS_CODE,
                    StringConstants.SUCCESS_MODIFIED_USER_NOTIFICATIONS,
                    requestId);

        } catch (Exception e) {
            return new ApiResponse(StringConstants.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    requestId);
        }
    }

    private String processNotification(MessageNotification messageNotification)
            throws Exception {

        String notificationRef = "";

        if (messageNotification != null) {
            Notification notification = new Notification();
            notification.setSubject(messageNotification.getSubject());
            notification.setText(messageNotification.getText());
            notification.setAffected(messageNotification.getAffected());
            notification.setType(Notification.NotificationType.valueOf(messageNotification.getType()));

            notificationRef = notificationService.add(notification);

            NotificationUser notificationUser = new NotificationUser();

            if (messageNotification.getUserRef() != null) {
                notificationUser.setUserRef(messageNotification.getUserRef());
                notificationUser.setNotificationRef(notificationRef);
                notificationUser.setRead(false);
                notificationService.updateNotificationUser(notificationUser);

            } else {
                notificationUser.setNotificationRef(notificationRef);
                notificationUser.setRead(false);

                List<User> users = userService.findAll();
                for (User user : users) {
                    notificationUser.setUserRef(user.getRef());
                    notificationService.updateNotificationUser(notificationUser);
                }
            }
        }

        return notificationRef;
    }

    public static class NotificationBundle {

        @JsonProperty("user_ref")
        private String userRef;

        @JsonProperty("notification_ref")
        private String notificationRef;

        private boolean status;

        @JsonProperty("read_timestamp")
        private long readTimestamp;

        public NotificationBundle() {}

        public NotificationBundle(String userRef, String notificationRef,
                                  boolean status, long readTimestamp) {
            setUserRef(userRef);
            setNotificationRef(notificationRef);
            setStatus(status);
            setReadTimestamp(readTimestamp);
        }

        public String getUserRef() {
            return userRef;
        }

        public void setUserRef(String userRef) {
            this.userRef = userRef;
        }

        public String getNotificationRef() {
            return notificationRef;
        }

        public void setNotificationRef(String notificationRef) {
            this.notificationRef = notificationRef;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public long getReadTimestamp() {
            return readTimestamp;
        }

        public void setReadTimestamp(long readTimestamp) {
            this.readTimestamp = readTimestamp;
        }
    }

    public static class MessageNotification {

        private String ref;
        private String subject;
        private String text;
        private String type;

        @JsonProperty("user_ref")
        private String userRef;

        private String affected;
        private boolean read;

        @JsonProperty("read_at")
        private Instant readDate;

        @JsonProperty("created_at")
        private Instant createdDate;

        public MessageNotification() {}

        public MessageNotification(String ref, String subject, String text, String type,
                                   String userRef, String affected, boolean read,
                                   Instant readDate, Instant createdDate) {
            setRef(ref);
            setSubject(subject);
            setText(text);
            setType(type);
            setUserRef(userRef);
            setAffected(affected);
            setRead(read);
            setReadDate(readDate);
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserRef() {
            return userRef;
        }

        public void setUserRef(String userRef) {
            this.userRef = userRef;
        }

        public String getAffected() {
            return affected;
        }

        public void setAffected(String affected) {
            this.affected = affected;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public Instant getReadDate() {
            return readDate;
        }

        public void setReadDate(Instant readDate) {
            this.readDate = readDate;
        }

        public Instant getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Instant createdDate) {
            this.createdDate = createdDate;
        }

    }
}
