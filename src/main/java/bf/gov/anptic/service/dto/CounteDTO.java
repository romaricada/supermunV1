package bf.gov.anptic.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Counte} entity.
 */
public class CounteDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Double nombreVisiteurs;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNombreVisiteurs() {
        return nombreVisiteurs;
    }

    public void setNombreVisiteurs(Double nombreVisiteurs) {
        this.nombreVisiteurs = nombreVisiteurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CounteDTO counteDTO = (CounteDTO) o;
        if (counteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), counteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CounteDTO{" +
            "id=" + getId() +
            ", nombreVisiteurs=" + getNombreVisiteurs() +
            "}";
    }
}
