
package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@DynamicUpdate
@Table(name = "utility_meters")
public class UtilityMeters {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(targetEntity = Meter.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "meter_id")
    private Meter meter;

    @JsonIgnore
    @OneToOne(targetEntity = Utility.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "utility_id")
    private Utility utility;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public UtilityMeters() {}

    public UtilityMeters(Meter meter, Utility utility, Instant createdAt, Instant updatedAt) {
        setMeter(meter);
        setUtility(utility);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
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

    public Meter getMeter() {
        return meter;
    }

    public void setMeter(Meter meter) {
        this.meter = meter;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility =  utility;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
