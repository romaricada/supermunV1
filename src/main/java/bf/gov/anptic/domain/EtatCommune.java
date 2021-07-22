package bf.gov.anptic.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A EtatCommune.
 */
@Entity
@Table(name = "etat_commune")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EtatCommune extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "prise_en_compte", nullable = false)
    private Boolean priseEnCompte;

    @NotNull
    @Column(name = "publication", nullable = false)
    private Boolean publication;

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

    public Boolean isPriseEnCompte() {
        return priseEnCompte;
    }

    public EtatCommune priseEnCompte(Boolean priseEnCompte) {
        this.priseEnCompte = priseEnCompte;
        return this;
    }

    public void setPriseEnCompte(Boolean priseEnCompte) {
        this.priseEnCompte = priseEnCompte;
    }

    public Commune getCommune() {
        return commune;
    }

    public EtatCommune commune(Commune commune) {
        this.commune = commune;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public EtatCommune exercice(Exercice exercice) {
        this.exercice = exercice;
        return this;
    }

    public Boolean getPublication() {
        return publication;
    }

    public void setPublication(Boolean publication) {
        this.publication = publication;
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
        if (!(o instanceof EtatCommune)) {
            return false;
        }
        return id != null && id.equals(((EtatCommune) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EtatCommune{" +
            "id=" + getId() +
            ", priseEnCompte='" + isPriseEnCompte() + "'" +
            "}";
    }
}
