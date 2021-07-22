package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.ValidationCommuneService;
import bf.gov.anptic.service.dto.ValidationCommuneDTO;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.EtatCommune}.
 */
@RestController
@RequestMapping("/api")
public class ValidationCommuneResource {

    private final Logger log = LoggerFactory.getLogger(ValidationCommuneResource.class);

    private static final String ENTITY_NAME = "validationCommune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidationCommuneService validationCommuneService;

    public ValidationCommuneResource(ValidationCommuneService validationCommuneService) {
        this.validationCommuneService = validationCommuneService;
    }

    /**
     * {@code POST  /etat-communes} : Create a new etatCommune.
     *
     * @param validationCommune the validationCommune to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validationCommune, or with status {@code 400 (Bad Request)} if the etatCommune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validation-commune")
    public ResponseEntity<ValidationCommuneDTO> createEtatCommune(@Valid @RequestBody ValidationCommuneDTO validationCommune) throws URISyntaxException {
        log.debug("REST request to save EtatCommune : {}", validationCommune);
        if (validationCommune.getId() != null) {
            throw new BadRequestAlertException("A new etatCommune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValidationCommuneDTO result = validationCommuneService.save(validationCommune);
        return ResponseEntity.created(new URI("/api/validation-commune/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validation-commune} : Updates an existing etatCommune.
     *
     * @param validationCommune the validationCommune to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationCommune,
     * or with status {@code 400 (Bad Request)} if the validationCommune is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validationCommune couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validation-commune")
    public ResponseEntity<ValidationCommuneDTO> updateEtatCommune(@Valid @RequestBody ValidationCommuneDTO validationCommune) throws URISyntaxException {
        log.debug("REST request to update EtatCommune : {}", validationCommune);
        if (validationCommune.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValidationCommuneDTO result = validationCommuneService.save(validationCommune);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, validationCommune.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /validation-commune} : get all the etatCommunes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etatCommunes in body.
     */
    @GetMapping("/validation-commune")
    public ResponseEntity<List<ValidationCommuneDTO>> getAllEtatCommunes(Pageable pageable) {
        log.debug("REST request to get a page of EtatCommunes");
        Page<ValidationCommuneDTO> page = validationCommuneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /validation-commune/:id} : get the "id" etatCommune.
     *
     * @param id the id of the validationCommune to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validationCommune, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validation-commune/{id}")
    public ResponseEntity<ValidationCommuneDTO> getEtatCommune(@PathVariable Long id) {
        log.debug("REST request to get EtatCommune : {}", id);
        Optional<ValidationCommuneDTO> validationCommune = validationCommuneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(validationCommune);
    }

    /**
     * {@code DELETE  /validation-commune/:id} : delete the "id" etatCommune.
     *
     * @param id the id of the validationCommune to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validation-commune/{id}")
    public ResponseEntity<Void> deleteEtatCommune(@PathVariable Long id) {
        log.debug("REST request to delete EtatCommune : {}", id);
        validationCommuneService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
