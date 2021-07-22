package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Dictionaires;
import bf.gov.anptic.repository.DictionairesRepository;
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
 * Integration tests for the {@link DictionairesResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class DictionairesResourceIT {

    private static final String DEFAULT_ENTITE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITE = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String DEFAULT_REGLE_CALCULE = "AAAAAAAAAA";
    private static final String UPDATED_REGLE_CALCULE = "BBBBBBBBBB";

    @Autowired
    private DictionairesRepository dictionairesRepository;

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

    private MockMvc restDictionairesMockMvc;

    private Dictionaires dictionaires;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DictionairesResource dictionairesResource = new DictionairesResource(dictionairesRepository);
        this.restDictionairesMockMvc = MockMvcBuilders.standaloneSetup(dictionairesResource)
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
    public static Dictionaires createEntity(EntityManager em) {
        Dictionaires dictionaires = new Dictionaires()
            .entite(DEFAULT_ENTITE)
            .definition(DEFAULT_DEFINITION)
            .regleCalcule(DEFAULT_REGLE_CALCULE);
        return dictionaires;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dictionaires createUpdatedEntity(EntityManager em) {
        Dictionaires dictionaires = new Dictionaires()
            .entite(UPDATED_ENTITE)
            .definition(UPDATED_DEFINITION)
            .regleCalcule(UPDATED_REGLE_CALCULE);
        return dictionaires;
    }

    @BeforeEach
    public void initTest() {
        dictionaires = createEntity(em);
    }

    @Test
    @Transactional
    public void createDictionaires() throws Exception {
        int databaseSizeBeforeCreate = dictionairesRepository.findAll().size();

        // Create the Dictionaires
        restDictionairesMockMvc.perform(post("/api/dictionaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaires)))
            .andExpect(status().isCreated());

        // Validate the Dictionaires in the database
        List<Dictionaires> dictionairesList = dictionairesRepository.findAll();
        assertThat(dictionairesList).hasSize(databaseSizeBeforeCreate + 1);
        Dictionaires testDictionaires = dictionairesList.get(dictionairesList.size() - 1);
        assertThat(testDictionaires.getEntite()).isEqualTo(DEFAULT_ENTITE);
        assertThat(testDictionaires.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
        assertThat(testDictionaires.getRegleCalcule()).isEqualTo(DEFAULT_REGLE_CALCULE);
    }

    @Test
    @Transactional
    public void createDictionairesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dictionairesRepository.findAll().size();

        // Create the Dictionaires with an existing ID
        dictionaires.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDictionairesMockMvc.perform(post("/api/dictionaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaires)))
            .andExpect(status().isBadRequest());

        // Validate the Dictionaires in the database
        List<Dictionaires> dictionairesList = dictionairesRepository.findAll();
        assertThat(dictionairesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDictionaires() throws Exception {
        // Initialize the database
        dictionairesRepository.saveAndFlush(dictionaires);

        // Get all the dictionairesList
        restDictionairesMockMvc.perform(get("/api/dictionaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dictionaires.getId().intValue())))
            .andExpect(jsonPath("$.[*].entite").value(hasItem(DEFAULT_ENTITE)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].regleCalcule").value(hasItem(DEFAULT_REGLE_CALCULE)));
    }
    
    @Test
    @Transactional
    public void getDictionaires() throws Exception {
        // Initialize the database
        dictionairesRepository.saveAndFlush(dictionaires);

        // Get the dictionaires
        restDictionairesMockMvc.perform(get("/api/dictionaires/{id}", dictionaires.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dictionaires.getId().intValue()))
            .andExpect(jsonPath("$.entite").value(DEFAULT_ENTITE))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION))
            .andExpect(jsonPath("$.regleCalcule").value(DEFAULT_REGLE_CALCULE));
    }

    @Test
    @Transactional
    public void getNonExistingDictionaires() throws Exception {
        // Get the dictionaires
        restDictionairesMockMvc.perform(get("/api/dictionaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDictionaires() throws Exception {
        // Initialize the database
        dictionairesRepository.saveAndFlush(dictionaires);

        int databaseSizeBeforeUpdate = dictionairesRepository.findAll().size();

        // Update the dictionaires
        Dictionaires updatedDictionaires = dictionairesRepository.findById(dictionaires.getId()).get();
        // Disconnect from session so that the updates on updatedDictionaires are not directly saved in db
        em.detach(updatedDictionaires);
        updatedDictionaires
            .entite(UPDATED_ENTITE)
            .definition(UPDATED_DEFINITION)
            .regleCalcule(UPDATED_REGLE_CALCULE);

        restDictionairesMockMvc.perform(put("/api/dictionaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDictionaires)))
            .andExpect(status().isOk());

        // Validate the Dictionaires in the database
        List<Dictionaires> dictionairesList = dictionairesRepository.findAll();
        assertThat(dictionairesList).hasSize(databaseSizeBeforeUpdate);
        Dictionaires testDictionaires = dictionairesList.get(dictionairesList.size() - 1);
        assertThat(testDictionaires.getEntite()).isEqualTo(UPDATED_ENTITE);
        assertThat(testDictionaires.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testDictionaires.getRegleCalcule()).isEqualTo(UPDATED_REGLE_CALCULE);
    }

    @Test
    @Transactional
    public void updateNonExistingDictionaires() throws Exception {
        int databaseSizeBeforeUpdate = dictionairesRepository.findAll().size();

        // Create the Dictionaires

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDictionairesMockMvc.perform(put("/api/dictionaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dictionaires)))
            .andExpect(status().isBadRequest());

        // Validate the Dictionaires in the database
        List<Dictionaires> dictionairesList = dictionairesRepository.findAll();
        assertThat(dictionairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDictionaires() throws Exception {
        // Initialize the database
        dictionairesRepository.saveAndFlush(dictionaires);

        int databaseSizeBeforeDelete = dictionairesRepository.findAll().size();

        // Delete the dictionaires
        restDictionairesMockMvc.perform(delete("/api/dictionaires/{id}", dictionaires.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dictionaires> dictionairesList = dictionairesRepository.findAll();
        assertThat(dictionairesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dictionaires.class);
        Dictionaires dictionaires1 = new Dictionaires();
        dictionaires1.setId(1L);
        Dictionaires dictionaires2 = new Dictionaires();
        dictionaires2.setId(dictionaires1.getId());
        assertThat(dictionaires1).isEqualTo(dictionaires2);
        dictionaires2.setId(2L);
        assertThat(dictionaires1).isNotEqualTo(dictionaires2);
        dictionaires1.setId(null);
        assertThat(dictionaires1).isNotEqualTo(dictionaires2);
    }
}
