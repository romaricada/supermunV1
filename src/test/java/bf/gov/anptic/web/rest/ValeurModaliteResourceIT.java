package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.ValeurModalite;
import bf.gov.anptic.repository.ValeurModaliteRepository;
import bf.gov.anptic.service.ValeurModaliteService;
import bf.gov.anptic.service.dto.ValeurModaliteDTO;
import bf.gov.anptic.service.mapper.ValeurModaliteMapper;
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
 * Integration tests for the {@link ValeurModaliteResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class ValeurModaliteResourceIT {

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    @Autowired
    private ValeurModaliteRepository valeurModaliteRepository;

    @Autowired
    private ValeurModaliteMapper valeurModaliteMapper;

    @Autowired
    private ValeurModaliteService valeurModaliteService;

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

    private MockMvc restValeurModaliteMockMvc;

    private ValeurModalite valeurModalite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ValeurModaliteResource valeurModaliteResource = new ValeurModaliteResource(valeurModaliteService);
        this.restValeurModaliteMockMvc = MockMvcBuilders.standaloneSetup(valeurModaliteResource)
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
    public static ValeurModalite createEntity(EntityManager em) {
        ValeurModalite valeurModalite = new ValeurModalite()
            .valeur(DEFAULT_VALEUR);
        return valeurModalite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValeurModalite createUpdatedEntity(EntityManager em) {
        ValeurModalite valeurModalite = new ValeurModalite()
            .valeur(UPDATED_VALEUR);
        return valeurModalite;
    }

    @BeforeEach
    public void initTest() {
        valeurModalite = createEntity(em);
    }

    @Test
    @Transactional
    public void createValeurModalite() throws Exception {
        int databaseSizeBeforeCreate = valeurModaliteRepository.findAll().size();

        // Create the ValeurModalite
        ValeurModaliteDTO valeurModaliteDTO = valeurModaliteMapper.toDto(valeurModalite);
        restValeurModaliteMockMvc.perform(post("/api/valeur-modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valeurModaliteDTO)))
            .andExpect(status().isCreated());

        // Validate the ValeurModalite in the database
        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeCreate + 1);
        ValeurModalite testValeurModalite = valeurModaliteList.get(valeurModaliteList.size() - 1);
        assertThat(testValeurModalite.getValeur()).isEqualTo(DEFAULT_VALEUR);
    }

    @Test
    @Transactional
    public void createValeurModaliteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valeurModaliteRepository.findAll().size();

        // Create the ValeurModalite with an existing ID
        valeurModalite.setId(1L);
        ValeurModaliteDTO valeurModaliteDTO = valeurModaliteMapper.toDto(valeurModalite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValeurModaliteMockMvc.perform(post("/api/valeur-modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valeurModaliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ValeurModalite in the database
        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkValeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = valeurModaliteRepository.findAll().size();
        // set the field null
        valeurModalite.setValeur(null);

        // Create the ValeurModalite, which fails.
        ValeurModaliteDTO valeurModaliteDTO = valeurModaliteMapper.toDto(valeurModalite);

        restValeurModaliteMockMvc.perform(post("/api/valeur-modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valeurModaliteDTO)))
            .andExpect(status().isBadRequest());

        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllValeurModalites() throws Exception {
        // Initialize the database
        valeurModaliteRepository.saveAndFlush(valeurModalite);

        // Get all the valeurModaliteList
        restValeurModaliteMockMvc.perform(get("/api/valeur-modalites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valeurModalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)));
    }
    
    @Test
    @Transactional
    public void getValeurModalite() throws Exception {
        // Initialize the database
        valeurModaliteRepository.saveAndFlush(valeurModalite);

        // Get the valeurModalite
        restValeurModaliteMockMvc.perform(get("/api/valeur-modalites/{id}", valeurModalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valeurModalite.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR));
    }

    @Test
    @Transactional
    public void getNonExistingValeurModalite() throws Exception {
        // Get the valeurModalite
        restValeurModaliteMockMvc.perform(get("/api/valeur-modalites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValeurModalite() throws Exception {
        // Initialize the database
        valeurModaliteRepository.saveAndFlush(valeurModalite);

        int databaseSizeBeforeUpdate = valeurModaliteRepository.findAll().size();

        // Update the valeurModalite
        ValeurModalite updatedValeurModalite = valeurModaliteRepository.findById(valeurModalite.getId()).get();
        // Disconnect from session so that the updates on updatedValeurModalite are not directly saved in db
        em.detach(updatedValeurModalite);
        updatedValeurModalite
            .valeur(UPDATED_VALEUR);
        ValeurModaliteDTO valeurModaliteDTO = valeurModaliteMapper.toDto(updatedValeurModalite);

        restValeurModaliteMockMvc.perform(put("/api/valeur-modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valeurModaliteDTO)))
            .andExpect(status().isOk());

        // Validate the ValeurModalite in the database
        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeUpdate);
        ValeurModalite testValeurModalite = valeurModaliteList.get(valeurModaliteList.size() - 1);
        assertThat(testValeurModalite.getValeur()).isEqualTo(UPDATED_VALEUR);
    }

    @Test
    @Transactional
    public void updateNonExistingValeurModalite() throws Exception {
        int databaseSizeBeforeUpdate = valeurModaliteRepository.findAll().size();

        // Create the ValeurModalite
        ValeurModaliteDTO valeurModaliteDTO = valeurModaliteMapper.toDto(valeurModalite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValeurModaliteMockMvc.perform(put("/api/valeur-modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(valeurModaliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ValeurModalite in the database
        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteValeurModalite() throws Exception {
        // Initialize the database
        valeurModaliteRepository.saveAndFlush(valeurModalite);

        int databaseSizeBeforeDelete = valeurModaliteRepository.findAll().size();

        // Delete the valeurModalite
        restValeurModaliteMockMvc.perform(delete("/api/valeur-modalites/{id}", valeurModalite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ValeurModalite> valeurModaliteList = valeurModaliteRepository.findAll();
        assertThat(valeurModaliteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValeurModalite.class);
        ValeurModalite valeurModalite1 = new ValeurModalite();
        valeurModalite1.setId(1L);
        ValeurModalite valeurModalite2 = new ValeurModalite();
        valeurModalite2.setId(valeurModalite1.getId());
        assertThat(valeurModalite1).isEqualTo(valeurModalite2);
        valeurModalite2.setId(2L);
        assertThat(valeurModalite1).isNotEqualTo(valeurModalite2);
        valeurModalite1.setId(null);
        assertThat(valeurModalite1).isNotEqualTo(valeurModalite2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValeurModaliteDTO.class);
        ValeurModaliteDTO valeurModaliteDTO1 = new ValeurModaliteDTO();
        valeurModaliteDTO1.setId(1L);
        ValeurModaliteDTO valeurModaliteDTO2 = new ValeurModaliteDTO();
        assertThat(valeurModaliteDTO1).isNotEqualTo(valeurModaliteDTO2);
        valeurModaliteDTO2.setId(valeurModaliteDTO1.getId());
        assertThat(valeurModaliteDTO1).isEqualTo(valeurModaliteDTO2);
        valeurModaliteDTO2.setId(2L);
        assertThat(valeurModaliteDTO1).isNotEqualTo(valeurModaliteDTO2);
        valeurModaliteDTO1.setId(null);
        assertThat(valeurModaliteDTO1).isNotEqualTo(valeurModaliteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(valeurModaliteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(valeurModaliteMapper.fromId(null)).isNull();
    }
}
