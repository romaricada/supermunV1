package bf.gov.anptic.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Couleur} entity.
 */
public class CouleurDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String couleur;

    @NotNull
    private Float minVal;

    @NotNull
    private Float maxVal;

    private Long indicateurId;

    private String libelleIndicateur;

    private Boolean deleted;

    public String getLibelleIndicateur() {
        return libelleIndicateur;
    }

    public void setLibelleIndicateur(String libelleIndicateur) {
        this.libelleIndicateur = libelleIndicateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CouleurDTO couleurDTO = (CouleurDTO) o;
        if (couleurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), couleurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CouleurDTO{" +
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

    public Long getIndicateurId() {
        return indicateurId;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }
}
