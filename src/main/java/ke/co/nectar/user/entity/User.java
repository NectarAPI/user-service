package ke.co.nectar.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ke.co.nectar.user.entity.audit.DateAudit;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @JsonProperty("first_name")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    private String username;

    @Column(name = "phone_no")
    @JsonProperty("phone_no")
    private String phoneNumber;

    @Column(name = "image_url")
    @JsonProperty("image_url")
    private String imageURL;

    private String ref;

    @Column(name = "remember_token")
    @JsonProperty("remember_token")
    private String rememberToken;

    @Email
    private String email;

    public String password;

    @Column(name = "email_verified_at")
    @JsonProperty("email_verified_at")
    private Instant emailVerifiedAt;

    private Boolean activated = true;

    @ManyToMany
    @JoinTable(name = "users_utilities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "utility_id"))
    private List<Utility> userUtilities = new ArrayList<>();

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    public User() {}

    public User(String firstName, String lastName, String username, String phoneNumber,
                String imageURL, String rememberToken, @Email String email, String password,
                Boolean activated) {
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        setPhoneNumber(phoneNumber);
        setImageURL(imageURL);
        setRememberToken(rememberToken);
        setEmail(email);
        setPassword(password);
        setActivated(activated);
    }

    public User(String firstName, String lastName, String username, String phoneNumber,
                String imageURL, String ref, String rememberToken, @Email String email, String password,
                Boolean activated) {
        this(firstName, lastName, username, phoneNumber, imageURL, rememberToken, email, password, activated);
        setRef(ref);
    }

    public User(String firstName, String lastName, String username, String phoneNumber,
                String imageURL, String ref, String rememberToken, @Email String email, String password,
                List<Utility> userUtilities, Boolean activated) {
        this(firstName, lastName, username, phoneNumber, imageURL, rememberToken, email, password, activated);
        setUserUtilities(userUtilities);
        setRef(ref);
    }

    public User(String firstName, String lastName, String username, String phoneNumber,
                 String imageURL, String ref, String rememberToken, @Email String email, String password, Boolean activated,
                Instant createdAt, Instant updatedAt) {
        this(firstName, lastName, username, phoneNumber, imageURL, ref, rememberToken, email, password, activated);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Instant getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Instant emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public boolean validate() {
        String NUMBER_REGEX = "\\d+";
        return !username.isBlank() && !firstName.isBlank()
                && !lastName.isBlank() && !phoneNumber.isBlank()
                && !email.isBlank() && phoneNumber.matches(NUMBER_REGEX);
    }

    public List<Utility> getUtilities() {
        return userUtilities;
    }

    public void setUserUtilities(List<Utility> userUtilities) {
        this.userUtilities = userUtilities;
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
