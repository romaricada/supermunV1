package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.CommuneService;
import bf.gov.anptic.service.dto.CommuneDTO;
import bf.gov.anptic.service.dto.CommunesDTO;
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
 * REST controller for managing {@link bf.gov.anptic.domain.Commune}.
 */
@RestController
@RequestMapping("/api")
public class CommuneResource {

    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    private static final String ENTITY_NAME = "commune";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommuneService communeService;

    public CommuneResource(CommuneService communeService) {
        this.communeService = communeService;
    }

    /**
     * {@code POST  /communes} : Create a new commune.
     *
     * @param communeDTO the communeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communeDTO, or with status {@code 400 (Bad Request)} if the commune has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/communes")
    public ResponseEntity<CommunesDTO> createCommune(@Valid @RequestBody CommunesDTO communeDTO) throws URISyntaxException {
        log.debug("REST request to save Commune : {}", communeDTO);
        if (communeDTO.getId() != null) {
            throw new BadRequestAlertException("A new commune cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommunesDTO result = communeService.save(communeDTO);
        return ResponseEntity.created(new URI("/api/communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communes} : Updates an existing commune.
     *
     * @param communeDTO the communeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communeDTO,
     * or with status {@code 400 (Bad Request)} if the communeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/communes")
    public ResponseEntity<CommunesDTO> updateCommune(@Valid @RequestBody CommunesDTO communeDTO) throws URISyntaxException {
        log.debug("REST request to update Commune : {}", communeDTO);
        if (communeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommunesDTO result = communeService.save(communeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, communeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /communes} : get all the communes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communes in body.
     */
    @GetMapping("/admin/communes")
    public ResponseEntity<List<CommunesDTO>> getAllCommunes(Pageable pageable) {
        log.debug("REST request to get a page of Communes");
        Page<CommunesDTO> page = communeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     *
     * @param anneeId
     * @param pageable
     * @return
     */
    @GetMapping("/admin/_not_prise_en_compte/communes")
    public ResponseEntity<List<CommunesDTO>> getAllNonPriseEnCompte(@RequestParam Long anneeId, Pageable pageable) {
        Page<CommunesDTO> page = communeService.getAllNotUsedForCurrentYear(anneeId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     *
     * @param anneeId
     * @param communeDTOS
     * @return
     */
    @PutMapping("/admin/_update_current_year/communes")
    public ResponseEntity<Boolean> updateCommuneForCurrentYear(@RequestParam Long anneeId, @RequestBody List<CommunesDTO> communeDTOS, @RequestParam boolean checked) {
        return ResponseEntity.ok(communeService.updateCommuneForCurrentYear(communeDTOS, anneeId, checked));
    }

    /**
     * {@code GET  /communes/:id} : get the "id" commune.
     *
     * @param id the id of the communeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/communes/{id}")
    public ResponseEntity<CommunesDTO> getCommune(@PathVariable Long id) {
        log.debug("REST request to get Commune : {}", id);
        Optional<CommunesDTO> communeDTO = communeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(communeDTO);
    }

    /**
     * {@code DELETE  /communes/:id} : delete the "id" commune.
     *
     * @param id the id of the communeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/communes/{id}")
    public ResponseEntity<Void> deleteCommune(@PathVariable Long id) {
        log.debug("REST request to delete Commune : {}", id);
        communeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET /portail/goejson} : get commune data from geometry
     *
     * @return
     */
    @GetMapping("/portail/geojson")
    public ResponseEntity<Object> getGeoJsonData() {
        return ResponseEntity.ok(communeService.getGeoJsonData());
    }

    /**
     * @param idProvince
     * @return
     */
    @GetMapping("/admin/communes/all-by-province")
    public ResponseEntity<List<CommunesDTO>> findAllCommuneByProvince(@RequestParam Long idProvince, @RequestParam Long anneeId) {
        log.debug("Rest request to get all communes by province {}", idProvince);
        return ResponseEntity.ok().body(communeService.findAllCommuneByProvince(idProvince, anneeId));
    }



    @PutMapping("/admin/communes/update-list")
    public ResponseEntity<Long> updateCommuneList( @RequestBody List<CommuneDTO> communesDTO) {
        log.debug("REST request to update Commune : {}", communesDTO);
        Long result = communeService.deleteAll(communesDTO);
        return ResponseEntity.ok().body(result);
    }

}
