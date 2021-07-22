package bf.gov.anptic.web.rest;

import bf.gov.anptic.service.InformationService;
import bf.gov.anptic.web.rest.errors.BadRequestAlertException;
import bf.gov.anptic.service.dto.InformationDTO;

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
 * REST controller for managing {@link bf.gov.anptic.domain.Information}.
 */
@RestController
@RequestMapping("/api")
public class InformationResource {

    private final Logger log = LoggerFactory.getLogger(InformationResource.class);

    private static final String ENTITY_NAME = "information";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformationService informationService;

    public InformationResource(InformationService informationService) {
        this.informationService = informationService;
    }

    /**
     * {@code POST  /information} : Create a new information.
     *
     * @param informationDTO the informationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informationDTO, or with status {@code 400 (Bad Request)} if the information has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin/information")
    public ResponseEntity<InformationDTO> createInformation(@RequestBody InformationDTO informationDTO) throws URISyntaxException {
        log.debug("REST request to save Information : {}", informationDTO);
        if (informationDTO.getId() != null) {
            throw new BadRequestAlertException("A new information cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InformationDTO result = informationService.save(informationDTO);
        return ResponseEntity.created(new URI("/api/information/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /information} : Updates an existing information.
     *
     * @param informationDTO the informationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informationDTO,
     * or with status {@code 400 (Bad Request)} if the informationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin/information")
    public ResponseEntity<InformationDTO> updateInformation(@RequestBody InformationDTO informationDTO) throws URISyntaxException {
        log.debug("REST request to update Information : {}", informationDTO);
        if (informationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InformationDTO result = informationService.save(informationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, informationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /information} : get all the information.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of information in body.
     */
    @GetMapping("/admin/information")
    public ResponseEntity<List<InformationDTO>> getAllInformation(Pageable pageable) {
        log.debug("REST request to get a page of Information");
        Page<InformationDTO> page = informationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /information/:id} : get the "id" information.
     *
     * @param id the id of the informationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/portail/information/{id}")
    public ResponseEntity<InformationDTO> getInformation(@PathVariable Long id) {
        log.debug("REST request to get Information : {}", id);
        Optional<InformationDTO> informationDTO = informationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informationDTO);
    }

    /**
     * {@code DELETE  /information/:id} : delete the "id" information.
     *
     * @param id the id of the informationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin/information/{id}")
    public ResponseEntity<Void> deleteInformation(@PathVariable Long id) {
        log.debug("REST request to delete Information : {}", id);
        informationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PutMapping("/admin/information/update-list")
    public ResponseEntity<Long> updateInformationList(List<InformationDTO> informationsDTO) {
        log.debug("REST request to update Information : {}", informationsDTO);
        Long result = informationService.deleteAll(informationsDTO);
        return ResponseEntity.ok().body(result);
    }
}
