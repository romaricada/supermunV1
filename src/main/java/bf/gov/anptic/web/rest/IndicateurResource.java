package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.IndicateurService;
import bf.gov.anptic.service.dto.IndicateurDTO;
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
 * REST controller for managing {@link bf.gov.anptic.domain.Indicateur}.
 */
@RestController
@RequestMapping("/api")
public class IndicateurResource {

    private final Logger log = LoggerFactory.getLogger(IndicateurResource.class);

    private static final String ENTITY_NAME = "indicateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndicateurService indicateurService;

    public IndicateurResource(IndicateurService indicateurService) {
        this.indicateurService = indicateurService;
    }

    /**
     * {@code POST  /indicateurs} : Create a new indicateur.
     *
     * @param indicateurDTO the indicateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indicateurDTO, or with status {@code 400 (Bad Request)} if the indicateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/indicateurs")
    public ResponseEntity<IndicateurDTO> createIndicateur(@Valid @RequestBody IndicateurDTO indicateurDTO) throws URISyntaxException {
        log.debug("REST request to save Indicateur : {}", indicateurDTO);
        if (indicateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new indicateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndicateurDTO result = indicateurService.save(indicateurDTO);
        return ResponseEntity.created(new URI("/api/indicateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /indicateurs} : Updates an existing indicateur.
     *
     * @param indicateurDTO the indicateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indicateurDTO,
     * or with status {@code 400 (Bad Request)} if the indicateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indicateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/indicateurs")
    public ResponseEntity<IndicateurDTO> updateIndicateur(@Valid @RequestBody IndicateurDTO indicateurDTO) throws URISyntaxException {
        log.debug("REST request to update Indicateur : {}", indicateurDTO);
        if (indicateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndicateurDTO result = indicateurService.save(indicateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, indicateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /indicateurs} : get all the indicateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indicateurs in body.
     */
    @GetMapping("/admin/indicateurs")
    public ResponseEntity<List<IndicateurDTO>> getAllIndicateurs(Pageable pageable) {
        log.debug("REST request to get a page of Indicateurs");
        Page<IndicateurDTO> page = indicateurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /indicateurs/:id} : get the "id" indicateur.
     *
     * @param id the id of the indicateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indicateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/indicateurs/{id}")
    public ResponseEntity<IndicateurDTO> getIndicateur(@PathVariable Long id) {
        log.debug("REST request to get Indicateur : {}", id);
        Optional<IndicateurDTO> indicateurDTO = indicateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indicateurDTO);
    }

    /**
     * {@code DELETE  /indicateurs/:id} : delete the "id" indicateur.
     *
     * @param id the id of the indicateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/indicateurs/{id}")
    public ResponseEntity<Void> deleteIndicateur(@PathVariable Long id) {
        log.debug("REST request to delete Indicateur : {}", id);
        indicateurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/indicateurs/update-list")
    public ResponseEntity<Long> updateIndicateurList(@RequestBody List<IndicateurDTO> indicateurs) {
        log.debug("REST request to update Indicateur : {}", indicateurs);
        Long result = indicateurService.deleteAll(indicateurs);
        return ResponseEntity.ok().body(result);
    }
}
