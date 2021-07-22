package bf.gov.anptic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A ValeurModalite.
 */
@Entity
@Table(name = "valeur_modalite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ValeurModalite extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "indicateur_id")
    private Long indicateurId;

    @NotNull
    @Column(name = "valeur", nullable = false)
    private String valeur;

    @ManyToOne
    @JsonIgnoreProperties("valeurModalites")
    private Exercice exercice;

    @ManyToOne
    @JsonIgnoreProperties("valeurModalites")
    private Commune commune;

    @ManyToOne
    @JsonIgnoreProperties("valeurModalites")
    private Modalite modalite;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public ValeurModalite valeur(String valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public ValeurModalite exercice(Exercice exercice) {
        this.exercice = exercice;
        return this;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Commune getCommune() {
        return commune;
    }

    public ValeurModalite commune(Commune commune) {
        this.commune = commune;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Modalite getModalite() {
        return modalite;
    }

    public ValeurModalite modalite(Modalite modalite) {
        this.modalite = modalite;
        return this;
    }

    public void setModalite(Modalite modalite) {
        this.modalite = modalite;
    }

    public Long getIndicateurId() {
        return indicateurId;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValeurModalite)) {
            return false;
        }
        return id != null && id.equals(((ValeurModalite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ValeurModalite{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", commune='" + getCommune() + "'" +
            ", modalite='" + getModalite() + "'" +
            ", exercice='" + getExercice() + "'" +
            "}";
    }
}
