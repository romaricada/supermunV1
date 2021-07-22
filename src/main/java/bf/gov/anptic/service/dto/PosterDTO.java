package bf.gov.anptic.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Poster} entity.
 */
public class PosterDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String url;

    @Lob
    private byte[] contenu;

    private String contenuContentType;
    private Boolean deleted;


    private Long exerciceId;

    private ExerciceDTO exercice = new ExerciceDTO();

    private Long communeId;
    private CommuneDTO commune = new CommuneDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getContenu() {
        return contenu;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public String getContenuContentType() {
        return contenuContentType;
    }

    public void setContenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PosterDTO posterDTO = (PosterDTO) o;
        if (posterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), posterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PosterDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", exerciceId=" + getExerciceId() +
            ", exercice=" + getExercice() +
            ", communeId=" + getCommuneId() +
            ", commune=" + getCommune() +
            "}";
    }

    public ExerciceDTO getExercice() {
        return exercice;
    }

    public void setExercice(ExerciceDTO exercice) {
        this.exercice = exercice;
    }

    public CommuneDTO getCommune() {
        return commune;
    }

    public void setCommune(CommuneDTO commune) {
        this.commune = commune;
    }
}
