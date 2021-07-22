package bf.gov.anptic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Commune.
 */
@Entity
@Table(name = "commune")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Commune extends AbstractAuditingEntity implements Serializable {

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

    @Column(name = "population")
    private Integer population;

    @Column(name = "superficie")
    private Double superficie;

    @Column(name = "position_label_lat")
    private Float positionLabelLat;

    @Column(name = "position_label_lon")
    private Float positionLabelLon;

    @Lob
    @Basic(optional = false)
    private byte[] geom;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties("communes")
    private Province province;

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

    public Commune code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public Commune libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getPopulation() {
        return population;
    }

    public Commune population(Integer population) {
        this.population = population;
        return this;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public Commune superficie(Double superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Float getPositionLabelLat() {
        return positionLabelLat;
    }

    public Commune positionLabelLat(Float positionLabelLat) {
        this.positionLabelLat = positionLabelLat;
        return this;
    }

    public void setPositionLabelLat(Float positionLabelLat) {
        this.positionLabelLat = positionLabelLat;
    }

    public Float getPositionLabelLon() {
        return positionLabelLon;
    }

    public Commune positionLabelLon(Float positionLabelLon) {
        this.positionLabelLon = positionLabelLon;
        return this;
    }

    public void setPositionLabelLon(Float positionLabelLon) {
        this.positionLabelLon = positionLabelLon;
    }

    public byte[] getGeom() {
        return geom;
    }

    public Commune geom(byte[] geom) {
        this.geom = geom;
        return this;
    }

    public void setGeom(byte[] geom) {
        this.geom = geom;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Commune deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Province getProvince() {
        return province;
    }

    public Commune province(Province province) {
        this.province = province;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commune)) {
            return false;
        }
        return id != null && id.equals(((Commune) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Commune{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", population=" + getPopulation() +
            ", superficie=" + getSuperficie() +
            ", positionLabelLat=" + getPositionLabelLat() +
            ", positionLabelLon=" + getPositionLabelLon() +
            ", geom='" + getGeom() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
