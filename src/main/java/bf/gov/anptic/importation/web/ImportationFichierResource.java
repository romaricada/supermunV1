package bf.gov.anptic.importation.web;

import bf.gov.anptic.importation.service.CalculScoreService;
import bf.gov.anptic.importation.service.ImportationFichierService;
import bf.gov.anptic.web.rest.IndicateurResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ImportationFichierResource {
    private final Logger log = LoggerFactory.getLogger(IndicateurResource.class);
    private final ImportationFichierService importationFichierService;
    private final CalculScoreService calculScoreService;

    public ImportationFichierResource(ImportationFichierService importationFichierService, CalculScoreService calculScoreService) {
        this.importationFichierService = importationFichierService;
        this.calculScoreService = calculScoreService;
    }
/*

    @GetMapping("/admin/importations/performances/export-excel")
    public ResponseEntity<byte[]> getExportPerformanceByCommuneAndExercice(@RequestParam Long idCommune, @RequestParam Long idExercice) {
        log.debug("Rest request to exporte performance in excel {}", idCommune, idExercice);
        return ResponseEntity.ok().body(importationFichierService.getExportPerformanceByCommuneAndExerciceToExcel(idCommune, idExercice));
    }
*/


   /* @GetMapping("/admin/importations/export-csv")
    public ResponseEntity<Resource> getExportIndicateurToCsv(@RequestParam(name = "typeId") Long typeIndicateurId, HttpServletResponse response) throws IOException {
        log.debug("REST request to get export indicateur : {}");

        Resource content = importationFichierService.getExportIndicateurToCsv(typeIndicateurId, response);
        return new ResponseEntity<>(content, getHeaders("text/csv"), HttpStatus.OK);
    }

    @GetMapping(value = "/admin/importations/export-excel")
    public ResponseEntity<Resource> getExportIndicateurToExcel(@RequestParam(name = "typeId") Long typeIndicateurId, HttpServletResponse response) {
        log.debug("REST request to get export indicateur : {}");
        Resource content = importationFichierService.getExportIndicateurToExcel(typeIndicateurId, response);
        return new ResponseEntity<>(content, getHeaders("application/vnd.ms.excel"), HttpStatus.OK);
    }

    @GetMapping("/admin/importations/import-excel")
    public ResponseEntity<Boolean> getImportationValeurModaliteExcel(@RequestParam String fieName, @RequestParam Long idExercice) {
        log.debug("Importation des fichiers excel de valeur modalites");
        return ResponseEntity.ok().body(importationFichierService.getImportationExcel(fieName, idExercice));
    }*/

    @GetMapping("/admin/importations/import-csv")
    public ResponseEntity<Void> getImportModaliteCsv() {
        return ResponseEntity.ok().body(null);
    }


    @GetMapping("/admin/performances/calcul")
    public ResponseEntity<Boolean> calculPerformance(@RequestParam(name = "anneeId") Long anneeId) {
        return ResponseEntity.ok(calculScoreService.CalculPerformance(anneeId));
    }

    private HttpHeaders getHeaders(String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        return headers;
    }

}
