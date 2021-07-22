package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Performance;
import bf.gov.anptic.repository.PerformanceRepository;
import bf.gov.anptic.service.PerformanceService;
import bf.gov.anptic.service.dto.PerformanceDTO;
import bf.gov.anptic.service.mapper.PerformanceMapper;
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
 * Integration tests for the {@link PerformanceResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class PerformanceResourceIT {

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    private PerformanceService performanceService;

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

    private MockMvc restPerformanceMockMvc;

    private Performance performance;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PerformanceResource performanceResource = new PerformanceResource(performanceService);
        this.restPerformanceMockMvc = MockMvcBuilders.standaloneSetup(performanceResource)
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
    public static Performance createEntity(EntityManager em) {
        Performance performance = new Performance()
            .score(DEFAULT_SCORE)
            .deleted(DEFAULT_DELETED);
        return performance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Performance createUpdatedEntity(EntityManager em) {
        Performance performance = new Performance()
            .score(UPDATED_SCORE)
            .deleted(UPDATED_DELETED);
        return performance;
    }

    @BeforeEach
    public void initTest() {
        performance = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerformance() throws Exception {
        int databaseSizeBeforeCreate = performanceRepository.findAll().size();

        // Create the Performance
        PerformanceDTO performanceDTO = performanceMapper.toDto(performance);
        restPerformanceMockMvc.perform(post("/api/communes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Performance in the database
        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeCreate + 1);
        Performance testPerformance = performanceList.get(performanceList.size() - 1);
        assertThat(testPerformance.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testPerformance.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createPerformanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = performanceRepository.findAll().size();

        // Create the Performance with an existing ID
        performance.setId(1L);
        PerformanceDTO performanceDTO = performanceMapper.toDto(performance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformanceMockMvc.perform(post("/api/communes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Performance in the database
        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceRepository.findAll().size();
        // set the field null
        performance.setScore(null);

        // Create the Performance, which fails.
        PerformanceDTO performanceDTO = performanceMapper.toDto(performance);

        restPerformanceMockMvc.perform(post("/api/communes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDTO)))
            .andExpect(status().isBadRequest());

        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPerformances() throws Exception {
        // Initialize the database
        performanceRepository.saveAndFlush(performance);

        // Get all the performanceList
        restPerformanceMockMvc.perform(get("/api/communes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performance.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPerformance() throws Exception {
        // Initialize the database
        performanceRepository.saveAndFlush(performance);

        // Get the performance
        restPerformanceMockMvc.perform(get("/api/communes/{id}", performance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(performance.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPerformance() throws Exception {
        // Get the performance
        restPerformanceMockMvc.perform(get("/api/communes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerformance() throws Exception {
        // Initialize the database
        performanceRepository.saveAndFlush(performance);

        int databaseSizeBeforeUpdate = performanceRepository.findAll().size();

        // Update the performance
        Performance updatedPerformance = performanceRepository.findById(performance.getId()).get();
        // Disconnect from session so that the updates on updatedPerformance are not directly saved in db
        em.detach(updatedPerformance);
        updatedPerformance
            .score(UPDATED_SCORE)
            .deleted(UPDATED_DELETED);
        PerformanceDTO performanceDTO = performanceMapper.toDto(updatedPerformance);

        restPerformanceMockMvc.perform(put("/api/communes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDTO)))
            .andExpect(status().isOk());

        // Validate the Performance in the database
        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeUpdate);
        Performance testPerformance = performanceList.get(performanceList.size() - 1);
        assertThat(testPerformance.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testPerformance.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingPerformance() throws Exception {
        int databaseSizeBeforeUpdate = performanceRepository.findAll().size();

        // Create the Performance
        PerformanceDTO performanceDTO = performanceMapper.toDto(performance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceMockMvc.perform(put("/api/communes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(performanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Performance in the database
        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePerformance() throws Exception {
        // Initialize the database
        performanceRepository.saveAndFlush(performance);

        int databaseSizeBeforeDelete = performanceRepository.findAll().size();

        // Delete the performance
        restPerformanceMockMvc.perform(delete("/api/communes/{id}", performance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Performance> performanceList = performanceRepository.findAll();
        assertThat(performanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Performance.class);
        Performance performance1 = new Performance();
        performance1.setId(1L);
        Performance performance2 = new Performance();
        performance2.setId(performance1.getId());
        assertThat(performance1).isEqualTo(performance2);
        performance2.setId(2L);
        assertThat(performance1).isNotEqualTo(performance2);
        performance1.setId(null);
        assertThat(performance1).isNotEqualTo(performance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerformanceDTO.class);
        PerformanceDTO performanceDTO1 = new PerformanceDTO();
        performanceDTO1.setId(1L);
        PerformanceDTO performanceDTO2 = new PerformanceDTO();
        assertThat(performanceDTO1).isNotEqualTo(performanceDTO2);
        performanceDTO2.setId(performanceDTO1.getId());
        assertThat(performanceDTO1).isEqualTo(performanceDTO2);
        performanceDTO2.setId(2L);
        assertThat(performanceDTO1).isNotEqualTo(performanceDTO2);
        performanceDTO1.setId(null);
        assertThat(performanceDTO1).isNotEqualTo(performanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(performanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(performanceMapper.fromId(null)).isNull();
    }
}
