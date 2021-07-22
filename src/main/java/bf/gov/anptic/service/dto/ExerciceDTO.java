package bf.gov.anptic.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Exercice} entity.
 */
public class ExerciceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer annee;

    private Boolean deleted;

    private Boolean validated;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
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

        ExerciceDTO exerciceDTO = (ExerciceDTO) o;
        if (exerciceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exerciceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExerciceDTO{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }
}
