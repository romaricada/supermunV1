package bf.gov.anptic.importation.web;

import bf.gov.anptic.web.rest.IndicateurResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportingResource {
    private final Logger log = LoggerFactory.getLogger(IndicateurResource.class);

}
