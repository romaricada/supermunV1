package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.DomaineService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.DomaineDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.Domaine}.
 */
@RestController
@RequestMapping("/api")
public class DomaineResource {

    private final Logger log = LoggerFactory.getLogger(DomaineResource.class);

    private static final String ENTITY_NAME = "domaine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineService domaineService;

    public DomaineResource(DomaineService domaineService) {
        this.domaineService = domaineService;
    }

    /**
     * {@code POST  /domaines} : Create a new domaine.
     *
     * @param domaineDTO the domaineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaineDTO, or with status {@code 400 (Bad Request)} if the domaine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/domaines")
    public ResponseEntity<DomaineDTO> createDomaine(@Valid @RequestBody DomaineDTO domaineDTO) throws URISyntaxException {
        log.debug("REST request to save Domaine : {}", domaineDTO);
        if (domaineDTO.getId() != null) {
            throw new BadRequestAlertException("A new domaine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DomaineDTO result = domaineService.save(domaineDTO);
        return ResponseEntity.created(new URI("/api/domaines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domaines} : Updates an existing domaine.
     *
     * @param domaineDTO the domaineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineDTO,
     * or with status {@code 400 (Bad Request)} if the domaineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/domaines")
    public ResponseEntity<DomaineDTO> updateDomaine(@Valid @RequestBody DomaineDTO domaineDTO) throws URISyntaxException {
        log.debug("REST request to update Domaine : {}", domaineDTO);
        if (domaineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DomaineDTO result = domaineService.save(domaineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, domaineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /domaines} : get all the domaines.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaines in body.
     */
    @GetMapping("/admin/domaines")
    public ResponseEntity<List<DomaineDTO>> getAllDomaines(Pageable pageable) {
        log.debug("REST request to get a page of Domaines");
        Page<DomaineDTO> page = domaineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /domaines/:id} : get the "id" domaine.
     *
     * @param id the id of the domaineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/domaines/{id}")
    public ResponseEntity<DomaineDTO> getDomaine(@PathVariable Long id) {
        log.debug("REST request to get Domaine : {}", id);
        Optional<DomaineDTO> domaineDTO = domaineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domaineDTO);
    }

    /**
     * {@code DELETE  /domaines/:id} : delete the "id" domaine.
     *
     * @param id the id of the domaineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/domaines/{id}")
    public ResponseEntity<Void> deleteDomaine(@PathVariable Long id) {
        log.debug("REST request to delete Domaine : {}", id);
        domaineService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/domaines/update-list")
    public ResponseEntity<Long> updateDomaineList(@RequestBody List<DomaineDTO> domainesDTO){
        log.debug("REST request to update Domaine : {}", domainesDTO);
        Long result = domaineService.deleteAll(domainesDTO);
        return ResponseEntity.ok().body(result);
    }
}
