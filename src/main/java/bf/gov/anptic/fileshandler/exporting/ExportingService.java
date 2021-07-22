package bf.gov.anptic.fileshandler.exporting;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.domain.enumeration.FileType;
import bf.gov.anptic.domain.enumeration.TypePerformance;
import bf.gov.anptic.fileshandler.HandlerConstant;
import bf.gov.anptic.repository.*;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExportingService {
    private final Logger log = LoggerFactory.getLogger(ExportingService.class);


    private final IndicateurRepository indicateurRepository;
    private final CommuneRepository communeRepository;
    private final TypeIndicateurRepository typeIndicateurRepository;
    private final DomaineRepository domaineRepository;
    private final ModaliteRepository modaliteRepository;
    private final ValeurModaliteRepository valeurModaliteRepository;
    private final PerformanceRepository performanceRepository;
    private final EtatCommuneRepository etatCommuneRepository;
    private List<Commune> COMMUNES = new ArrayList<>();
    private List<Indicateur> INDICATEURS = new ArrayList<>();
    private List<Domaine> DOMAINES = new ArrayList<>();
    private TypeIndicateur typeIndicateur;

    public ExportingService(EtatCommuneRepository etatCommuneRepository, PerformanceRepository performanceRepository, ValeurModaliteRepository valeurModaliteRepository, ModaliteRepository modaliteRepository, IndicateurRepository indicateurRepository, CommuneRepository communeRepository, TypeIndicateurRepository typeIndicateurRepository, DomaineRepository domaineRepository) {
        this.indicateurRepository = indicateurRepository;
        this.communeRepository = communeRepository;
        this.typeIndicateurRepository = typeIndicateurRepository;
        this.domaineRepository = domaineRepository;
        this.modaliteRepository = modaliteRepository;
        this.valeurModaliteRepository = valeurModaliteRepository;
        this.performanceRepository = performanceRepository;
        this.etatCommuneRepository = etatCommuneRepository;
    }

    private void initData(Long typeId, Long anneeId) {
        this.COMMUNES = communeRepository.findAll().stream().filter(commune -> !commune.isDeleted() && etatCommuneRepository.findTop1ByExerciceIdAndCommuneId(anneeId, commune.getId()) != null)
            .sorted(Comparator.comparing(Commune::getLibelle)).collect(Collectors.toList());

        this.INDICATEURS = indicateurRepository.findAll().stream().filter(indicateur -> !indicateur.isDeleted() && indicateur.getDomaine().getTypeIndicateur().getId().equals(typeId))
            .sorted(Comparator.comparing(Indicateur::getLibelle)).collect(Collectors.toList());

        this.DOMAINES = domaineRepository.findAll()
            .stream()
            .filter(domaine -> !domaine.isDeleted() && domaine.getTypeIndicateur().getId().equals(typeId))
            .sorted(Comparator.comparing(Domaine::getLibelle)).collect(Collectors.toList());

        this.typeIndicateur = typeIndicateurRepository.getOne(typeId);
    }

    public void start(Long typeId, Extension extension, FileType fileType, Long anneeId, Long regionId, Long provinceId, HttpServletResponse response) {
        initData(typeId, anneeId);
        String fileName = createFileName(typeId);
        if (extension.equals(Extension.XLSX)) {
            if (fileType.equals(FileType.MODEL)) {
                exportExcelModel(fileName, response);
            } else {
                exportExcelData(fileName, anneeId, regionId, provinceId, response);
            }
        } else {
            if (fileType.equals(FileType.MODEL)) {
                exportCsvModel(fileName, response);
            } else {
                exportCsvData(fileName, anneeId, regionId, provinceId, response);
            }
        }
    }

    /*=========================================== EXPORT EXCEL ============================================*/

    /**
     * @param response
     */
    private void exportExcelModel(String fileName, HttpServletResponse response) {
        XSSFWorkbook workbook = createWorkbook(fileName);

        createExcelHeaders(workbook);
        setCellValues(workbook, this.COMMUNES);

        buildExcelResponse(workbook, fileName, response);
    }

    private void setCellValues(XSSFWorkbook workbook, List<Commune> communes) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        int i = sheet.getLastRowNum();
        for (Commune commune : communes) {
            i++;
            initExcelRow(commune, sheet, i);
        }
    }

    private void exportExcelData(String fileName, Long anneeId, Long regionId, Long provinceId, HttpServletResponse response) {
        XSSFWorkbook workbook = createWorkbook(fileName);

        createExcelHeaders(workbook);
        setExcelDataToExport(workbook, getCommunes(regionId, provinceId), anneeId);

        buildExcelResponse(workbook, fileName, response);
    }

    private void setExcelDataToExport(XSSFWorkbook workbook, List<Commune> communes, Long anneeId) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        int i = sheet.getLastRowNum();
        for (Commune commune : communes) {
            i++;
            XSSFRow row = initExcelRow(commune, sheet, i);
            for (Indicateur indicateur : INDICATEURS) {
                if (!indicateur.getSousIndicateur()) {
                    Optional<ValeurModalite> valeurModalite = valeurModaliteRepository.findTop1ByIndicateurIdAndCommuneIdAndExerciceId(indicateur.getId(), commune.getId(), anneeId);
                    if (valeurModalite.isPresent()) {
                        row.createCell(row.getLastCellNum()).setCellValue(Double.parseDouble(valeurModalite.get().getValeur().replaceAll(",", ".")));
                    } else {
                        row.createCell(row.getLastCellNum()).setCellValue("");
                    }
                } else {
                    List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNotNullAndDeletedIsFalse(indicateur.getId())
                        .stream()
                        .sorted(Comparator.comparing((Modalite mod) -> mod.getIndicateur().getId())).collect(Collectors.toList());
                    for (Modalite modalite : modalites) {
                        Optional<ValeurModalite> valeurMod = valeurModaliteRepository.findTop1ByModaliteIdAndCommuneIdAndExerciceId(modalite.getId(), commune.getId(), anneeId);
                        if (valeurMod.isPresent()) {
                            row.createCell(row.getLastCellNum()).setCellValue(valeurMod.get().getValeur());
                        } else {
                            row.createCell(row.getLastCellNum()).setCellValue("");
                        }
                    }
                }
            }

            for (Indicateur indicateur : INDICATEURS) {
                Optional<Performance> performance = performanceRepository.findTopByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(commune.getId(), anneeId, indicateur.getId());
                if (performance.isPresent()) {
                    row.createCell(row.getLastCellNum()).setCellValue(performance.get().getScore());
                } else {
                    row.createCell(row.getLastCellNum()).setCellValue("");
                }
            }

            for (Domaine domaine : DOMAINES) {
                Performance performance = performanceRepository.findTop1ByCommuneIdAndExerciceIdAndDomaineIdAndTypePerformanceAndDeletedIsFalse(commune.getId(), anneeId, domaine.getId(), TypePerformance.DOMAINE);
                setExcelData(performance, row);
            }

            Performance performance = performanceRepository.findTop1ByCommuneIdAndExerciceIdAndTypePerformanceAndTypeDomainIdAndDeletedIsFalse(commune.getId(), anneeId, TypePerformance.COMMUNE, typeIndicateur.getId());
            setExcelData(performance, row);
        }
    }

    private void setExcelData(Performance performance, Row row) {
        if (performance != null) {
            row.createCell(row.getLastCellNum()).setCellValue(performance.getScore());
            row.createCell(row.getLastCellNum()).setCellValue(performance.getNbEtoile());
        } else {
            row.createCell(row.getLastCellNum()).setCellValue("");
            row.createCell(row.getLastCellNum()).setCellValue("");
        }
    }

    private XSSFRow initExcelRow(Commune commune, XSSFSheet sheet, int index) {
        XSSFRow row = sheet.createRow(index);
        row.createCell(0).setCellValue(commune.getProvince().getRegion().getLibelle());
        row.createCell(1).setCellValue(commune.getProvince().getLibelle());
        row.createCell(2).setCellValue(commune.getLibelle());

        return row;
    }


    private void buildExcelResponse(XSSFWorkbook workbook, String fileName, HttpServletResponse response) {
        File file = getFile(fileName, ".xlsx");
        HandlerConstant.buildResponse(workbook, response, file);
    }

    private void createExcelHeaders(XSSFWorkbook workbook) {
        Row header = workbook.getSheetAt(0).createRow(0);
        header.createCell(0).setCellValue("Région");
        header.createCell(1).setCellValue("Province");
        header.createCell(2).setCellValue("Commune");
        int i = header.getLastCellNum();
        for (Indicateur indicateur : INDICATEURS) {
            if (!indicateur.getSousIndicateur()) {
                header.createCell(i).setCellValue(indicateur.getLibelle());
                i++;
            } else {
                List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNotNullAndDeletedIsFalse(indicateur.getId())
                    .stream()
                    .sorted(Comparator.comparing((Modalite mod) -> mod.getIndicateur().getId())).collect(Collectors.toList());
                for (Modalite modalite : modalites) {
                    header.createCell(i).setCellValue(modalite.getLibelle());
                    i++;
                }
            }
        }
        i = header.getLastCellNum();
        for (Indicateur indicateur : INDICATEURS) {
            header.createCell(i).setCellValue("Points-" + indicateur.getLibelle());
            i++;
        }

        for (Domaine domaine : DOMAINES) {
            header.createCell(header.getLastCellNum()).setCellValue("Points-" + domaine.getLibelle());
            header.createCell(header.getLastCellNum()).setCellValue("Nombre d'étoiles-" + domaine.getLibelle());
        }

        i = header.getLastCellNum();
        header.createCell(i).setCellValue("Performance globale");
        header.createCell(i + 1).setCellValue("Nombre d'étoiles-Performance globale");
    }


    private XSSFWorkbook createWorkbook(String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet(fileName.replaceAll("-", " "));
        return workbook;
    }


    /*=====================================================CSV EXPORTING===================================================*/

    private void exportCsvModel(String fileName, HttpServletResponse response) {
        CsvWriter csvWriter = createCsvWriter(fileName, true);
        this.writeFirstHeadersRows(csvWriter);
        buildCsvResponse(fileName, response);
    }

    private void buildCsvResponse(String fileName, HttpServletResponse response) {
        File file = getFile(fileName, ".csv");
        HandlerConstant.buildResponseCsv(response, file);
    }

    private CsvWriter createCsvWriter(String fileName, boolean select) {
        File file = getFile(fileName, ".csv");
        CsvWriterSettings csvWriterSettings = new CsvWriterSettings();

        String[] headers = createCsvHeaders();
        csvWriterSettings.getFormat().setDelimiter(';');
        csvWriterSettings.setHeaders(headers);
        if (select) {
            csvWriterSettings.selectFields("Région", "Province", "Commune");
        }
        CsvWriter csvWriter = new CsvWriter(file, Charset.defaultCharset(), csvWriterSettings);
        csvWriter.writeHeaders(headers);

        return csvWriter;
    }

    private String[] createCsvHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Région");
        headers.add("Province");
        headers.add("Commune");

        INDICATEURS.forEach(indicateur -> {
            if (!indicateur.getSousIndicateur()) {
                headers.add(indicateur.getLibelle());
            } else {
                List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNotNullAndDeletedIsFalse(indicateur.getId());
                modalites.forEach(modalite -> headers.add(modalite.getLibelle()));
            }
        });

        for (Indicateur indicateur : INDICATEURS) {
            headers.add(headers.size(), "Points-" + indicateur.getLibelle());
        }

        for (Domaine domaine : DOMAINES) {
            headers.add(headers.size(), "Points-" + domaine.getLibelle());
            headers.add(headers.size(), "Nombre d'étoiles-" + domaine.getLibelle());
        }

        headers.add(headers.size(), "Performance globale");
        headers.add(headers.size(), "Nombre d'étoiles-Performance globale");

        String[] trueHeaders = new String[headers.size()];
        trueHeaders = headers.toArray(trueHeaders);
        return trueHeaders;
    }

    private void exportCsvData(String fileName, Long anneeId, Long regionId, Long provinceId, HttpServletResponse response) {
        createCsvWriter(fileName, true);

        setCsvDataToExport(fileName, getCommunes(regionId, provinceId), anneeId);
        buildCsvResponse(fileName, response);
    }

    private void setCsvDataToExport(String fileName, List<Commune> communes, Long anneeId) {
        CsvWriter csvWriter = createCsvWriter(fileName, false);
        for (Commune commune : communes) {
            List<Object> row = new ArrayList<>();
            row.add(commune.getProvince().getRegion().getLibelle());
            row.add(commune.getProvince().getLibelle());
            row.add(commune.getLibelle());
            for (Indicateur indicateur : INDICATEURS) {
                if (!indicateur.getSousIndicateur()) {
                    Optional<ValeurModalite> valeurModalite = valeurModaliteRepository.findTop1ByIndicateurIdAndCommuneIdAndExerciceId(indicateur.getId(), commune.getId(), anneeId);
                    if (valeurModalite.isPresent()) {
                        row.add(Math.round(Double.valueOf(valeurModalite.get().getValeur().replaceAll(",", "."))));
                    } else {
                        row.add(null);
                    }
                } else {
                    List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNotNullAndDeletedIsFalse(indicateur.getId())
                        .stream()
                        .sorted(Comparator.comparing((Modalite mod) -> mod.getIndicateur().getId())).collect(Collectors.toList());
                    for (Modalite modalite : modalites) {
                        Optional<ValeurModalite> valeurMod = valeurModaliteRepository.findTop1ByModaliteIdAndCommuneIdAndExerciceId(modalite.getId(), commune.getId(), anneeId);
                        if (valeurMod.isPresent()) {
                            row.add(valeurMod.get().getValeur());
                        } else {
                            row.add(null);
                        }
                    }
                }
            }

            for (Indicateur indicateur : INDICATEURS) {
                Optional<Performance> performance = performanceRepository.findTopByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(commune.getId(), anneeId, indicateur.getId());
                if (performance.isPresent()) {
                    row.add(performance.get().getScore());
                } else {
                    row.add(null);
                }
            }

            for (Domaine domaine : DOMAINES) {
                Performance performance = performanceRepository.findTop1ByCommuneIdAndExerciceIdAndDomaineIdAndTypePerformanceAndDeletedIsFalse(commune.getId(), anneeId, domaine.getId(), TypePerformance.DOMAINE);
                if (performance != null) {
                    row.add(performance.getScore());
                    row.add(performance.getNbEtoile());
                } else {
                    row.add(null);
                    row.add(null);
                }
            }

            Performance performance = performanceRepository.findTop1ByCommuneIdAndExerciceIdAndTypePerformanceAndTypeDomainIdAndDeletedIsFalse(commune.getId(), anneeId, TypePerformance.COMMUNE, typeIndicateur.getId());
            if (performance == null) {
                row.add(null);
                row.add(null);
            } else {
                row.add(performance.getScore());
                row.add(performance.getNbEtoile());
            }
            csvWriter.writeRow(row);
            csvWriter.flush();
        }
    }

    private void writeFirstHeadersRows(CsvWriter csvWriter) {
        COMMUNES.forEach(commune -> csvWriter.writeRow(commune.getProvince().getRegion().getLibelle(), commune.getProvince().getLibelle(), commune.getLibelle()));
    }

    private String createFileName(Long id) {
        return typeIndicateurRepository.findById(id).get().getLibelle().replaceAll(" ", "-");
    }

    private File getFile(String fileName, String extension) {
        return new File("./src/main/resources/exports/",  fileName.concat(extension));
    }

    private List<Commune> getCommunes(Long regionId, Long provinceId) {
        List<Commune> communes = COMMUNES;
        if (regionId != null && provinceId != null) {
            communes = COMMUNES.stream().filter(commune -> commune.getProvince().getId().equals(provinceId)).collect(Collectors.toList());
        } else if (regionId != null) {
            communes = COMMUNES.stream().filter(commune -> commune.getProvince().getRegion().getId().equals(regionId)).collect(Collectors.toList());
        }

        return communes;
    }
}
