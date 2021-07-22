package bf.gov.anptic.domain;

import bf.gov.anptic.domain.enumeration.TypePerformance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Performance.
 */
@Entity
@Table(name = "performance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Performance extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "score")
    private Double score;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "domaine_id")
    private Long domaineId;

    @Column(name = "type_domaine_id")
    private Long typeDomainId;

    @Column(name = "nb_etoile")
    private Double nbEtoile;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "type_performance", nullable = false)
    private TypePerformance typePerformance;

    @ManyToOne
    @JsonIgnoreProperties("communes")
    private Commune commune;

    @ManyToOne
    @JsonIgnoreProperties("communes")
    private Indicateur indicateur;

    @ManyToOne
    @JsonIgnoreProperties("communes")
    private Exercice exercice;


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public Performance score(Double score) {
        this.score = score;
        return this;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Performance deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Commune getCommune() {
        return commune;
    }

    public Performance commune(Commune commune) {
        this.commune = commune;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }

    public Indicateur getIndicateur() {
        return indicateur;
    }

    public Performance indicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
        return this;
    }

    public void setIndicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public Performance exercice(Exercice exercice) {
        this.exercice = exercice;
        return this;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Long getDomaineId() {
        return domaineId;
    }

    public void setDomaineId(Long domaineId) {
        this.domaineId = domaineId;
    }

    public Long getTypeDomainId() {
        return typeDomainId;
    }

    public void setTypeDomainId(Long typeDomainId) {
        this.typeDomainId = typeDomainId;
    }

    public Double getNbEtoile() {
        return nbEtoile;
    }

    public void setNbEtoile(Double nbEtoile) {
        this.nbEtoile = nbEtoile;
    }

    public TypePerformance getTypePerformance() {
        return typePerformance;
    }

    public void setTypePerformance(TypePerformance typePerformance) {
        this.typePerformance = typePerformance;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Performance)) {
            return false;
        }
        return id != null && id.equals(((Performance) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Performance{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
