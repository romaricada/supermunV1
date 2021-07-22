package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.ModaliteService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.ModaliteDTO;

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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.Modalite}.
 */
@RestController
@RequestMapping("/api")
public class ModaliteResource {

    private final Logger log = LoggerFactory.getLogger(ModaliteResource.class);

    private static final String ENTITY_NAME = "modalite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModaliteService modaliteService;

    public ModaliteResource(ModaliteService modaliteService) {
        this.modaliteService = modaliteService;
    }

    /**
     * {@code POST  /modalites} : Create a new modalite.
     *
     * @param modaliteDTO the modaliteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modaliteDTO, or with status {@code 400 (Bad Request)} if the modalite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/modalites")
    public ResponseEntity<ModaliteDTO> createModalite(@RequestBody ModaliteDTO modaliteDTO) throws URISyntaxException {
        log.debug("REST request to save Modalite : {}", modaliteDTO);
        if (modaliteDTO.getId() != null) {
            throw new BadRequestAlertException("A new modalite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ModaliteDTO result = modaliteService.save(modaliteDTO);
        return ResponseEntity.created(new URI("/api/modalites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /modalites} : Updates an existing modalite.
     *
     * @param modaliteDTO the modaliteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modaliteDTO,
     * or with status {@code 400 (Bad Request)} if the modaliteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modaliteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/modalites")
    public ResponseEntity<ModaliteDTO> updateModalite(@RequestBody ModaliteDTO modaliteDTO) throws URISyntaxException {
        log.debug("REST request to update Modalite : {}", modaliteDTO);
        if (modaliteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ModaliteDTO result = modaliteService.save(modaliteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, modaliteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /modalites} : get all the modalites.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modalites in body.
     */
    @GetMapping("/admin/modalites")
    public ResponseEntity<List<ModaliteDTO>> getAllModalites(Pageable pageable) {
        log.debug("REST request to get a page of Modalites");
        Page<ModaliteDTO> page = modaliteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /modalites/:id} : get the "id" modalite.
     *
     * @param id the id of the modaliteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modaliteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/modalites/{id}")
    public ResponseEntity<ModaliteDTO> getModalite(@PathVariable Long id) {
        log.debug("REST request to get Modalite : {}", id);
        Optional<ModaliteDTO> modaliteDTO = modaliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modaliteDTO);
    }

    /**
     * {@code DELETE  /modalites/:id} : delete the "id" modalite.
     *
     * @param id the id of the modaliteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/modalites/{id}")
    public ResponseEntity<Void> deleteModalite(@PathVariable Long id) {
        log.debug("REST request to delete Modalite : {}", id);
        modaliteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/modalites/update-list")
    public ResponseEntity<Long> updateModaliteList(@RequestBody List<ModaliteDTO> modalitesDTO) {
        log.debug("REST request to update Modalite : {}", modalitesDTO);
        Long result = modaliteService.deleteAll(modalitesDTO);
        return ResponseEntity.ok().body(result);
    }
}
