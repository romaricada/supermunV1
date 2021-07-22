package bf.gov.anptic.service;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.domain.enumeration.ObjectType;
import bf.gov.anptic.domain.enumeration.TypePerformance;
import bf.gov.anptic.repository.*;
import bf.gov.anptic.service.dto.*;
import bf.gov.anptic.service.mapper.*;
import bf.gov.anptic.util.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Performance}.
 */
@Service
@Transactional
public class PerformanceService {

    private final Logger log = LoggerFactory.getLogger(PerformanceService.class);

    private final PerformanceRepository performanceRepository;

    private final PerformanceMapper performanceMapper;

    @Autowired
    private CommuneService communeService;

    @Autowired
    private ValidationCommuneRepository validationCommuneRepository;

    @Autowired
    private CommuneRepository communeRepository;

    @Autowired
    private CommuneMapper communeMapper;

    @Autowired
    private CommunesMapper communesMapper;

    @Autowired
    private IndicateurMapper indicateurMapper;

    @Autowired
    ValeurModaliteService valeurModaliteService;

    @Autowired
    ValeurModaliteRepository valeurModaliteRepository;

    @Autowired
    ValeurModaliteMapper valeurModaliteMapper;

    @Autowired
    ExerciceRepository exerciceRepository;

    @Autowired
    DomaineRepository domaineRepository;

    @Autowired
    IndicateurRepository indicateurRepository;

    @Autowired
    EtatCommuneRepository etatCommuneRepository;

    @Autowired
    EtatCommuneMapper etatCommuneMapper;

    @Autowired
    ValidationCommuneMapper validationCommuneMapper;

    @Autowired
    TypeIndicateurRepository typeIndicateurRepository;


    public PerformanceService(PerformanceRepository performanceRepository, PerformanceMapper performanceMapper) {
        this.performanceRepository = performanceRepository;
        this.performanceMapper = performanceMapper;
    }

    /**
     * Save a performance.
     *
     * @param performanceDTO the entity to save.
     * @return the persisted entity.
     */
    public PerformanceDTO save(PerformanceDTO performanceDTO) {
        log.debug("Request to save Performance : {}", performanceDTO);
        Performance performance = performanceMapper.toEntity(performanceDTO);
        performance = performanceRepository.save(performance);
        return performanceMapper.toDto(performance);
    }

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PerformanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Performances");
        List<PerformanceDTO> performances = performanceRepository.findAll().stream().filter(val -> !val.isDeleted())
            .map(performanceMapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(performances, pageable, performances.size());
    }


    /**
     * Get one performance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PerformanceDTO> findOne(Long id) {
        log.debug("Request to get Performance : {}", id);
        return performanceRepository.findById(id)
            .map(performanceMapper::toDto);
    }

    /**
     * Delete the performance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Performance : {}", id);
        performanceRepository.deleteById(id);
    }


    List<Commune> getListCommune(Long anneeId, Pageable pageable) {
        List<Commune> communes = new ArrayList<>();
        etatCommuneRepository.findAllByExerciceId(anneeId, pageable).forEach(etatCommune -> communes.add(etatCommune.getCommune()));

        return communes;
    }

    /**
     * @param performancesDTO
     * @return
     */
    public Long deleteAll(List<PerformanceDTO> performancesDTO) {
        log.debug("Request to delete Performance : {}", performancesDTO);
        List<Performance> performances = performanceMapper.toEntity(performancesDTO);
        if (!performances.isEmpty()) {
            performanceRepository.saveAll(performances);
        }
        return Long.parseLong("" + performances.size());
    }

