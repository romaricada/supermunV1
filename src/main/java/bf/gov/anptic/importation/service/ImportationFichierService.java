package bf.gov.anptic.importation.service;

import bf.gov.anptic.domain.Commune;
import bf.gov.anptic.domain.Exercice;
import bf.gov.anptic.domain.Indicateur;
import bf.gov.anptic.domain.enumeration.DataFilter;
import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.fileshandler.HandlerConstant;
import bf.gov.anptic.repository.CommuneRepository;
import bf.gov.anptic.repository.ExerciceRepository;
import bf.gov.anptic.repository.IndicateurRepository;
import bf.gov.anptic.repository.TypeIndicateurRepository;
import bf.gov.anptic.service.CommuneService;
import bf.gov.anptic.service.IndicateurService;
import bf.gov.anptic.service.PerformanceService;
import bf.gov.anptic.service.dto.CommunesDTO;
import bf.gov.anptic.service.dto.IndicateursDTO;
import bf.gov.anptic.service.mapper.CommuneMapper;
import bf.gov.anptic.service.mapper.CommunesMapper;
import bf.gov.anptic.util.Score;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImportationFichierService {

    private final Logger log = LoggerFactory.getLogger(ImportationFichierService.class);

    private final IndicateurRepository indicateurRepository;
    private final IndicateurService indicateurService;
    private final PerformanceService performanceService;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    TypeIndicateurRepository typeIndicateurRepository;
    @Autowired
    ExerciceRepository exerciceRepository;
    @Autowired
    CommuneService communeService;
    @Autowired
    CommuneMapper communeMapper;
    @Autowired
    CommunesMapper communesMapper;


    public ImportationFichierService(
        IndicateurRepository indicateurRepository,
        IndicateurService indicateurService,
        PerformanceService performanceService) {
        this.indicateurRepository = indicateurRepository;
        this.indicateurService = indicateurService;
        this.performanceService = performanceService;
    }

    /**
     * pour exporter les donnees d'une commune selon l'intervale d'annee
     *
     * @param communeId
     * @param anneeId1
     * @param anneeId2
     * @param response
     */
    public void getExportPerformanceOfCommuneBewteenYear(Long communeId, Long anneeId1, Long anneeId2, HttpServletResponse response, Extension extension) {
        log.debug("===================={}", communeId, anneeId1, anneeId2, response);
        CommunesDTO commune = communesMapper.toDto(communeRepository.getOne(communeId));
        List<IndicateursDTO> indicateurs = indicateurService.getScoreBetweenYear(communeId, anneeId1, anneeId2);
        List<Score> scores = indicateurs.get(0).getScores();
        switch (extension) {
            case XLSX:
                String filname = "evolutionScore_" + commune.getLibelle().replaceAll(" ", "_");

                XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                XSSFSheet xssfSheet = xssfWorkbook.createSheet(filname);
                int rowNum = 0;
                Row row = xssfSheet.createRow(rowNum);

                row.createCell(0).setCellValue("Indicateur");
                for (Score score : scores) {
                    row.createCell(row.getLastCellNum()).setCellValue(score.getAnnee());
                }
                Row nameCommune = xssfSheet.createRow(1);
                nameCommune.createCell(0).setCellValue("COMMUNE DE " + commune.getLibelle());
                rowNum = rowNum + 1;
                for (IndicateursDTO indicateur : indicateurs) {
                    int i = 0;
                    rowNum++;
                    Row rowCreate = xssfSheet.createRow(rowNum);
                    rowCreate.createCell(i).setCellValue(indicateur.getLibelle());
                    for (Score score : indicateur.getScores()) {
                        i++;
                        if (score.getScore() != null) {
                            rowCreate.createCell(i).setCellValue(score.getScore());
                        } else {
                            rowCreate.createCell(i).setCellValue("-");
                        }
                    }
                }
                this.convertWorkbootToByteArray(xssfWorkbook, filname, response);
                break;
            case CSV:
                this.writeCsvForManyYear(commune, scores, indicateurs, response);
                break;
        }


    }

    /**
     * Exportation des donnees de toutes les années
     *
     * @param response
     */
    public void exportationAllData(HttpServletResponse response, Extension extension) {
        List<Exercice> exercices = exerciceRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted())
            .sorted(Comparator.comparing(Exercice::getAnnee))
            .collect(Collectors.toList());
        List<CommunesDTO> communes = performanceService.allDataCommuneForAllYear(exercices);

        switch (extension) {
            case XLSX:
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                String filname = "evolution_performance_commune";
                XSSFSheet xssfSheet = xssfWorkbook.createSheet(filname);
                Row rowHeader = xssfSheet.createRow(0);
                // creation du header
                createHeader(rowHeader, exercices);
                // creation du corps
                if (!communes.isEmpty()) {
                    int rowNum = 0;
                    for (CommunesDTO commune : communes) {
                        rowNum++;
                        Row rowBody = xssfSheet.createRow(rowNum);
                        rowBody.createCell(0).setCellValue(commune.getLibelleRegion());
                        rowBody.createCell(1).setCellValue(commune.getLibelleProvince());
                        rowBody.createCell(2).setCellValue(commune.getLibelle());
                        int i = 2;
                        for (Score score : commune.getScores()) {
                            i++;
                            if (score.getScore() != null) {
                                rowBody.createCell(i).setCellValue(score.getScore());
                            } else {
                                rowBody.createCell(i).setCellValue("-");
                            }
                        }
                    }
                }
                this.convertWorkbootToByteArray(xssfWorkbook, filname, response);
                break;
            case CSV:
                this.allDatatCsv(communes, exercices, response);
                break;

        }

    }

    /**
     * Exportation des donnees d'un indicateur
     *
     * @param idIndic
     * @param response
     */
    public void exportationDataSelected(Long idIndic, HttpServletResponse response, Extension extension) {
        List<Exercice> exercices = exerciceRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted())
            .sorted(Comparator.comparing(Exercice::getAnnee))
            .collect(Collectors.toList());
        Indicateur indicateur = indicateurRepository.getOne(idIndic);
        List<CommunesDTO> communes = performanceService.allDataCommuneForAllYearOfIndicateur(idIndic, exercices);

        switch (extension) {
            case XLSX:
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                String filname = "evolution_" + indicateur.getLibelle().replaceAll(" ", "_");
                XSSFSheet xssfSheet = xssfWorkbook.createSheet(filname);
                Row rowHeader = xssfSheet.createRow(0);
                createHeader(rowHeader, exercices);
                //pour le nom
                Row rowTitile = xssfSheet.createRow(1);
                rowTitile.createCell(0).setCellValue(indicateur.getLibelle());

                if (!communes.isEmpty()) {
                    int rowNum = 1;
                    for (CommunesDTO commune : communes) {
                        rowNum++;
                        Row rowTocreat = xssfSheet.createRow(rowNum);
                        rowTocreat.createCell(0).setCellValue(commune.getLibelleRegion());
                        rowTocreat.createCell(1).setCellValue(commune.getLibelleProvince());
                        rowTocreat.createCell(2).setCellValue(commune.getLibelle());
                        int i = 2;
                        for (Score score : commune.getScores()) {
                            i++;
                            if (score.getScore() != null) {
                                rowTocreat.createCell(i).setCellValue(score.getScore());
                            } else {
                                rowTocreat.createCell(i).setCellValue("-");
                            }
                        }
                    }
                }
                this.convertWorkbootToByteArray(xssfWorkbook, filname, response);
                break;
            case CSV:
                this.allDatatSelectedCsv(communes, exercices, indicateur, response);
                break;

        }

    }

    public void exportationDataCommuneByIndicateur(Long idComm, Long idAnnee, HttpServletResponse response) {
        Exercice exercice = exerciceRepository.getOne(idAnnee);
        Commune commune = communeRepository.getOne(idComm);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        String filname = "performance_" + commune.getLibelle();
        XSSFSheet xssfSheet = xssfWorkbook.createSheet(filname);
        //pour le nom
        Row rowTitile = xssfSheet.createRow(0);
        rowTitile.createCell(0).setCellValue("COMMUNE DE " + commune.getLibelle().toUpperCase());

        List<IndicateursDTO> indicateurs = indicateurService.calculeScoreByOneExercice(idComm, exercice, indicateurService.findAllIndicateur());
        if (!indicateurs.isEmpty()) {
            int rowNum = 0;
            for (IndicateursDTO indicateur : indicateurs) {
                rowNum++;
                Row row = xssfSheet.createRow(rowNum);
                row.createCell(0).setCellValue(indicateur.getLibelle());
                row.createCell(1).setCellValue(indicateur.getSroreIndicateur());
            }
        }
        this.convertWorkbootToByteArray(xssfWorkbook, filname, response);
    }

    public void classementExportation(Long idAnnee, DataFilter dataFilter, Long id, Extension extension, HttpServletResponse response) {
        List<CommunesDTO> communes = communeService.classementCommune(idAnnee, dataFilter, id);
        String filname = "classement_commune";
        switch (extension) {
            case XLSX:
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                XSSFSheet xssfSheet = xssfWorkbook.createSheet(filname);
                createHeaderClassement(xssfSheet);
                creatBodyWorkBody(xssfWorkbook, xssfSheet, communes, response, filname);
                break;
            case CSV:
                this.createCsvWorbookClassement(communes, filname, response);
                break;
        }

    }

    public void creatBodyWorkBody(XSSFWorkbook xssfWorkbook, XSSFSheet xssfSheet, List<CommunesDTO> communes, HttpServletResponse response, String filname) {
        if (!communes.isEmpty()) {
            int rowNum = 0;
            for (CommunesDTO commune : communes) {
                rowNum++;
                Row rowTocreat = xssfSheet.createRow(rowNum);
                rowTocreat.createCell(0).setCellValue(commune.getRangNational());
                rowTocreat.createCell(1).setCellValue(commune.getLibelleRegion());
                rowTocreat.createCell(2).setCellValue(commune.getLibelleProvince());
                rowTocreat.createCell(3).setCellValue(commune.getLibelle());
                rowTocreat.createCell(4).setCellValue(commune.getScoreCommune());
            }
        }
        this.convertWorkbootToByteArray(xssfWorkbook, filname, response);
    }

    /**
     * @param row
     * @param exercices
     */
    private void createHeader(Row row, List<Exercice> exercices) {
        row.createCell(0).setCellValue("Region");
        row.createCell(1).setCellValue("Province");
        row.createCell(2).setCellValue("Commune");
        for (Exercice exercice : exercices) {
            row.createCell(row.getLastCellNum()).setCellValue(exercice.getAnnee());
        }
    }

    private void createHeaderClassement(XSSFSheet xssfSheet) {
        Row rowHeader = xssfSheet.createRow(0);
        rowHeader.createCell(0).setCellValue("Rang");
        rowHeader.createCell(1).setCellValue("Région");
        rowHeader.createCell(2).setCellValue("Province");
        rowHeader.createCell(3).setCellValue("Commune");
        rowHeader.createCell(4).setCellValue("Score");
    }

    /**
     * @param workbook
     * @param fileName
     * @param response
     */
    private void convertWorkbootToByteArray(XSSFWorkbook workbook, String fileName, HttpServletResponse response) {
        File file = getFileresource(fileName, ".xlsx");
        HandlerConstant.buildResponse(workbook, response, file);
    }

    private void convertCsvWorkbootToByteArray(String fileName, HttpServletResponse response) {
        File file = getFileresource(fileName, ".csv");
        HandlerConstant.buildResponseCsv(response, file);
    }

    /**
     * @param fileName
     * @param extension
     * @return
     */
    public File getFileresource(String fileName, String extension) {
        return new File("./src/main/resources/exports/", fileName.toLowerCase().concat(extension));
    }

    /*========================================================= CSV =====================================================================*/

    private void writeCsvForManyYear(CommunesDTO commune, List<Score> scores, List<IndicateursDTO> indicateurs, HttpServletResponse response) {
        String fileName = "evolutionScore_" + commune.getLibelle().replaceAll(" ", "_");
        File file = getFileresource(fileName, ".csv");
        CsvWriterSettings csvWriterSettings = new CsvWriterSettings();
        String[] headers = createHeaderCsv(null, scores, 1);
        csvWriterSettings.getFormat().setDelimiter(';');
        csvWriterSettings.setHeaders(headers);
        CsvWriter csvWriter = new CsvWriter(file, Charset.defaultCharset(), csvWriterSettings);
        csvWriter.writeHeaders(headers);
        for (IndicateursDTO indicateurDTO : indicateurs) {
            this.writeFirstHeadersRows(csvWriter, commune, indicateurDTO, 1);
        }
        this.convertCsvWorkbootToByteArray(fileName, response);
    }

    private void allDatatCsv(List<CommunesDTO> communes, List<Exercice> exercices, HttpServletResponse response) {
        String fileName = "evolution_performance_commune";
        File file = getFileresource(fileName, ".csv");
        CsvWriterSettings csvWriterSettings = new CsvWriterSettings();

        String[] headers = createHeaderCsv(exercices, null, 2);
        csvWriterSettings.getFormat().setDelimiter(';');
        csvWriterSettings.setHeaders(headers);

        CsvWriter csvWriter = new CsvWriter(file, Charset.defaultCharset(), csvWriterSettings);
        csvWriter.writeHeaders(headers);
        for (CommunesDTO communeDTO : communes) {
            this.writeFirstHeadersRows(csvWriter, communeDTO, null, 2);
        }
        this.convertCsvWorkbootToByteArray(fileName, response);
    }

    private void allDatatSelectedCsv(List<CommunesDTO> communes, List<Exercice> exercices, Indicateur indicateur, HttpServletResponse response) {
        String fileName = "evolution_" + indicateur.getLibelle().replaceAll(" ", "_");
        File file = getFileresource(fileName, ".csv");
        CsvWriterSettings csvWriterSettings = new CsvWriterSettings();

        String[] headers = createHeaderCsv(exercices, null, 2);
        csvWriterSettings.getFormat().setDelimiter(';');
        csvWriterSettings.setHeaders(headers);

        CsvWriter csvWriter = new CsvWriter(file, Charset.defaultCharset(), csvWriterSettings);
        csvWriter.writeHeaders(headers);
        for (CommunesDTO communeDTO : communes) {
            this.writeFirstHeadersRows(csvWriter, communeDTO, null, 2);
        }
        this.convertCsvWorkbootToByteArray(fileName, response);
    }

    private String[] createHeaderCsv(List<Exercice> exercices, List<Score> scores, int type) {
        List<String> headers = new ArrayList<>();
        if (type == 1) {
            headers.add("Indicateur");
            scores.forEach(score -> {
                headers.add(score.getAnnee().toString());
            });
        } else {
            headers.add("Région");
            headers.add("Province");
            headers.add("Commune");
            exercices.forEach(exercice -> {
                if (exercice.getAnnee() != null) {
                    headers.add(exercice.getAnnee().toString());
                }
            });
        }

        String[] trueHeaders = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            trueHeaders[i] = headers.get(i);
        }
        return trueHeaders;
    }

    private String[] createClassementHeaderCsv() {
        List<String> headers = new ArrayList<>();
        headers.add("Rang");
        headers.add("Région");
        headers.add("Province");
        headers.add("Commune");
        headers.add("Score");

        String[] trueHeaders = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            trueHeaders[i] = headers.get(i);
        }
        return trueHeaders;
    }

    private void writeFirstHeadersRows(CsvWriter csvWriter, CommunesDTO commune, IndicateursDTO indicateur, int type) {
        List<Object> body = new ArrayList<>();
        if (type == 1) {
            body.add(indicateur.getLibelle());
            indicateur.getScores().forEach(score -> {
                if (score.getScore() != null) {
                    body.add(score.getScore());
                } else {
                    body.add("-");
                }
            });
        } else {
            body.add(commune.getLibelleRegion());
            body.add(commune.getLibelleProvince());
            body.add(commune.getLibelle());
            indicateur.getScores().forEach(score -> {
                if (score.getScore() != null) {
                    body.add(score.getScore());
                } else {
                    body.add("-");
                }
            });
        }

        csvWriter.writeRow(body);
        csvWriter.flush();
    }

    private void createCsvWorbookClassement(List<CommunesDTO> communes, String fileName, HttpServletResponse response) {
        File file = getFileresource(fileName, ".csv");
        CsvWriterSettings csvWriterSettings = new CsvWriterSettings();

        String[] headers = createClassementHeaderCsv();
        csvWriterSettings.getFormat().setDelimiter(';');
        csvWriterSettings.setHeaders(headers);

        CsvWriter csvWriter = new CsvWriter(file, Charset.defaultCharset(), csvWriterSettings);
        csvWriter.writeHeaders(headers);

        for (CommunesDTO commune : communes) {
            List<Object> row = new ArrayList<>();
            row.add(commune.getRangNational());
            row.add(commune.getLibelleRegion());
            row.add(commune.getLibelleProvince());
            row.add(commune.getLibelle());
            row.add(commune.getScoreCommune());
            csvWriter.writeRow(row);
            csvWriter.flush();
        }
        this.convertCsvWorkbootToByteArray(fileName, response);
    }
}



