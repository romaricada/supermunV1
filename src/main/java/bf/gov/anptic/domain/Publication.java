package bf.gov.anptic.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Publication.
 */
@Entity
@Table(name = "publication")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Publication extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "contenu")
    private byte[] contenu;

    @Column(name = "contenu_content_type")
    private String contenuContentType;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("publications")
    private TypePublication typePublication;

        
    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Publication libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public Publication description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getContenu() {
        return contenu;
    }

    public Publication contenu(byte[] contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public String getContenuContentType() {
        return contenuContentType;
    }

    public Publication contenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
        return this;
    }

    public void setContenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
    }

    public Boolean isPublished() {
        return published;
    }

    public Publication published(Boolean published) {
        this.published = published;
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public TypePublication getTypePublication() {
        return typePublication;
    }

    public Publication typePublication(TypePublication typePublication) {
        this.typePublication = typePublication;
        return this;
    }

    public void setTypePublication(TypePublication typePublication) {
        this.typePublication = typePublication;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Publication)) {
            return false;
        }
        return id != null && id.equals(((Publication) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Publication{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", contenuContentType='" + getContenuContentType() + "'" +
            ", published='" + isPublished() + "'" +
            "}";
    }

    public Boolean getDeleted() {
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
