package bf.gov.anptic.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, bf.gov.anptic.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, bf.gov.anptic.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, bf.gov.anptic.domain.User.class.getName());
            createCache(cm, bf.gov.anptic.domain.Authority.class.getName());
            createCache(cm, bf.gov.anptic.domain.User.class.getName() + ".authorities");
            cm.createCache(bf.gov.anptic.domain.EntityAuditEvent.class.getName(), jcacheConfiguration);
            createCache(cm, bf.gov.anptic.domain.Region.class.getName());
            createCache(cm, bf.gov.anptic.domain.Province.class.getName());
            createCache(cm, bf.gov.anptic.domain.Commune.class.getName());
            createCache(cm, bf.gov.anptic.domain.Indicateur.class.getName());
            createCache(cm, bf.gov.anptic.domain.TypeIndicateur.class.getName());
            createCache(cm, bf.gov.anptic.domain.Domaine.class.getName());
            createCache(cm, bf.gov.anptic.domain.Performance.class.getName());
            createCache(cm, bf.gov.anptic.domain.Couleur.class.getName());
            createCache(cm, bf.gov.anptic.domain.Modalite.class.getName());
            createCache(cm, bf.gov.anptic.domain.ValeurModalite.class.getName());
            createCache(cm, bf.gov.anptic.domain.Exercice.class.getName());
            createCache(cm, bf.gov.anptic.domain.Poster.class.getName());
            createCache(cm, bf.gov.anptic.domain.Information.class.getName());
            createCache(cm, bf.gov.anptic.domain.Publication.class.getName());
            createCache(cm, bf.gov.anptic.domain.Profil.class.getName());
            createCache(cm, bf.gov.anptic.domain.Profil.class.getName() + ".authorities");
            createCache(cm, bf.gov.anptic.domain.TypePublication.class.getName());
            createCache(cm, bf.gov.anptic.domain.TypePublication.class.getName() + ".publications");
            createCache(cm, bf.gov.anptic.domain.EtatCommune.class.getName());
            createCache(cm, bf.gov.anptic.domain.ValidationCommune.class.getName());
            createCache(cm, bf.gov.anptic.domain.Dictionaires.class.getName());
            createCache(cm, bf.gov.anptic.domain.Counte.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
