package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.ProvinceService;
import bf.gov.anptic.service.dto.ProvinceDTO;
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
 * REST controller for managing {@link bf.gov.anptic.domain.Province}.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

    private static final String ENTITY_NAME = "province";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinceService provinceService;

    public ProvinceResource(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    /**
     * {@code POST  /provinces} : Create a new province.
     *
     * @param provinceDTO the provinceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provinceDTO, or with status {@code 400 (Bad Request)} if the province has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/provinces")
    public ResponseEntity<ProvinceDTO> createProvince(@Valid @RequestBody ProvinceDTO provinceDTO) throws URISyntaxException {
        log.debug("REST request to save Province : {}", provinceDTO);
        if (provinceDTO.getId() != null) {
            throw new BadRequestAlertException("A new province cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProvinceDTO result = provinceService.save(provinceDTO);
        return ResponseEntity.created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provinces} : Updates an existing province.
     *
     * @param provinceDTO the provinceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinceDTO,
     * or with status {@code 400 (Bad Request)} if the provinceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provinceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/provinces")
    public ResponseEntity<ProvinceDTO> updateProvince(@Valid @RequestBody ProvinceDTO provinceDTO) throws URISyntaxException {
        log.debug("REST request to update Province : {}", provinceDTO);
        if (provinceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProvinceDTO result = provinceService.save(provinceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, provinceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /provinces} : get all the provinces.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provinces in body.
     */
    @GetMapping("/admin/provinces")
    public ResponseEntity<List<ProvinceDTO>> getAllProvinces(Pageable pageable) {
        log.debug("REST request to get a page of Provinces");
        Page<ProvinceDTO> page = provinceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /provinces/:id} : get the "id" province.
     *
     * @param id the id of the provinceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/provinces/{id}")
    public ResponseEntity<ProvinceDTO> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Optional<ProvinceDTO> provinceDTO = provinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provinceDTO);
    }

    /**
     * {@code DELETE  /provinces/:id} : delete the "id" province.
     *
     * @param id the id of the provinceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/provinces/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * @return
     */
    @GetMapping("/admin/provinces/all-by-region")
    public ResponseEntity<List<ProvinceDTO>> findAllProvincesByRegion(@RequestParam Long idRegion) {
        log.debug("Rest request to get all provinces by region {}", idRegion);
        return ResponseEntity.ok().body(provinceService.findAllProvinceByRegion(idRegion));
    }
    @GetMapping("/admin/provinces/all-communes-by-province")
    public ResponseEntity<List<ProvinceDTO>> findAllCommuesByProvince(@RequestParam Long anneeId) {
        log.debug("Rest request to get all provinces by region {}");
        return ResponseEntity.ok().body(provinceService.findAllCommunesbyProvince(anneeId));
    }

    @PutMapping("/admin/provinces/update-list")
    public ResponseEntity<Long> updateProvinceList(@RequestBody List<ProvinceDTO> provincesDTO) {
        log.debug("REST request to update Province : {}", provincesDTO);
        Long result = provinceService.deleteAll(provincesDTO);
        return ResponseEntity.ok().body(result);
    }

}
