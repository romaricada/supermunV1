package bf.gov.anptic.service.dto;
import bf.gov.anptic.domain.enumeration.TypePerformance;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Performance} entity.
 */
public class PerformanceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Double score;

    private Boolean deleted;

    private Long communeId;

    private Long domaineId;

    private String domaineLibelle;

    private String indicateurLibelle;

    private Long typeDomainId;

    private Double nbEtoile;

    private TypePerformance typePerformance;

    private CommuneDTO commune = new CommuneDTO();

    private Long indicateurId;

   // private IndicateurDTO indicateur = new IndicateurDTO();

    private IndicateursDTO indicateur = new IndicateursDTO();

    private Long exerciceId;

    private ExerciceDTO exercice = new ExerciceDTO();

    private Integer anneePreced;

    private String scoreAnneePrec;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCommuneId() {
        return communeId;
    }

    public void setCommuneId(Long communeId) {
        this.communeId = communeId;
    }

    public Long getIndicateurId() {
        return indicateurId;
    }

    public void setIndicateurId(Long indicateurId) {
        this.indicateurId = indicateurId;
    }

    public Long getExerciceId() {
        return exerciceId;
    }

    public void setExerciceId(Long exerciceId) {
        this.exerciceId = exerciceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PerformanceDTO performanceDTO = (PerformanceDTO) o;
        if (performanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), performanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PerformanceDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", deleted='" + isDeleted() + "'" +
            ", communeId=" + getCommuneId() +
            ", commune=" + getCommune() +
            ", indicateurId=" + getIndicateurId() +
            ", indicateur=" + getIndicateur() +
            ", exerciceId=" + getExerciceId() +
            ", exercice=" + getExercice() +
            "}";
    }

    public CommuneDTO getCommune() {
        return commune;
    }

    public void setCommune(CommuneDTO commune) {
        this.commune = commune;
    }

    public IndicateursDTO getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(IndicateursDTO indicateur) {
        this.indicateur = indicateur;
    }

    public ExerciceDTO getExercice() {
        return exercice;
    }

    public void setExercice(ExerciceDTO exercice) {
        this.exercice = exercice;
    }

    public Long getDomaineId() {
        return domaineId;
    }

    public void setDomaineId(Long domaineId) {
        this.domaineId = domaineId;
    }

    public Long getTypeDomainId() {
        return typeDomainId;
    }

    public void setTypeDomainId(Long typeDomainId) {
        this.typeDomainId = typeDomainId;
    }

    public Double getNbEtoile() {
        return nbEtoile;
    }

    public void setNbEtoile(Double nbEtoile) {
        this.nbEtoile = nbEtoile;
    }

    public TypePerformance getTypePerformance() {
        return typePerformance;
    }

    public void setTypePerformance(TypePerformance typePerformance) {
        this.typePerformance = typePerformance;
    }

    public String getDomaineLibelle() {
        return domaineLibelle;
    }

    public void setDomaineLibelle(String domaineLibelle) {
        this.domaineLibelle = domaineLibelle;
    }

    public String getIndicateurLibelle() {
        return indicateurLibelle;
    }

    public void setIndicateurLibelle(String indicateurLibelle) {
        this.indicateurLibelle = indicateurLibelle;
    }

    public Integer getAnneePreced() {
        return anneePreced;
    }

    public void setAnneePreced(Integer anneePreced) {
        this.anneePreced = anneePreced;
    }

    public String getScoreAnneePrec() {
        return scoreAnneePrec;
    }

    public void setScoreAnneePrec(String scoreAnneePrec) {
        this.scoreAnneePrec = scoreAnneePrec;
    }
}
