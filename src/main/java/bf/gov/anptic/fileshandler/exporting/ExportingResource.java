package bf.gov.anptic.fileshandler.exporting;

import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.domain.enumeration.FileType;
import bf.gov.anptic.fileshandler.error.FileProcessingException;
import bf.gov.anptic.repository.EtatCommuneRepository;
import bf.gov.anptic.repository.PerformanceRepository;
import bf.gov.anptic.web.rest.errors.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ExportingResource {

    private final Logger log = LoggerFactory.getLogger(ExportingService.class);

    private final ExportingService exportingService;

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    EtatCommuneRepository etatCommuneRepository;

    public ExportingResource(ExportingService exportingService) {
        this.exportingService = exportingService;
    }

    @GetMapping("/admin/export")
    public ResponseEntity<?> exportFile(@RequestParam(name = "typeId") Long id,
                                        @RequestParam(name = "ext") Extension extension,
                                        @RequestParam(name = "fileType") FileType fileType,
                                        @RequestParam(name = "anneeId", required = false) Long anneeId,
                                        @RequestParam(name = "regionId", required = false) Long regionid,
                                        @RequestParam(name = "provinceId", required = false) Long provinceId,
                                        HttpServletResponse response) {
        if (fileType.equals(FileType.DATA) && !performanceRepository.existsAllByExerciceIdAndTypeDomainId(anneeId, id)) {
            throw new FileProcessingException(ErrorConstants.DATA_NOT_AVAILABLE, "exportdata", Status.NOT_FOUND);
        }

        if (fileType.equals(FileType.MODEL) && !etatCommuneRepository.existsByExerciceId(anneeId)) {
            throw new FileProcessingException(ErrorConstants.NO_SELECTED_COMMUNE, "exportmodel", Status.NOT_FOUND);
        }
        exportingService.start(id, extension, fileType, anneeId, regionid, provinceId, response);
        return ResponseEntity.noContent().build();
    }
}
