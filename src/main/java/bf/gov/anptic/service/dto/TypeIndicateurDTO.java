package bf.gov.anptic.service.dto;

import bf.gov.anptic.domain.Domaine;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.TypeIndicateur} entity.
 */
public class TypeIndicateurDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;


    private String libelle;

    private Double scoreTypeIndicateur;

    private Double nombreEtoile;

    private  Double totalTypeDomaine;

    private List<DomaineDTO> domaines = new ArrayList<>();

    private Boolean deleted;


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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Double getScoreTypeIndicateur() {
        return scoreTypeIndicateur;
    }

    public void setScoreTypeIndicateur(Double scoreTypeIndicateur) {
        this.scoreTypeIndicateur = scoreTypeIndicateur;
    }

    public List<DomaineDTO> getDomaines() {
        return domaines;
    }

    public void setDomaines(List<DomaineDTO> domaines) {
        this.domaines = domaines;
    }

    public Double getNombreEtoile() {
        return nombreEtoile;
    }

    public void setNombreEtoile(Double nombreEtoile) {
        this.nombreEtoile = nombreEtoile;
    }

    public Double getTotalTypeDomaine() {
        return totalTypeDomaine;
    }

    public void setTotalTypeDomaine(Double totalTypeDomaine) {
        this.totalTypeDomaine = totalTypeDomaine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeIndicateurDTO typeIndicateurDTO = (TypeIndicateurDTO) o;
        if (typeIndicateurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeIndicateurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeIndicateurDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", deleted='" + isDeleted() + "'" +
            "}";
    }
}
