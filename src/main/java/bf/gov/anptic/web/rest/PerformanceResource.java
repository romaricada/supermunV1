package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.PerformanceService;
import bf.gov.anptic.service.dto.CommuneCopyDTO;
import bf.gov.anptic.service.dto.CommuneData;
import bf.gov.anptic.service.dto.PerformanceDTO;
import bf.gov.anptic.service.dto.ValeurModaliteDTO;
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
 * REST controller for managing {@link bf.gov.anptic.domain.Performance}.
 */
@RestController
@RequestMapping("/api")
public class PerformanceResource {

    private final Logger log = LoggerFactory.getLogger(PerformanceResource.class);

    private static final String ENTITY_NAME = "performance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerformanceService performanceService;

    public PerformanceResource(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * {@code POST  /communes} : Create a new performance.
     *
     * @param performanceDTO the performanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new performanceDTO, or with status {@code 400 (Bad Request)} if the performance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/performances")
    public ResponseEntity<PerformanceDTO> createPerformance(@Valid @RequestBody PerformanceDTO performanceDTO) throws URISyntaxException {
        log.debug("REST request to save Performance : {}", performanceDTO);
        if (performanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new performance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerformanceDTO result = performanceService.save(performanceDTO);
        return ResponseEntity.created(new URI("/api/communes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /communes} : Updates an existing performance.
     *
     * @param performanceDTO the performanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated performanceDTO,
     * or with status {@code 400 (Bad Request)} if the performanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the performanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/performances")
    public ResponseEntity<PerformanceDTO> updatePerformance(@Valid @RequestBody PerformanceDTO performanceDTO) throws URISyntaxException {
        log.debug("REST request to update Performance : {}", performanceDTO);
        if (performanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PerformanceDTO result = performanceService.save(performanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, performanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /communes} : get all the communes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communes in body.
     */
    @GetMapping("/admin/performances")
    public ResponseEntity<List<PerformanceDTO>> getAllPerformances(Pageable pageable) {
        log.debug("REST request to get a page of Performances");
        Page<PerformanceDTO> page = performanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /communes/:id} : get the "id" performance.
     *
     * @param id the id of the performanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the performanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/performances/{id}")
    public ResponseEntity<PerformanceDTO> getPerformance(@PathVariable Long id) {
        log.debug("REST request to get Performance : {}", id);
        Optional<PerformanceDTO> performanceDTO = performanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(performanceDTO);
    }

    /**
     * {@code DELETE  /communes/:id} : delete the "id" performance.
     *
     * @param id the id of the performanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/performances/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable Long id) {
        log.debug("REST request to delete Performance : {}", id);
        performanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/performances/update-list")
    public ResponseEntity<Long> updatePerformanceList(@RequestBody List<PerformanceDTO> performancesDTO) {
        log.debug("REST request to update Performance : {}", performancesDTO);
        Long result = performanceService.deleteAll(performancesDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/admin/performances/all-list")
    public ResponseEntity<List<CommuneCopyDTO>> getAllListCommuneWithAllData(@RequestParam Long idAnnee, @RequestParam Long idType, Pageable pageable) {
        log.debug("REST request to get all data of commune : {}", idAnnee, idType);
        Page<CommuneCopyDTO> content = performanceService.getAllCommuneWithAllData(idType, idAnnee, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), content);
        return ResponseEntity.ok().headers(headers).body(content.getContent());
    }

    @PutMapping("admin/performances/update")
    public ResponseEntity<Optional<PerformanceDTO>> updatePerformance(@RequestParam Long id, @RequestParam Double valeur, @RequestParam Boolean score) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, valeur, score));
    }

    @PutMapping("admin/valeur-modalite/performances/update")
    public ResponseEntity<ValeurModaliteDTO> updateValeurModalite(@RequestParam Long id, @RequestParam String valeur) {
        return ResponseUtil.wrapOrNotFound(performanceService.updateModalite(id, valeur));
    }

    @GetMapping("admin/commune-data/validate-all")
    public ResponseEntity<Boolean> validateAllCommuneData(@RequestParam Long anneeId, @RequestParam Long typeId) {
        return ResponseEntity.ok().body(performanceService.validateAllCommuneData(anneeId, typeId));
    }

    @GetMapping("portail/commune-data/check-validate-all")
    public ResponseEntity<Boolean> chekValidateAllCommuneData(@RequestParam Long anneeId, @RequestParam Long typeId) {
        return ResponseEntity.ok().body(performanceService.checkIfExistValidatedData(anneeId, typeId));
    }

    @PutMapping("admin/commune-data/validate")
    public ResponseEntity<Boolean> validateCommuneData(@RequestBody CommuneData data,
                                                       @RequestParam Long anneeId,
                                                       @RequestParam Long typeId) {
        return ResponseEntity.ok().body(performanceService.validateCommuneData(data, anneeId, typeId));
    }

    @GetMapping("admin/vider_performance")
    public ResponseEntity<Void> viderPerformanceByAnneeAndTypIndic(@RequestParam Long anneId, @RequestParam Long typeId) {
        performanceService.viderDataImporterByExerciceAnTypDomaine(anneId, typeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("admin/devalider-donnee")
    public ResponseEntity<Void> devaliderDonnees(@RequestParam Long anneId, @RequestParam Long typeId) {
        performanceService.devaliderDonnees(anneId, typeId);
        return ResponseEntity.noContent().build();
    }

}
