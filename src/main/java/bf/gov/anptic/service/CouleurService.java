package bf.gov.anptic.service;

import bf.gov.anptic.domain.Couleur;
import bf.gov.anptic.repository.CouleurRepository;
import bf.gov.anptic.service.dto.CouleurDTO;
import bf.gov.anptic.service.mapper.CouleurMapper;
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
 * Service Implementation for managing {@link Couleur}.
 */
@Service
@Transactional
public class CouleurService {

    private final Logger log = LoggerFactory.getLogger(CouleurService.class);

    private final CouleurRepository couleurRepository;

    private final CouleurMapper couleurMapper;


    public CouleurService(CouleurRepository couleurRepository, CouleurMapper couleurMapper) {
        this.couleurRepository = couleurRepository;
        this.couleurMapper = couleurMapper;
    }

    /**
     * Save a couleur.
     *
     * @param couleurDTO the entity to save.
     * @return the persisted entity.
     */
    public CouleurDTO save(CouleurDTO couleurDTO) {
        log.debug("Request to save Couleur : {}", couleurDTO);
        Couleur couleur = couleurMapper.toEntity(couleurDTO);
        couleur = couleurRepository.save(couleur);
        return couleurMapper.toDto(couleur);
    }

    /**
     * Get all the couleurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CouleurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Couleurs");
        return couleurRepository.findAllByDeletedFalse(pageable)
            .map(couleurMapper::toDto);
    }


    /**
     * Get one couleur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CouleurDTO> findOne(Long id) {
        log.debug("Request to get Couleur : {}", id);
        return couleurRepository.findById(id)
            .map(couleurMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CouleurDTO> saveListeColor(List<CouleurDTO> couleurs) {
        log.debug("Request to put a color by indicateur {}");
            if(!couleurs.isEmpty()){
                return couleurMapper.toDto(couleurRepository.saveAll(couleurMapper.toEntity(couleurs))) ;
            }else{
                return null;
            }

    }

    /**
     * Delete the couleur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Couleur : {}", id);
        couleurRepository.deleteById(id);
    }
    public Long deleteAll(List<CouleurDTO> couleursDTO) {
        log.debug("Request to delete Couleur : {}", couleursDTO);
        List<Couleur> couleurs = couleurMapper.toEntity(couleursDTO);
        if(!couleurs.isEmpty()){
            couleurRepository.saveAll(couleurs);
        }
        return Long.parseLong(""+couleurs.size());
    }

    public List<CouleurDTO> findAllCouleurByIndicateur(Long idIndicateur) {
       return couleurRepository.findAllByDeletedFalseAndIndicateurId(idIndicateur).stream()
           .map(couleurMapper::toDto).collect(Collectors.toList());
    }

    public List<CouleurDTO> findAllCouleur() {
       return couleurRepository.findAllByDeletedFalse().stream()
           .map(couleurMapper::toDto).collect(Collectors.toList());
    }
}
