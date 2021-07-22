package bf.gov.anptic.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Domaine} entity.
 */
public class DomaineDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String libelle;

    private String description;

    private Double pointTotal;

    @Lob
    private byte[] image;

    private String imageContentType;

    private  Double totalScore;

    private List<IndicateurDTO> indicateurs = new ArrayList<>();

    private Double nbEtoile;

    private Boolean deleted;


    private Long typeIndicateurId;

    private TypeIndicateurDTO typeIndicateur = new TypeIndicateurDTO();

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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getTypeIndicateurId() {
        return typeIndicateurId;
    }

    public void setTypeIndicateurId(Long typeIndicateurId) {
        this.typeIndicateurId = typeIndicateurId;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public List<IndicateurDTO> getIndicateurs() {
        return indicateurs;
    }

    public void setIndicateurs(List<IndicateurDTO> indicateurs) {
        this.indicateurs = indicateurs;
    }

    public Double getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(Double pointTotal) {
        this.pointTotal = pointTotal;
    }

    public Double getNbEtoile() {
        return nbEtoile;
    }

    public void setNbEtoile(Double nbEtoile) {
        this.nbEtoile = nbEtoile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DomaineDTO domaineDTO = (DomaineDTO) o;
        if (domaineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), domaineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DomaineDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", typeIndicateur=" + getTypeIndicateurId() +
            ", totalScore=" + getTotalScore() +
            ", totalPoint=" + getPointTotal() +
            "}";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeIndicateurDTO getTypeIndicateur() {
        return typeIndicateur;
    }

    public void setTypeIndicateur(TypeIndicateurDTO typeIndicateur) {
        this.typeIndicateur = typeIndicateur;
    }
}
