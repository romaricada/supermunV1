package bf.gov.anptic.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Poster.
 */
@Entity
@Table(name = "poster")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Poster extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "contenu")
    private byte[] contenu;

    @Column(name = "contenu_content_type")
    private String contenuContentType;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties("posters")
    private Exercice exercice;

    @ManyToOne
    @JsonIgnoreProperties("posters")
    private Commune commune;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Poster url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getContenu() {
        return contenu;
    }

    public Poster contenu(byte[] contenu) {
        this.contenu = contenu;
        return this;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public String getContenuContentType() {
        return contenuContentType;
    }

    public Poster contenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
        return this;
    }

    public void setContenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Poster deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public Poster exercice(Exercice exercice) {
        this.exercice = exercice;
        return this;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Commune getCommune() {
        return commune;
    }

    public Poster commune(Commune commune) {
        this.commune = commune;
        return this;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poster)) {
            return false;
        }
        return id != null && id.equals(((Poster) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Poster{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", contenuContentType='" + getContenuContentType() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
