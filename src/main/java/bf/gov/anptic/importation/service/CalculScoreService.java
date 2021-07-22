package bf.gov.anptic.importation.service;

import bf.gov.anptic.domain.*;
import bf.gov.anptic.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CalculScoreService {
    private final Logger log = LoggerFactory.getLogger(CalculScoreService.class);

    private final IndicateurRepository indicateurRepository;
    private final ModaliteRepository modaliteRepository;
    private final ExerciceRepository exerciceRepository;
    private final ValeurModaliteRepository valeurModaliteRepository;
    private final PerformanceRepository performanceRepository;
    private final CommuneRepository communeRepository;


    public CalculScoreService(IndicateurRepository indicateurRepository, ModaliteRepository modaliteRepository,
                              ExerciceRepository exerciceRepository, ValeurModaliteRepository valeurModaliteRepository,
                              PerformanceRepository performanceRepository, CommuneRepository communeRepository) {
        this.indicateurRepository = indicateurRepository;
        this.modaliteRepository = modaliteRepository;
        this.exerciceRepository = exerciceRepository;
        this.valeurModaliteRepository = valeurModaliteRepository;
        this.performanceRepository = performanceRepository;
        this.communeRepository = communeRepository;
    }

    /**
     * Service pour l'appel des fonctions :  {@link this.calculScoreFromValeurModalite},
     * {@link this.calculScoreFromValeurIndicateur}, {@link this.calculScoreWithFormule}<br/>
     * L'ensemble de ces méthodes servent à calculer les performance des communes sur la base des indicateurs
     *
     * @param anneeId
     * @return
     */
    public boolean CalculPerformance(Long anneeId) {
        List<Indicateur> indicateurs = indicateurRepository.findAll();
        List<Performance> performances = new ArrayList<>();
        communeRepository.findAll().forEach(commune -> indicateurs.forEach(indicateur -> {
            Method[] methods = this.getClass().getDeclaredMethods();
            Performance performance = null;
            for (Method method : methods) {
                switch (method.getName()) {
                    case "calculScoreFromValeurModalite":
                        performance = this.calculScoreFromValeurModalite(anneeId, indicateur, commune);
                        log.debug("=====================Annee = {} =======================", method.getName());
                        break;
                    case "calculScoreFromValeurIndicateur":
                        performance = this.calculScoreFromValeurIndicateur(anneeId, indicateur, commune);
                        log.debug("=====================Annee = {} =======================", method.getName());
                        break;
                    case "calculScoreWithFormule":
                        performance = this.calculScoreWithFormule(anneeId, indicateur, commune);
                        log.debug("=====================Annee = {} =======================", method.getName());
                        break;
                }
                if (performance != null) {
                    performances.add(performance);
                }
            }
        }));
        return performanceRepository.saveAll(performances).isEmpty();
    }

    /**
     * Calcul de score d'un indicateur dont la valeur est la somme de des valeurs de ses modalités.
     *
     * @param anneeId    L'année choisie par l'utilisateur
     * @param indicateur L'indicateur pour lequel on calcul le score
     * @return Le score calculé
     */
    private Performance calculScoreFromValeurModalite(Long anneeId, Indicateur indicateur, Commune commune) {
        Double score = 0d;
        List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNotNullAndFormuleIsNullAndDeletedIsFalse(indicateur.getId());
        if (!modalites.isEmpty()) {
            ValeurModalite valeurModalite;
            for (Modalite modalite : modalites) {
                valeurModalite = valeurModaliteRepository.findTop1ByModaliteIdAndExerciceIdAndCommuneId(modalite.getId(), anneeId, commune.getId());
                if (valeurModalite != null) {
                    String value = valeurModalite.getValeur().toLowerCase();
                    if (value.equals("oui")) {
                        score += modalite.getValeur();
                    }
                }
            }
            return this.createPerformance(score, anneeId, indicateur, commune);
        }

        return null;
    }

    /**
     * Calcul de score d'un indicateur dont la valeur de {@code ValeurModalite}<br/>
     * est celle de l'indicateur lui-même.
     *
     * @param anneeId    L'année choisie par l'utilisateur
     * @param indicateur L'indicateur pour lequel on calcul le score
     * @return Le score calculé
     */
    private Performance calculScoreFromValeurIndicateur(Long anneeId, Indicateur indicateur, Commune commune) {
        Double score = null;
        ValeurModalite valeurModalite = valeurModaliteRepository.findTop1ByIndicateurIdAndExerciceIdAndCommuneId(indicateur.getId(), anneeId, commune.getId());

        if (valeurModalite != null) {
            Double indicValue = Double.valueOf(valeurModalite.getValeur());
            List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndLibelleIsNullAndFormuleIsNullAndDeletedIsFalse(indicateur.getId());
            if (!modalites.isEmpty()) {
                for (Modalite modalite : modalites) {
                    score = getScore(modalite, indicValue);
                }
                if (score != null) {
                    return this.createPerformance(score, anneeId, indicateur, commune);
                }
            }
        }
        return null;
    }

    /**
     * Methode permettant de calculer la performance d'une commune<br/>
     * Dont les modalités de l'indicateur sont fonction d'une formule
     *
     * @param anneeId
     * @param indicateur
     * @param commune
     * @return
     */
    private Performance calculScoreWithFormule(Long anneeId, Indicateur indicateur, Commune commune) {
        List<Modalite> modalites = modaliteRepository.findAllByIndicateurIdAndFormuleIsNotNullAndDeletedIsFalse(indicateur.getId());
        Double score = null;
        if (!modalites.isEmpty()) {
            ValeurModalite valeurModalite = valeurModaliteRepository.findTop1ByIndicateurIdAndExerciceIdAndCommuneId(indicateur.getId(), anneeId, commune.getId());
            if (valeurModalite != null) {
                double indicValue = Double.parseDouble(valeurModalite.getValeur());
                for (Modalite modalite : modalites) {
                    switch (modalite.getFormule()) {
                        case TAUXCSPS: {
                            score = indicValue / 10;
                            break;
                        }
                        case RETARDMOYEN: {
                            if (indicValue <= 1) {
                                score = 10d;
                            } else if (indicValue > 1 && indicValue <= 200) {
                                score = 10 / Math.sqrt(indicValue);
                            } else if (indicValue >= 200) {
                                score = 0d;
                            }
                            break;
                        }
                        case TAUXPARTICIPATION: {
                            if (indicValue > 100) {
                                score = 10d;
                            } else if (indicValue > 90 && indicValue <= 100) {
                                score = 10 - ((100 - indicValue) * 3 / 10);
                            } else if (indicValue > 80 && indicValue <= 90) {
                                score = 7 - ((90 - indicValue) / 5);
                            } else if (indicValue > 40 && indicValue <= 80) {
                                score = 5 - ((80 - indicValue) / 10);
                            } else if (indicValue > 20 && indicValue <= 40) {
                                score = 1 - ((40 - indicValue) / 20);
                            } else if (indicValue <= 20) {
                                score = 0d;
                            }
                            break;
                        }
                    }
                }
                return this.createPerformance(score, anneeId, indicateur, commune);
            }
        }
        return null;
    }


    /**
     * Utilitaire pour recupère l'instance de l'année dont l'{@code Id} est passé en paramètre
     *
     * @param id {@code Id} de l'année en cours
     * @return l'instance de la classe {@link Exercice}
     */
    private Exercice getExercice(Long id) {
        return exerciceRepository.getOne(id);
    }

    /**
     * Utilitaire pour créer une instance de Performance avec le score calculé
     *
     * @param score
     * @param anneeId
     * @param indicateur
     * @param commune
     * @return
     */
    private Performance createPerformance(Double score, Long anneeId, Indicateur indicateur, Commune commune) {
        Performance performance = new Performance();
        performance.setCommune(commune);
        performance.setExercice(this.getExercice(anneeId));
        performance.setIndicateur(indicateur);
        performance.setScore(score);
        indicateur.setDeleted(false);
        return performance;
    }

    /**
     * Utilitaire pour calculer le score d'un indicateur à par de ses modalités
     *
     * @param modalite
     * @param indicValue
     * @return
     */
    private Double getScore(Modalite modalite, Double indicValue) {
        Double score = null;
        Double min = modalite.getBorneMinimale();
        Double max = modalite.getBorneMaximale();
        if (min != null && max != null) {
            if (indicValue >= min && indicValue < max) {
                score = modalite.getValeur();
            }
        } else if (min == null && max != null) {
            if (indicValue < max) {
                score = modalite.getValeur();
            }
        } else if (min != null) {
            if (indicValue >= min) {
                score = modalite.getValeur();
            }
        }
        return score;
    }
}
