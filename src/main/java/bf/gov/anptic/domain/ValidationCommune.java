package bf.gov.anptic.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A EtatCommune.
 */
@Entity
@Table(name = "validation_commune")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValidationCommune extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "validated", nullable = false)
    private Boolean validated;

    @NotNull
    @Column(name = "type_domaine_id", nullable = false)
    private Long typeDomaineId;

    @NotNull
    @ManyToOne(optional = false)
    private Commune commune;

    @NotNull
    @ManyToOne(optional = false)
    private Exercice exercice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isValidated() {
        return validated;
    }

    public ValidationCommune validated(Boolean validated) {
        this.validated = validated;
        return this;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Commune getCommune() {
        return commune;
    }

    public ValidationCommune commune(Commune commune) {
        this.commune = commune;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public ValidationCommune exercice(Exercice exercice) {
        this.exercice = exercice;
        return this;
    }

    public Long getTypeDomaineId() {
        return typeDomaineId;
    }

    public void setTypeDomaineId(Long typeDomaineId) {
        this.typeDomaineId = typeDomaineId;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationCommune)) {
            return false;
        }
        return id != null && id.equals(((ValidationCommune) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EtatCommune{" +
            "id=" + getId() +
            ", validated='" + isValidated() + "'" +
            "}";
    }
}
