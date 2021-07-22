package bf.gov.anptic.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.EtatCommune} entity.
 */
public class EtatCommuneDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean priseEnCompte;

    private Long communeId;

    private String communeLibelle;

    private Long exerciceId;

    private Integer exerciceAnnee;

    private Boolean publication;

    private CommuneDTO commune = new CommuneDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isPriseEnCompte() {
        return priseEnCompte;
    }

    public void setPriseEnCompte(Boolean priseEnCompte) {
        this.priseEnCompte = priseEnCompte;
    }

    public Long getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }

    public String getCommuneLibelle() {
        return communeLibelle;
    }

    public void setCommuneLibelle(String communeLibelle) {
        this.communeLibelle = communeLibelle;
    }

    public Long getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(Long exerciceId) {
        this.exerciceId = exerciceId;
    }

    public Boolean getPublication() {
        return publication;
    }

    public void setPublication(Boolean publication) {
        this.publication = publication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EtatCommuneDTO etatCommuneDTO = (EtatCommuneDTO) o;
        if (etatCommuneDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), etatCommuneDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EtatCommuneDTO{" +
            "id=" + getId() +
            ", priseEnCompte='" + isPriseEnCompte() + "'" +
            ", commune=" + getCommuneId() +
            ", commune='" + getCommuneLibelle() + "'" +
            ", exercice=" + getExerciceId() +
            "}";
    }

    public Integer getExerciceAnnee() {
        return exerciceAnnee;
    }

    public void setExerciceAnnee(Integer exerciceAnnee) {
        this.exerciceAnnee = exerciceAnnee;
    }

    public CommuneDTO getCommune() {
        return commune;
    }

    public void setCommune(CommuneDTO commune) {
        this.commune = commune;
    }
}
