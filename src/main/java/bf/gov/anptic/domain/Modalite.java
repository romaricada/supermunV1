package bf.gov.anptic.domain;

import bf.gov.anptic.domain.enumeration.Formule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * A Modalite.
 */
@Entity
@Table(name = "modalite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Modalite extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "borne_maximale")
    private Double borneMaximale;

    @Column(name = "borne_minimale")
    private Double borneMinimale;

    @Column(name = "valeur")
    private Double valeur;

    @Column(name = "deleted")
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    private Formule formule;

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

    @ManyToOne()
    @JsonIgnoreProperties("modalites")
    private Indicateur indicateur;


    @OneToMany(mappedBy = "modalite", cascade = CascadeType.ALL)
    private Set<ValeurModalite> valeurModalites;

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

    public Modalite code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Modalite libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getBorneMaximale() {
        return borneMaximale;
    }

    public Modalite borneMaximale(Double borneMaximale) {
        this.borneMaximale = borneMaximale;
        return this;
    }

    public void setBorneMaximale(Double borneMaximale) {
        this.borneMaximale = borneMaximale;
    }

    public Double getBorneMinimale() {
        return borneMinimale;
    }

    public Modalite borneMinimale(Double borneMinimale) {
        this.borneMinimale = borneMinimale;
        return this;
    }

    public void setBorneMinimale(Double borneMinimale) {
        this.borneMinimale = borneMinimale;
    }

    public Double getValeur() {
        return valeur;
    }

    public Modalite valeur(Double valeur) {
        this.valeur = valeur;
        return this;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Modalite deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Indicateur getIndicateur() {
        return indicateur;
    }

    public Modalite indicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
        return this;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public Modalite formule(Formule formule) {
        this.formule = formule;
        return this;
    }

    public void setIndicateur(Indicateur indicateur) {
        this.indicateur = indicateur;
    }

    public byte[] getImage1() {
        return image1;
    }

    public Modalite image1(byte[] image1) {
        this.image1 = image1;
        return this;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return image1ContentType;
    }

    public Modalite image1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
        return this;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return image2;
    }

    public Modalite image2(byte[] image2) {
        this.image2 = image2;
        return this;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return image2ContentType;
    }

    public Modalite image2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
        return this;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Modalite)) {
            return false;
        }
        return id != null && id.equals(((Modalite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Modalite{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", borneMaximale=" + getBorneMaximale() +
            ", borneMinimale=" + getBorneMinimale() +
            ", valeur=" + getValeur() +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }

    public Set<ValeurModalite> getValeurModalites() {
        return valeurModalites;
    }

    public void setValeurModalites(Set<ValeurModalite> valeurModalites) {
        this.valeurModalites = valeurModalites;
    }
}
