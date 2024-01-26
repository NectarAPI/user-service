package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilities")
public class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;

    private String ref;

    @JsonProperty("contact_phone_no")
    private String contactPhoneNo;

    @JsonProperty("unit_charge")
    private double unitCharge;

    private Boolean activated;

    @JsonProperty("config_ref")
    private String configRef;

    @OneToMany
    @JoinTable(name = "utility_meters",
            joinColumns = @JoinColumn(name = "utility_id"),
            inverseJoinColumns = @JoinColumn(name = "meter_id"))
    private List<Meter> meters = new ArrayList<>();

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public Utility() {}

    public Utility(String name, String ref, String contactPhoneNo, Double unitCharge,
                   Boolean activated, String configRef) {
        setName(name);
        setRef(ref);
        setContactPhoneNo(contactPhoneNo);
        setUnitCharge(unitCharge);
        setActivated(activated);
        setConfigRef(configRef);
    }

    public Utility(Long id, String name, String ref, String contactPhoneNo, Double unitCharge,
                   Boolean activated, String configRef, List<Meter> meters,
                   Instant createdAt, Instant updatedAt) {
        this(name, ref, contactPhoneNo, unitCharge, activated, configRef);
        setId(id);
        setMeters(meters);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public Utility(Long id, String name, String ref, String contactPhoneNo, Double unitCharge,
                   Boolean activated, String configRef, Instant createdAt, Instant updatedAt) {
        this(name, ref, contactPhoneNo, unitCharge, activated, configRef);
        setId(id);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public Double getUnitCharge() {
        return unitCharge;
    }

    public void setUnitCharge(Double unitCharge) {
        this.unitCharge = unitCharge;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getConfigRef() {
        return configRef;
    }

    public void setConfigRef(String configRef) {
        this.configRef = configRef;
    }

    public List<Meter> getMeters() {
        return meters;
    }

    public void setMeters(List<Meter> meters) {
        this.meters = meters;
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
        return !name.isBlank()
                && !contactPhoneNo.isBlank() && unitCharge >= 0
                && !configRef.isBlank();
    }
}
