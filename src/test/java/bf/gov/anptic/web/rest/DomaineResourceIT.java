package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Domaine;
import bf.gov.anptic.repository.DomaineRepository;
import bf.gov.anptic.service.DomaineService;
import bf.gov.anptic.service.dto.DomaineDTO;
import bf.gov.anptic.service.mapper.DomaineMapper;
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
 * Integration tests for the {@link DomaineResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class DomaineResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private DomaineRepository domaineRepository;

    @Autowired
    private DomaineMapper domaineMapper;

    @Autowired
    private DomaineService domaineService;

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

    private MockMvc restDomaineMockMvc;

    private Domaine domaine;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DomaineResource domaineResource = new DomaineResource(domaineService);
        this.restDomaineMockMvc = MockMvcBuilders.standaloneSetup(domaineResource)
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
    public static Domaine createEntity(EntityManager em) {
        Domaine domaine = new Domaine()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .deleted(DEFAULT_DELETED);
        return domaine;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domaine createUpdatedEntity(EntityManager em) {
        Domaine domaine = new Domaine()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .deleted(UPDATED_DELETED);
        return domaine;
    }

    @BeforeEach
    public void initTest() {
        domaine = createEntity(em);
    }

    @Test
    @Transactional
    public void createDomaine() throws Exception {
        int databaseSizeBeforeCreate = domaineRepository.findAll().size();

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);
        restDomaineMockMvc.perform(post("/api/domaines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineDTO)))
            .andExpect(status().isCreated());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate + 1);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDomaine.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDomaine.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDomaine.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testDomaine.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createDomaineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = domaineRepository.findAll().size();

        // Create the Domaine with an existing ID
        domaine.setId(1L);
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineMockMvc.perform(post("/api/domaines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = domaineRepository.findAll().size();
        // set the field null
        domaine.setLibelle(null);

        // Create the Domaine, which fails.
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        restDomaineMockMvc.perform(post("/api/domaines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineDTO)))
            .andExpect(status().isBadRequest());

        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDomaines() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get all the domaineList
        restDomaineMockMvc.perform(get("/api/domaines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaine.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        // Get the domaine
        restDomaineMockMvc.perform(get("/api/domaines/{id}", domaine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(domaine.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDomaine() throws Exception {
        // Get the domaine
        restDomaineMockMvc.perform(get("/api/domaines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Update the domaine
        Domaine updatedDomaine = domaineRepository.findById(domaine.getId()).get();
        // Disconnect from session so that the updates on updatedDomaine are not directly saved in db
        em.detach(updatedDomaine);
        updatedDomaine
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .deleted(UPDATED_DELETED);
        DomaineDTO domaineDTO = domaineMapper.toDto(updatedDomaine);

        restDomaineMockMvc.perform(put("/api/domaines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineDTO)))
            .andExpect(status().isOk());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
        Domaine testDomaine = domaineList.get(domaineList.size() - 1);
        assertThat(testDomaine.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDomaine.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDomaine.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDomaine.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testDomaine.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingDomaine() throws Exception {
        int databaseSizeBeforeUpdate = domaineRepository.findAll().size();

        // Create the Domaine
        DomaineDTO domaineDTO = domaineMapper.toDto(domaine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineMockMvc.perform(put("/api/domaines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(domaineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Domaine in the database
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDomaine() throws Exception {
        // Initialize the database
        domaineRepository.saveAndFlush(domaine);

        int databaseSizeBeforeDelete = domaineRepository.findAll().size();

        // Delete the domaine
        restDomaineMockMvc.perform(delete("/api/domaines/{id}", domaine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domaine> domaineList = domaineRepository.findAll();
        assertThat(domaineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domaine.class);
        Domaine domaine1 = new Domaine();
        domaine1.setId(1L);
        Domaine domaine2 = new Domaine();
        domaine2.setId(domaine1.getId());
        assertThat(domaine1).isEqualTo(domaine2);
        domaine2.setId(2L);
        assertThat(domaine1).isNotEqualTo(domaine2);
        domaine1.setId(null);
        assertThat(domaine1).isNotEqualTo(domaine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomaineDTO.class);
        DomaineDTO domaineDTO1 = new DomaineDTO();
        domaineDTO1.setId(1L);
        DomaineDTO domaineDTO2 = new DomaineDTO();
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
        domaineDTO2.setId(domaineDTO1.getId());
        assertThat(domaineDTO1).isEqualTo(domaineDTO2);
        domaineDTO2.setId(2L);
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
        domaineDTO1.setId(null);
        assertThat(domaineDTO1).isNotEqualTo(domaineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(domaineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(domaineMapper.fromId(null)).isNull();
    }
}
