package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "meters")
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private BigDecimal no;

    private Boolean activated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @JsonProperty("meter_type")
    private MeterType meterType;

    @OneToOne
    @JoinTable(name = "subscriber_meters",
            joinColumns = @JoinColumn(name = "meter_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private Subscriber subscriber;

    private String ref;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public Meter() {}

    public Meter(BigDecimal no, Boolean activated,
                 String ref, MeterType meterType) {
        setNo(no);
        setActivated(activated);
        setRef(ref);
        setMeterType(meterType);
    }

    public Meter(Long id, BigDecimal no, Boolean activated, String ref,
                 MeterType meterType, Subscriber subscriber,
                 Instant createdAt, Instant updatedAt) {
        setId(id);
        setNo(no);
        setActivated(activated);
        setRef(ref);
        setMeterType(meterType);
        setSubscriber(subscriber);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    @Override
    public String toString() {
        return String.format("ref: %s", ref);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNo() {
        return no;
    }

    public void setNo(BigDecimal no) {
        this.no = no;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public MeterType getMeterType() {
        return meterType;
    }

    public void setMeterType(MeterType meterType) {
        this.meterType = meterType;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber  = subscriber;
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

    public boolean validate() {
        int noLen = no.signum() == 0 ? 1 : no.precision() - no.scale();
        return (noLen == 11 || noLen == 13);
    }
}
