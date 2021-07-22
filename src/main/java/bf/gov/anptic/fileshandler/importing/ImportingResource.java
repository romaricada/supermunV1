package bf.gov.anptic.fileshandler.importing;

import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.fileshandler.error.FileProcessingException;
import bf.gov.anptic.repository.EtatCommuneRepository;
import bf.gov.anptic.web.rest.errors.ErrorConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Status;

@RestController
@RequestMapping("/api")
public class ImportingResource {

    private final ImportingService importingService;

    @Autowired
    EtatCommuneRepository etatCommuneRepository;

    public ImportingResource(ImportingService importingService) {
        this.importingService = importingService;
    }

    /**
     * @param typeId
     * @param anneeId
     * @param file
     * @return
     */
    @PostMapping("/admin/import")
    public ResponseEntity<Void> importFile(@RequestParam(name = "typeId") Long typeId,
                                           @RequestParam(name = "anneeId") Long anneeId,
                                           @RequestPart(name = "file") MultipartFile file,
                                           @RequestParam(name = "ext") Extension extension,
                                           @RequestParam(name = "update") boolean update
                                           ) {
        if (importingService.checkIfDataAlreadyImport(anneeId, typeId) && !update) {
            throw new FileProcessingException(ErrorConstants.DATA_ALREADY_IMPORT, "import", Status.FORBIDDEN);
        }
        if (!etatCommuneRepository.existsByExerciceId(anneeId)) {
            throw new FileProcessingException(ErrorConstants.NO_SELECTED_COMMUNE, "importdata", Status.NOT_FOUND);
        }
        importingService.saveFiles(file, anneeId, typeId, extension, update);
        return ResponseEntity.noContent().build();
    }
}
