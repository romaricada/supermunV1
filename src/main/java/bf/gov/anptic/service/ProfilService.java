package bf.gov.anptic.service;

import bf.gov.anptic.domain.Authority;
import bf.gov.anptic.domain.Profil;
import bf.gov.anptic.domain.User;
import bf.gov.anptic.repository.AuthorityRepository;
import bf.gov.anptic.repository.ProfilRepository;
import bf.gov.anptic.repository.UserRepository;
import bf.gov.anptic.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Profil}.
 */
@Service
@Transactional
public class ProfilService {

    private final Logger log = LoggerFactory.getLogger(ProfilService.class);

    private final ProfilRepository profilRepository;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public ProfilService(AuthorityRepository authorityRepository, UserRepository userRepository, ProfilRepository profilRepository) {
        this.profilRepository = profilRepository;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Save a profil.
     *
     * @param prof the entity to save.
     * @return the persisted entity.
     */
    public Profil save(Profil prof) {
        Set<Authority> authorities = prof.getAuthorities();
        authorities.add(authorityRepository.getOne(AuthoritiesConstants.USER));
        prof.setAuthorities(authorities);
        if(prof.getId() != null) {
            Profil profil1 = profilRepository.getOne(prof.getId());
            if (profil1.getAuthorities().size() != authorities.size()) {
                List<User> users = userRepository.findAllWithProfilsByLoginNot(AuthoritiesConstants.ADMIN);

                for (User user : users) {
                    if (user.getProfils().contains(profil1)) {
                        user.getProfils().remove(profil1);
                        user.getAuthorities().clear();
                        user.getProfils().add(prof);
                        user.getProfils().forEach(profi -> user.getAuthorities().addAll(profi.getAuthorities()));
                        userRepository.saveAndFlush(user);
                    }
                }
            }
        }

        return profilRepository.saveAndFlush(prof);
    }

    /**
     * Get all the profils.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Profil> findAll() {
        log.debug("Request to get all Profils");
        return profilRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the profils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Profil> findAllWithEagerRelationships(Pageable pageable) {
        return profilRepository.findAllWithEagerRelationships(pageable);
    }


    /**
     * Get one profil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Profil> findOne(Long id) {
        log.debug("Request to get Profil : {}", id);
        return profilRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the profil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        Profil profil = profilRepository.getOne(id);
        profilRepository.deleteById(id);

        List<User> users = userRepository.findAllWithProfilsByLoginNot(AuthoritiesConstants.ADMIN);

        users.stream().filter(user -> user.getProfils().contains(profil)).forEach(user -> {
            user.getProfils().remove(profil);
            user.getAuthorities().clear();
            user.getProfils().forEach(profi -> user.getAuthorities().addAll(profi.getAuthorities()));
            userRepository.saveAndFlush(user);
        });

    }
}
