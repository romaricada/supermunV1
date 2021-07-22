package bf.gov.anptic.service;

import bf.gov.anptic.domain.TypePublication;
import bf.gov.anptic.repository.TypePublicationRepository;
import bf.gov.anptic.service.dto.TypePublicationDTO;
import bf.gov.anptic.service.mapper.TypePublicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TypePublication}.
 */
@Service
@Transactional
public class TypePublicationService {

    private final Logger log = LoggerFactory.getLogger(TypePublicationService.class);

    private final TypePublicationRepository typePublicationRepository;

    private final TypePublicationMapper typePublicationMapper;

    public TypePublicationService(TypePublicationRepository typePublicationRepository, TypePublicationMapper typePublicationMapper) {
        this.typePublicationRepository = typePublicationRepository;
        this.typePublicationMapper = typePublicationMapper;
    }

    /**
     * Save a typePublication.
     *
     * @param typePublicationDTO the entity to save.
     * @return the persisted entity.
     */
    public TypePublicationDTO save(TypePublicationDTO typePublicationDTO) {
        log.debug("Request to save TypePublication : {}", typePublicationDTO);
        TypePublication typePublication = typePublicationMapper.toEntity(typePublicationDTO);
        typePublication = typePublicationRepository.save(typePublication);
        return typePublicationMapper.toDto(typePublication);
    }

    /**
     * Get all the typePublications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePublicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypePublications");
        return typePublicationRepository.findAll(pageable)
            .map(typePublicationMapper::toDto);
    }


    /**
     * Get one typePublication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypePublicationDTO> findOne(Long id) {
        log.debug("Request to get TypePublication : {}", id);
        return typePublicationRepository.findById(id)
            .map(typePublicationMapper::toDto);
    }

    /**
     * Delete the typePublication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypePublication : {}", id);
        TypePublication typePublication = typePublicationRepository.getOne(id);
        if (typePublication.getPublications().isEmpty()) {
            typePublicationRepository.deleteById(id);
        }
    }

    public List<TypePublicationDTO> getAllTypePublication() {
        return typePublicationRepository.findAll().stream()
            .map(typePublicationMapper::toDto).collect(Collectors.toList());
    }
}
