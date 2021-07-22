package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.TypeIndicateur;
import bf.gov.anptic.repository.TypeIndicateurRepository;
import bf.gov.anptic.service.TypeIndicateurService;
import bf.gov.anptic.service.dto.TypeIndicateurDTO;
import bf.gov.anptic.service.mapper.TypeIndicateurMapper;
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
 * Integration tests for the {@link TypeIndicateurResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class TypeIndicateurResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private TypeIndicateurRepository typeIndicateurRepository;

    @Autowired
    private TypeIndicateurMapper typeIndicateurMapper;

    @Autowired
    private TypeIndicateurService typeIndicateurService;

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

    private MockMvc restTypeIndicateurMockMvc;

    private TypeIndicateur typeIndicateur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeIndicateurResource typeIndicateurResource = new TypeIndicateurResource(typeIndicateurService);
        this.restTypeIndicateurMockMvc = MockMvcBuilders.standaloneSetup(typeIndicateurResource)
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
    public static TypeIndicateur createEntity(EntityManager em) {
        TypeIndicateur typeIndicateur = new TypeIndicateur()
            .libelle(DEFAULT_LIBELLE)
            .deleted(DEFAULT_DELETED);
        return typeIndicateur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeIndicateur createUpdatedEntity(EntityManager em) {
        TypeIndicateur typeIndicateur = new TypeIndicateur()
            .libelle(UPDATED_LIBELLE)
            .deleted(UPDATED_DELETED);
        return typeIndicateur;
    }

    @BeforeEach
    public void initTest() {
        typeIndicateur = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeIndicateur() throws Exception {
        int databaseSizeBeforeCreate = typeIndicateurRepository.findAll().size();

        // Create the TypeIndicateur
        TypeIndicateurDTO typeIndicateurDTO = typeIndicateurMapper.toDto(typeIndicateur);
        restTypeIndicateurMockMvc.perform(post("/api/type-indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndicateurDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeIndicateur in the database
        List<TypeIndicateur> typeIndicateurList = typeIndicateurRepository.findAll();
        assertThat(typeIndicateurList).hasSize(databaseSizeBeforeCreate + 1);
        TypeIndicateur testTypeIndicateur = typeIndicateurList.get(typeIndicateurList.size() - 1);
        assertThat(testTypeIndicateur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeIndicateur.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createTypeIndicateurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeIndicateurRepository.findAll().size();

        // Create the TypeIndicateur with an existing ID
        typeIndicateur.setId(1L);
        TypeIndicateurDTO typeIndicateurDTO = typeIndicateurMapper.toDto(typeIndicateur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeIndicateurMockMvc.perform(post("/api/type-indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndicateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeIndicateur in the database
        List<TypeIndicateur> typeIndicateurList = typeIndicateurRepository.findAll();
        assertThat(typeIndicateurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeIndicateurs() throws Exception {
        // Initialize the database
        typeIndicateurRepository.saveAndFlush(typeIndicateur);

        // Get all the typeIndicateurList
        restTypeIndicateurMockMvc.perform(get("/api/type-indicateurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeIndicateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getTypeIndicateur() throws Exception {
        // Initialize the database
        typeIndicateurRepository.saveAndFlush(typeIndicateur);

        // Get the typeIndicateur
        restTypeIndicateurMockMvc.perform(get("/api/type-indicateurs/{id}", typeIndicateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeIndicateur.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeIndicateur() throws Exception {
        // Get the typeIndicateur
        restTypeIndicateurMockMvc.perform(get("/api/type-indicateurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeIndicateur() throws Exception {
        // Initialize the database
        typeIndicateurRepository.saveAndFlush(typeIndicateur);

        int databaseSizeBeforeUpdate = typeIndicateurRepository.findAll().size();

        // Update the typeIndicateur
        TypeIndicateur updatedTypeIndicateur = typeIndicateurRepository.findById(typeIndicateur.getId()).get();
        // Disconnect from session so that the updates on updatedTypeIndicateur are not directly saved in db
        em.detach(updatedTypeIndicateur);
        updatedTypeIndicateur
            .libelle(UPDATED_LIBELLE)
            .deleted(UPDATED_DELETED);
        TypeIndicateurDTO typeIndicateurDTO = typeIndicateurMapper.toDto(updatedTypeIndicateur);

        restTypeIndicateurMockMvc.perform(put("/api/type-indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndicateurDTO)))
            .andExpect(status().isOk());

        // Validate the TypeIndicateur in the database
        List<TypeIndicateur> typeIndicateurList = typeIndicateurRepository.findAll();
        assertThat(typeIndicateurList).hasSize(databaseSizeBeforeUpdate);
        TypeIndicateur testTypeIndicateur = typeIndicateurList.get(typeIndicateurList.size() - 1);
        assertThat(testTypeIndicateur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeIndicateur.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeIndicateur() throws Exception {
        int databaseSizeBeforeUpdate = typeIndicateurRepository.findAll().size();

        // Create the TypeIndicateur
        TypeIndicateurDTO typeIndicateurDTO = typeIndicateurMapper.toDto(typeIndicateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeIndicateurMockMvc.perform(put("/api/type-indicateurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeIndicateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeIndicateur in the database
        List<TypeIndicateur> typeIndicateurList = typeIndicateurRepository.findAll();
        assertThat(typeIndicateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeIndicateur() throws Exception {
        // Initialize the database
        typeIndicateurRepository.saveAndFlush(typeIndicateur);

        int databaseSizeBeforeDelete = typeIndicateurRepository.findAll().size();

        // Delete the typeIndicateur
        restTypeIndicateurMockMvc.perform(delete("/api/type-indicateurs/{id}", typeIndicateur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeIndicateur> typeIndicateurList = typeIndicateurRepository.findAll();
        assertThat(typeIndicateurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeIndicateur.class);
        TypeIndicateur typeIndicateur1 = new TypeIndicateur();
        typeIndicateur1.setId(1L);
        TypeIndicateur typeIndicateur2 = new TypeIndicateur();
        typeIndicateur2.setId(typeIndicateur1.getId());
        assertThat(typeIndicateur1).isEqualTo(typeIndicateur2);
        typeIndicateur2.setId(2L);
        assertThat(typeIndicateur1).isNotEqualTo(typeIndicateur2);
        typeIndicateur1.setId(null);
        assertThat(typeIndicateur1).isNotEqualTo(typeIndicateur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeIndicateurDTO.class);
        TypeIndicateurDTO typeIndicateurDTO1 = new TypeIndicateurDTO();
        typeIndicateurDTO1.setId(1L);
        TypeIndicateurDTO typeIndicateurDTO2 = new TypeIndicateurDTO();
        assertThat(typeIndicateurDTO1).isNotEqualTo(typeIndicateurDTO2);
        typeIndicateurDTO2.setId(typeIndicateurDTO1.getId());
        assertThat(typeIndicateurDTO1).isEqualTo(typeIndicateurDTO2);
        typeIndicateurDTO2.setId(2L);
        assertThat(typeIndicateurDTO1).isNotEqualTo(typeIndicateurDTO2);
        typeIndicateurDTO1.setId(null);
        assertThat(typeIndicateurDTO1).isNotEqualTo(typeIndicateurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeIndicateurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeIndicateurMapper.fromId(null)).isNull();
    }
}
