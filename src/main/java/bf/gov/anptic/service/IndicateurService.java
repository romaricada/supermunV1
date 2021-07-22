package bf.gov.anptic.service;

import bf.gov.anptic.domain.Exercice;
import bf.gov.anptic.domain.Indicateur;
import bf.gov.anptic.domain.Modalite;
import bf.gov.anptic.domain.Performance;
import bf.gov.anptic.domain.enumeration.DataFilter;
import bf.gov.anptic.repository.ExerciceRepository;
import bf.gov.anptic.repository.IndicateurRepository;
import bf.gov.anptic.repository.ModaliteRepository;
import bf.gov.anptic.service.dto.IndicateurDTO;
import bf.gov.anptic.service.dto.IndicateursDTO;
import bf.gov.anptic.service.dto.ModaliteDTO;
import bf.gov.anptic.service.mapper.IndicateurMapper;
import bf.gov.anptic.service.mapper.IndicateursMapper;
import bf.gov.anptic.service.mapper.ModaliteMapper;
import bf.gov.anptic.util.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Indicateur}.
 */
@Service
@Transactional
public class IndicateurService {

    private final Logger log = LoggerFactory.getLogger(IndicateurService.class);

    private final IndicateurRepository indicateurRepository;
    private final ModaliteRepository modaliteRepository;
    private final ModaliteService modaliteService;

    private final IndicateurMapper indicateurMapper;

    private final IndicateursMapper indicateursMapper;

    private final ModaliteMapper modaliteMapper;
    @Autowired
    ExerciceRepository exerciceRepository;

    @Autowired
    PerformanceService performanceService;

    public IndicateurService(
        IndicateurRepository indicateurRepository,
        ModaliteRepository modaliteRepository,
        IndicateurMapper indicateurMapper,
        IndicateursMapper indicateursMapper,
        ModaliteMapper modaliteMapper,
        ModaliteService modaliteService
    ) {
        this.indicateurRepository = indicateurRepository;
        this.modaliteRepository = modaliteRepository;
        this.indicateurMapper = indicateurMapper;
        this.indicateursMapper = indicateursMapper;
        this.modaliteMapper = modaliteMapper;
        this.modaliteService = modaliteService;
    }

    /*
     * Save a indicateur.
     *
     * @param indicateurDTO the entity to save.
     * @return the persisted entity.
     */
    public IndicateurDTO save(IndicateurDTO indicateurDTO) {
        log.debug("Request to save Indicateur : {}", indicateurDTO);
        Indicateur indicateur = indicateurMapper.toEntity(indicateurDTO);

        List<Modalite> modalites = modaliteMapper.toEntity(new ArrayList<>(indicateurDTO.getModalites()));

        indicateur = indicateurRepository.save(indicateur);

        if (indicateurDTO.getId() != null) {
            List<Modalite> modaliteListexist = modaliteService.findAllModaliteByIndicateur(indicateurDTO.getId());
            if (modalites.isEmpty() && !modaliteListexist.isEmpty()) {
                modaliteRepository.deleteAll(modaliteListexist);
            } else {
                int i = 0;
                for (Modalite modalite : modalites) {
                    if (modalite.getId() != null) {
                        for (Modalite val : modaliteListexist) {
                            if (modalite.getId().equals(val.getId())) {
                                i++;
                            } else {
                                Modalite delElement = modaliteListexist.get(i);
                                if (delElement != null) {
                                    modaliteRepository.delete(delElement);
                                }
                            }
                        }

                    }
                }
            }
        }
        if (!modalites.isEmpty()) {
            Indicateur finalIndicateur = indicateur;
            modalites.forEach(value -> value.setIndicateur(finalIndicateur));
            modaliteRepository.saveAll(modalites);
        }

        return indicateurMapper.toDto(indicateur);

    }

