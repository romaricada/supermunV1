package bf.gov.anptic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Indicateur.
 */
@Entity
@Table(name = "indicateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Indicateur extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @Column(name = "code", unique = true)
    private String code;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image_1")
    private byte[] image1;

    @Column(name = "image_1_content_type")
    private String image1ContentType;

    @Lob
    @Column(name = "image_2")
    private byte[] image2;

    @Column(name = "image_2_content_type")
    private String image2ContentType;

    @Column(name = "total_point")
    private Double totalPoint;

    @Column(name = "interval")
    private Boolean interval;

    @Column(name = "deleted")
    private Boolean deleted;

    @NotNull
    @Column(name = "sous_indicateur", nullable = false)
    private Boolean sousIndicateur;

    @Lob
    @Column(name = "image_modalite_ok")
    private byte[] imageModaliteOK;

    @Column(name = "image_modalite_ok_content_type")
    private String imageModaliteOKContentType;

    @Lob
    @Column(name = "image_modalite_nok")
    private byte[] imageModaliteNOK;

    @Column(name = "image_modalite_nok_content_type")
    private String imageModaliteNOKContentType;

    @NotNull
    @Column(name = "unite_borne", nullable = false)
    private String uniteBorne;

    @NotNull
    @Column(name = "borne_min", nullable = false)
    private Double borneMin;

    @NotNull
    @Column(name = "borne_max", nullable = false)
    private Double borneMax;

    @ManyToOne
    @JsonIgnoreProperties("indicateurs")
    private Domaine domaine;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Indicateur code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Indicateur libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public Indicateur description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage1() {
        return image1;
    }

    public Indicateur image1(byte[] image1) {
        this.image1 = image1;
        return this;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return image1ContentType;
    }

    public Indicateur image1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
        return this;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return image2;
    }

    public Indicateur image2(byte[] image2) {
        this.image2 = image2;
        return this;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return image2ContentType;
    }

    public Indicateur image2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
        return this;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }

    public Double getTotalPoint() {
        return totalPoint;
    }

    public Indicateur totalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
        return this;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Boolean isInterval() {
        return interval;
    }

    public Indicateur interval(Boolean interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(Boolean interval) {
        this.interval = interval;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Indicateur deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public Indicateur domaine(Domaine domaine) {
        this.domaine = domaine;
        return this;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Indicateur)) {
            return false;
        }
        return id != null && id.equals(((Indicateur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Indicateur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", image1='" + getImage1() + "'" +
            ", image1ContentType='" + getImage1ContentType() + "'" +
            ", image2='" + getImage2() + "'" +
            ", image2ContentType='" + getImage2ContentType() + "'" +
            ", totalPoint=" + getTotalPoint() +
            ", interval='" + isInterval() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

    public byte[] getImageModaliteOK() {
        return imageModaliteOK;
    }

    public void setImageModaliteOK(byte[] imageModaliteOK) {
        this.imageModaliteOK = imageModaliteOK;
    }

    public String getImageModaliteOKContentType() {
        return imageModaliteOKContentType;
    }

    public void setImageModaliteOKContentType(String imageModaliteOKContentType) {
        this.imageModaliteOKContentType = imageModaliteOKContentType;
    }

    public byte[] getImageModaliteNOK() {
        return imageModaliteNOK;
    }

    public void setImageModaliteNOK(byte[] imageModaliteNOK) {
        this.imageModaliteNOK = imageModaliteNOK;
    }

    public String getImageModaliteNOKContentType() {
        return imageModaliteNOKContentType;
    }

    public void setImageModaliteNOKContentType(String imageModaliteNOKContentType) {
        this.imageModaliteNOKContentType = imageModaliteNOKContentType;
    }

    public Boolean getSousIndicateur() {
        return sousIndicateur;
    }

    public void setSousIndicateur(Boolean sousIndicateur) {
        this.sousIndicateur = sousIndicateur;
    }

    public String getUniteBorne() {
        return uniteBorne;
    }

    public void setUniteBorne(String uniteBorne) {
        this.uniteBorne = uniteBorne;
    }

    public Double getBorneMin() {
        return borneMin;
    }

    public void setBorneMin(Double borneMin) {
        this.borneMin = borneMin;
    }

    public Double getBorneMax() {
        return borneMax;
    }

    public void setBorneMax(Double borneMax) {
        this.borneMax = borneMax;
    }
}
