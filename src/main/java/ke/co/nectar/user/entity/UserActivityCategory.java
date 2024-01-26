package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.entity.audit.DateAudit;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@DynamicUpdate
@Table(name = "users_activity_categories")
public class UserActivityCategory extends DateAudit {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "ref")
    @JsonProperty("ref")
    private String ref;

    @Column(name = "notes")
    @JsonProperty("notes")
    private String notes;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public UserActivityCategory() {}

    public UserActivityCategory(String name, String ref,
                                String notes, Instant createdAt,
                                Instant updatedAt) {
        setName(name);
        setRef(ref);
        setNotes(notes);
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
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