    /**
     * Get all the indicateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndicateurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Indicateurs");
        List<IndicateurDTO> indicateurs = indicateurRepository.findAll().stream().filter(val -> !val.isDeleted())
            .sorted(Comparator.comparing(Indicateur::getLibelle)).map(indicateurMapper::toDto).collect(Collectors.toList());
        Page<IndicateurDTO> page = new PageImpl<>(indicateurs, pageable, indicateurs.size());
        return page;
    }

    @Transactional(readOnly = true)
    public List<IndicateursDTO> findAllIndicateur() {
        log.debug("Request to get all indicateurs no page");
        return indicateurRepository.findAll().stream()
            .sorted(Comparator.comparing(Indicateur::getLibelle)).filter(val -> val.isDeleted() != null && !val.isDeleted()).map(indicateursMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IndicateursDTO> findAllIndicateurByDomaine(Long idDomaine) {
        log.debug("Request to get all indicateurs by domaine");
        return indicateurRepository.findAll().stream()
            .filter(indicateur -> indicateur.getDomaine() != null && indicateur.getDomaine().getId().equals(idDomaine)
                && indicateur.isDeleted() != null && !indicateur.isDeleted())
            .sorted(Comparator.comparing(Indicateur::getLibelle))
            .map(indicateursMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one indicateur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndicateurDTO> findOne(Long id) {
        log.debug("Request to get Indicateur : {}", id);
        List<ModaliteDTO> modalites = modaliteService.findAllModaliteByIndicateur(id).
            stream().map(modaliteMapper::toDto).collect(Collectors.toList());
        Optional<IndicateurDTO> indicateur = indicateurRepository.findById(id).map(indicateurMapper::toDto);
        if (!modalites.isEmpty()) {
            indicateur.get().setModalites(modalites);
        }
        return indicateur;
    }

    /**
     * Delete the indicateur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Indicateur : {}", id);
        indicateurRepository.deleteById(id);
    }

    public Long deleteAll(List<IndicateurDTO> indcateursDTO) {
        log.debug("Request to delete Indicateur : {}", indcateursDTO);
        List<Indicateur> indicateurs = indicateurMapper.toEntity(indcateursDTO);
        if (!indicateurs.isEmpty()) {
            indicateurRepository.saveAll(indicateurs);
        }
        return Long.parseLong("" + indicateurs.size());
    }

    public List<IndicateursDTO> getScoreBetweenYear(Long idCommune, Long idAnne1, Long idAnne2) {
        log.debug("get score between to year");
        Optional<Exercice> exercice1 = exerciceRepository.findById(idAnne1);
        Optional<Exercice> exercice2 = exerciceRepository.findById(idAnne2);

        log.debug("============== annee1 {}= annee2 {}=================", idAnne1, idAnne2);
        List<IndicateursDTO> indicateurDTOS = new ArrayList<>();

        List<IndicateursDTO> indicateurs = indicateurRepository.findAll().stream().map(indicateursMapper::toDto)
            .filter(value -> value.isDeleted() != null && !value.isDeleted())
            .collect(Collectors.toList());

        if (exercice1.isPresent() && exercice2.isPresent() && exercice2.get().getAnnee() > exercice1.get().getAnnee()) {

            indicateurDTOS.addAll(this.calculeScoreByExercice(idCommune, exercice1.get(), exercice2.get(), indicateurs));

        } else if (exercice1.isPresent() && exercice2.isPresent() && exercice1.get().getId().equals(exercice2.get().getAnnee())) {
            // TO A SAME YEAR
            log.debug("============== condition =================");
            indicateurDTOS.addAll(this.calculeScoreByOneExercice(idCommune, exercice1.get(), indicateurs));

        } else if (!exercice1.isPresent() && exercice2.isPresent()) {

            List<Exercice> exercices = exerciceRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted())
                .sorted(Comparator.comparing(Exercice::getAnnee))
                .collect(Collectors.toList());
            if (!exercices.isEmpty()) {
                Exercice annee1 = exercices.get(0);
                indicateurDTOS.addAll(this.calculeScoreByExercice(idCommune, annee1, exercice2.get(), indicateurs));
            }
        } else if (exercice1.isPresent() && !exercice2.isPresent()) {

            List<Exercice> exercices = exerciceRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted())
                .sorted(Comparator.comparing(Exercice::getAnnee))
                .collect(Collectors.toList());
            if (!exercices.isEmpty()) {
                Exercice annee2 = exercices.get(exercices.size() - 1);
                indicateurDTOS.addAll(this.calculeScoreByExercice(idCommune, exercice1.get(), annee2, indicateurs));
            }

        } else if (!exercice1.isPresent() && !exercice2.isPresent()) {

            List<Exercice> exercices = exerciceRepository.findAll().stream().filter(val -> val.isDeleted() != null && !val.isDeleted())
                .sorted(Comparator.comparing(Exercice::getAnnee))
                .collect(Collectors.toList());
            if (!exercices.isEmpty()) {
                Exercice annee1 = exercices.get(0);
                Exercice annee2 = exercices.get(exercices.size() - 1);
                indicateurDTOS.addAll(this.calculeScoreByExercice(idCommune, annee1, annee2, indicateurs));
            }
        } else {
            log.debug("==============  =================");
            indicateurDTOS.addAll(this.calculeScoreByOneExercice(idCommune, exercice1.get(), indicateurs));
        }
        return indicateurDTOS;
    }

    public List<IndicateursDTO> calculeScoreByExercice(Long idCommune, Exercice exercice1, Exercice exercice2, List<IndicateursDTO> indicateurs) {
        List<IndicateursDTO> indicateurDTOS = new ArrayList<>();
        for (IndicateursDTO indicateur : indicateurs) {
            List<Score> valeurs = new ArrayList<>();
            for (int i = exercice1.getAnnee(); i <= exercice2.getAnnee(); i++) {
                Exercice exercice = exerciceRepository.findExerciceByAnnee(i);
                Score score = new Score();
                Optional<Performance> performance = performanceService.getPerformanceByExercice(idCommune, exercice.getId(), indicateur.getId());
                if (performance.isPresent()) {
                    score.setAnnee(exercice.getAnnee());
                    if(performance.get().getScore() != null) {
                        score.setScore(performance.get().getScore());
                    } else {
                        score.setScore(null);
                        score.setValeurInterval(null);
                    }
                } else {
                    score.setAnnee(exercice.getAnnee());
                    score.setScore(null);
                    score.setValeurInterval(null);
                }
                valeurs.add(score);
            }
            valeurs = valeurs.stream().sorted(Comparator.comparing(Score::getAnnee)).collect(Collectors.toList());

            indicateur.setScores(valeurs);
            indicateurDTOS.add(indicateur);
        }
        return indicateurDTOS;
    }

    public List<IndicateursDTO> calculeScoreByOneExercice(Long idCommune, Exercice exercice, List<IndicateursDTO> indicateurs) {
        List<IndicateursDTO> indicateurDTOS = new ArrayList<>();
        for (IndicateursDTO indicateur : indicateurs) {
            List<Score> valeurs = new ArrayList<>();
            Score score = new Score();
            Optional<Performance> performance = performanceService.getPerformanceByExercice(idCommune, exercice.getId(), indicateur.getId());
            if (performance.isPresent()) {
                score.setAnnee(exercice.getAnnee());
                score.setScore(performance.get().getScore());
                setIntervalValue(score, indicateur);
            } else {
                score.setAnnee(exercice.getAnnee());
                score.setScore(null);
                score.setValeurInterval(null);
            }
            log.debug("============= fffffffffffff ============{}", score.getAnnee());
            valeurs.add(score);
            valeurs = valeurs.stream().sorted(Comparator.comparing(Score::getAnnee)).collect(Collectors.toList());
            indicateur.setScores(valeurs);
            indicateurDTOS.add(indicateur);
        }
        return indicateurDTOS;
    }


    public List<IndicateursDTO> getToCompareCommune(Long idAnnee, Long idCommune, DataFilter dataFilter, Long id) {
        List<IndicateursDTO> indicateurs = new ArrayList<>();
        switch (dataFilter) {
            case GENERAL:
                indicateurs = indicateurRepository.findAll().stream().map(indicateursMapper::toDto)
                    .filter(value -> value.isDeleted() != null && !value.isDeleted())
                    .collect(Collectors.toList());
                 indicateurs.forEach(val -> val.setSroreIndicateur(performanceService.calculePerformanceByIndicateur(idAnnee, idCommune, val.getId())));
                 return indicateurs;
            case TYPEINDICATEUR:
                indicateurs = indicateurRepository.findAll().stream().map(indicateursMapper::toDto)
                    .filter(value -> value.getDomaine().getTypeIndicateur().getId().equals(id) && value.isDeleted() != null && !value.isDeleted())
                    .collect(Collectors.toList());
              indicateurs.forEach(val -> val.setSroreIndicateur(performanceService.calculePerformanceByIndicateur(idAnnee, idCommune, val.getId())));
              return indicateurs;
            case INDICATEUR:
                indicateurs = indicateurRepository.findAll().stream().map(indicateursMapper::toDto)
                    .filter(value -> value.getId().equals(id) && value.isDeleted() != null && !value.isDeleted())
                    .collect(Collectors.toList());
                indicateurs.forEach(val -> val.setSroreIndicateur(performanceService.calculePerformanceByIndicateur(idAnnee, idCommune, val.getId())));
                return  indicateurs;
            case DOMAINE:
                indicateurs = indicateurRepository.findAll().stream().map(indicateursMapper::toDto)
                    .filter(value -> value.getDomaine().getId().equals(id) && value.isDeleted() != null && !value.isDeleted())
                    .collect(Collectors.toList());
                indicateurs.forEach(val -> val.setSroreIndicateur(performanceService.calculePerformanceByIndicateur(idAnnee, idCommune, val.getId())));
                return indicateurs;
        }
        return null;
    }

    private void setIntervalValue(Score score, IndicateursDTO indicateur) {
        if (indicateur.getTotalPoint() != null && indicateur.getBorneMax() != null) {
            Double value = score.getScore() * indicateur.getBorneMax() / indicateur.getTotalPoint();
            score.setValeurInterval(value);
        }
    }

    public List<IndicateursDTO> findIndicateurByTypeIndica(Long typeIndic) {
        return indicateurRepository.findAllByDeletedIsFalse().stream().filter(indicateur ->
            indicateur.getDomaine() != null && indicateur.getDomaine().getTypeIndicateur() != null &&
                indicateur.getDomaine().getTypeIndicateur().getId().equals(typeIndic)
            ).map(indicateursMapper::toDto).collect(Collectors.toList());
    }
}