    /**
     * @param idTypeDomaine
     * @param idAnne
     * @return
     */
    public Page<CommuneCopyDTO> getAllCommuneWithAllData(Long idTypeDomaine, Long idAnne, Pageable pageable) {
        List<CommuneCopyDTO> communeCopys = new ArrayList<>();
        long total = 0L;
        if (performanceRepository.existsAllByExerciceIdAndTypeDomainId(idAnne, idTypeDomaine)) {
            List<Commune> communes = getListCommune(idAnne, pageable)
                .stream().sorted(Comparator.comparing(Commune::getLibelle)).collect(Collectors.toList());
            List<Domaine> domaines = domaineRepository.findAllByTypeIndicateurIdAndDeletedIsFalse(idTypeDomaine)
                .stream().sorted(Comparator.comparing(Domaine::getLibelle)).collect(Collectors.toList());
            for (Commune commune : communes) {
                List<PerformanceDTO> performanceDomaineDTOS = new ArrayList<>();
                List<ValeurModaliteDTO> valeurModalites = new ArrayList<>();
                CommuneCopyDTO communeCopyDTO = new CommuneCopyDTO();
                communeCopyDTO.setId(commune.getId());
                communeCopyDTO.setLibelle(commune.getLibelle());
                communeCopyDTO.setEtatCommune(etatCommuneMapper.toDto(etatCommuneRepository.findTop1ByExerciceIdAndCommuneId(idAnne, commune.getId())));
                communeCopyDTO.setValidationCommuneDTO(validationCommuneMapper.toDto(validationCommuneRepository.findTop1ByCommuneIdAndTypeDomaineIdAndExerciceId(commune.getId(), idTypeDomaine, idAnne)));
                for (Domaine domaine : domaines) {
                    List<PerformanceDTO> performances = performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(commune.getId(), idAnne)
                        .stream()
                        .filter(performance -> performance.getIndicateur() != null && performance.getIndicateur().getDomaine().getId().equals(domaine.getId()))
                        .sorted(Comparator.comparing((Performance perf) -> perf.getIndicateur().getLibelle())).map(performanceMapper::toDto).collect(Collectors.toList());
                    communeCopyDTO.getPerformances().addAll(performances);
                    performanceDomaineDTOS.add(performanceMapper.toDto(performanceRepository.findTop1ByCommuneIdAndExerciceIdAndDomaineIdAndTypePerformanceAndDeletedIsFalse(commune.getId(), idAnne, domaine.getId(), TypePerformance.DOMAINE)));
                    valeurModalites.addAll(
                        getValeurModalites(commune, idAnne, domaine));
                }
                communeCopyDTO.getPerformances().addAll(communeCopyDTO.getPerformances().size(), performanceDomaineDTOS);
                communeCopyDTO.getPerformances().add(communeCopyDTO.getPerformances().size(), performanceMapper.toDto(performanceRepository.findTop1ByCommuneIdAndExerciceIdAndTypePerformanceAndTypeDomainIdAndDeletedIsFalse(commune.getId(), idAnne, TypePerformance.COMMUNE, idTypeDomaine)));
                communeCopyDTO.setValeurModalites(filterValeurs(valeurModalites));
                communeCopys.add(communeCopyDTO);
            }
            total = etatCommuneRepository.countAllByExerciceId(idAnne);
            log.debug("==========================================================>");
            log.debug("==========================COMMUNECOPYS================================>");
            log.debug("{}", communeCopys);
            log.debug("==========================================================>");
            log.debug("==========================================================>");
        }
        return new PageImpl<>(communeCopys, pageable, total);
    }

    private List<ValeurModaliteDTO> getValeurModalites(Commune commune, Long anneeId, Domaine domaine) {
        List<ValeurModalite> valeurModalites = new ArrayList<>();
        indicateurRepository.findAllByDomaineIdAndDeletedIsFalse(domaine.getId())
            .stream().sorted(Comparator.comparing(Indicateur::getLibelle))
            .forEach(indicateur -> valeurModalites.addAll(valeurModaliteRepository.findAllByCommuneIdAndExerciceId(commune.getId(), anneeId)
                .stream()
                .filter(valeurModalite -> {
                    if (valeurModalite.getIndicateurId() != null) {
                        return valeurModalite.getIndicateurId().equals(indicateur.getId());
                    } else return valeurModalite.getModalite().getIndicateur().getId().equals(indicateur.getId());
                }).sorted(Comparator.comparing(valeurModalite -> valeurModalite.getModalite().getCode())).collect(Collectors.toList())));

        return valeurModaliteMapper.toDto(valeurModalites);
    }

    private List<ValeurModaliteDTO> filterValeurs(List<ValeurModaliteDTO> valeurModaliteDTOS) {
        List<ValeurModaliteDTO> list1 = valeurModaliteDTOS.stream().filter(valeurModaliteDTO -> valeurModaliteDTO.getIndicateurId() == null).collect(Collectors.toList());
        List<ValeurModaliteDTO> list2 = valeurModaliteDTOS.stream().filter(valeurModaliteDTO -> valeurModaliteDTO.getIndicateurId() != null).collect(Collectors.toList());

        List<ValeurModaliteDTO> finalList = new ArrayList<>();
        finalList.addAll(list1);
        finalList.addAll(list1.size(), list2);
        return finalList;
    }

