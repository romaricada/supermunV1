package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.ValeurModaliteService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.ValeurModaliteDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.ValeurModalite}.
 */
@RestController
@RequestMapping("/api")
public class ValeurModaliteResource {

    private final Logger log = LoggerFactory.getLogger(ValeurModaliteResource.class);

    private static final String ENTITY_NAME = "valeurModalite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValeurModaliteService valeurModaliteService;

    public ValeurModaliteResource(ValeurModaliteService valeurModaliteService) {
        this.valeurModaliteService = valeurModaliteService;
    }

    /**
     * {@code POST  /valeur-modalites} : Create a new valeurModalite.
     *
     * @param valeurModaliteDTO the valeurModaliteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valeurModaliteDTO, or with status {@code 400 (Bad Request)} if the valeurModalite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/valeur-modalites")
    public ResponseEntity<ValeurModaliteDTO> createValeurModalite(@Valid @RequestBody ValeurModaliteDTO valeurModaliteDTO) throws URISyntaxException {
        log.debug("REST request to save ValeurModalite : {}", valeurModaliteDTO);
        if (valeurModaliteDTO.getId() != null) {
            throw new BadRequestAlertException("A new valeurModalite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValeurModaliteDTO result = valeurModaliteService.save(valeurModaliteDTO);
        return ResponseEntity.created(new URI("/api/valeur-modalites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /valeur-modalites} : Updates an existing valeurModalite.
     *
     * @param valeurModaliteDTO the valeurModaliteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valeurModaliteDTO,
     * or with status {@code 400 (Bad Request)} if the valeurModaliteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valeurModaliteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/valeur-modalites")
    public ResponseEntity<ValeurModaliteDTO> updateValeurModalite(@Valid @RequestBody ValeurModaliteDTO valeurModaliteDTO) throws URISyntaxException {
        log.debug("REST request to update ValeurModalite : {}", valeurModaliteDTO);
        if (valeurModaliteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ValeurModaliteDTO result = valeurModaliteService.save(valeurModaliteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, valeurModaliteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /valeur-modalites} : get all the valeurModalites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valeurModalites in body.
     */
    @GetMapping("/admin/valeur-modalites")
    public ResponseEntity<List<ValeurModaliteDTO>> getAllValeurModalites(Pageable pageable) {
        log.debug("REST request to get a page of ValeurModalites");
        Page<ValeurModaliteDTO> page = valeurModaliteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /valeur-modalites/:id} : get the "id" valeurModalite.
     *
     * @param id the id of the valeurModaliteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valeurModaliteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/valeur-modalites/{id}")
    public ResponseEntity<ValeurModaliteDTO> getValeurModalite(@PathVariable Long id) {
        log.debug("REST request to get ValeurModalite : {}", id);
        Optional<ValeurModaliteDTO> valeurModaliteDTO = valeurModaliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valeurModaliteDTO);
    }

    /**
     * {@code DELETE  /valeur-modalites/:id} : delete the "id" valeurModalite.
     *
     * @param id the id of the valeurModaliteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/valeur-modalites/{id}")
    public ResponseEntity<Void> deleteValeurModalite(@PathVariable Long id) {
        log.debug("REST request to delete ValeurModalite : {}", id);
        valeurModaliteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/valeur-modalites/update-list")
    public ResponseEntity<Long> updateValeurModaliteList(@RequestBody List<ValeurModaliteDTO> valeurModalitesDTO) {
        log.debug("REST request to update ValeurModalite : {}", valeurModalitesDTO);
        Long result = valeurModaliteService.deleteAll(valeurModalitesDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/portail/valeur-modalites/all-by-exercice-commune")
    public ResponseEntity<List<ValeurModaliteDTO>> getValeurModalitebyCommunebyExercice(@RequestParam Long idExercice, @RequestParam Long idCommune) {
        log.debug("REST request to get ValeurModalite : {}");
        List<ValeurModaliteDTO> valeurModalites = valeurModaliteService.findModalitebyCommune(idExercice, idCommune);
        return ResponseEntity.ok().body(valeurModalites);
    }

    @GetMapping("/admin/valeur-modalites/all-by-exercice-commune")
    public ResponseEntity<List<ValeurModaliteDTO>> findAllValeurModalitebyCommunebyExercice(@RequestParam Long idExercice, @RequestParam Long idCommune, Pageable pageable) {
        log.debug("REST request to get ValeurModalite withi page : {}");
        Page<ValeurModaliteDTO> page = valeurModaliteService.findAllModalitebyCommune(idExercice, idCommune, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());

    }
}
