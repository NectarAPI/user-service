package ke.co.nectar.user.entity.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications_users")
public class NotificationUser {

    // Nasty implementation due to issues
    // implementing update on join tables
    // using @Embeddable composite id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "notification_ref")
    private String notificationRef;

    @Column(name = "user_ref")
    private String userRef;

    private boolean read;

    @Column(name = "read_date")
    private Instant readDate;

    public NotificationUser() {}

    public NotificationUser(String notificationRef, String userRef,
                            boolean read, Instant readDate) {
        setNotificationRef(notificationRef);
        setUserRef(userRef);
        setRead(read);
        setReadDate(readDate);
    }

    @Override
    public String toString() {
        return String.format("id: %d", id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationRef() {
        return notificationRef;
    }

    public void setNotificationRef(String notificationRef) {
        this.notificationRef = notificationRef;
    }

    public String getUserRef() {
        return userRef;
    }

    public void setUserRef(String userRef) {
        this.userRef = userRef;
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

}
