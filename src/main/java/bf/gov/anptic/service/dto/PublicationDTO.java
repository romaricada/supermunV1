package bf.gov.anptic.service.dto;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Publication} entity.
 */
public class PublicationDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String libelle;

    private String description;

    @Lob
    private byte[] contenu;

    private String contenuContentType;

    private Boolean published;

    private Boolean deleted;

    private Long typePublicationId;

    private String typePublicationLibelle;

    @Lob
    private byte[] image;

    private String imageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean isPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Long getTypePublicationId() {
        return typePublicationId;
    }

    public void setTypePublicationId(Long typePublicationId) {
        this.typePublicationId = typePublicationId;
    }

    public String getTypePublicationLibelle() {
        return typePublicationLibelle;
    }

    public void setTypePublicationLibelle(String typePublicationLibelle) {
        this.typePublicationLibelle = typePublicationLibelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicationDTO publicationDTO = (PublicationDTO) o;
        if (publicationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publicationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PublicationDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", published='" + isPublished() + "'" +
            ", typePublication=" + getTypePublicationId() +
            ", typePublication='" + getTypePublicationLibelle() + "'" +
            "}";
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
}
