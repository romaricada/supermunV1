package bf.gov.anptic.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.ValeurModalite} entity.
 */
public class ValeurModaliteDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long indicateurId;

    @NotNull
    private String valeur;

    private Long exerciceId;

    private Long communeId;

    private Long modaliteId;

    private ModaliteDTO modalite = new ModaliteDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Long getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(Long exerciceId) {
        this.exerciceId = exerciceId;
    }

    public Long getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }

    public Long getModaliteId() {
        return modaliteId;
    }

    public void setModaliteId(Long modaliteId) {
        this.modaliteId = modaliteId;
    }

    public ModaliteDTO getModalite() {
        return modalite;
    }

    public void setModalite(ModaliteDTO modalite) {
        this.modalite = modalite;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ValeurModaliteDTO valeurModaliteDTO = (ValeurModaliteDTO) o;
        if (valeurModaliteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), valeurModaliteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ValeurModaliteDTO{" +
            "id=" + getId() +
            ", valeur='" + getValeur() + "'" +
            ", exercice=" + getExerciceId() +
            ", commune=" + getCommuneId() +
            ", modaliteId=" + getModaliteId() +
            ", indicateurId=" + getIndicateurId() +
            "}";
    }

    public Long getIndicateurId() {
        return indicateurId;
    }

}
