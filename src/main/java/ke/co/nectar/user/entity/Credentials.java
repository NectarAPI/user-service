package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.entity.audit.DateAudit;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credentials")
public class Credentials extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String key;

    private String secret;

    private String ref;

    private Boolean activated = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "credentials_permissions",
            joinColumns = @JoinColumn(name = "credentials_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private List<Permissions> permissions = new ArrayList<>();

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public Credentials() {}

    public Credentials(String key, String secret, String ref,
                       Boolean activated, List<Permissions> permissions,
                       Instant createdAt, Instant updatedAt) {
        setKey(key);
        setSecret(secret);
        setRef(ref);
        setActivated(activated);
        setPermissions(permissions);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public Credentials(String key, String secret, String ref, User user,
                       Boolean activated, List<Permissions> permissions) {
        setKey(key);
        setSecret(secret);
        setRef(ref);
        setUser(user);
        setActivated(activated);
        setPermissions(permissions);
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Boolean getActivated() {
        return activated;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
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
