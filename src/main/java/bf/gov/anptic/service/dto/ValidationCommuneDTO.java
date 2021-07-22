package bf.gov.anptic.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.EtatCommune} entity.
 */
public class ValidationCommuneDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean validated;

    @NotNull
    private Long communeId;

    private String communeLibelle;

    @NotNull
    private Long exerciceId;

    private Integer exerciceAnnee;

    @NotNull
    private Long typeDomaineId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
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

    public Long getTypeDomaineId() {
        return typeDomaineId;
    }

    public void setTypeDomaineId(Long typeDomaineId) {
        this.typeDomaineId = typeDomaineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValidationCommuneDTO etatCommuneDTO = (ValidationCommuneDTO) o;
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
            ", validated='" + isValidated() + "'" +
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
}
