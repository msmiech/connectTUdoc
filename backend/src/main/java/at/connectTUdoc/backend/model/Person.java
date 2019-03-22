package at.connectTUdoc.backend.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String uid;
    private String preTitle;
    private String firstName;
    private String lastName;
    private String posTitle;
    private String eMail;
    private String privateKey;
    private String publicKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPreTitle() {
        return preTitle;
    }

    public void setPreTitle(String preTitle) {
        this.preTitle = preTitle;
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

    public String getPosTitle() {
        return posTitle;
    }

    public void setPosTitle(String posTitle) {
        this.posTitle = posTitle;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getId(), person.getId()) &&
                Objects.equals(getUid(), person.getUid()) &&
                Objects.equals(getPreTitle(), person.getPreTitle()) &&
                Objects.equals(getFirstName(), person.getFirstName()) &&
                Objects.equals(getLastName(), person.getLastName()) &&
                Objects.equals(getPosTitle(), person.getPosTitle()) &&
                Objects.equals(geteMail(), person.geteMail()) &&
                Objects.equals(getPrivateKey(), person.getPrivateKey()) &&
                Objects.equals(getPublicKey(), person.getPublicKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUid(), getPreTitle(), getFirstName(), getLastName(), getPosTitle(), geteMail(), getPrivateKey(), getPublicKey());
    }
}
