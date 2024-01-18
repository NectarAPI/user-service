package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.entity.audit.DateAudit;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@DynamicUpdate
@Table(name = "subscriber_utilities")
public class SubscriberUtility extends DateAudit {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(targetEntity = Subscriber.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "subscriber_id")
    private Subscriber subscriber;

    @JsonIgnore
    @OneToOne(targetEntity = Utility.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "utility_id")
    private Utility utility;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public SubscriberUtility() {}

    public SubscriberUtility(Subscriber subscriber, Utility utility,
                             Instant createdAt, Instant updatedAt) {
        setSubscriber(subscriber);
        setUtility(utility);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
