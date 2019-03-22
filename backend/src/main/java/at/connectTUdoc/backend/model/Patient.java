package at.connectTUdoc.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity(name = "patient")
public class Patient extends Person {
    private String svnr;

    public String getSvnr() {
        return svnr;
    }

    public void setSvnr(String svnr) {
        this.svnr = svnr;
    }

    @OneToMany(
            mappedBy = "patient",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<ChatThread> chatThreads;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(svnr, patient.svnr) &&
                Objects.equals(chatThreads, patient.chatThreads);
    }

    @Override
    public int hashCode() {
        return Objects.hash(svnr, chatThreads);
    }

    public List<ChatThread> getChatThreads() {
        return chatThreads;
    }

    public void setChatThreads(List<ChatThread> chatThreads) {
        this.chatThreads = chatThreads;
    }
}
