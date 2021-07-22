package bf.gov.anptic.web.rest;

import bf.gov.anptic.domain.Profil;
import bf.gov.anptic.service.ProfilService;
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
 * REST controller for managing {@link bf.gov.anptic.domain.Profil}.
 */
@RestController
@RequestMapping("/api")
public class ProfilResource {

    private final Logger log = LoggerFactory.getLogger(ProfilResource.class);

    private static final String ENTITY_NAME = "profil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilService profilService;

    public ProfilResource(ProfilService profilService) {
        this.profilService = profilService;
    }

    /**
     * {@code POST  /profils} : Create a new profil.
     *
     * @param profil the profil to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profilDTO, or with status {@code 400 (Bad Request)} if the profil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profils")
    public ResponseEntity<Profil> createProfil(@Valid @RequestBody Profil profil) throws URISyntaxException {
        log.debug("REST request to save Profil : {}", profil);
        if (profil.getId() != null) {
            throw new BadRequestAlertException("A new profil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Profil result = profilService.save(profil);
        return ResponseEntity.created(new URI("/api/profils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profils} : Updates an existing profil.
     *
     * @param profil the profil to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profil,
     * or with status {@code 400 (Bad Request)} if the profil is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profil couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profils")
    public ResponseEntity<Profil> updateProfil(@Valid @RequestBody Profil profil) throws URISyntaxException {
        log.debug("REST request to update Profil : {}", profil);
        if (profil.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Profil result = profilService.save(profil);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profil.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /profils} : get all the profils.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profils in body.
     */
    @GetMapping("/profils")
    public List<Profil> getAllProfils(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Profils");
        return profilService.findAll();
    }

    /**
     * {@code GET  /profils/:id} : get the "id" profil.
     *
     * @param id the id of the profil to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profil, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profils/{id}")
    public ResponseEntity<Profil> getProfil(@PathVariable Long id) {
        log.debug("REST request to get Profil : {}", id);
        Optional<Profil> profil = profilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profil);
    }

    /**
     * {@code DELETE  /profils/:id} : delete the "id" profil.
     *
     * @param id the id of the profil to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profils/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable Long id) {
        log.debug("REST request to delete Profil : {}", id);
        profilService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
