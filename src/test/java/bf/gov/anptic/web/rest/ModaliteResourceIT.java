package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Modalite;
import bf.gov.anptic.repository.ModaliteRepository;
import bf.gov.anptic.service.ModaliteService;
import bf.gov.anptic.service.dto.ModaliteDTO;
import bf.gov.anptic.service.mapper.ModaliteMapper;
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
 * Integration tests for the {@link ModaliteResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class ModaliteResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_BORNE_MAXIMALE = 1D;
    private static final Double UPDATED_BORNE_MAXIMALE = 2D;

    private static final Double DEFAULT_BORNE_MINIMALE = 1D;
    private static final Double UPDATED_BORNE_MINIMALE = 2D;

    private static final Double DEFAULT_VALEUR = 1D;
    private static final Double UPDATED_VALEUR = 2D;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private ModaliteRepository modaliteRepository;

    @Autowired
    private ModaliteMapper modaliteMapper;

    @Autowired
    private ModaliteService modaliteService;

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

    private MockMvc restModaliteMockMvc;

    private Modalite modalite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModaliteResource modaliteResource = new ModaliteResource(modaliteService);
        this.restModaliteMockMvc = MockMvcBuilders.standaloneSetup(modaliteResource)
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
    public static Modalite createEntity(EntityManager em) {
        Modalite modalite = new Modalite()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .borneMaximale(DEFAULT_BORNE_MAXIMALE)
            .borneMinimale(DEFAULT_BORNE_MINIMALE)
            .valeur(DEFAULT_VALEUR)
            .deleted(DEFAULT_DELETED);
        return modalite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Modalite createUpdatedEntity(EntityManager em) {
        Modalite modalite = new Modalite()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .borneMaximale(UPDATED_BORNE_MAXIMALE)
            .borneMinimale(UPDATED_BORNE_MINIMALE)
            .valeur(UPDATED_VALEUR)
            .deleted(UPDATED_DELETED);
        return modalite;
    }

    @BeforeEach
    public void initTest() {
        modalite = createEntity(em);
    }

    @Test
    @Transactional
    public void createModalite() throws Exception {
        int databaseSizeBeforeCreate = modaliteRepository.findAll().size();

        // Create the Modalite
        ModaliteDTO modaliteDTO = modaliteMapper.toDto(modalite);
        restModaliteMockMvc.perform(post("/api/modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modaliteDTO)))
            .andExpect(status().isCreated());

        // Validate the Modalite in the database
        List<Modalite> modaliteList = modaliteRepository.findAll();
        assertThat(modaliteList).hasSize(databaseSizeBeforeCreate + 1);
        Modalite testModalite = modaliteList.get(modaliteList.size() - 1);
        assertThat(testModalite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModalite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testModalite.getBorneMaximale()).isEqualTo(DEFAULT_BORNE_MAXIMALE);
        assertThat(testModalite.getBorneMinimale()).isEqualTo(DEFAULT_BORNE_MINIMALE);
        assertThat(testModalite.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testModalite.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createModaliteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modaliteRepository.findAll().size();

        // Create the Modalite with an existing ID
        modalite.setId(1L);
        ModaliteDTO modaliteDTO = modaliteMapper.toDto(modalite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModaliteMockMvc.perform(post("/api/modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modaliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Modalite in the database
        List<Modalite> modaliteList = modaliteRepository.findAll();
        assertThat(modaliteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllModalites() throws Exception {
        // Initialize the database
        modaliteRepository.saveAndFlush(modalite);

        // Get all the modaliteList
        restModaliteMockMvc.perform(get("/api/modalites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modalite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].borneMaximale").value(hasItem(DEFAULT_BORNE_MAXIMALE.doubleValue())))
            .andExpect(jsonPath("$.[*].borneMinimale").value(hasItem(DEFAULT_BORNE_MINIMALE.doubleValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getModalite() throws Exception {
        // Initialize the database
        modaliteRepository.saveAndFlush(modalite);

        // Get the modalite
        restModaliteMockMvc.perform(get("/api/modalites/{id}", modalite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modalite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.borneMaximale").value(DEFAULT_BORNE_MAXIMALE.doubleValue()))
            .andExpect(jsonPath("$.borneMinimale").value(DEFAULT_BORNE_MINIMALE.doubleValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModalite() throws Exception {
        // Get the modalite
        restModaliteMockMvc.perform(get("/api/modalites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModalite() throws Exception {
        // Initialize the database
        modaliteRepository.saveAndFlush(modalite);

        int databaseSizeBeforeUpdate = modaliteRepository.findAll().size();

        // Update the modalite
        Modalite updatedModalite = modaliteRepository.findById(modalite.getId()).get();
        // Disconnect from session so that the updates on updatedModalite are not directly saved in db
        em.detach(updatedModalite);
        updatedModalite
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .borneMaximale(UPDATED_BORNE_MAXIMALE)
            .borneMinimale(UPDATED_BORNE_MINIMALE)
            .valeur(UPDATED_VALEUR)
            .deleted(UPDATED_DELETED);
        ModaliteDTO modaliteDTO = modaliteMapper.toDto(updatedModalite);

        restModaliteMockMvc.perform(put("/api/modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modaliteDTO)))
            .andExpect(status().isOk());

        // Validate the Modalite in the database
        List<Modalite> modaliteList = modaliteRepository.findAll();
        assertThat(modaliteList).hasSize(databaseSizeBeforeUpdate);
        Modalite testModalite = modaliteList.get(modaliteList.size() - 1);
        assertThat(testModalite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModalite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testModalite.getBorneMaximale()).isEqualTo(UPDATED_BORNE_MAXIMALE);
        assertThat(testModalite.getBorneMinimale()).isEqualTo(UPDATED_BORNE_MINIMALE);
        assertThat(testModalite.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testModalite.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingModalite() throws Exception {
        int databaseSizeBeforeUpdate = modaliteRepository.findAll().size();

        // Create the Modalite
        ModaliteDTO modaliteDTO = modaliteMapper.toDto(modalite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModaliteMockMvc.perform(put("/api/modalites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modaliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Modalite in the database
        List<Modalite> modaliteList = modaliteRepository.findAll();
        assertThat(modaliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModalite() throws Exception {
        // Initialize the database
        modaliteRepository.saveAndFlush(modalite);

        int databaseSizeBeforeDelete = modaliteRepository.findAll().size();

        // Delete the modalite
        restModaliteMockMvc.perform(delete("/api/modalites/{id}", modalite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Modalite> modaliteList = modaliteRepository.findAll();
        assertThat(modaliteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modalite.class);
        Modalite modalite1 = new Modalite();
        modalite1.setId(1L);
        Modalite modalite2 = new Modalite();
        modalite2.setId(modalite1.getId());
        assertThat(modalite1).isEqualTo(modalite2);
        modalite2.setId(2L);
        assertThat(modalite1).isNotEqualTo(modalite2);
        modalite1.setId(null);
        assertThat(modalite1).isNotEqualTo(modalite2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModaliteDTO.class);
        ModaliteDTO modaliteDTO1 = new ModaliteDTO();
        modaliteDTO1.setId(1L);
        ModaliteDTO modaliteDTO2 = new ModaliteDTO();
        assertThat(modaliteDTO1).isNotEqualTo(modaliteDTO2);
        modaliteDTO2.setId(modaliteDTO1.getId());
        assertThat(modaliteDTO1).isEqualTo(modaliteDTO2);
        modaliteDTO2.setId(2L);
        assertThat(modaliteDTO1).isNotEqualTo(modaliteDTO2);
        modaliteDTO1.setId(null);
        assertThat(modaliteDTO1).isNotEqualTo(modaliteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(modaliteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(modaliteMapper.fromId(null)).isNull();
    }
}
