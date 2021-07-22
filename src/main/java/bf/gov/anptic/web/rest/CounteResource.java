package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.CounteService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.CounteDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.Counte}.
 */
@RestController
@RequestMapping("/api")
public class CounteResource {

    private final Logger log = LoggerFactory.getLogger(CounteResource.class);

    private static final String ENTITY_NAME = "counte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounteService counteService;

    public CounteResource(CounteService counteService) {
        this.counteService = counteService;
    }

    /**
     * {@code POST  /countes} : Create a new counte.
     *
     * @param counteDTO the counteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counteDTO, or with status {@code 400 (Bad Request)} if the counte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/countes")
    public ResponseEntity<CounteDTO> createCounte(@RequestBody CounteDTO counteDTO) throws URISyntaxException {
        log.debug("REST request to save Counte : {}", counteDTO);
        if (counteDTO.getId() != null) {
            throw new BadRequestAlertException("A new counte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounteDTO result = counteService.save(counteDTO);
        return ResponseEntity.created(new URI("/api/countes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /countes} : Updates an existing counte.
     *
     * @param counteDTO the counteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counteDTO,
     * or with status {@code 400 (Bad Request)} if the counteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/countes")
    public ResponseEntity<CounteDTO> updateCounte(@RequestBody CounteDTO counteDTO) throws URISyntaxException {
        log.debug("REST request to update Counte : {}", counteDTO);
        if (counteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CounteDTO result = counteService.save(counteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, counteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /countes} : get all the countes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countes in body.
     */
    @GetMapping("/countes")
    public List<CounteDTO> getAllCountes() {
        log.debug("REST request to get all Countes");
        return counteService.findAll();
    }

    /**
     * {@code GET  /countes/:id} : get the "id" counte.
     *
     * @param id the id of the counteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/countes/{id}")
    public ResponseEntity<CounteDTO> getCounte(@PathVariable Long id) {
        log.debug("REST request to get Counte : {}", id);
        Optional<CounteDTO> counteDTO = counteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counteDTO);
    }

    /**
     * {@code DELETE  /countes/:id} : delete the "id" counte.
     *
     * @param id the id of the counteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/countes/{id}")
    public ResponseEntity<Void> deleteCounte(@PathVariable Long id) {
        log.debug("REST request to delete Counte : {}", id);
        counteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
