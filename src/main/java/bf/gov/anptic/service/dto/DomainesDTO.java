package bf.gov.anptic.service.dto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DomainesDTO {
    private Long id;

    private String code;

    @NotNull
    private String libelle;

    private String description;

    private Double pointTotal;

    private String imageContentType;

    private  Double totalScore;

    private Double nbEtoile;

    private Boolean deleted;

    private Long typeIndicateurId;

    private TypeIndicateurDTO typeIndicateur = new TypeIndicateurDTO();

    private List<IndicateursDTO> indicateurs = new ArrayList<>();

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

    public Double getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(Double pointTotal) {
        this.pointTotal = pointTotal;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Double getNbEtoile() {
        return nbEtoile;
    }

    public void setNbEtoile(Double nbEtoile) {
        this.nbEtoile = nbEtoile;
    }

    public Boolean getDeleted() {
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

    public TypeIndicateurDTO getTypeIndicateur() {
        return typeIndicateur;
    }

    public void setTypeIndicateur(TypeIndicateurDTO typeIndicateur) {
        this.typeIndicateur = typeIndicateur;
    }

    public List<IndicateursDTO> getIndicateurs() {
        return indicateurs;
    }

    public void setIndicateurs(List<IndicateursDTO> indicateurs) {
        this.indicateurs = indicateurs;
    }
}