    /**
     * @param idCommune
     * @param idExercice
     * @param idIndicateur
     * @return
     */
    public PerformanceDTO findAllPerformanceBycommuneAndExerciceAndIndicateur(Long idCommune, Long idExercice, Long idIndicateur) {
        log.debug("Request to get performace by commune and exercice and indicateur");
        Exercice exercice = exerciceRepository.getOne(idExercice);
        Exercice exercicePreced = exerciceRepository.findExerciceByAnnee(exercice.getAnnee()-1);
        Optional<PerformanceDTO> performa1= Optional.empty();

        Optional<PerformanceDTO> performa = performanceRepository.findAllByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(idCommune, idExercice, idIndicateur).stream()
            .filter(performance ->
                performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR)
            )
            .map(performanceMapper::toDto).findFirst();
        if (exercicePreced != null && exercicePreced.getValidated()) {
            performa1 = performanceRepository.findAllByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(idCommune, exercicePreced.getId(), idIndicateur).stream()
                .filter(performance ->
                        performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR)
                ).map(performanceMapper::toDto).findFirst();
        }
        if (performa.isPresent()) {
            performa.get().getCommune().setRangNational(this.getRangNational(idCommune, idExercice, idIndicateur));
            performa.get().getCommune().setRangRegional(this.getRangRegional(idCommune, idExercice, idIndicateur));
            if (performa1.isPresent()) {
                performa.get().setAnneePreced(exercicePreced.getAnnee());
                performa.get().setScoreAnneePrec(new DecimalFormat("#.###").format(performa.get().getScore() - performa1.get().getScore()));
            } else {
                performa.get().setScoreAnneePrec("Nom prise compte");
            }
            return performa.get();
        }
        return null;
    }

    /**
     * @param idExercice
     * @param idIndicateur
     * @return
     */
    public List<PerformanceDTO> findAllByExerciceAndIndicateur(Long idExercice, Long idIndicateur) {
        /*return performanceRepository.findAll().stream()
            .filter(performance -> performance.getExercice() != null && performance.getExercice().getId().equals(idExercice)
                && performance.getIndicateur() != null && performance.getIndicateur().getId().equals(idIndicateur)
                && performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && performance.isDeleted() != null && !performance.isDeleted())
            .map(performanceMapper::toDto).collect(Collectors.toList());*/

        return performanceRepository.findPerformancesByExerciceIdAndIndicateurIdAndDeletedIsFalse(idExercice, idIndicateur).stream()
            .filter(performance -> performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR))
            .map(performanceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * @param idExercice
     * @return
     */
    public List<PerformanceDTO> findAllByExercice(Long idExercice) {
        return performanceRepository.findPerformancesByExerciceIdAndDeletedIsFalse(idExercice).stream()
            .filter(performance -> performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR))
            .map(performanceMapper::toDto).collect(Collectors.toList());
    }

    /**
     * @param idCommune
     * @param idExercice
     * @return
     */
    public List<PerformanceDTO> findAllPerformanceBycommuneAndExercice(Long idCommune, Long idExercice) {
        log.debug("Request to get performace by commune and exercice without dto ");
        /*return performanceRepository.findAll().stream()
            .filter(performance -> performance.getCommune() != null && performance.getCommune().getId().equals(idCommune)
                && performance.getExercice() != null && performance.getExercice().getId().equals(idExercice)
                && performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && performance.isDeleted() != null && !performance.isDeleted())
            .map(performanceMapper::toDto)
            .collect(Collectors.toList());*/

        return performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(idCommune, idExercice).stream()
            .filter(performance -> performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR))
            .map(performanceMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * @param idCommune
     * @param idExercice
     * @param idDomaine
     * @return
     */
    public Double getScoreOfComuneAndExercice(Long idCommune, Long idExercice, Long idDomaine) {
        /*return performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(idCommune, idExercice)
            .stream().filter(val -> val.getCommune() != null && val.getCommune().getId().equals(idCommune)
                && val.getExercice() != null && val.getExercice().getId().equals(idExercice)
                && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && val.getIndicateur() != null && val.getIndicateur().getDomaine() != null
                && val.getIndicateur().getDomaine().getId().equals(idDomaine) && val.isDeleted() != null && !val.isDeleted())
            .filter(performance -> performance.getScore()!=null).map(Performance::getScore).mapToDouble(Double::doubleValue).sum();*/

        return performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(idCommune, idExercice)
            .stream().filter(val -> val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && val.getIndicateur() != null && val.getIndicateur().getDomaine() != null
                && val.getIndicateur().getDomaine().getId().equals(idDomaine))
            .filter(performance -> performance.getScore()!=null).map(Performance::getScore).mapToDouble(Double::doubleValue).sum();
    }

    /**
     * Calcul le score des indicateur par commune et par exercice
     *
     * @param idCommune
     * @param idExercice
     * @param idDomaine
     * @return
     */
    public List<IndicateurDTO> findAllIndicateurByDomaineWithScore(Long idCommune, Long idExercice, Long idDomaine) {
        List<IndicateurDTO> indicateurDTOList = new ArrayList<>();
        List<IndicateurDTO> indicateurs = getIndicateurByDomaine(idDomaine);
        for (IndicateurDTO indicateur : indicateurs) {
            Optional<Performance> performance = performanceRepository.findTopByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(idCommune, idExercice, indicateur.getId())
                .filter(val -> val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.INDICATEUR));
            if (performance.isPresent()) {
                if(performance.get().getScore()!=null) {
                    indicateur.setSroreIndicateur(performance.get().getScore());
                } else {
                    indicateur.setSroreIndicateur(null);
                }
            } else {
                indicateur.setSroreIndicateur(null);
            }
            if (!indicateur.getSousIndicateur()) {
                ValeurModalite valeurModalite = valeurModaliteRepository.findTop1ByIndicateurIdAndExerciceIdAndCommuneId(indicateur.getId(), idExercice, idCommune);
                log.debug("====valeur indicateur {}=========", valeurModalite);
                if (valeurModalite != null) {
                    if(valeurModalite.getValeur() != null && valeurModalite.getValeur().length()>0){
                        log.debug("====valeur indicateur valeur length {}=========", valeurModalite.getValeur().length());
                        indicateur.setValeurIndicateur(Double.parseDouble(valeurModalite.getValeur().replaceAll(",", ".")));
                    } else {
                        indicateur.setValeurIndicateur(null);
                    }
                }
            } else {
                List<ValeurModaliteDTO> valeurModaliteDTOS = valeurModaliteService.findModalitebyCommune(idExercice, idCommune).stream().filter(val -> val.getModalite() != null
                    && val.getModalite().getIndicateur().getId().equals(indicateur.getId()))
                    .sorted(Comparator.comparing(valeurModaliteDTO -> valeurModaliteDTO.getModalite().getCode()))
                    .collect(Collectors.toList());
                indicateur.setValeurmodalites(valeurModaliteDTOS);
            }
            // liste des modalites et desb indicateurs
            // indicateur.setModalites(modaliteService.getModaliteByIndicateur(indicateur.getId()));
            indicateurDTOList.add(indicateur);
        }

        return indicateurDTOList;
    }

    public List<IndicateurDTO> getIndicateurByDomaine(Long domId) {
        /*return indicateurRepository.findAll().stream()
            .filter(indicateur -> indicateur.getDomaine() != null && indicateur.getDomaine().getId().equals(domId)
                && indicateur.isDeleted() != null && !indicateur.isDeleted())
            .sorted(Comparator.comparing(Indicateur::getLibelle))
            .map(indicateurMapper::toDto).collect(Collectors.toList());*/

        return indicateurRepository.findAllByDomaineIdAndDeletedIsFalse(domId).stream().sorted(Comparator.comparing(Indicateur::getLibelle))
            .map(indicateurMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Pour recuperer le score d'un d'un indicateur de la commune par exercice
     *
     * @param idCommune
     * @param idAnnee
     * @param idIndicateur
     * @return
     */
    public Optional<Performance> getPerformanceByExercice(Long idCommune, Long idAnnee, Long idIndicateur) {
        /*return performanceRepository.findAll().stream()
            .filter(performance -> performance.getExercice() != null && performance.getExercice().getId().equals(idAnnee)
                && performance.getCommune() != null && performance.getCommune().getId().equals(idCommune)
                && performance.getTypePerformance() != null && performance.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && performance.getId() != null && performance.getIndicateur().getId().equals(idIndicateur)
                && !performance.isDeleted()).findFirst();*/

       return performanceRepository.findTop1ByDeletedIsFalseAndCommuneIdAndExerciceIdAndIndicateurIdAndTypePerformance(idCommune, idAnnee, idIndicateur, TypePerformance.INDICATEUR);
    }

    /**
     * Pour calculer le score de la commune par indicateur d'une commune
     *
     * @param idExercice
     * @param idIndicateur
     * @return
     */
    public List<CommunesDTO> scoreCommunesByIndicateur(Long idExercice, Long idIndicateur) {
        log.debug("Request to get all communes by Indicateur");
        List<CommunesDTO> communeDTOS = etatCommuneRepository.findAllByExerciceId(idExercice).stream()
            .map(EtatCommune::getCommune)
            .map(communesMapper::toDto).collect(Collectors.toList());
        if (!communeDTOS.isEmpty()) {
            return communeDTOS.stream()
                .peek(communeDTO -> communeDTO.setScoreCommune(this.calculePerformanceByIndicateur(idExercice, communeDTO.getId(), idIndicateur)))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Pour calculer le score de la commune par domaine d'iun exercice
     *
     * @param idExercice
     * @param idDomaine
     * @return
     */
    public List<CommunesDTO> scoreCommuneByDomaine(Long idExercice, Long idDomaine) {
        log.debug("Request to get all communes by domaine");
        List<CommunesDTO> communeDTOS = etatCommuneRepository.findAllByExerciceId(idExercice).stream()
            .map(EtatCommune::getCommune).map(communesMapper::toDto).collect(Collectors.toList());
        if (!communeDTOS.isEmpty()) {
            communeDTOS.forEach(communeDTO -> communeDTO.setScoreCommune(this.calculePerformanceByDomaine(idExercice, communeDTO.getId(), idDomaine)));
            return communeDTOS;
        }
        return new ArrayList<>();
    }

    /**
     * Pour calculer le score de la commune par type indicateur d'un exercice
     *
     * @param idExercice
     * @param idTypeIndicateur
     * @return
     */
    public List<CommunesDTO> scoreCommunesBTypeIndicateur(Long idExercice, Long idTypeIndicateur) {
        log.debug("Request to get all communes by typeIndicateur");
        List<CommunesDTO> communeDTOS = etatCommuneRepository.findAllByExerciceId(idExercice).stream()
            .map(EtatCommune::getCommune).map(communesMapper::toDto).collect(Collectors.toList());
        if (!communeDTOS.isEmpty()) {
            return communeDTOS.stream()
                .peek(communeDTO -> communeDTO.setScoreCommune(this.calculePerformanceByTypedomaine(idExercice, communeDTO.getId(), idTypeIndicateur))
                ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Pour calculer le score global de la commune par exercice
     *
     * @param idExercice
     * @return
     */
    public List<CommunesDTO> scoreCommuneGlobal(Long idExercice) {
       return etatCommuneRepository.findAllByExerciceId(idExercice).stream()
           .map(EtatCommune::getCommune)
           .sorted(Comparator.comparing(Commune::getLibelle)).map(communesMapper::toDto)
            .peek(communeDTO -> communeDTO.setScoreCommune(this.calculePerformanceGlobal(idExercice, communeDTO.getId())))
            .collect(Collectors.toList());
    }

    /**
     * @param idExercice
     * @param idCommune
     * @param idDomaine
     * @return
     */
    public Double calculePerformanceByDomaine(Long idExercice, Long idCommune, Long idDomaine) {
       /* Optional<Performance> performance = performanceRepository.findAll().stream().filter(val -> val.getExercice() != null
            && val.getExercice().getId().equals(idExercice)
            && val.getDomaineId() != null && val.getDomaineId().equals(idDomaine)
            && val.getCommune() != null && val.getCommune().getId().equals(idCommune)
            && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.DOMAINE)
            && val.isDeleted() != null && val.getScore() != null &&
            !val.isDeleted()).findFirst();
        return performance.map(Performance::getScore).orElse(null);*/

        Optional<Performance> performance = performanceRepository
            .findAllByDeletedIsFalseAndExerciceIdAndCommuneIdAndDomaineIdAndTypePerformance(idExercice, idCommune, idDomaine, TypePerformance.DOMAINE).stream().filter(val ->
                val.isDeleted() != null && val.getScore() != null &&
            !val.isDeleted()).findFirst();
        return performance.map(Performance::getScore).orElse(null);
    }

    /**
     * @param idExercice
     * @param idCommune
     * @param idDomaine
     * @return
     */
    public Double getStarByDomaine(Long idExercice, Long idCommune, Long idDomaine) {
        /*Optional<Performance> performance = performanceRepository.findAll().stream().filter(val -> val.getExercice() != null
            && val.getExercice().getId().equals(idExercice)
            && val.getDomaineId() != null && val.getDomaineId().equals(idDomaine)
            && val.getCommune() != null && val.getCommune().getId().equals(idCommune)
            && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.DOMAINE)
            && val.isDeleted() != null && val.getScore() != null &&
            !val.isDeleted()).findFirst();
        return performance.map(Performance::getNbEtoile).orElse(null);*/

        Optional<Performance> performance = performanceRepository
            .findAllByDeletedIsFalseAndExerciceIdAndCommuneIdAndDomaineIdAndTypePerformance(idExercice, idCommune, idDomaine, TypePerformance.DOMAINE)
            .stream().filter(val -> val.getScore() != null).findFirst();
        return performance.map(Performance::getNbEtoile).orElse(null);
    }

    /**
     * @param idExercice
     * @param idCommune
     * @param idtypeIndicateur
     * @return
     */
    public Double calculePerformanceByTypedomaine(Long idExercice, Long idCommune, Long idtypeIndicateur) {
        /*Optional<Performance> performance = performanceRepository.findAll().stream().filter(val -> val.getExercice() != null
            && val.getExercice().getId().equals(idExercice) && val.getTypeDomainId() != null && val.getTypeDomainId().equals(idtypeIndicateur)
            && val.getCommune() != null && val.getCommune().getId().equals(idCommune)
            && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.COMMUNE)
            && val.isDeleted() != null && val.getScore() != null &&
            !val.isDeleted()).findFirst();
        return performance.map(Performance::getScore).orElse(null);*/

        Optional<Performance> performance = performanceRepository
            .findAllByDeletedIsFalseAndCommuneIdAndExerciceIdAndTypePerformance(idCommune, idExercice, TypePerformance.COMMUNE).stream().filter(val ->
            val.getTypeDomainId() != null && val.getTypeDomainId().equals(idtypeIndicateur)
                && val.getScore() != null ).findFirst();
        return performance.map(Performance::getScore).orElse(null);

    }

    /**
     * @param idExercice
     * @param idCommune
     * @param idIndicateur
     * @return
     */
    public Double calculePerformanceByIndicateur(Long idExercice, Long idCommune, Long idIndicateur) {
        /*List<Performance> performances = performanceRepository.findAll().stream().filter(val -> val.getExercice() != null
            && val.getExercice().getId().equals(idExercice)
            && val.getIndicateur() != null && val.getIndicateur().getId().equals(idIndicateur)
            && val.getCommune() != null && val.getCommune().getId().equals(idCommune)
            && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.INDICATEUR)
            && val.isDeleted() != null && val.getScore() != null &&
            !val.isDeleted()).collect(Collectors.toList());
        return performances.stream().map(Performance::getScore).mapToDouble(Double::doubleValue).sum();*/

        List<Performance> performances = performanceRepository
            .findAllByCommuneIdAndExerciceIdAndIndicateurIdAndDeletedIsFalse(idCommune, idExercice, idIndicateur)
            .stream().filter(val -> val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && val.getScore() != null).collect(Collectors.toList());
        return performances.stream().map(Performance::getScore).mapToDouble(Double::doubleValue).sum();

    }

    /**
     * @param idExercice
     * @param idCommune
     * @return
     */
    public Double calculePerformanceGlobal(Long idExercice, Long idCommune) {
        return performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(idCommune, idExercice)
            .stream().filter(val ->
            val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.COMMUNE)
                && val.getScore() != null).map(Performance::getScore).mapToDouble(Double::doubleValue).sum();
    }

    /**
     * @param idExercice
     * @param idCommune
     * @param typeDomaineId
     * @return
     */
    public Double getStartGlobalCommune(Long idExercice, Long idCommune, Long typeDomaineId) {
        return performanceRepository.findAllByCommuneIdAndExerciceIdAndDeletedIsFalse(idCommune, idExercice)
            .stream().filter(val ->
                val.getTypeDomainId() != null && val.getTypeDomainId().equals(typeDomaineId)
            && val.getTypePerformance() != null && val.getTypePerformance().equals(TypePerformance.COMMUNE)
                    && val.getScore() != null).map(Performance::getNbEtoile).mapToDouble(Double::doubleValue).sum();
    }

    /**
     * @param idCommune
     * @param idExercice
     * @param indicateurId
     * @return
     */
    public Integer getRangNational(Long idCommune, Long idExercice, Long indicateurId) {
        List<Performance> performances = performanceRepository.findPerformancesByExerciceIdAndDeletedIsFalse(idExercice)
            .stream().filter(value -> value.getTypePerformance().equals(TypePerformance.INDICATEUR)
                && value.getIndicateur() != null && value.getIndicateur().getId().equals(indicateurId)
                && value.getScore() != null)
            .sorted(Comparator.comparing(Performance::getScore).reversed()).collect(Collectors.toList());
        int index = 0;
        for (Performance performance : performances) {
            if (performance.getCommune() != null && performance.getCommune().getId().equals(idCommune)) {
                index = performances.indexOf(performance);
            }
        }
        return index + 1;
    }

    /**
     * @param idCommune
     * @param idExercice
     * @param indicateurId
     * @return
     */
    public Integer getRangRegional(Long idCommune, Long idExercice, Long indicateurId) {
        Commune commune = communeRepository.getOne(idCommune);
        List<Performance> performances = performanceRepository.
            findPerformancesByExerciceIdAndDeletedIsFalse(idExercice).stream().filter(value -> value.getTypePerformance().equals(TypePerformance.INDICATEUR)
            && value.getIndicateur() != null && value.getIndicateur().getId().equals(indicateurId)
            && value.getCommune() != null && value.getCommune().getProvince().getRegion().getId().equals(commune.getProvince().getRegion().getId()) &&
            value.getScore() != null)
            .sorted(Comparator.comparing(Performance::getScore).reversed()).collect(Collectors.toList());
        int index = 0;
        for (Performance performance : performances) {
            if (performance.getCommune() != null && performance.getCommune().getId().equals(idCommune)) {
                index = performances.indexOf(performance);
            }
        }
        return index + 1;
    }

    /**
     * @param exercices
     * @return
     */
    public List<CommunesDTO> allDataCommuneForAllYear(List<Exercice> exercices) {
        List<CommunesDTO> mesCommunes = new ArrayList<>();
        List<CommunesDTO> communeDTOS = communeRepository.findAllByDeletedIsFalse().stream().map(communesMapper::toDto).collect(Collectors.toList());
        if (!communeDTOS.isEmpty()) {
            for (CommunesDTO commune : communeDTOS) {
                commune.setScores(this.getExerciceValExerciceOfAllData(commune, exercices, null, 1));
                mesCommunes.add(commune);
            }
        }
        return mesCommunes;
    }

    /**
     * La fonction pour ecrire dans le fichier excel de l'anneé 0 à l'année n+1
     *
     * @param commune
     * @param exercices
     * @param idIndicateur
     * @param type
     * @return
     */
    public List<Score> getExerciceValExerciceOfAllData(CommunesDTO commune, List<Exercice> exercices, Long idIndicateur, int type) {
        List<Score> valeurs = new ArrayList<>();
        Exercice exercice1 = exercices.get(0);
        Exercice exercice2 = exercices.get(exercices.size() - 1);
        switch (type) {
            case 1:
                for (int i = exercice1.getAnnee(); i <= exercice2.getAnnee(); i++) {
                    Exercice exercice = exerciceRepository.findExerciceByAnnee(i);
                    Score score = new Score();
                    score.setAnnee(exercice.getAnnee());
                    score.setScore(this.calculePerformanceGlobal(exercice.getId(), commune.getId()));
                    valeurs.add(score);
                }
                return valeurs.stream().sorted(Comparator.comparing(Score::getAnnee)).collect(Collectors.toList());
            case 2:
                for (int i = exercice1.getAnnee(); i <= exercice2.getAnnee(); i++) {
                    Exercice exercice = exerciceRepository.findExerciceByAnnee(i);
                    Score score = new Score();
                    score.setAnnee(exercice.getAnnee());
                    score.setScore(this.calculePerformanceByIndicateur(exercice.getId(), commune.getId(), idIndicateur));
                    valeurs.add(score);
                }
                return valeurs.stream().sorted(Comparator.comparing(Score::getAnnee)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * @param idIndicateur
     * @param exercices
     * @return
     */
    public List<CommunesDTO> allDataCommuneForAllYearOfIndicateur(Long idIndicateur, List<Exercice> exercices) {
        List<CommunesDTO> mesCommunes = new ArrayList<>();
        List<CommunesDTO> communeDTOS = communeRepository.findAllByDeletedIsFalse().stream().map(communesMapper::toDto).collect(Collectors.toList());
        if (!communeDTOS.isEmpty()) {
            for (CommunesDTO commune : communeDTOS) {
                commune.setScores(this.getExerciceValExerciceOfAllData(commune, exercices, idIndicateur, 2));
                mesCommunes.add(commune);
            }
        }
        return mesCommunes;
    }

    public Optional<PerformanceDTO> updatePerformance(Long id, Double valeur, Boolean score) {
        Optional<Performance> performance = performanceRepository.findById(id);
        if (performance.isPresent()) {
            if (score) {
                performance.get().setScore(valeur);
            } else {
                performance.get().setNbEtoile(valeur);
            }

            return Optional.of(performanceRepository.save(performance.get())).map(performanceMapper::toDto);
        }
        return Optional.empty();
    }

    public Optional<ValeurModaliteDTO> updateModalite(Long id, String valeur) {
        ValeurModalite valeurModalite = valeurModaliteRepository.getOne(id);
        valeurModalite.setValeur(valeur);

        return Optional.of(valeurModaliteRepository.save(valeurModalite)).map(valeurModaliteMapper::toDto);
    }

    /**
     * Validate {@link Commune} data
     *
     * @param data
     * @param anneeId
     * @param typeId
     * @return
     */
    public boolean validateCommuneData(CommuneData data, Long anneeId, Long typeId) {

        for (int i = 0; i < data.getCols().size(); i++) {
            Col col = data.getCols().get(0);
            if (col.getTypeObjet().equals(ObjectType.valeurModalite)) {
                ValeurModalite valeurModalite = valeurModaliteRepository.getOne(col.getId());
                valeurModalite.setValeur(col.getValue());
                valeurModaliteRepository.saveAndFlush(valeurModalite);
            } else if (col.getTypeObjet().equals(ObjectType.performance)) {
                Performance performance = performanceRepository.getOne(col.getId());
                if (col.getUnite().equals(ObjectType.etoiles)) {
                    performance.setNbEtoile(Double.parseDouble(col.getValue()));
                } else {
                    performance.setScore(Double.parseDouble(col.getValue()));
                }
                performanceRepository.saveAndFlush(performance);
            }
        }

        return validationCommuneRepository.save(createValidationCommune(anneeId, typeId, data.getId())).isValidated();
    }

    private ValidationCommune createValidationCommune(Long anneeId, Long typeId, Long comId) {
        Commune commune = communeRepository.getOne(comId);
        Exercice exercice = exerciceRepository.getOne(anneeId);

        ValidationCommune validationCommune = new ValidationCommune();
        validationCommune.setCommune(commune);
        validationCommune.setExercice(exercice);
        validationCommune.setTypeDomaineId(typeId);
        validationCommune.validated(true);
        exercice.setValidated(true);
        exerciceRepository.save(exercice);

        return validationCommune;
    }


    /**
     * @param anneeId
     * @param typeId
     * @return
     */
    public Boolean validateAllCommuneData(Long anneeId, Long typeId) {
        boolean result = false;
        List<EtatCommune> etatCommunes = etatCommuneRepository.findAllByExerciceId(anneeId).stream().filter(etatCommune -> !etatCommune.getCommune().isDeleted()).collect(Collectors.toList());
        for (EtatCommune etatCommune : etatCommunes) {
            Long comid = etatCommune.getCommune().getId();
            if (!validationCommuneRepository.existsByCommuneIdAndTypeDomaineIdAndExerciceId(comid, typeId, anneeId)) {
                result = validationCommuneRepository.save(createValidationCommune(anneeId, typeId, comid)).isValidated();
            }
        }
        return result;
    }

    public Boolean checkIfExistValidatedData(Long anneeId, Long typeId) {
        List<EtatCommune> etatCommunes = etatCommuneRepository.findAllByExerciceId(anneeId).stream().filter(etatCommune -> !etatCommune.getCommune().isDeleted()).collect(Collectors.toList());
        int check = 0;
        for (EtatCommune etatCommune : etatCommunes) {
            if (validationCommuneRepository.existsByCommuneIdAndTypeDomaineIdAndExerciceId(etatCommune.getCommune().getId(), typeId, anneeId)) {
                check += 1;
            }
        }

        return (check != 0 && check == etatCommunes.size());
    }

    public void viderDataImporterByExerciceAnTypDomaine(Long exerciceId, Long typeIndic) {
        TypeIndicateur typeIndicateur = typeIndicateurRepository.getOne(typeIndic);
        Exercice exercice = exerciceRepository.getOne(exerciceId);
        List<ValeurModaliteDTO> valeurModalites = new ArrayList<>();
        List<Performance> performances = performanceRepository.findAllByExerciceIdAndTypeDomainIdAndDeletedIsFalse(exercice.getId(), typeIndicateur.getId());
        List<Domaine> domaines = domaineRepository.findAllByTypeIndicateurIdAndDeletedIsFalse(typeIndicateur.getId());
        if(!domaines.isEmpty()) {
            domaines.forEach(domaine -> etatCommuneRepository.findAllByExerciceId(exercice.getId())
                    .forEach(etatCommune ->
                        valeurModalites.addAll(getValeurModalites(etatCommune.getCommune(), exercice.getId(), domaine)))
            );
        }

        if (!performances.isEmpty() && !valeurModalites.isEmpty()) {
            valeurModaliteRepository.deleteAll(valeurModalites.stream().map(valeurModaliteMapper::toEntity).collect(Collectors.toList()));
            performanceRepository.deleteAll(performances);
        }
    }

    public void devaliderDonnees(Long exerciceId, Long typeIndic) {
        TypeIndicateur typeIndicateur = typeIndicateurRepository.getOne(typeIndic);
        Exercice exercice = exerciceRepository.getOne(exerciceId);
        List<ValidationCommune> validationCommunes;
        if (exercice.getId()!=null) {
            validationCommunes = validationCommuneRepository.findAllByTypeDomaineIdAndExerciceId(typeIndicateur.getId(), exercice.getId());
            if(!validationCommunes.isEmpty()) {
                validationCommuneRepository.deleteAll(validationCommunes);
                if(exercice.getValidated()) {
                    exercice.setValidated(false);
                    exerciceRepository.save(exercice);
                }
            }
        }
    }
}
