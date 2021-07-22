package bf.gov.anptic.fileshandler.importing;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.domain.enumeration.Extension;
import bf.gov.anptic.domain.enumeration.TypePerformance;
import bf.gov.anptic.fileshandler.HandlerConstant;
import bf.gov.anptic.repository.*;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImportingService {

    private final Logger log = LoggerFactory.getLogger(ImportingService.class);


    private final IndicateurRepository indicateurRepository;
    private final CommuneRepository communeRepository;
    private final DomaineRepository domaineRepository;
    private final ModaliteRepository modaliteRepository;
    private final ExerciceRepository exerciceRepository;
    private final ValeurModaliteRepository valeurModaliteRepository;
    private final PerformanceRepository performanceRepository;

    @Autowired
    public EtatCommuneRepository etatCommuneRepository;

    @Autowired
    public ValidationCommuneRepository validationCommuneRepository;

    public ImportingService(PerformanceRepository performanceRepository, ValeurModaliteRepository valeurModaliteRepository, ExerciceRepository exerciceRepository, IndicateurRepository indicateurRepository, CommuneRepository communeRepository, DomaineRepository domaineRepository, ModaliteRepository modaliteRepository) {
        this.indicateurRepository = indicateurRepository;
        this.communeRepository = communeRepository;
        this.domaineRepository = domaineRepository;
        this.modaliteRepository = modaliteRepository;
        this.exerciceRepository = exerciceRepository;
        this.valeurModaliteRepository = valeurModaliteRepository;
        this.performanceRepository = performanceRepository;
    }

    /**
     * Service d'importation du fichier excel des performances
     *
     * @param file
     * @param anneeId
     * @param typeId
     * @return
     */
    public void saveFiles(MultipartFile file, Long anneeId, Long typeId, Extension extension, boolean update) {
        try {
            if (update) {
                removePerformanceAndValeurModalite(anneeId, typeId);
            }
            File file1 = createFile(file.getInputStream(), new File("./src/main/resources/exports/" + Objects.requireNonNull(file.getResource().getFilename())));
            if (extension.equals(Extension.XLSX)) {
                saveExcelEntityValue(XSSFWorkbookFactory.createWorkbook(file1, true), anneeId, typeId);
            } else {
                saveCsvEntityValue(file1, anneeId, typeId);
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private File createFile(InputStream inputStream, File file) throws IOException {
        FileUtils.copyInputStreamToFile(inputStream, file);
        return file;
    }

    /**
     * Méthode pour la récupération d'une entité à partir de l'entête d'une colonne
     *
     * @param libelle
     * @return
     */
    private Object getEntity(String libelle) {
        Optional<Modalite> modalite = modaliteRepository.findTop1ByLibelleAndDeletedIsFalse(libelle);
        if (modalite.isPresent()) {
            return modalite.get();
        } else {
            Optional<Indicateur> indicateur = indicateurRepository.findTop1ByLibelleAndDeletedIsFalse(libelle);
            if (indicateur.isPresent()) {
                return indicateur.get();
            } else {
                Optional<Domaine> domaine = domaineRepository.findTop1ByLibelleAndDeletedIsFalse(libelle);
                return domaine.orElse(null);
            }
        }
    }

    /*=============================================================================== EXCEL ==============================================================================*/
    /*============================================= DEBUT DE L'IMPORTATION DU FICHIER EXCEL DES PERFORMANCES MUNICIPALES =================================================*/
    /*====================================================================================================================================================================*/

    /**
     * Enregistrement des performances de chaque commune par indicateur
     *
     * @param workbook
     * @param anneeId
     * @param typeId
     * @return
     */
    private void saveExcelEntityValue(XSSFWorkbook workbook, Long anneeId, Long typeId) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row headers = sheet.getRow(0);
        Exercice exercice = exerciceRepository.getOne(anneeId);
        for (Row row : sheet) {
            if (row.getRowNum() > 0) {
                if (row.getCell(2).getStringCellValue() != null) {
                    String lib = row.getCell(2).getStringCellValue();
                    String plib = row.getCell(1).getStringCellValue().toLowerCase();
                    Commune commune = getCommune(plib, lib);
                    if (commune != null) {
                        for (Cell col : row) {
                            if (col.getColumnIndex() > 2) {
                                String libelle = headers.getCell(col.getColumnIndex()).getStringCellValue();
                                if (libelle != null) {
                                    if (!libelle.contains(HandlerConstant.GLOBAL)) {
                                        Object object = getEntity(getSplitLibelle(libelle));
                                        if (object != null) {
                                            if (object instanceof Modalite) {
                                                createValeurModalite(commune, exercice, (Modalite) object, col, null);
                                            } else if (object instanceof Indicateur) {
                                                createIndicateurPerformance(libelle, col, null, commune, exercice, typeId, (Indicateur) object);
                                            } else if (object instanceof Domaine) {
                                                createDomainePerformance(col, null, commune, exercice, typeId, (Domaine) object, libelle);
                                            }
                                        }
                                    } else {
                                        createGlobalPerformance(col, null, commune, exercice, typeId, libelle);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Commune getCommune(String plib, String lib) {
        List<Commune> communes = communeRepository.findAllByLibelleAndDeletedIsFalse(lib);
        Commune commune;
        if (communes.size() > 1) {
            commune = communes.stream().reduce((Commune com1, Commune com2) -> com1.getProvince().getLibelle().toLowerCase().equals(plib) ? com1 : com2).get();
        } else {
            commune = communeRepository.findTop1ByLibelleAndDeletedIsFalse(lib);
        }

        return commune;
    }


    /*=============================================================================== CSV ==============================================================================*/
    /*============================================= DEBUT DE L'IMPORTATION DU FICHIER CSV DES PERFORMANCES MUNICIPALES =================================================*/
    /*====================================================================================================================================================================*/

    /**
     * @param file
     * @param anneeId
     * @param typeId
     * @return
     */
    private void saveCsvEntityValue(File file, Long anneeId, Long typeId) throws IOException {
        CsvParserSettings csvParserSettings = new CsvParserSettings();
        csvParserSettings.getFormat().setDelimiter(';');
        csvParserSettings.getFormat().setQuoteEscape('\\');

        CsvParser csvParser = new CsvParser(csvParserSettings);
        Exercice exercice = exerciceRepository.getOne(anneeId);
        List<Record> rows = csvParser.parseAllRecords(file);
        Record headers = rows.get(0);
        for (Record row : rows) {
            if (rows.indexOf(row) > 0) {
                String[] cols = row.getValues();
                if (cols.length > 0 && cols[2] != null) {
                    String lib = cols[2];
                    String plib = cols[1].toLowerCase();
                    Commune commune = getCommune(plib, lib);
                    int i = 0;
                    if (commune != null) {
                        for (String col : cols) {
                            if (i > 2) {
                                String libelle = headers.getValues()[i];
                                if (libelle != null) {
                                    if (!libelle.contains(HandlerConstant.GLOBAL)) {
                                        Object object = getEntity(getSplitLibelle(libelle));
                                        if (object != null) {
                                            if (object instanceof Modalite) {
                                                createValeurModalite(commune, exercice, (Modalite) object, null, col);
                                            } else if (object instanceof Indicateur) {
                                                createIndicateurPerformance(libelle, null, col, commune, exercice, typeId, (Indicateur) object);
                                            } else if (object instanceof Domaine) {
                                                createDomainePerformance(null, col, commune, exercice, typeId, (Domaine) object, libelle);
                                            }
                                        }
                                    } else {
                                        createGlobalPerformance(null, col, commune, exercice, typeId, libelle);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Récupération du libellé sans le caractère "-"
     *
     * @param libelle
     * @return
     */
    private String getSplitLibelle(String libelle) {
        if (libelle.toLowerCase().contains(HandlerConstant.ETOILES) || libelle.toLowerCase().contains(HandlerConstant.POINTS)) {
            return libelle.split("-")[1];
        } else return libelle;
    }

    /**
     * Méthode de création d'une instance de performance
     *
     * @param commune  La commune concernée
     * @param exercice L'année excercice
     * @param typeId   id du groupe de domaine
     * @return performance
     */
    private Performance createInstance(Commune commune, Exercice exercice, Long typeId) {
        Performance performance = new Performance();
        performance.setCommune(commune);
        performance.setExercice(exercice);
        performance.setTypeDomainId(typeId);
        performance.setDeleted(false);
        return performance;
    }

    /**
     * Enregistrement d'une ${@link Performance} de type ${@link Indicateur}
     *
     * @param cell       La colonne en cours dans le fichier
     * @param commune    La commune concernée
     * @param exercice   L'année excercice
     * @param typeId     id du groupe de domaine
     * @param indicateur L'indicateur pour lequel on crée la performance
     */
    private void createIndicateurPerformance(String libelle, Cell cell, String col, Commune commune, Exercice exercice, Long typeId, Indicateur indicateur) {
        if (libelle.toLowerCase().contains(HandlerConstant.POINTS)) {
            Performance performance = createInstance(commune, exercice, typeId);
            performance.setIndicateur(indicateur);
            performance.setTypePerformance(TypePerformance.INDICATEUR);
            if (cell == null) {
                performance.setScore(Double.parseDouble(col.replaceAll(",", ".")));
            } else {
                CellType cellType =  cell.getCellType();
                if (cellType == CellType.BLANK || cellType == CellType.STRING){
                    performance.setScore(null);
                } else {
                    performance.setScore(cell.getNumericCellValue());
                }
            }
            performanceRepository.saveAndFlush(performance);
        } else {
            ValeurModalite valeurModalite = new ValeurModalite();
            valeurModalite.setCommune(commune);
            valeurModalite.setExercice(exercice);
            valeurModalite.setIndicateurId(indicateur.getId());
            if (cell == null) {
                valeurModalite.setValeur(col);
            } else {
                CellType cellType =  cell.getCellType();
                if (cellType == CellType.BLANK || cellType == CellType.STRING){
                    valeurModalite.setValeur(cell.getStringCellValue());
                } else {
                    valeurModalite.setValeur(String.valueOf(cell.getNumericCellValue()));
                }
            }
            valeurModaliteRepository.saveAndFlush(valeurModalite);
        }
    }

    /**
     * Enregistrement d'une ${@link Performance} de type ${@link Domaine}
     *
     * @param cell
     * @param commune
     * @param exercice
     * @param typeId
     * @param domaine
     * @param libelle
     */
    private void createDomainePerformance(Cell cell, String col, Commune commune, Exercice exercice, Long typeId, Domaine domaine, String libelle) {
        Performance performance = performanceRepository
            .findTop1ByCommuneIdAndExerciceIdAndDomaineIdAndTypePerformanceAndDeletedIsFalse(commune.getId(), exercice.getId(), domaine.getId(), TypePerformance.DOMAINE);
        if (performance == null) {
            performance = createInstance(commune, exercice, typeId);
            performance.setDomaineId(domaine.getId());
            performance.setTypePerformance(TypePerformance.DOMAINE);
            if (cell == null) {
                encapsulate(libelle, performance, null, col);
            } else {
                encapsulate(libelle, performance, cell, null);
            }
        } else {
            if (cell == null) {
                encapsulate(libelle, performance, null, col);
            } else {
                encapsulate(libelle, performance, cell, null);
            }
        }
        performanceRepository.saveAndFlush(performance);
    }

    /**
     * Enregistrement d'une ${@link Performance} de type ${@link Commune}
     *
     * @param cell
     * @param commune
     * @param exercice
     * @param typeId
     * @param libelle
     */
    private void createGlobalPerformance(Cell cell, String col, Commune commune, Exercice exercice, Long typeId, String libelle) {
        Performance performance = performanceRepository.findTop1ByCommuneIdAndExerciceIdAndTypePerformanceAndTypeDomainIdAndDeletedIsFalse(commune.getId(), exercice.getId(), TypePerformance.COMMUNE, typeId);
        if (performance == null) {
            performance = createInstance(commune, exercice, typeId);
            performance.setTypePerformance(TypePerformance.COMMUNE);
            if (cell == null) {
                encapsulate(libelle, performance, null, col);
            } else {
                encapsulate(libelle, performance, cell, null);
            }
        } else {
            if (cell == null) {
                encapsulate(libelle, performance, null, col);
            } else {
                encapsulate(libelle, performance, cell, null);
            }
        }
        performanceRepository.saveAndFlush(performance);
    }


    private void encapsulate(String libelle, Performance performance, Cell cell, String col) {
        if (libelle.toLowerCase().contains(HandlerConstant.ETOILES)) {
            if (cell == null) {
                performance.setNbEtoile(Double.parseDouble(col.replaceAll(",", ".")));
            } else {
                CellType cellType =  cell.getCellType();
                if (cellType == CellType.BLANK || cellType == CellType.STRING){
                    performance.setNbEtoile(null);
                } else {
                    performance.setNbEtoile(cell.getNumericCellValue());
                }
              //  performance.setNbEtoile(cell.getNumericCellValue());
            }
        } else if (libelle.toLowerCase().contains(HandlerConstant.POINTS) || libelle.contains(HandlerConstant.GLOBAL)) {
            if (cell == null) {
                performance.setScore(Double.parseDouble(col.replaceAll(",", ".")));
            } else {
                CellType cellType =  cell.getCellType();
                if (cellType == CellType.BLANK || cellType == CellType.STRING){
                    performance.setScore(null);
                } else {
                    performance.setScore(cell.getNumericCellValue());
                }
               // performance.setScore(cell.getNumericCellValue());
            }
        }
    }

    /**
     * Enregistrement d'une ${@link ValeurModalite} pour une ${@link Modalite} donné
     *
     * @param commune
     * @param exercice
     * @param modalite
     * @param col
     */
    private void createValeurModalite(Commune commune, Exercice exercice, Modalite modalite, Cell cell, String col) {
        ValeurModalite valeurModalite = new ValeurModalite();
        valeurModalite.setCommune(commune);
        valeurModalite.setExercice(exercice);
        valeurModalite.setModalite(modalite);
        if (cell == null) {
            if (col != null) {
                valeurModalite.setValeur(col);
            } else {
                valeurModalite.setValeur("");
            }
        } else {
            if (cell.getStringCellValue() != null) {
                valeurModalite.setValeur(cell.getStringCellValue());
            } else {
                valeurModalite.setValeur("");
            }
        }
        valeurModaliteRepository.saveAndFlush(valeurModalite);
    }


    public Boolean checkIfDataAlreadyImport(Long anneeId, Long typeId) {
        return performanceRepository.existsAllByExerciceIdAndTypeDomainId(anneeId, typeId);
    }

    private void removePerformanceAndValeurModalite(Long anneeId, Long typeId) {
        List<Performance> performances = performanceRepository.findAllByExerciceIdAndTypeDomainIdAndDeletedIsFalse(anneeId, typeId);
        List<ValeurModalite> valeurModalites = valeurModaliteRepository.findAllByExerciceId(anneeId)
            .stream()
            .filter(valeurModalite ->
                (valeurModalite.getModalite() != null && valeurModalite.getModalite().getIndicateur().getDomaine().getTypeIndicateur().getId().equals(typeId))
                ||
                (valeurModalite.getIndicateurId() != null && indicateurRepository.getOne(valeurModalite.getIndicateurId()).getDomaine().getTypeIndicateur().getId().equals(typeId))
            )
            .collect(Collectors.toList());
        if (!performances.isEmpty()) {
            performanceRepository.deleteAll(performances);
        }

        if (!valeurModalites.isEmpty()) {
            valeurModaliteRepository.deleteAll(valeurModalites);
        }
    }
}
