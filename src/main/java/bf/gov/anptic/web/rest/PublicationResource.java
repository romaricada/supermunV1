package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.PublicationService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.PublicationDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.Publication}.
 */
@RestController
@RequestMapping("/api")
public class PublicationResource {

    private final Logger log = LoggerFactory.getLogger(PublicationResource.class);

    private static final String ENTITY_NAME = "publication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublicationService publicationService;

    public PublicationResource(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    /**
     * {@code POST  /publications} : Create a new publication.
     *
     * @param publicationDTO the publicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publicationDTO, or with status {@code 400 (Bad Request)} if the publication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/publications")
    public ResponseEntity<PublicationDTO> createPublication(@RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to save Publication : {}", publicationDTO);
        if (publicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new publication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.created(new URI("/api/publications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /publications} : Updates an existing publication.
     *
     * @param publicationDTO the publicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publicationDTO,
     * or with status {@code 400 (Bad Request)} if the publicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/publications")
    public ResponseEntity<PublicationDTO> updatePublication(@RequestBody PublicationDTO publicationDTO) throws URISyntaxException {
        log.debug("REST request to update Publication : {}", publicationDTO);
        if (publicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PublicationDTO result = publicationService.save(publicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /publications} : get all the publications.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publications in body.
     */
    @GetMapping("/admin/publications")
    public ResponseEntity<List<PublicationDTO>> getAllPublications(Pageable pageable) {
        log.debug("REST request to get a page of Publications");
        Page<PublicationDTO> page = publicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /publications/:id} : get the "id" publication.
     *
     * @param id the id of the publicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/publications/{id}")
    public ResponseEntity<PublicationDTO> getPublication(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<PublicationDTO> publicationDTO = publicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicationDTO);
    }

    /**
     * {@code DELETE  /publications/:id} : delete the "id" publication.
     *
     * @param id the id of the publicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/publications/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);
        publicationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
    @PutMapping("/admin/publications/update-list")
    public ResponseEntity<Long> updatePublicationList(@RequestBody List<PublicationDTO> publicationsDTO) {
        log.debug("REST request to update Publication : {}", publicationsDTO);
        Long result = publicationService.deleteAll(publicationsDTO);
        return ResponseEntity.ok().body(result);
    }
}
