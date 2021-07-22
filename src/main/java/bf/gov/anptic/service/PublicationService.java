package bf.gov.anptic.service;

import bf.gov.anptic.domain.Publication;
import bf.gov.anptic.repository.PublicationRepository;
import bf.gov.anptic.service.dto.PublicationDTO;
import bf.gov.anptic.service.mapper.PublicationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Marker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Publication}.
 */
@Service
@Transactional
public class PublicationService {

    private final Logger log = LoggerFactory.getLogger(PublicationService.class);

    private final PublicationRepository publicationRepository;

    private final PublicationMapper publicationMapper;

    public PublicationService(PublicationRepository publicationRepository, PublicationMapper publicationMapper) {
        this.publicationRepository = publicationRepository;
        this.publicationMapper = publicationMapper;
    }

    /**
     * Save a publication.
     *
     * @param publicationDTO the entity to save.
     * @return the persisted entity.
     */
    public PublicationDTO save(PublicationDTO publicationDTO) {
        log.debug("Request to save Publication : {}", publicationDTO);
        Publication publication = publicationMapper.toEntity(publicationDTO);
        publication = publicationRepository.save(publication);
        return publicationMapper.toDto(publication);
    }

    /**
     * Get all the publications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PublicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Publications");
        return publicationRepository.findAllByDeletedIsFalse(pageable)
            .map(publicationMapper::toDto);
    }

    /**
     * Get one publication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublicationDTO> findOne(Long id) {
        log.debug("Request to get Publication : {}", id);
        return publicationRepository.findById(id)
            .map(publicationMapper::toDto);
    }

    /*** mohams modification****/
    @Transactional
    public byte[] downloagPub(Long id) {
        log.debug("Request to get Publication : {}", id);
        byte[] contenus = publicationRepository.getOne(id).getContenu();
        return contenus;
    }

    /**
     *
     * Delete the publication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Publication : {}", id);
        Publication publication = publicationRepository.getOne(id);
        publicationRepository.save(publication);
    }
    public Long deleteAll(List<PublicationDTO> publicationsDTO) {
        log.debug("Request to delete Publication : {}", publicationsDTO);
        List<Publication> publications = publicationMapper.toEntity(publicationsDTO);
        if(!publications.isEmpty()){
            publicationRepository.saveAll(publications);
        }
        return Long.parseLong(""+publications.size());
    }
}
