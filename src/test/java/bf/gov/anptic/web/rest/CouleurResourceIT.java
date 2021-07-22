package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Couleur;
import bf.gov.anptic.repository.CouleurRepository;
import bf.gov.anptic.service.CouleurService;
import bf.gov.anptic.service.dto.CouleurDTO;
import bf.gov.anptic.service.mapper.CouleurMapper;
import bf.gov.anptic.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static bf.gov.anptic.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CouleurResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class CouleurResourceIT {

    private static final String DEFAULT_COULEUR = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_PERFORMANCE = 1L;
    private static final Long UPDATED_ID_PERFORMANCE = 2L;

    @Autowired
    private CouleurRepository couleurRepository;

    @Autowired
    private CouleurMapper couleurMapper;

    @Autowired
    private CouleurService couleurService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCouleurMockMvc;

    private Couleur couleur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CouleurResource couleurResource = new CouleurResource(couleurService);
        this.restCouleurMockMvc = MockMvcBuilders.standaloneSetup(couleurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Couleur createEntity(EntityManager em) {
        Couleur couleur = new Couleur()
            .couleur(DEFAULT_COULEUR);
        return couleur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Couleur createUpdatedEntity(EntityManager em) {
        Couleur couleur = new Couleur()
            .couleur(UPDATED_COULEUR);
        return couleur;
    }

    @BeforeEach
    public void initTest() {
        couleur = createEntity(em);
    }

    @Test
    @Transactional
    public void createCouleur() throws Exception {
        int databaseSizeBeforeCreate = couleurRepository.findAll().size();

        // Create the Couleur
        CouleurDTO couleurDTO = couleurMapper.toDto(couleur);
        restCouleurMockMvc.perform(post("/api/couleurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(couleurDTO)))
            .andExpect(status().isCreated());

        // Validate the Couleur in the database
        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeCreate + 1);
        Couleur testCouleur = couleurList.get(couleurList.size() - 1);
        assertThat(testCouleur.getCouleur()).isEqualTo(DEFAULT_COULEUR);
    }

    @Test
    @Transactional
    public void createCouleurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = couleurRepository.findAll().size();

        // Create the Couleur with an existing ID
        couleur.setId(1L);
        CouleurDTO couleurDTO = couleurMapper.toDto(couleur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCouleurMockMvc.perform(post("/api/couleurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(couleurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Couleur in the database
        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCouleurIsRequired() throws Exception {
        int databaseSizeBeforeTest = couleurRepository.findAll().size();
        // set the field null
        couleur.setCouleur(null);

        // Create the Couleur, which fails.
        CouleurDTO couleurDTO = couleurMapper.toDto(couleur);

        restCouleurMockMvc.perform(post("/api/couleurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(couleurDTO)))
            .andExpect(status().isBadRequest());

        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCouleurs() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        // Get all the couleurList
        restCouleurMockMvc.perform(get("/api/couleurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(couleur.getId().intValue())))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR)))
            .andExpect(jsonPath("$.[*].idPerformance").value(hasItem(DEFAULT_ID_PERFORMANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCouleur() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        // Get the couleur
        restCouleurMockMvc.perform(get("/api/couleurs/{id}", couleur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(couleur.getId().intValue()))
            .andExpect(jsonPath("$.couleur").value(DEFAULT_COULEUR))
            .andExpect(jsonPath("$.idPerformance").value(DEFAULT_ID_PERFORMANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCouleur() throws Exception {
        // Get the couleur
        restCouleurMockMvc.perform(get("/api/couleurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCouleur() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        int databaseSizeBeforeUpdate = couleurRepository.findAll().size();

        // Update the couleur
        Couleur updatedCouleur = couleurRepository.findById(couleur.getId()).get();
        // Disconnect from session so that the updates on updatedCouleur are not directly saved in db
        em.detach(updatedCouleur);
        updatedCouleur
            .couleur(UPDATED_COULEUR);
        CouleurDTO couleurDTO = couleurMapper.toDto(updatedCouleur);

        restCouleurMockMvc.perform(put("/api/couleurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(couleurDTO)))
            .andExpect(status().isOk());

        // Validate the Couleur in the database
        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeUpdate);
        Couleur testCouleur = couleurList.get(couleurList.size() - 1);
        assertThat(testCouleur.getCouleur()).isEqualTo(UPDATED_COULEUR);
    }

    @Test
    @Transactional
    public void updateNonExistingCouleur() throws Exception {
        int databaseSizeBeforeUpdate = couleurRepository.findAll().size();

        // Create the Couleur
        CouleurDTO couleurDTO = couleurMapper.toDto(couleur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCouleurMockMvc.perform(put("/api/couleurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(couleurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Couleur in the database
        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCouleur() throws Exception {
        // Initialize the database
        couleurRepository.saveAndFlush(couleur);

        int databaseSizeBeforeDelete = couleurRepository.findAll().size();

        // Delete the couleur
        restCouleurMockMvc.perform(delete("/api/couleurs/{id}", couleur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Couleur> couleurList = couleurRepository.findAll();
        assertThat(couleurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Couleur.class);
        Couleur couleur1 = new Couleur();
        couleur1.setId(1L);
        Couleur couleur2 = new Couleur();
        couleur2.setId(couleur1.getId());
        assertThat(couleur1).isEqualTo(couleur2);
        couleur2.setId(2L);
        assertThat(couleur1).isNotEqualTo(couleur2);
        couleur1.setId(null);
        assertThat(couleur1).isNotEqualTo(couleur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CouleurDTO.class);
        CouleurDTO couleurDTO1 = new CouleurDTO();
        couleurDTO1.setId(1L);
        CouleurDTO couleurDTO2 = new CouleurDTO();
        assertThat(couleurDTO1).isNotEqualTo(couleurDTO2);
        couleurDTO2.setId(couleurDTO1.getId());
        assertThat(couleurDTO1).isEqualTo(couleurDTO2);
        couleurDTO2.setId(2L);
        assertThat(couleurDTO1).isNotEqualTo(couleurDTO2);
        couleurDTO1.setId(null);
        assertThat(couleurDTO1).isNotEqualTo(couleurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(couleurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(couleurMapper.fromId(null)).isNull();
    }
}
