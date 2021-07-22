package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.TypePublicationService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.TypePublicationDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.TypePublication}.
 */
@RestController
@RequestMapping("/api")
public class TypePublicationResource {

    private final Logger log = LoggerFactory.getLogger(TypePublicationResource.class);

    private static final String ENTITY_NAME = "typePublication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePublicationService typePublicationService;

    public TypePublicationResource(TypePublicationService typePublicationService) {
        this.typePublicationService = typePublicationService;
    }

    /**
     * {@code POST  /type-publications} : Create a new typePublication.
     *
     * @param typePublicationDTO the typePublicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePublicationDTO, or with status {@code 400 (Bad Request)} if the typePublication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/type-publications")
    public ResponseEntity<TypePublicationDTO> createTypePublication(@Valid @RequestBody TypePublicationDTO typePublicationDTO) throws URISyntaxException {
        log.debug("REST request to save TypePublication : {}", typePublicationDTO);
        if (typePublicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePublication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePublicationDTO result = typePublicationService.save(typePublicationDTO);
        return ResponseEntity.created(new URI("/api/type-publications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-publications} : Updates an existing typePublication.
     *
     * @param typePublicationDTO the typePublicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePublicationDTO,
     * or with status {@code 400 (Bad Request)} if the typePublicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePublicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/type-publications")
    public ResponseEntity<TypePublicationDTO> updateTypePublication(@Valid @RequestBody TypePublicationDTO typePublicationDTO) throws URISyntaxException {
        log.debug("REST request to update TypePublication : {}", typePublicationDTO);
        if (typePublicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypePublicationDTO result = typePublicationService.save(typePublicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typePublicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-publications} : get all the typePublications.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePublications in body.
     */
    @GetMapping("/admin/type-publications")
    public ResponseEntity<List<TypePublicationDTO>> getAllTypePublications(Pageable pageable) {
        log.debug("REST request to get a page of TypePublications");
        Page<TypePublicationDTO> page = typePublicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-publications/:id} : get the "id" typePublication.
     *
     * @param id the id of the typePublicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePublicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/admin/type-publications/{id}")
    public ResponseEntity<TypePublicationDTO> getTypePublication(@PathVariable Long id) {
        log.debug("REST request to get TypePublication : {}", id);
        Optional<TypePublicationDTO> typePublicationDTO = typePublicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePublicationDTO);
    }

    /**
     * {@code DELETE  /type-publications/:id} : delete the "id" typePublication.
     *
     * @param id the id of the typePublicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/type-publications/{id}")
    public ResponseEntity<Void> deleteTypePublication(@PathVariable Long id) {
        log.debug("REST request to delete TypePublication : {}", id);
        typePublicationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
