package bf.gov.anptic.service.dto;
import bf.gov.anptic.domain.enumeration.Formule;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Modalite} entity.
 */
public class ModaliteDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String code;

    private String libelle;

    private Double borneMaximale;

    private Double borneMinimale;

    private Double valeur;

    private Boolean deleted;

    private Formule formule;

    @Lob
    private byte[] image1;

    private String image1ContentType;
    @Lob
    private byte[] image2;

    private String image2ContentType;

    private Long indicateurId;

    private IndicateurDTO indicateur = new IndicateurDTO();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getBorneMaximale() {
        return borneMaximale;
    }

    public void setBorneMaximale(Double borneMaximale) {
        this.borneMaximale = borneMaximale;
    }

    public Double getBorneMinimale() {
        return borneMinimale;
    }

    public void setBorneMinimale(Double borneMinimale) {
        this.borneMinimale = borneMinimale;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getIndicateurId() {
        return indicateurId;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return image1ContentType;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return image2ContentType;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ModaliteDTO modaliteDTO = (ModaliteDTO) o;
        if (modaliteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modaliteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModaliteDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", borneMaximale=" + getBorneMaximale() +
            ", borneMinimale=" + getBorneMinimale() +
            ", valeur=" + getValeur() +
            ", deleted='" + isDeleted() + "'" +
            ", indicateurId=" + getIndicateurId() +
            ", indicateur=" + getIndicateur() +
            "}";
    }

    public IndicateurDTO getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(IndicateurDTO indicateur) {
        this.indicateur = indicateur;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }
}
