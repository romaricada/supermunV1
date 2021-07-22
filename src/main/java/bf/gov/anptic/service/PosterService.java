package bf.gov.anptic.service;

import bf.gov.anptic.domain.Poster;
import bf.gov.anptic.repository.PosterRepository;
import bf.gov.anptic.service.dto.PosterDTO;
import bf.gov.anptic.service.mapper.PosterMapper;
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
 * Service Implementation for managing {@link Poster}.
 */
@Service
@Transactional
public class PosterService {

    private final Logger log = LoggerFactory.getLogger(PosterService.class);

    private final PosterRepository posterRepository;

    private final PosterMapper posterMapper;

    public PosterService(PosterRepository posterRepository, PosterMapper posterMapper) {
        this.posterRepository = posterRepository;
        this.posterMapper = posterMapper;
    }

    /**
     * Save a poster.
     *
     * @param posterDTO the entity to save.
     * @return the persisted entity.
     */
    public PosterDTO save(PosterDTO posterDTO) {
        log.debug("Request to save Poster : {}", posterDTO);
        Poster poster = posterMapper.toEntity(posterDTO);
        poster = posterRepository.save(poster);
        return posterMapper.toDto(poster);
    }

    /**
     * Get all the posters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PosterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posters");
        List<PosterDTO> posters =posterRepository.findAll().stream().filter(val-> !val.isDeleted())
        .map(posterMapper::toDto).collect(Collectors.toList());
        Page<PosterDTO> page = new PageImpl<>(posters,pageable,posters.size());
        return page;
    }


    /**
     * Get one poster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PosterDTO> findOne(Long id) {
        log.debug("Request to get Poster : {}", id);
        return posterRepository.findById(id)
            .map(posterMapper::toDto);
    }

    /**
     * Delete the poster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Poster : {}", id);
        posterRepository.deleteById(id);
    }
    public Long deleteAll(List<PosterDTO> postersDTO) {
        log.debug("Request to delete Poster : {}", postersDTO);
        List<Poster> posters = posterMapper.toEntity(postersDTO);
        if(!posters.isEmpty()){
            posterRepository.saveAll(posters);
        }
        return Long.parseLong(""+posters.size());
    }
}
