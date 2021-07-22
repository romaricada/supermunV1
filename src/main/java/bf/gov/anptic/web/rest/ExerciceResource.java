package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.ExerciceService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.ExerciceDTO;

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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bf.gov.anptic.domain.Exercice}.
 */
@RestController
@RequestMapping("/api")
public class ExerciceResource {

    private final Logger log = LoggerFactory.getLogger(ExerciceResource.class);

    private static final String ENTITY_NAME = "exercice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciceService exerciceService;

    public ExerciceResource(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    /**
     * {@code POST  /exercices} : Create a new exercice.
     *
     * @param exerciceDTO the exerciceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciceDTO, or with status {@code 400 (Bad Request)} if the exercice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/exercices")
    public ResponseEntity<ExerciceDTO> createExercice(@Valid @RequestBody ExerciceDTO exerciceDTO) throws URISyntaxException {
        log.debug("REST request to save Exercice : {}", exerciceDTO);
        if (exerciceDTO.getId() != null) {
            throw new BadRequestAlertException("A new exercice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciceDTO result = exerciceService.save(exerciceDTO);
        return ResponseEntity.created(new URI("/api/exercices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercices} : Updates an existing exercice.
     *
     * @param exerciceDTO the exerciceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciceDTO,
     * or with status {@code 400 (Bad Request)} if the exerciceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/exercices")
    public ResponseEntity<ExerciceDTO> updateExercice(@Valid @RequestBody ExerciceDTO exerciceDTO) throws URISyntaxException {
        log.debug("REST request to update Exercice : {}", exerciceDTO);
        if (exerciceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExerciceDTO result = exerciceService.save(exerciceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, exerciceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exercices} : get all the exercices.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exercices in body.
     */
    @GetMapping("/admin/exercices")
    public ResponseEntity<List<ExerciceDTO>> getAllExercices(Pageable pageable) {
        log.debug("REST request to get a page of Exercices");
        Page<ExerciceDTO> page = exerciceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercices/:id} : get the "id" exercice.
     *
     * @param id the id of the exerciceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/exercices/{id}")
    public ResponseEntity<ExerciceDTO> getExercice(@PathVariable Long id) {
        log.debug("REST request to get Exercice : {}", id);
        Optional<ExerciceDTO> exerciceDTO = exerciceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciceDTO);
    }

    /**
     * {@code DELETE  /exercices/:id} : delete the "id" exercice.
     *
     * @param id the id of the exerciceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/exercices/{id}")
    public ResponseEntity<Void> deleteExercice(@PathVariable Long id) {
        log.debug("REST request to delete Exercice : {}", id);
        exerciceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/exercices/update-list")
    public ResponseEntity<Long> updateExerciceList(@RequestBody List<ExerciceDTO> exercicesDTO) {
        log.debug("REST request to update Exercice : {}", exercicesDTO);
        Long result = exerciceService.deleteAll(exercicesDTO);
        return ResponseEntity.ok().body(result);
    }
}
