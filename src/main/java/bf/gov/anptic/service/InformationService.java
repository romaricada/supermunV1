package bf.gov.anptic.service;

import bf.gov.anptic.domain.Information;
import bf.gov.anptic.repository.InformationRepository;
import bf.gov.anptic.service.dto.IndicateurDTO;
import bf.gov.anptic.service.dto.InformationDTO;
import bf.gov.anptic.service.mapper.InformationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Information}.
 */
@Service
@Transactional
public class InformationService {

    private final Logger log = LoggerFactory.getLogger(InformationService.class);

    private final InformationRepository informationRepository;

    private final InformationMapper informationMapper;

    public InformationService(InformationRepository informationRepository, InformationMapper informationMapper) {
        this.informationRepository = informationRepository;
        this.informationMapper = informationMapper;
    }

    /**
     * Save a information.
     *
     * @param informationDTO the entity to save.
     * @return the persisted entity.
     */
    public InformationDTO save(InformationDTO informationDTO) {
        log.debug("Request to save Information : {}", informationDTO);
        Information information = informationMapper.toEntity(informationDTO);
        information = informationRepository.save(information);
        return informationMapper.toDto(information);
    }

    /**
     * Get all the information.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InformationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Information");
        List<InformationDTO> infos =informationRepository.findAll().stream().filter(val-> !val.isDeleted())
        .map(informationMapper::toDto).collect(Collectors.toList());
        Page<InformationDTO> page = new PageImpl<>(infos,pageable,infos.size());
        return page;
    }

    @Transactional(readOnly = true)
    public List<InformationDTO> findAllInformation() {
        log.debug("Request to get all informations no page");
        return informationRepository.findAll().stream().filter(val->val.isDeleted()!= null && !val.isDeleted()).map(informationMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one information by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InformationDTO> findOne(Long id) {
        log.debug("Request to get Information : {}", id);
        return informationRepository.findById(id)
            .map(informationMapper::toDto);
    }

    /**
     * Delete the information by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Information : {}", id);
        informationRepository.deleteById(id);
    }
    public Long deleteAll(List<InformationDTO> informationsDTO) {
        log.debug("Request to delete Information : {}", informationsDTO);
        List<Information> informations = informationMapper.toEntity(informationsDTO);
        if(!informations.isEmpty()){
            informationRepository.saveAll(informations);
        }
        return Long.parseLong(""+informations.size());
    }
}
