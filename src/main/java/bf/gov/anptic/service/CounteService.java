package bf.gov.anptic.service;

import bf.gov.anptic.domain.Counte;
import bf.gov.anptic.repository.CounteRepository;
import bf.gov.anptic.service.dto.CounteDTO;
import bf.gov.anptic.service.mapper.CounteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Counte}.
 */
@Service
@Transactional
public class CounteService {

    private final Logger log = LoggerFactory.getLogger(CounteService.class);

    private final CounteRepository counteRepository;

    private final CounteMapper counteMapper;

    public CounteService(CounteRepository counteRepository, CounteMapper counteMapper) {
        this.counteRepository = counteRepository;
        this.counteMapper = counteMapper;
    }

    /**
     * Save a counte.
     *
     * @param counteDTO the entity to save.
     * @return the persisted entity.
     */
    public CounteDTO save(CounteDTO counteDTO) {
        log.debug("Request to save Counte : {}", counteDTO);
        Counte counte = counteMapper.toEntity(counteDTO);
        try {
            InetAddress iAddress = InetAddress.getLocalHost();
            log.debug("====================== IP adresse ===========>");
            log.debug("{}",iAddress.getHostAddress());
            log.debug("========================= IP =====================>");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        counte = counteRepository.save(counte);
        return counteMapper.toDto(counte);
    }

    /**
     * Get all the countes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CounteDTO> findAll() {
        log.debug("Request to get all Countes");
        return counteRepository.findAll().stream()
            .map(counteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one counte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CounteDTO> findOne(Long id) {
        log.debug("Request to get Counte : {}", id);
        return counteRepository.findById(id)
            .map(counteMapper::toDto);
    }

    /**
     * Delete the counte by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Counte : {}", id);
        counteRepository.deleteById(id);
    }
}
