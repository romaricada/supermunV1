package bf.gov.anptic.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Counte.
 */
@Entity
@Table(name = "counte")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Counte extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre_visiteurs")
    private Double nombreVisiteurs;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNombreVisiteurs() {
        return nombreVisiteurs;
    }

    public Counte nombreVisiteurs(Double nombreVisiteurs) {
        this.nombreVisiteurs = nombreVisiteurs;
        return this;
    }

    public void setNombreVisiteurs(Double nombreVisiteurs) {
        this.nombreVisiteurs = nombreVisiteurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Counte)) {
            return false;
        }
        return id != null && id.equals(((Counte) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Counte{" +
            "id=" + getId() +
            ", nombreVisiteurs=" + getNombreVisiteurs() +
            "}";
    }
}
