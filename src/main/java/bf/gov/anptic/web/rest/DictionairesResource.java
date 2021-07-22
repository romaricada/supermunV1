package bf.gov.anptic.web.rest;

import bf.gov.anptic.domain.Dictionaires;
import bf.gov.anptic.repository.DictionairesRepository;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.Dictionaires}.
 */
@RestController
@RequestMapping("/api")
public class DictionairesResource {

    private final Logger log = LoggerFactory.getLogger(DictionairesResource.class);

    private static final String ENTITY_NAME = "dictionaires";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DictionairesRepository dictionairesRepository;

    public DictionairesResource(DictionairesRepository dictionairesRepository) {
        this.dictionairesRepository = dictionairesRepository;
    }

    /**
     * {@code POST  /dictionaires} : Create a new dictionaires.
     *
     * @param dictionaires the dictionaires to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dictionaires, or with status {@code 400 (Bad Request)} if the dictionaires has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dictionaires")
    public ResponseEntity<Dictionaires> createDictionaires(@Valid @RequestBody Dictionaires dictionaires) throws URISyntaxException {
        log.debug("REST request to save Dictionaires : {}", dictionaires);
        if (dictionaires.getId() != null) {
            throw new BadRequestAlertException("A new dictionaires cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dictionaires result = dictionairesRepository.save(dictionaires);
        return ResponseEntity.created(new URI("/api/dictionaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dictionaires} : Updates an existing dictionaires.
     *
     * @param dictionaires the dictionaires to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dictionaires,
     * or with status {@code 400 (Bad Request)} if the dictionaires is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dictionaires couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dictionaires")
    public ResponseEntity<Dictionaires> updateDictionaires(@Valid @RequestBody Dictionaires dictionaires) throws URISyntaxException {
        log.debug("REST request to update Dictionaires : {}", dictionaires);
        if (dictionaires.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dictionaires result = dictionairesRepository.save(dictionaires);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dictionaires.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dictionaires} : get all the dictionaires.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dictionaires in body.
     */
    @GetMapping("/dictionaires")
    public List<Dictionaires> getAllDictionaires() {
        log.debug("REST request to get all Dictionaires");
        return dictionairesRepository.findAll();
    }

    /**
     * {@code GET  /dictionaires/:id} : get the "id" dictionaires.
     *
     * @param id the id of the dictionaires to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dictionaires, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dictionaires/{id}")
    public ResponseEntity<Dictionaires> getDictionaires(@PathVariable Long id) {
        log.debug("REST request to get Dictionaires : {}", id);
        Optional<Dictionaires> dictionaires = dictionairesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dictionaires);
    }

    /**
     * {@code DELETE  /dictionaires/:id} : delete the "id" dictionaires.
     *
     * @param id the id of the dictionaires to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dictionaires/{id}")
    public ResponseEntity<Void> deleteDictionaires(@PathVariable Long id) {
        log.debug("REST request to delete Dictionaires : {}", id);
        dictionairesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
