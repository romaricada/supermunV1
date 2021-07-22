package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Counte;
import bf.gov.anptic.repository.CounteRepository;
import bf.gov.anptic.service.CounteService;
import bf.gov.anptic.service.dto.CounteDTO;
import bf.gov.anptic.service.mapper.CounteMapper;
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
 * Integration tests for the {@link CounteResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class CounteResourceIT {

    private static final Double DEFAULT_NOMBRE_VISITEURS = 1D;
    private static final Double UPDATED_NOMBRE_VISITEURS = 2D;

    @Autowired
    private CounteRepository counteRepository;

    @Autowired
    private CounteMapper counteMapper;

    @Autowired
    private CounteService counteService;

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

    private MockMvc restCounteMockMvc;

    private Counte counte;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CounteResource counteResource = new CounteResource(counteService);
        this.restCounteMockMvc = MockMvcBuilders.standaloneSetup(counteResource)
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
    public static Counte createEntity(EntityManager em) {
        Counte counte = new Counte()
            .nombreVisiteurs(DEFAULT_NOMBRE_VISITEURS);
        return counte;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Counte createUpdatedEntity(EntityManager em) {
        Counte counte = new Counte()
            .nombreVisiteurs(UPDATED_NOMBRE_VISITEURS);
        return counte;
    }

    @BeforeEach
    public void initTest() {
        counte = createEntity(em);
    }

    @Test
    @Transactional
    public void createCounte() throws Exception {
        int databaseSizeBeforeCreate = counteRepository.findAll().size();

        // Create the Counte
        CounteDTO counteDTO = counteMapper.toDto(counte);
        restCounteMockMvc.perform(post("/api/countes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(counteDTO)))
            .andExpect(status().isCreated());

        // Validate the Counte in the database
        List<Counte> counteList = counteRepository.findAll();
        assertThat(counteList).hasSize(databaseSizeBeforeCreate + 1);
        Counte testCounte = counteList.get(counteList.size() - 1);
        assertThat(testCounte.getNombreVisiteurs()).isEqualTo(DEFAULT_NOMBRE_VISITEURS);
    }

    @Test
    @Transactional
    public void createCounteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = counteRepository.findAll().size();

        // Create the Counte with an existing ID
        counte.setId(1L);
        CounteDTO counteDTO = counteMapper.toDto(counte);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCounteMockMvc.perform(post("/api/countes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(counteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Counte in the database
        List<Counte> counteList = counteRepository.findAll();
        assertThat(counteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCountes() throws Exception {
        // Initialize the database
        counteRepository.saveAndFlush(counte);

        // Get all the counteList
        restCounteMockMvc.perform(get("/api/countes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(counte.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreVisiteurs").value(hasItem(DEFAULT_NOMBRE_VISITEURS.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getCounte() throws Exception {
        // Initialize the database
        counteRepository.saveAndFlush(counte);

        // Get the counte
        restCounteMockMvc.perform(get("/api/countes/{id}", counte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(counte.getId().intValue()))
            .andExpect(jsonPath("$.nombreVisiteurs").value(DEFAULT_NOMBRE_VISITEURS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCounte() throws Exception {
        // Get the counte
        restCounteMockMvc.perform(get("/api/countes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCounte() throws Exception {
        // Initialize the database
        counteRepository.saveAndFlush(counte);

        int databaseSizeBeforeUpdate = counteRepository.findAll().size();

        // Update the counte
        Counte updatedCounte = counteRepository.findById(counte.getId()).get();
        // Disconnect from session so that the updates on updatedCounte are not directly saved in db
        em.detach(updatedCounte);
        updatedCounte
            .nombreVisiteurs(UPDATED_NOMBRE_VISITEURS);
        CounteDTO counteDTO = counteMapper.toDto(updatedCounte);

        restCounteMockMvc.perform(put("/api/countes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(counteDTO)))
            .andExpect(status().isOk());

        // Validate the Counte in the database
        List<Counte> counteList = counteRepository.findAll();
        assertThat(counteList).hasSize(databaseSizeBeforeUpdate);
        Counte testCounte = counteList.get(counteList.size() - 1);
        assertThat(testCounte.getNombreVisiteurs()).isEqualTo(UPDATED_NOMBRE_VISITEURS);
    }

    @Test
    @Transactional
    public void updateNonExistingCounte() throws Exception {
        int databaseSizeBeforeUpdate = counteRepository.findAll().size();

        // Create the Counte
        CounteDTO counteDTO = counteMapper.toDto(counte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCounteMockMvc.perform(put("/api/countes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(counteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Counte in the database
        List<Counte> counteList = counteRepository.findAll();
        assertThat(counteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCounte() throws Exception {
        // Initialize the database
        counteRepository.saveAndFlush(counte);

        int databaseSizeBeforeDelete = counteRepository.findAll().size();

        // Delete the counte
        restCounteMockMvc.perform(delete("/api/countes/{id}", counte.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Counte> counteList = counteRepository.findAll();
        assertThat(counteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Counte.class);
        Counte counte1 = new Counte();
        counte1.setId(1L);
        Counte counte2 = new Counte();
        counte2.setId(counte1.getId());
        assertThat(counte1).isEqualTo(counte2);
        counte2.setId(2L);
        assertThat(counte1).isNotEqualTo(counte2);
        counte1.setId(null);
        assertThat(counte1).isNotEqualTo(counte2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CounteDTO.class);
        CounteDTO counteDTO1 = new CounteDTO();
        counteDTO1.setId(1L);
        CounteDTO counteDTO2 = new CounteDTO();
        assertThat(counteDTO1).isNotEqualTo(counteDTO2);
        counteDTO2.setId(counteDTO1.getId());
        assertThat(counteDTO1).isEqualTo(counteDTO2);
        counteDTO2.setId(2L);
        assertThat(counteDTO1).isNotEqualTo(counteDTO2);
        counteDTO1.setId(null);
        assertThat(counteDTO1).isNotEqualTo(counteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(counteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(counteMapper.fromId(null)).isNull();
    }
}
