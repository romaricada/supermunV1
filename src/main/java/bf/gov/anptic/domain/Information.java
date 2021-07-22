package bf.gov.anptic.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Information.
 */
@Entity
@Table(name = "information")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Information extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "presentation")
    private String presentation;

    @Column(name = "contact")
    private String contact;

    @Column(name = "deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentation() {
        return presentation;
    }

    public Information presentation(String presentation) {
        this.presentation = presentation;
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getContact() {
        return contact;
    }

    public Information contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Information deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Information)) {
            return false;
        }
        return id != null && id.equals(((Information) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Information{" +
            "id=" + getId() +
            ", presentation='" + getPresentation() + "'" +
            ", contact='" + getContact() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
