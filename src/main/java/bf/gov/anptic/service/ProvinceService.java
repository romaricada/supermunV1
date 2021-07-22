package bf.gov.anptic.service;

import bf.gov.anptic.domain.Province;
import bf.gov.anptic.repository.ProvinceRepository;
import bf.gov.anptic.service.dto.ProvinceDTO;
import bf.gov.anptic.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceRepository provinceRepository;

    @Autowired
    private CommuneService communeService;

    private final ProvinceMapper provinceMapper;

    public ProvinceService(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.toEntity(provinceDTO);
        province = provinceRepository.save(province);
        return provinceMapper.toDto(province);
    }

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        List<ProvinceDTO> provinces =provinceRepository.findAll().stream().filter(val-> !val.isDeleted())
        .map(provinceMapper::toDto).collect(Collectors.toList());
        Page<ProvinceDTO> page = new PageImpl<>(provinces,pageable,provinces.size());
        return page;
    }


    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProvinceDTO> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id)
            .map(provinceMapper::toDto);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
    public Long deleteAll(List<ProvinceDTO> provincesDTO) {
        log.debug("Request to delete Province : {}", provincesDTO);
        List<Province> provinces = provinceMapper.toEntity(provincesDTO);
        if(!provinces.isEmpty()){
            provinceRepository.saveAll(provinces);
        }
        return Long.parseLong(""+provinces.size());
    }

    @Transactional(readOnly = true)
    public List<ProvinceDTO> findAllProvinces() {
        log.debug("Request to get all provinces no page");
        return provinceRepository.findAll().stream().filter(val->val.isDeleted()!= null && !val.isDeleted())
            .map(provinceMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProvinceDTO> findAllProvinceByRegion(Long idRegion) {
        log.debug("Request to get all provinces by region");
        return provinceRepository.findAll().stream().
            filter(province -> province.getRegion()!=null && province.getRegion().getId().equals(idRegion) &&
             province.isDeleted()!= null && !province.isDeleted())
            .map(provinceMapper::toDto).collect(Collectors.toList());
    }

    public List<ProvinceDTO> findAllCommunesbyProvince(Long anneeId){
        log.debug("request to find all communes by province");
        List<ProvinceDTO> provinceDTOs = provinceRepository.findAll().stream().map(provinceMapper::toDto)
        .collect(Collectors.toList());
        provinceDTOs.forEach(Province-> Province.setCommunes(communeService.findAllCommuneByProvince(Province.getId(), anneeId)));

        return provinceDTOs;
    }


}
