package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.CouleurService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.CouleurDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.Couleur}.
 */
@RestController
@RequestMapping("/api")
public class CouleurResource {

    private final Logger log = LoggerFactory.getLogger(CouleurResource.class);

    private static final String ENTITY_NAME = "couleur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CouleurService couleurService;

    public CouleurResource(CouleurService couleurService) {
        this.couleurService = couleurService;
    }

    /**
     * {@code POST  /couleurs} : Create a new couleur.
     *
     * @param couleurDTO the couleurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new couleurDTO, or with status {@code 400 (Bad Request)} if the couleur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/couleurs")
    public ResponseEntity<CouleurDTO> createCouleur(@Valid @RequestBody CouleurDTO couleurDTO) throws URISyntaxException {
        log.debug("REST request to save Couleur : {}", couleurDTO);
        if (couleurDTO.getId() != null) {
            throw new BadRequestAlertException("A new couleur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CouleurDTO result = couleurService.save(couleurDTO);
        return ResponseEntity.created(new URI("/api/couleurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /couleurs} : Updates an existing couleur.
     *
     * @param couleurDTO the couleurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated couleurDTO,
     * or with status {@code 400 (Bad Request)} if the couleurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the couleurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/couleurs")
    public ResponseEntity<CouleurDTO> updateCouleur(@Valid @RequestBody CouleurDTO couleurDTO) throws URISyntaxException {
        log.debug("REST request to update Couleur : {}", couleurDTO);
        if (couleurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CouleurDTO result = couleurService.save(couleurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, couleurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /couleurs} : get all the couleurs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of couleurs in body.
     */
    @GetMapping("/admin/couleurs")
    public ResponseEntity<List<CouleurDTO>> getAllCouleurs(Pageable pageable) {
        log.debug("REST request to get a page of Couleurs");
        Page<CouleurDTO> page = couleurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /couleurs/:id} : get the "id" couleur.
     *
     * @param id the id of the couleurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the couleurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/couleurs/{id}")
    public ResponseEntity<CouleurDTO> getCouleur(@PathVariable Long id) {
        log.debug("REST request to get Couleur : {}", id);
        Optional<CouleurDTO> couleurDTO = couleurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(couleurDTO);
    }

    /**
     * {@code DELETE  /couleurs/:id} : delete the "id" couleur.
     *
     * @param id the id of the couleurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/couleurs/{id}")
    public ResponseEntity<Void> deleteCouleur(@PathVariable Long id) {
        log.debug("REST request to delete Couleur : {}", id);
        couleurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/admin/couleurs/couleur")
    public ResponseEntity<List<CouleurDTO>> saveListeColor(List<CouleurDTO> couleurs) {
        log.debug("Rest request to putt a color by indicateur {}");
        return ResponseEntity.ok().body(couleurService.saveListeColor(couleurs));
    }

    @PutMapping("/admin/couleurs/update-list")
    public ResponseEntity<Long> updateCouleurList(@RequestBody List<CouleurDTO> couleurs) {
        log.debug("REST request to update Couleur : {}", couleurs);
        Long result = couleurService.deleteAll(couleurs);
        return ResponseEntity.ok().body(result);
    }
}
