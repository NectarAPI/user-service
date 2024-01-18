package ke.co.nectar.user.entity;

import ke.co.nectar.user.entity.audit.DateAudit;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permissions extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String identifier;

    private String ref;

    private String notes;

    public Permissions() {}

    public Permissions(String name, String identifier, String ref, String notes) {
        setName(name);
        setIdentifier(identifier);
        setRef(ref);
        setNotes(notes);
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
}
