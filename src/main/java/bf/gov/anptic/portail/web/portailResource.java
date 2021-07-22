package bf.gov.anptic.portail.web;

import bf.gov.anptic.domain.Dictionaires;
import bf.gov.anptic.domain.enumeration.DataFilter;
import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.importation.service.ImportationFichierService;
import bf.gov.anptic.repository.DictionairesRepository;
import bf.gov.anptic.service.*;
import bf.gov.anptic.service.dto.*;
import bf.gov.anptic.web.rest.CommuneResource;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * REST controller for managing {@link bf.gov.anptic.domain.Commune}.
 */
@RestController
@RequestMapping("/api")
public class portailResource {
    private final Logger log = LoggerFactory.getLogger(CommuneResource.class);

    private final CommuneService communeService;
    private final IndicateurService indicateurService;
    private final ExerciceService exerciceService;
    private final InformationService informationService;
    private final TypeIndicateurService typeIndicateurService;
    private final PerformanceService performanceService;
    private final DomaineService domaineService;
    private final PublicationService publicationService;
    private final ProvinceService provinceService;
    private final CouleurService couleurService;
    private final ImportationFichierService importationFichierService;
    private final TypePublicationService typePublicationService;
    private final RegionService regionService;
    private final DictionairesRepository dictionairesRepository;

    public portailResource(
        CommuneService communeService,
        IndicateurService indicateurService,
        ExerciceService exerciceService,
        InformationService informationService,
        TypeIndicateurService typeIndicateurService,
        PerformanceService performanceService,
        DomaineService domaineService,
        PublicationService publicationService,
        ProvinceService provinceService,
        CouleurService couleurService,
        ImportationFichierService importationFichierService,
        TypePublicationService typePublicationService,
        RegionService regionService, DictionairesRepository dictionairesRepository) {
        this.communeService = communeService;
        this.indicateurService = indicateurService;
        this.exerciceService = exerciceService;
        this.informationService = informationService;
        this.typeIndicateurService = typeIndicateurService;
        this.performanceService = performanceService;
        this.domaineService = domaineService;
        this.publicationService = publicationService;
        this.provinceService = provinceService;
        this.couleurService = couleurService;
        this.importationFichierService = importationFichierService;
        this.typePublicationService = typePublicationService;
        this.regionService = regionService;
        this.dictionairesRepository = dictionairesRepository;
    }


    /**
     * @return
     */
    @GetMapping("/portail/provinces")
    public ResponseEntity<List<ProvinceDTO>> findAllProvinces() {
        log.debug("Rest request to get all provinces without page {}");
        return ResponseEntity.ok().body(provinceService.findAllProvinces());
    }

    /**
     * @return
     */
    @GetMapping("/portail/provinces/all-by-region")
    public ResponseEntity<List<ProvinceDTO>> findAllProvincesByRegion(@RequestParam Long idRegion) {
        log.debug("Rest request to get all provinces by region {}", idRegion);
        return ResponseEntity.ok().body(provinceService.findAllProvinceByRegion(idRegion));
    }

    /**
     * @return
     */
    @GetMapping("/portail/communes")
    public ResponseEntity<List<CommunesDTO>> findAllCommune(@RequestParam Long anneeId, @RequestParam String typReturn) {
        log.debug("Rest request to get all communes without page {}");
        return ResponseEntity.ok().body(communeService.findAllCommune(anneeId, typReturn));
    }

    /**
     * @param idProvince
     * @return
     */
    @GetMapping("/portail/communes/all-by-province")
    public ResponseEntity<List<CommunesDTO>> findAllCommuneByProvince(@RequestParam Long idProvince, @RequestParam Long anneeId) {
        log.debug("Rest request to get all communes by province {}", idProvince);
        return ResponseEntity.ok().body(communeService.findAllCommuneByProvince(idProvince, anneeId));
    }

    /**
     * @return
     */
    @GetMapping("/portail/exercices")
    public ResponseEntity<List<ExerciceDTO>> findAllExercice() {
        log.debug("Rest request to get all exercice without page {}");
        return ResponseEntity.ok().body(exerciceService.findAllExercice());
    }

    /**
     * @return
     */
    @GetMapping("/portail/informations")
    public ResponseEntity<List<InformationDTO>> findAllInformation() {
        log.debug("Rest request to get all information without page {}");
        return ResponseEntity.ok().body(informationService.findAllInformation());
    }

    /**
     * @return
     */
    @GetMapping("/portail/domaines")
    public ResponseEntity<List<DomainesDTO>> findAllDomaine() {
        log.debug("Rest request to get all domaine {}");
        return ResponseEntity.ok().body(domaineService.findAllDomaine());
    }

