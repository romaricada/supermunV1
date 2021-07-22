package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.TypePublication;
import bf.gov.anptic.repository.TypePublicationRepository;
import bf.gov.anptic.service.TypePublicationService;
import bf.gov.anptic.service.dto.TypePublicationDTO;
import bf.gov.anptic.service.mapper.TypePublicationMapper;
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
 * Integration tests for the {@link TypePublicationResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class TypePublicationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypePublicationRepository typePublicationRepository;

    @Autowired
    private TypePublicationMapper typePublicationMapper;

    @Autowired
    private TypePublicationService typePublicationService;

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

    private MockMvc restTypePublicationMockMvc;

    private TypePublication typePublication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypePublicationResource typePublicationResource = new TypePublicationResource(typePublicationService);
        this.restTypePublicationMockMvc = MockMvcBuilders.standaloneSetup(typePublicationResource)
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
    public static TypePublication createEntity(EntityManager em) {
        TypePublication typePublication = new TypePublication()
            .libelle(DEFAULT_LIBELLE);
        return typePublication;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePublication createUpdatedEntity(EntityManager em) {
        TypePublication typePublication = new TypePublication()
            .libelle(UPDATED_LIBELLE);
        return typePublication;
    }

    @BeforeEach
    public void initTest() {
        typePublication = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypePublication() throws Exception {
        int databaseSizeBeforeCreate = typePublicationRepository.findAll().size();

        // Create the TypePublication
        TypePublicationDTO typePublicationDTO = typePublicationMapper.toDto(typePublication);
        restTypePublicationMockMvc.perform(post("/api/type-publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePublicationDTO)))
            .andExpect(status().isCreated());

        // Validate the TypePublication in the database
        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeCreate + 1);
        TypePublication testTypePublication = typePublicationList.get(typePublicationList.size() - 1);
        assertThat(testTypePublication.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypePublicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typePublicationRepository.findAll().size();

        // Create the TypePublication with an existing ID
        typePublication.setId(1L);
        TypePublicationDTO typePublicationDTO = typePublicationMapper.toDto(typePublication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePublicationMockMvc.perform(post("/api/type-publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePublicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePublication in the database
        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePublicationRepository.findAll().size();
        // set the field null
        typePublication.setLibelle(null);

        // Create the TypePublication, which fails.
        TypePublicationDTO typePublicationDTO = typePublicationMapper.toDto(typePublication);

        restTypePublicationMockMvc.perform(post("/api/type-publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePublicationDTO)))
            .andExpect(status().isBadRequest());

        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypePublications() throws Exception {
        // Initialize the database
        typePublicationRepository.saveAndFlush(typePublication);

        // Get all the typePublicationList
        restTypePublicationMockMvc.perform(get("/api/type-publications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePublication.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getTypePublication() throws Exception {
        // Initialize the database
        typePublicationRepository.saveAndFlush(typePublication);

        // Get the typePublication
        restTypePublicationMockMvc.perform(get("/api/type-publications/{id}", typePublication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typePublication.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    public void getNonExistingTypePublication() throws Exception {
        // Get the typePublication
        restTypePublicationMockMvc.perform(get("/api/type-publications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypePublication() throws Exception {
        // Initialize the database
        typePublicationRepository.saveAndFlush(typePublication);

        int databaseSizeBeforeUpdate = typePublicationRepository.findAll().size();

        // Update the typePublication
        TypePublication updatedTypePublication = typePublicationRepository.findById(typePublication.getId()).get();
        // Disconnect from session so that the updates on updatedTypePublication are not directly saved in db
        em.detach(updatedTypePublication);
        updatedTypePublication
            .libelle(UPDATED_LIBELLE);
        TypePublicationDTO typePublicationDTO = typePublicationMapper.toDto(updatedTypePublication);

        restTypePublicationMockMvc.perform(put("/api/type-publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePublicationDTO)))
            .andExpect(status().isOk());

        // Validate the TypePublication in the database
        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeUpdate);
        TypePublication testTypePublication = typePublicationList.get(typePublicationList.size() - 1);
        assertThat(testTypePublication.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypePublication() throws Exception {
        int databaseSizeBeforeUpdate = typePublicationRepository.findAll().size();

        // Create the TypePublication
        TypePublicationDTO typePublicationDTO = typePublicationMapper.toDto(typePublication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePublicationMockMvc.perform(put("/api/type-publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePublicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePublication in the database
        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypePublication() throws Exception {
        // Initialize the database
        typePublicationRepository.saveAndFlush(typePublication);

        int databaseSizeBeforeDelete = typePublicationRepository.findAll().size();

        // Delete the typePublication
        restTypePublicationMockMvc.perform(delete("/api/type-publications/{id}", typePublication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePublication> typePublicationList = typePublicationRepository.findAll();
        assertThat(typePublicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePublication.class);
        TypePublication typePublication1 = new TypePublication();
        typePublication1.setId(1L);
        TypePublication typePublication2 = new TypePublication();
        typePublication2.setId(typePublication1.getId());
        assertThat(typePublication1).isEqualTo(typePublication2);
        typePublication2.setId(2L);
        assertThat(typePublication1).isNotEqualTo(typePublication2);
        typePublication1.setId(null);
        assertThat(typePublication1).isNotEqualTo(typePublication2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePublicationDTO.class);
        TypePublicationDTO typePublicationDTO1 = new TypePublicationDTO();
        typePublicationDTO1.setId(1L);
        TypePublicationDTO typePublicationDTO2 = new TypePublicationDTO();
        assertThat(typePublicationDTO1).isNotEqualTo(typePublicationDTO2);
        typePublicationDTO2.setId(typePublicationDTO1.getId());
        assertThat(typePublicationDTO1).isEqualTo(typePublicationDTO2);
        typePublicationDTO2.setId(2L);
        assertThat(typePublicationDTO1).isNotEqualTo(typePublicationDTO2);
        typePublicationDTO1.setId(null);
        assertThat(typePublicationDTO1).isNotEqualTo(typePublicationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typePublicationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typePublicationMapper.fromId(null)).isNull();
    }
}
