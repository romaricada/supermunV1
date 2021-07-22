package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.PosterService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.PosterDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.Poster}.
 */
@RestController
@RequestMapping("/api")
public class PosterResource {

    private final Logger log = LoggerFactory.getLogger(PosterResource.class);

    private static final String ENTITY_NAME = "poster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PosterService posterService;

    public PosterResource(PosterService posterService) {
        this.posterService = posterService;
    }

    /**
     * {@code POST  /posters} : Create a new poster.
     *
     * @param posterDTO the posterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posterDTO, or with status {@code 400 (Bad Request)} if the poster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/posters")
    public ResponseEntity<PosterDTO> createPoster(@RequestBody PosterDTO posterDTO) throws URISyntaxException {
        log.debug("REST request to save Poster : {}", posterDTO);
        if (posterDTO.getId() != null) {
            throw new BadRequestAlertException("A new poster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PosterDTO result = posterService.save(posterDTO);
        return ResponseEntity.created(new URI("/api/posters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /posters} : Updates an existing poster.
     *
     * @param posterDTO the posterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posterDTO,
     * or with status {@code 400 (Bad Request)} if the posterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/posters")
    public ResponseEntity<PosterDTO> updatePoster(@RequestBody PosterDTO posterDTO) throws URISyntaxException {
        log.debug("REST request to update Poster : {}", posterDTO);
        if (posterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PosterDTO result = posterService.save(posterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, posterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /posters} : get all the posters.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posters in body.
     */
    @GetMapping("/admin/posters")
    public ResponseEntity<List<PosterDTO>> getAllPosters(Pageable pageable) {
        log.debug("REST request to get a page of Posters");
        Page<PosterDTO> page = posterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posters/:id} : get the "id" poster.
     *
     * @param id the id of the posterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/posters/{id}")
    public ResponseEntity<PosterDTO> getPoster(@PathVariable Long id) {
        log.debug("REST request to get Poster : {}", id);
        Optional<PosterDTO> posterDTO = posterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(posterDTO);
    }

    /**
     * {@code DELETE  /posters/:id} : delete the "id" poster.
     *
     * @param id the id of the posterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/posters/{id}")
    public ResponseEntity<Void> deletePoster(@PathVariable Long id) {
        log.debug("REST request to delete Poster : {}", id);
        posterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/posters/update-list")
    public ResponseEntity<Long> updatePosterList(@RequestBody List<PosterDTO> postersDTO) {
        log.debug("REST request to update Poster : {}", postersDTO);
        Long result = posterService.deleteAll(postersDTO);
        return ResponseEntity.ok().body(result);
    }
}