    /**
     * @return
     */
    @GetMapping("/portail/indicateurs")
    public ResponseEntity<List<IndicateursDTO>> findAllIndicateur() {
        log.debug("Rest request to get all indicateurs without page {}");
        return ResponseEntity.ok().body(indicateurService.findAllIndicateur());
    }

    @GetMapping("/portail/type-indicateurs")
    public ResponseEntity<List<TypeIndicateurDTO>> findAllTypeIndicateur() {
        log.debug("Rest request to get all type indicateurs without page {}");
        return ResponseEntity.ok().body(typeIndicateurService.findAlltypeIndicateur());
    }

    /**
     * @param idDomaine
     * @return
     */
    @GetMapping("/portail/indicateurs/all-by-domaine")
    public ResponseEntity<List<IndicateursDTO>> findAllIndicateurByDomaine(@RequestParam Long idDomaine) {
        log.debug("Rest request to get all indicateurs by domaine {}");
        return ResponseEntity.ok().body(indicateurService.findAllIndicateurByDomaine(idDomaine));
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("/portail/publications")
    public ResponseEntity<List<PublicationDTO>> findAllPublication(Pageable pageable) {
        log.debug("Rest request to get all publications {}");
        Page<PublicationDTO> page = publicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * Pour recuperer la performances d'une commune par exercice et par indicateur
     *
     * @param idCommune
     * @param idExercice
     * @param idIndicateur
     * @return
     */
    @GetMapping("/portail/performances/all-by-commune-exercice-indicateur")
    public ResponseEntity<PerformanceDTO> findAllPerformanceByCommuneAndExerciceAndIndicateur(@RequestParam Long idCommune, @RequestParam Long idExercice, @RequestParam Long idIndicateur) {
        log.debug("Rest request to get all performance by commune and exercice and indicateur {}", idCommune, idExercice, idIndicateur);
        return ResponseEntity.ok().body(performanceService.findAllPerformanceBycommuneAndExerciceAndIndicateur(idCommune, idExercice, idIndicateur));
    }

    /**
     * Pour recuperer les performances de toutes les communes  par exercice et par indicateur
     *
     * @param idExercice
     * @param idIndicateur
     * @return
     */
    @GetMapping("/portail/performances/all-by-exercice-indicateur")
    public ResponseEntity<List<PerformanceDTO>> findAllByExerciceAndIndicateur(@RequestParam Long idExercice, @RequestParam Long idIndicateur) {
        log.debug("Rest request to get all performance by commune and exercice {}", idExercice, idIndicateur);
        return ResponseEntity.ok().body(performanceService.findAllByExerciceAndIndicateur(idExercice, idIndicateur));
    }

    /**
     * Pour recuperer les performances de toutes les communes  par exercice
     *
     * @param idExercice
     * @return
     */
    @GetMapping("/portail/performances/all-by-exercice")
    public ResponseEntity<List<PerformanceDTO>> findAllByExercice(@RequestParam Long idExercice) {
        log.debug("Rest request to get all performance by commune and exercice {}", idExercice);
        return ResponseEntity.ok().body(performanceService.findAllByExercice(idExercice));
    }


    @GetMapping("/portail/downloadPub")
    public ResponseEntity<byte[]> downloadPub(
        @RequestParam(name = "id") Long id
    ) {
        byte[] contents = publicationService.downloagPub(id);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @GetMapping("/portail/domaines/score-by-groupe-domaine-indicateur")
    public ResponseEntity<List<TypeIndicateurDTO>> findAllDomaineAndIndicateurToCalculateScore(@RequestParam Long idCommune, @RequestParam Long idExercice) {
        return ResponseEntity.ok().body(typeIndicateurService.findAllTypeIndicateurAndAllDommaineAndAllIndicateur(idCommune, idExercice));
    }

    @GetMapping("/portail/indicateurs/score-indicateur-between-year")
    public ResponseEntity<List<IndicateursDTO>> getScoreBetweenYear(@RequestParam Long idCommune, @RequestParam Long idAnnee1, @RequestParam Long idAnnee2) {
        return ResponseEntity.ok().body(indicateurService.getScoreBetweenYear(idCommune, idAnnee1, idAnnee2));
    }

    @GetMapping("/portail/performances/all-performance-by-commune-and-exercice")
    public ResponseEntity<List<PerformanceDTO>> findAllPerformanceBycommuneAndExercice(@RequestParam Long idCommune, @RequestParam Long idExercice) {
        return ResponseEntity.ok().body(performanceService.findAllPerformanceBycommuneAndExercice(idCommune, idExercice));
    }

    @GetMapping("/portail/couleurs/all-by-indicateur")
    public ResponseEntity<List<CouleurDTO>> findAllCouleurByIndicateur(@RequestParam Long idIndicateur) {
        return ResponseEntity.ok().body(couleurService.findAllCouleurByIndicateur(idIndicateur));
    }

    @GetMapping("/portail/couleurs/find-all")
    public ResponseEntity<List<CouleurDTO>> findAllCouleurByIndicateur() {
        return ResponseEntity.ok().body(couleurService.findAllCouleur());
    }

    @GetMapping("/portail/communes/classement")
    public ResponseEntity<List<CommunesDTO>> classementCommune(@RequestParam Long idExercice, @RequestParam DataFilter dataFilter, @RequestParam Long id) {
        log.debug("Rest request to order communes for many parameter {} ", idExercice, id, dataFilter);
        return ResponseEntity.ok().body(communeService.classementCommune(idExercice, dataFilter, id));
    }

    @GetMapping("/portail/communes/classement-top-5")
    public ResponseEntity<List<CommunesDTO>> classementTop5(@RequestParam Long idExercice, @RequestParam Long id) {
        log.debug("Rest request to order communes for top 5 {} ", idExercice, id);
        return ResponseEntity.ok().body(communeService.classementTop5(idExercice,id));
    }

    @GetMapping("/portail/communes/classement-print")
    public ResponseEntity<byte[]> classementCommuneImpresion(@RequestParam Long idExercice, @RequestParam DataFilter dataFilter, @RequestParam Long id) {
        log.debug("Rest request to order communes for many parameter {} ", idExercice, id, dataFilter);
        return communeService.getToPrintClassement(idExercice, dataFilter, id);
    }

    @GetMapping("/portail/communes/classement-exportation")
    public ResponseEntity<Void> classementCommuneExportation(@RequestParam Long idExercice, @RequestParam DataFilter dataFilter, @RequestParam Long id, HttpServletResponse response, @RequestParam Extension extension) {
        log.debug("Rest request to exportatation {} ", idExercice, id, dataFilter);
         importationFichierService.classementExportation(idExercice, dataFilter, id, extension, response);
         return ResponseEntity.noContent().build();
    }

    @GetMapping("/portail/download-data-between-year")
    public ResponseEntity<Void> getExportPerformanceOfCommuneBewteenYear(
        @RequestParam Long idCommune,
        @RequestParam Long idAnnee1,
        @RequestParam Long idAnnee2,
        HttpServletResponse response,
        @RequestParam Extension extension) {
        importationFichierService.getExportPerformanceOfCommuneBewteenYear(idCommune, idAnnee1, idAnnee2, response, extension);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portail/typePublications/all-type")
    public ResponseEntity<List<TypePublicationDTO>> findAllTypePublication() {
        log.debug("Rest request to order communes for many parameter {} ");
        return ResponseEntity.ok().body(typePublicationService.getAllTypePublication());
    }

    @GetMapping("/portail/regions/all-region")
    public ResponseEntity<List<RegionDTO>> findAllRegion() {
        log.debug("Res request to order communes for many parameter {} ");
        return ResponseEntity.ok().body(regionService.findAllRegion());
    }

    @GetMapping("/portail/indicateurs/all-to-compare")
    public ResponseEntity<List<IndicateursDTO>> getIndicateurToCompare(
        @RequestParam Long idAnnee,
        @RequestParam Long idCommune,
        @RequestParam DataFilter dataFilter,
        @RequestParam Long id) {
        log.debug("Rest request to order communes for many parameter {}",idCommune,idAnnee, dataFilter, id);
        return ResponseEntity.ok().body(indicateurService.getToCompareCommune(idAnnee, idCommune, dataFilter,id));
    }

    @GetMapping("/portail/download-all-data")
    public ResponseEntity<Void> getExportAllData(@RequestParam Extension extension, HttpServletResponse response) {
        importationFichierService.exportationAllData(response,extension);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portail/download-seleted-data")
    public ResponseEntity<Void> getExportSelectedData(@RequestParam Long idIndicateur,@RequestParam Extension extension, HttpServletResponse response) {
        importationFichierService.exportationDataSelected(idIndicateur,response,extension);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portail/download-data-commune-exercice-indicateurs")
    public ResponseEntity<Void> exportationDataCommuneByIndicateur(@RequestParam Long idCom,@RequestParam Long idAnnee, HttpServletResponse response) {
        importationFichierService.exportationDataCommuneByIndicateur(idCom,idAnnee,response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/portail/finAllDictionaire")
    public List<Dictionaires> getAllDictionaires() {
        log.debug("REST request to get all Dictionaires");
        return dictionairesRepository.findAll().stream()
            .sorted(Comparator.comparing(Dictionaires::getEntite)).collect(Collectors.toList());
    }

    @GetMapping("portail/find-indicateur-by-typeIndic")
    public  List<IndicateursDTO> findIndicateurByTypeIndic(@RequestParam Long id) {
        return indicateurService.findIndicateurByTypeIndica(id);
    }
}
