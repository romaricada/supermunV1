package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Indicateur;
import bf.gov.anptic.repository.IndicateurRepository;
import bf.gov.anptic.service.IndicateurService;
import bf.gov.anptic.service.dto.IndicateurDTO;
import bf.gov.anptic.service.mapper.IndicateurMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static bf.gov.anptic.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IndicateurResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class IndicateurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_1_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_2_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_TOTAL_POINT = 1D;
    private static final Double UPDATED_TOTAL_POINT = 2D;

    private static final Boolean DEFAULT_INTERVAL = false;
    private static final Boolean UPDATED_INTERVAL = true;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private IndicateurRepository indicateurRepository;

    @Autowired
    private IndicateurMapper indicateurMapper;

    @Autowired
    private IndicateurService indicateurService;

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

    private MockMvc restIndicateurMockMvc;

    private Indicateur indicateur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndicateurResource indicateurResource = new IndicateurResource(indicateurService);
        this.restIndicateurMockMvc = MockMvcBuilders.standaloneSetup(indicateurResource)
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
    public static Indicateur createEntity(EntityManager em) {
        Indicateur indicateur = new Indicateur()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .image1(DEFAULT_IMAGE_1)
            .image1ContentType(DEFAULT_IMAGE_1_CONTENT_TYPE)
            .image2(DEFAULT_IMAGE_2)
            .image2ContentType(DEFAULT_IMAGE_2_CONTENT_TYPE)
            .totalPoint(DEFAULT_TOTAL_POINT)
            .interval(DEFAULT_INTERVAL)
            .deleted(DEFAULT_DELETED);
        return indicateur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Indicateur createUpdatedEntity(EntityManager em) {
        Indicateur indicateur = new Indicateur()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .totalPoint(UPDATED_TOTAL_POINT)
            .interval(UPDATED_INTERVAL)
            .deleted(UPDATED_DELETED);
        return indicateur;
    }

    @BeforeEach
    public void initTest() {
        indicateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndicateur() throws Exception {
        int databaseSizeBeforeCreate = indicateurRepository.findAll().size();

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);
        restIndicateurMockMvc.perform(post("/api/indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicateurDTO)))
            .andExpect(status().isCreated());

        // Validate the Indicateur in the database
        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeCreate + 1);
        Indicateur testIndicateur = indicateurList.get(indicateurList.size() - 1);
        assertThat(testIndicateur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testIndicateur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testIndicateur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIndicateur.getImage1()).isEqualTo(DEFAULT_IMAGE_1);
        assertThat(testIndicateur.getImage1ContentType()).isEqualTo(DEFAULT_IMAGE_1_CONTENT_TYPE);
        assertThat(testIndicateur.getImage2()).isEqualTo(DEFAULT_IMAGE_2);
        assertThat(testIndicateur.getImage2ContentType()).isEqualTo(DEFAULT_IMAGE_2_CONTENT_TYPE);
        assertThat(testIndicateur.getTotalPoint()).isEqualTo(DEFAULT_TOTAL_POINT);
        assertThat(testIndicateur.isInterval()).isEqualTo(DEFAULT_INTERVAL);
        assertThat(testIndicateur.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createIndicateurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indicateurRepository.findAll().size();

        // Create the Indicateur with an existing ID
        indicateur.setId(1L);
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndicateurMockMvc.perform(post("/api/indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = indicateurRepository.findAll().size();
        // set the field null
        indicateur.setLibelle(null);

        // Create the Indicateur, which fails.
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        restIndicateurMockMvc.perform(post("/api/indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIndicateurs() throws Exception {
        // Initialize the database
        indicateurRepository.saveAndFlush(indicateur);

        // Get all the indicateurList
        restIndicateurMockMvc.perform(get("/api/indicateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indicateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].image1ContentType").value(hasItem(DEFAULT_IMAGE_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image1").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_1))))
            .andExpect(jsonPath("$.[*].image2ContentType").value(hasItem(DEFAULT_IMAGE_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_2))))
            .andExpect(jsonPath("$.[*].totalPoint").value(hasItem(DEFAULT_TOTAL_POINT.doubleValue())))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(DEFAULT_INTERVAL.booleanValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getIndicateur() throws Exception {
        // Initialize the database
        indicateurRepository.saveAndFlush(indicateur);

        // Get the indicateur
        restIndicateurMockMvc.perform(get("/api/indicateurs/{id}", indicateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indicateur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.image1ContentType").value(DEFAULT_IMAGE_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.image1").value(Base64Utils.encodeToString(DEFAULT_IMAGE_1)))
            .andExpect(jsonPath("$.image2ContentType").value(DEFAULT_IMAGE_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.image2").value(Base64Utils.encodeToString(DEFAULT_IMAGE_2)))
            .andExpect(jsonPath("$.totalPoint").value(DEFAULT_TOTAL_POINT.doubleValue()))
            .andExpect(jsonPath("$.interval").value(DEFAULT_INTERVAL.booleanValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIndicateur() throws Exception {
        // Get the indicateur
        restIndicateurMockMvc.perform(get("/api/indicateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndicateur() throws Exception {
        // Initialize the database
        indicateurRepository.saveAndFlush(indicateur);

        int databaseSizeBeforeUpdate = indicateurRepository.findAll().size();

        // Update the indicateur
        Indicateur updatedIndicateur = indicateurRepository.findById(indicateur.getId()).get();
        // Disconnect from session so that the updates on updatedIndicateur are not directly saved in db
        em.detach(updatedIndicateur);
        updatedIndicateur
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .totalPoint(UPDATED_TOTAL_POINT)
            .interval(UPDATED_INTERVAL)
            .deleted(UPDATED_DELETED);
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(updatedIndicateur);

        restIndicateurMockMvc.perform(put("/api/indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicateurDTO)))
            .andExpect(status().isOk());

        // Validate the Indicateur in the database
        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeUpdate);
        Indicateur testIndicateur = indicateurList.get(indicateurList.size() - 1);
        assertThat(testIndicateur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIndicateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testIndicateur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIndicateur.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testIndicateur.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testIndicateur.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testIndicateur.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testIndicateur.getTotalPoint()).isEqualTo(UPDATED_TOTAL_POINT);
        assertThat(testIndicateur.isInterval()).isEqualTo(UPDATED_INTERVAL);
        assertThat(testIndicateur.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = indicateurRepository.findAll().size();

        // Create the Indicateur
        IndicateurDTO indicateurDTO = indicateurMapper.toDto(indicateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndicateurMockMvc.perform(put("/api/indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indicateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Indicateur in the database
        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIndicateur() throws Exception {
        // Initialize the database
        indicateurRepository.saveAndFlush(indicateur);

        int databaseSizeBeforeDelete = indicateurRepository.findAll().size();

        // Delete the indicateur
        restIndicateurMockMvc.perform(delete("/api/indicateurs/{id}", indicateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Indicateur> indicateurList = indicateurRepository.findAll();
        assertThat(indicateurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Indicateur.class);
        Indicateur indicateur1 = new Indicateur();
        indicateur1.setId(1L);
        Indicateur indicateur2 = new Indicateur();
        indicateur2.setId(indicateur1.getId());
        assertThat(indicateur1).isEqualTo(indicateur2);
        indicateur2.setId(2L);
        assertThat(indicateur1).isNotEqualTo(indicateur2);
        indicateur1.setId(null);
        assertThat(indicateur1).isNotEqualTo(indicateur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndicateurDTO.class);
        IndicateurDTO indicateurDTO1 = new IndicateurDTO();
        indicateurDTO1.setId(1L);
        IndicateurDTO indicateurDTO2 = new IndicateurDTO();
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
        indicateurDTO2.setId(indicateurDTO1.getId());
        assertThat(indicateurDTO1).isEqualTo(indicateurDTO2);
        indicateurDTO2.setId(2L);
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
        indicateurDTO1.setId(null);
        assertThat(indicateurDTO1).isNotEqualTo(indicateurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(indicateurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(indicateurMapper.fromId(null)).isNull();
    }
}
