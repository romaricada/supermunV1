package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.TypeIndicateurService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.TypeIndicateurDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.TypeIndicateur}.
 */
@RestController
@RequestMapping("/api")
public class TypeIndicateurResource {

    private final Logger log = LoggerFactory.getLogger(TypeIndicateurResource.class);

    private static final String ENTITY_NAME = "typeIndicateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeIndicateurService typeIndicateurService;

    public TypeIndicateurResource(TypeIndicateurService typeIndicateurService) {
        this.typeIndicateurService = typeIndicateurService;
    }

    /**
     * {@code POST  /type-indicateurs} : Create a new typeIndicateur.
     *
     * @param typeIndicateurDTO the typeIndicateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeIndicateurDTO, or with status {@code 400 (Bad Request)} if the typeIndicateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/type-indicateurs")
    public ResponseEntity<TypeIndicateurDTO> createTypeIndicateur(@Valid @RequestBody TypeIndicateurDTO typeIndicateurDTO) throws URISyntaxException {
        log.debug("REST request to save TypeIndicateur : {}", typeIndicateurDTO);
        if (typeIndicateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeIndicateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeIndicateurDTO result = typeIndicateurService.save(typeIndicateurDTO);
        return ResponseEntity.created(new URI("/api/type-indicateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-indicateurs} : Updates an existing typeIndicateur.
     *
     * @param typeIndicateurDTO the typeIndicateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeIndicateurDTO,
     * or with status {@code 400 (Bad Request)} if the typeIndicateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeIndicateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/type-indicateurs")
    public ResponseEntity<TypeIndicateurDTO> updateTypeIndicateur(@Valid @RequestBody TypeIndicateurDTO typeIndicateurDTO) throws URISyntaxException {
        log.debug("REST request to update TypeIndicateur : {}", typeIndicateurDTO);
        if (typeIndicateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeIndicateurDTO result = typeIndicateurService.save(typeIndicateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeIndicateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-indicateurs} : get all the typeIndicateurs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeIndicateurs in body.
     */
    @GetMapping("/admin/type-indicateurs")
    public ResponseEntity<List<TypeIndicateurDTO>> getAllTypeIndicateurs(Pageable pageable) {
        log.debug("REST request to get a page of TypeIndicateurs");
        Page<TypeIndicateurDTO> page = typeIndicateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @GetMapping("/admin/type-indicateurs/type")
    public ResponseEntity<List<TypeIndicateurDTO>> getAllTypeIndicateursNoPage() {
        log.debug("REST request to get a TypeIndicateurs");
              return ResponseEntity.ok().body(typeIndicateurService.findAlltypeIndicateur());
    }

    /**
     * {@code GET  /type-indicateurs/:id} : get the "id" typeIndicateur.
     *
     * @param id the id of the typeIndicateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeIndicateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/type-indicateurs/{id}")
    public ResponseEntity<TypeIndicateurDTO> getTypeIndicateur(@PathVariable Long id) {
        log.debug("REST request to get TypeIndicateur : {}", id);
        Optional<TypeIndicateurDTO> typeIndicateurDTO = typeIndicateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeIndicateurDTO);
    }

    /**
     * {@code DELETE  /type-indicateurs/:id} : delete the "id" typeIndicateur.
     *
     * @param id the id of the typeIndicateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/type-indicateurs/{id}")
    public ResponseEntity<Void> deleteTypeIndicateur(@PathVariable Long id) {
        log.debug("REST request to delete TypeIndicateur : {}", id);
        typeIndicateurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/type-indicateurs/update-list")
    public ResponseEntity<Long> updateTypeIndicateurList(@RequestBody List<TypeIndicateurDTO> typeIndicateursDTO) {
        log.debug("REST request to update TypeIndicateur : {}", typeIndicateursDTO);
        Long result = typeIndicateurService.deleteAll(typeIndicateursDTO);
        return ResponseEntity.ok().body(result);
    }
}
