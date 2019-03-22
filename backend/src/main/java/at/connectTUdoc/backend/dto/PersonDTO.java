package at.connectTUdoc.backend.dto;

import java.util.Objects;

/**
 * This class contains common used methods and objects for the DTO Layer
 */
public abstract class PersonDTO extends AbstractDTO {
    private Long id;
    private String uid;
    private String preTitle;
    private String firstName;
    private String lastName;
    private String posTitle;
    private String eMail;
    private String privateKey;
    private String publicKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return Objects.equals(id, personDTO.id) &&
                Objects.equals(uid, personDTO.uid) &&
                Objects.equals(preTitle, personDTO.preTitle) &&
                Objects.equals(firstName, personDTO.firstName) &&
                Objects.equals(lastName, personDTO.lastName) &&
                Objects.equals(posTitle, personDTO.posTitle) &&
                Objects.equals(eMail, personDTO.eMail) &&
                Objects.equals(privateKey, personDTO.privateKey) &&
                Objects.equals(publicKey, personDTO.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uid, preTitle, firstName, lastName, posTitle, eMail, privateKey, publicKey);
    }

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
}
