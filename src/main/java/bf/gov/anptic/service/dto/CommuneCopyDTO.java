package bf.gov.anptic.service.dto;

import java.util.ArrayList;
import java.util.List;

public class CommuneCopyDTO {
    private Long id;
    private String libelle;
    private Boolean validated;
    private List<PerformanceDTO> performances = new ArrayList<>();
    private List<ValeurModaliteDTO> valeurModalites = new ArrayList<>();
    private EtatCommuneDTO etatCommune = new EtatCommuneDTO();
    private ValidationCommuneDTO validationCommune = new ValidationCommuneDTO();

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

    public List<PerformanceDTO> getPerformances() {
        return performances;
    }

    public void setPerformances(List<PerformanceDTO> performances) {
        this.performances = performances;
    }

    public List<ValeurModaliteDTO> getValeurModalites() {
        return valeurModalites;
    }

    public void setValeurModalites(List<ValeurModaliteDTO> valeurModalites) {
        this.valeurModalites = valeurModalites;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public EtatCommuneDTO getEtatCommune() {
        return etatCommune;
    }

    public void setEtatCommune(EtatCommuneDTO etatCommune) {
        this.etatCommune = etatCommune;
    }

    public ValidationCommuneDTO getValidationCommune() {
        return validationCommune;
    }

    public void setValidationCommuneDTO(ValidationCommuneDTO validationCommune) {
        this.validationCommune = validationCommune;
    }
}
