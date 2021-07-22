package bf.gov.anptic.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Couleur.
 */
@Entity
@Table(name = "couleur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Couleur extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "couleur", nullable = false)
    private String couleur;

    @NotNull
    @Column(name = "min_val", nullable = false)
    private Float minVal;

    @NotNull
    @Column(name = "max_val", nullable = false)
    private Float maxVal;

    @ManyToOne
    @JsonIgnoreProperties("couleurs")
    private Indicateur indicateur;

    @Column(name = "deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouleur() {
        return couleur;
    }

    public Couleur couleur(String couleur) {
        this.couleur = couleur;
        return this;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Couleur)) {
            return false;
        }
        return id != null && id.equals(((Couleur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Couleur{" +
            "id=" + getId() +
            ", couleur='" + getCouleur() + "'" +
            "}";
    }

    public Float getMinVal() {
        return minVal;
    }

    public void setMinVal(Float minVal) {
        this.minVal = minVal;
    }

    public Float getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(Float maxVal) {
        this.maxVal = maxVal;
    }

    public Indicateur getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Couleur deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
