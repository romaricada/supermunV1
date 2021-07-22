package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.EtatCommuneService;
import bf.gov.anptic.service.dto.EtatCommuneDTO;
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
public class EtatCommuneResource {

    private final Logger log = LoggerFactory.getLogger(EtatCommuneResource.class);

    private static final String ENTITY_NAME = "etatCommune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EtatCommuneService etatCommuneService;

    public EtatCommuneResource(EtatCommuneService etatCommuneService) {
        this.etatCommuneService = etatCommuneService;
    }

    /**
     * {@code POST  /etat-communes} : Create a new etatCommune.
     *
     * @param etatCommuneDTO the etatCommuneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new etatCommuneDTO, or with status {@code 400 (Bad Request)} if the etatCommune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/etat-communes")
    public ResponseEntity<EtatCommuneDTO> createEtatCommune(@Valid @RequestBody EtatCommuneDTO etatCommuneDTO) throws URISyntaxException {
        log.debug("REST request to save EtatCommune : {}", etatCommuneDTO);
        if (etatCommuneDTO.getId() != null) {
            throw new BadRequestAlertException("A new etatCommune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EtatCommuneDTO result = etatCommuneService.save(etatCommuneDTO);
        return ResponseEntity.created(new URI("/api/etat-communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /etat-communes} : Updates an existing etatCommune.
     *
     * @param etatCommuneDTO the etatCommuneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated etatCommuneDTO,
     * or with status {@code 400 (Bad Request)} if the etatCommuneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the etatCommuneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/etat-communes")
    public ResponseEntity<EtatCommuneDTO> updateEtatCommune(@Valid @RequestBody EtatCommuneDTO etatCommuneDTO) throws URISyntaxException {
        log.debug("REST request to update EtatCommune : {}", etatCommuneDTO);
        if (etatCommuneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EtatCommuneDTO result = etatCommuneService.save(etatCommuneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, etatCommuneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /etat-communes} : get all the etatCommunes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of etatCommunes in body.
     */
    @GetMapping("/etat-communes")
    public ResponseEntity<List<EtatCommuneDTO>> getAllEtatCommunes(@RequestParam Long anneeId, Pageable pageable) {
        log.debug("REST request to get a page of EtatCommunes");
        Page<EtatCommuneDTO> page = etatCommuneService.getCommuneByExercice(anneeId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/etat-communes/by-province")
    public ResponseEntity<List<EtatCommuneDTO>> getAllEtatCommunesByProvince (@RequestParam Long anneeId,@RequestParam Long id, @RequestParam String criteria, Pageable pageable) {
        log.debug("REST request to get a page of EtatCommunes");
        Page<EtatCommuneDTO> page = etatCommuneService.getCommuneByRegionORProvince(anneeId, id, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /etat-communes/:id} : get the "id" etatCommune.
     *
     * @param id the id of the etatCommuneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the etatCommuneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/etat-communes/{id}")
    public ResponseEntity<EtatCommuneDTO> getEtatCommune(@PathVariable Long id) {
        log.debug("REST request to get EtatCommune : {}", id);
        Optional<EtatCommuneDTO> etatCommuneDTO = etatCommuneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(etatCommuneDTO);
    }

    /**
     * {@code DELETE  /etat-communes/:id} : delete the "id" etatCommune.
     *
     * @param id the id of the etatCommuneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/etat-communes/{id}")
    public ResponseEntity<Void> deleteEtatCommune(@PathVariable Long id) {
        log.debug("REST request to delete EtatCommune : {}", id);
        etatCommuneService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @DeleteMapping("/etat-communes/remove-commune-from-year")
    public ResponseEntity<Void> removeAllFromYear(@RequestParam Long id) {
        log.debug("REST request to remove EtatCommunes from : {}", id);
        etatCommuneService.removeAllCommuneFromYear(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }



}
