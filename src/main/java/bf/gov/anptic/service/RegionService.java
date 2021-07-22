package bf.gov.anptic.service;

import bf.gov.anptic.domain.Region;
import bf.gov.anptic.repository.RegionRepository;
import bf.gov.anptic.service.dto.RegionDTO;
import bf.gov.anptic.service.mapper.RegionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Region}.
 */
@Service
@Transactional
public class RegionService {

    private final Logger log = LoggerFactory.getLogger(RegionService.class);

    private final RegionRepository regionRepository;

    private final RegionMapper regionMapper;

    public RegionService(RegionRepository regionRepository, RegionMapper regionMapper) {
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
    }

    /**
     * Save a region.
     *
     * @param regionDTO the entity to save.
     * @return the persisted entity.
     */
    public RegionDTO save(RegionDTO regionDTO) {
        log.debug("Request to save Region : {}", regionDTO);
        Region region = regionMapper.toEntity(regionDTO);
        region = regionRepository.save(region);
        return regionMapper.toDto(region);
    }

    /**
     * Get all the regions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RegionDTO> findAll(Pageable pageable) throws IOException {
        log.debug("Request to get all Regions");
        List<RegionDTO> regions = regionRepository.findAll().stream().filter(val -> !val.isDeleted())
            .sorted(Comparator.comparing(Region::getLibelle)).map(regionMapper::toDto).collect(Collectors.toList());
        Page<RegionDTO> page = new PageImpl<>(regions, pageable, regions.size());
        return page;
    }


    /**
     * Get one region by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegionDTO> findOne(Long id) {
        log.debug("Request to get Region : {}", id);
        return regionRepository.findById(id)
            .map(regionMapper::toDto);
    }

    /**
     * Delete the region by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Region : {}", id);
        regionRepository.deleteById(id);
    }

    public Long deleteAll(List<RegionDTO> regionsDTO) {
        log.debug("Request to delete Region : {}", regionsDTO);
        List<Region> regions = regionMapper.toEntity(regionsDTO);
        if (!regions.isEmpty()) {
            regionRepository.saveAll(regions);
        }
        return Long.parseLong("" + regions.size());
    }


    public List<RegionDTO> findAllRegion() {
        return regionRepository.findAllByDeletedIsFalse().stream()
            .sorted(Comparator.comparing(Region::getCode)).map(regionMapper::toDto).collect(Collectors.toList());
    }
}
