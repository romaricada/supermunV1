package bf.gov.anptic.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Dictionaires.
 */
@Entity
@Table(name = "dictionaires")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dictionaires extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "entite")
    private String entite;

    @Size(max = 1024)
    @Column(name = "definition", length = 1024)
    private String definition;

    @Size(max = 1024)
    @Column(name = "regle_calcule", length = 1024)
    private String regleCalcule;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntite() {
        return entite;
    }

    public Dictionaires entite(String entite) {
        this.entite = entite;
        return this;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public String getDefinition() {
        return definition;
    }

    public Dictionaires definition(String definition) {
        this.definition = definition;
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getRegleCalcule() {
        return regleCalcule;
    }

    public Dictionaires regleCalcule(String regleCalcule) {
        this.regleCalcule = regleCalcule;
        return this;
    }

    public void setRegleCalcule(String regleCalcule) {
        this.regleCalcule = regleCalcule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dictionaires)) {
            return false;
        }
        return id != null && id.equals(((Dictionaires) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dictionaires{" +
            "id=" + getId() +
            ", entite='" + getEntite() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", regleCalcule='" + getRegleCalcule() + "'" +
            "}";
    }
}
