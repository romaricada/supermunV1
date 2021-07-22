package bf.gov.anptic.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Information} entity.
 */
public class InformationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String presentation;

    private String contact;

    private Boolean deleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

        InformationDTO informationDTO = (InformationDTO) o;
        if (informationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), informationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InformationDTO{" +
            "id=" + getId() +
            ", presentation='" + getPresentation() + "'" +
            ", contact='" + getContact() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
