package bf.gov.anptic.service.dto;

import bf.gov.anptic.util.Score;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Indicateur} entity.
 */
public class IndicateursDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String libelle;

    private String description;

    @NotNull
    private Boolean sousIndicateur;

    private Double totalPoint;

    private Double sroreIndicateur;

    private Boolean interval;

    private Boolean deleted;

    private List<ModaliteDTO> modalites = new ArrayList<>();

    private List<Score> scores = new ArrayList<>();

    private Long domaineId;

    private DomainesDTO domaine = new DomainesDTO();

    private List<ValeurModaliteDTO> valeurmodalites = new ArrayList<>();

     private Double ValeurIndicateur;

    @NotNull
    private String uniteBorne;

    @NotNull
    private Double borneMin;

    @NotNull
    private Double borneMax;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Boolean isInterval() {
        return interval;
    }

    public void setInterval(Boolean interval) {
        this.interval = interval;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getDomaineId() {
        return domaineId;
    }

    public void setDomaineId(Long domaineId) {
        this.domaineId = domaineId;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public Double getSroreIndicateur() {
        return sroreIndicateur;
    }

    public void setSroreIndicateur(Double sroreIndicateur) {
        this.sroreIndicateur = sroreIndicateur;
    }

    public List<ValeurModaliteDTO> getValeurmodalites() {
        return valeurmodalites;
    }

    public void setValeurmodalites(List<ValeurModaliteDTO> valeurmodalites) {
        this.valeurmodalites = valeurmodalites;
    }

    public Double getValeurIndicateur() {
        return ValeurIndicateur;
    }

    public void setValeurIndicateur(Double valeurIndicateur) {
        ValeurIndicateur = valeurIndicateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IndicateursDTO indicateurDTO = (IndicateursDTO) o;
        if (indicateurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), indicateurDTO.getId());
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IndicateurDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalPoint=" + getTotalPoint() +
            ", interval='" + isInterval() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", domaineId=" + getDomaineId() +
            ", domaine=" + getDomaine() +
            "}";
    }

    public DomainesDTO getDomaine() {
        return domaine;
    }

    public void setDomaine(DomainesDTO domaine) {
        this.domaine = domaine;
    }

    public List<ModaliteDTO> getModalites() {
        return modalites;
    }

    public void setModalites(List<ModaliteDTO> modalites) {
        this.modalites = modalites;
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
