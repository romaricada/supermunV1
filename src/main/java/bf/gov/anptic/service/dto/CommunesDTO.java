package bf.gov.anptic.service.dto;
import bf.gov.anptic.util.Score;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link bf.gov.anptic.domain.Commune} entity.
 */
public class CommunesDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String code;

    @NotNull
    private String libelle;

    @NotNull
    private Boolean validated;

    @NotNull
    private Boolean participe;

    private Integer population;

    private Double superficie;

    private Float positionLabelLat;

    private Float positionLabelLon;

    private Boolean deleted;

    private Long provinceId;

    private String libelleProvince;

    private Double scoreCommune;

    private Long regionId;

    private String libelleRegion;

    private Integer rangNational;

    private Integer RangRegional;

    private List<Score> scores = new ArrayList<>();

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

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public Float getPositionLabelLat() {
        return positionLabelLat;
    }

    public void setPositionLabelLat(Float positionLabelLat) {
        this.positionLabelLat = positionLabelLat;
    }

    public Float getPositionLabelLon() {
        return positionLabelLon;
    }

    public void setPositionLabelLon(Float positionLabelLon) {
        this.positionLabelLon = positionLabelLon;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Double getScoreCommune() {
        return scoreCommune;
    }

    public void setScoreCommune(Double scoreCommune) {
        this.scoreCommune = scoreCommune;
    }

    public Integer getRangNational() {
        return rangNational;
    }

    public void setRangNational(Integer rangNational) {
        this.rangNational = rangNational;
    }

    public Integer getRangRegional() {
        return RangRegional;
    }

    public void setRangRegional(Integer rangRegional) {
        RangRegional = rangRegional;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommunesDTO communeDTO = (CommunesDTO) o;
        if (communeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), communeDTO.getId());
    }



    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommuneDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", population=" + getPopulation() +
            ", superficie=" + getSuperficie() +
            ", positionLabelLat=" + getPositionLabelLat() +
            ", positionLabelLon=" + getPositionLabelLon() +
            ", deleted='" + isDeleted() + "'" +
            ", province=" + getProvinceId() +
            ", libelleProvince=" + getLibelleProvince() +

            "}";
    }

    public String getLibelleProvince() {
        return libelleProvince;
    }

    public void setLibelleProvince(String libelleProvince) {
        this.libelleProvince = libelleProvince;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getLibelleRegion() {
        return libelleRegion;
    }

    public void setLibelleRegion(String libelleRegion) {
        this.libelleRegion = libelleRegion;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Boolean getParticipe() {
        return participe;
    }

    public void setParticipe(Boolean participe) {
        this.participe = participe;
    }
}
