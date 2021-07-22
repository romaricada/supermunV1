package bf.gov.anptic.service;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.domain.enumeration.DataFilter;
import bf.gov.anptic.repository.*;
import bf.gov.anptic.service.dto.CommuneDTO;
import bf.gov.anptic.service.dto.CommunesDTO;
import bf.gov.anptic.service.mapper.CommuneMapper;
import bf.gov.anptic.service.mapper.CommunesMapper;
import bf.gov.anptic.util.Constante;
import bf.gov.anptic.util.PrintDocumentService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneService.class);

    private final CommuneRepository communeRepository;
    private final DomaineRepository domaineRepository;
    private final ApplicationContext applicationContext;
    private final CommuneMapper communeMapper;
    private final CommunesMapper communesMapper;

    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private PrintDocumentService printDocumentService;
    @Autowired
    private ExerciceRepository exerciceRepository;
    @Autowired
    private TypeIndicateurRepository typeIndicateurRepository;
    @Autowired
    private IndicateurRepository indicateurRepository;
    @Autowired
    private EtatCommuneRepository etatCommuneRepository;

    public CommuneService(
        CommuneRepository communeRepository,
        CommuneMapper communeMapper,
        DomaineRepository domaineRepository,
        ApplicationContext applicationContext,
        CommunesMapper communesMapper
    ) {
        this.communeRepository = communeRepository;
        this.communeMapper = communeMapper;
        this.domaineRepository = domaineRepository;
        this.applicationContext = applicationContext;
        this.communesMapper = communesMapper;
    }

    /**
     * Save a commune.
     *
     * @param communeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommunesDTO save(CommunesDTO communeDTO) {
        log.debug("Request to save Commune : {}", communeDTO);
        Commune commune = communesMapper.toEntity(communeDTO);
        commune = communeRepository.save(commune);
        return communesMapper.toDto(commune);
    }

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommunesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
       /* List<CommuneDTO>communes = communeRepository.findAll().stream().filter(val-> !val.isDeleted()).map(communeMapper::toDto).collect(Collectors.toList());
        Page<CommuneDTO> page = new PageImpl<>(communes,pageable, communes.size());
        return page;*/
         return communeRepository.findCommunesByDeletedIsFalse(pageable).map(communesMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CommunesDTO> findAllCommune(Long anneeId, String typReturn) {
        log.debug("Request to get all communes no page");
        List<CommunesDTO> communes = new ArrayList<>();
        switch (typReturn) {
            case "all" :  communes = communeRepository.findAll().stream()
                .sorted(Comparator.comparing(Commune::getLibelle)).map(communesMapper::toDto)
                .collect(Collectors.toList()); break;

            case "byAnnee" :  communes = etatCommuneRepository.findAll().stream().filter(etatCommune -> etatCommune.getExercice().getId().equals(anneeId))
                .map(EtatCommune::getCommune)
                .sorted(Comparator.comparing(Commune::getLibelle)).map(communesMapper::toDto)
                .collect(Collectors.toList()); break;

        }
        /*return etatCommuneRepository.findAll().stream().filter(etatCommune -> etatCommune.getExercice().getId().equals(anneeId))
            .map(EtatCommune::getCommune)
            .sorted(Comparator.comparing(Commune::getLibelle)).map(communesMapper::toDto)
            .collect(Collectors.toList());*/
        return communes;
    }

    @Transactional(readOnly = true)
    public List<CommunesDTO> findAllCommuneByProvince(Long idProvince, Long anneeId) {
        log.debug("Request to get all communes by province");
        return etatCommuneRepository.findAll().stream().filter(etatCommune -> etatCommune.getExercice().getId().equals(anneeId))
            .map(EtatCommune::getCommune)
            .filter(commune -> commune.getProvince() != null && commune.getProvince().getId().equals(idProvince)
                && commune.isDeleted() != null && !commune.isDeleted())
            .sorted(Comparator.comparing(Commune::getLibelle)).map(communesMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get one commune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommunesDTO> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findById(id).map(communesMapper::toDto);
    }

    /**
     * Delete the commune by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.deleteById(id);
    }

    public Long deleteAll(List<CommuneDTO> communesDTO) {
        log.debug("Request to delete all Communes : {}", communesDTO);
        List<Commune> communes = communeMapper.toEntity(communesDTO);
        if (!communes.isEmpty()) {
            communeRepository.saveAll(communes);
        }
        return Long.parseLong("" + communes.size());
    }

    /**
     * @return
     */
    public Object getGeoJsonData() {
        return communeRepository.getAllConvertToGeoJSON();
    }

    public List<CommunesDTO> classementCommune(Long idExercice, DataFilter dataFilter, Long id) {
        log.debug("methode pour filtrer les communes par performormance selon les parametres fournis");
        List<CommunesDTO> communeDTOS = new ArrayList<>();
        switch (dataFilter) {
            case GENERAL:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommuneGlobal(idExercice));
                break;
            case DOMAINE:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommuneByDomaine(idExercice, id));
                break;
            case INDICATEUR:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommunesByIndicateur(idExercice, id));
                break;
            case TYPEINDICATEUR:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommunesBTypeIndicateur(idExercice, id));
                break;
        }
        List<CommunesDTO> listOrdonne = new ArrayList<>();
        List<CommunesDTO> communeAvecScore = communeDTOS.stream().filter(val -> val.getScoreCommune() != null).sorted(Comparator.comparing(CommunesDTO::getScoreCommune).reversed())
            .collect(Collectors.toList());

        listOrdonne.addAll(communeAvecScore);
        List<CommunesDTO> communeSansScore = communeDTOS.stream().filter(val -> val.getScoreCommune() == null)
            .collect(Collectors.toList());

        listOrdonne.addAll(communeSansScore);

        return listOrdonne;
    }

    /****** reordonner la liste with en prenant compte les execo  *******/
    private List<CommunesDTO> classerListeCommuneWithExeco(List<CommunesDTO> mesCommunes) {
        List<CommunesDTO> comms;
        List<CommunesDTO> comms1;
        if(!mesCommunes.isEmpty()) {
            comms1 = mesCommunes.stream().sorted(Comparator.comparing(CommunesDTO::getScoreCommune).reversed()).collect(Collectors.toList());
            comms =  comms1.stream().peek(communeDTO -> {

                int index2 = comms1.indexOf(communeDTO);
                if(index2 == 0) {
                    communeDTO.setRangNational(index2+1);
                } else {
                    if(comms1.get(index2-1).getScoreCommune().equals(communeDTO.getScoreCommune())) {
                        communeDTO.setRangNational(comms1.get(index2-1).getRangNational());
                    } else {
                        communeDTO.setRangNational(index2+1);
                    }
                }
            }).collect(Collectors.toList());
             return comms;
        } else {
            return null;
        }
    }

    public List<CommunesDTO> classementTop5(Long idExercice, Long idIndicateur) {
        return classementCommune(idExercice,DataFilter.INDICATEUR,idIndicateur).stream().limit(5).collect(Collectors.toList());
    }

    public ResponseEntity<byte[]> getToPrintClassement(Long idExercice, DataFilter dataFilter, Long id) {
        log.debug("Request to print classemnt commune");
        List<CommunesDTO> communeDTOS = new ArrayList<>();
        Exercice exercice = exerciceRepository.getOne(idExercice);
        String typeFiltre = null;
        switch (dataFilter) {
            case GENERAL:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommuneGlobal(idExercice));
                typeFiltre = "Classement général des communes année " + exercice.getAnnee();
                break;
            case DOMAINE:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommuneByDomaine(idExercice, id));
                Domaine domaine = domaineRepository.getOne(id);
                typeFiltre = "Classement des communes par dommaine annéé " + exercice.getAnnee() + " : " + domaine.getLibelle();
                break;
            case INDICATEUR:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommunesByIndicateur(idExercice, id));
                Indicateur indicateur = indicateurRepository.getOne(id);
                typeFiltre = "Classement des communes par indicateur annéé " + exercice.getAnnee() + " : " + indicateur.getLibelle();
                break;
            case TYPEINDICATEUR:
                communeDTOS = classerListeCommuneWithExeco(performanceService.scoreCommunesBTypeIndicateur(idExercice, id));
                TypeIndicateur typeIndicateur = typeIndicateurRepository.getOne(id);
                typeFiltre = "Classement des communes par type d'indicateur annéé " + exercice.getAnnee() + " : " + typeIndicateur.getLibelle();
                break;
        }
        List<CommunesDTO> listOrdonne = new ArrayList<>();
        List<CommunesDTO> communeAvecScore = communeDTOS.stream().filter(val -> val.getScoreCommune() != null).sorted(Comparator.comparing(CommunesDTO::getScoreCommune).reversed())
            .collect(Collectors.toList());

        listOrdonne.addAll(communeAvecScore);
        List<CommunesDTO> communeSansScore = communeDTOS.stream().filter(val -> val.getScoreCommune() == null)
            .collect(Collectors.toList());

        listOrdonne.addAll(communeSansScore);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listOrdonne);
        Resource resource = applicationContext.getResource(Constante.CLASS_PATH_REPORT + "classement.jasper");
        HashMap<String, Object> parametres = new HashMap<>();
        parametres.put("FILTRE", typeFiltre);


        try {
            return printDocumentService.ImprimerPDF(resource.getInputStream(), dataSource, parametres);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public Page<CommunesDTO> getAllNotUsedForCurrentYear(Long anneeId, Pageable pageable) {
        List<CommunesDTO> communes = communeRepository.findAllByDeletedIsFalse().stream().filter(commune -> {
            EtatCommune etatCommune = etatCommuneRepository.findTop1ByExerciceIdAndCommuneId(anneeId, commune.getId());
            return etatCommune == null;
        }).map(communesMapper::toDto).collect(Collectors.toList());

        return new PageImpl<>(communes, pageable,communes.size());
    }

    public boolean updateCommuneForCurrentYear(List<CommunesDTO> communeDTOS, Long anneeId, boolean checked) {
        List<EtatCommune> etatCommunes = new ArrayList<>();
        List<Commune> communes;
        if (checked) {
            communes = communeRepository.findAllByDeletedIsFalse().stream()
                .filter(commune -> !etatCommuneRepository.existsByExerciceIdAndAndCommuneId(anneeId, commune.getId())).collect(Collectors.toList());
        } else {
            communes = communeDTOS.stream().map(communesMapper::toEntity).collect(Collectors.toList());
        }
        Exercice exercice = exerciceRepository.getOne(anneeId);
        communes.forEach(commune -> {
            EtatCommune etatCommune = new EtatCommune();
            etatCommune.setPriseEnCompte(true);
            etatCommune.setExercice(exercice);
            etatCommune.setPublication(false);
            etatCommune.setCommune(commune);
            etatCommunes.add(etatCommune);
        });

        return !etatCommuneRepository.saveAll(etatCommunes).isEmpty();
    }
}
