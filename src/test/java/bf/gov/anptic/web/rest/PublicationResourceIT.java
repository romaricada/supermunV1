package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Publication;
import bf.gov.anptic.domain.TypePublication;
import bf.gov.anptic.repository.PublicationRepository;
import bf.gov.anptic.service.PublicationService;
import bf.gov.anptic.service.dto.PublicationDTO;
import bf.gov.anptic.service.mapper.PublicationMapper;
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
 * Integration tests for the {@link PublicationResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class PublicationResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENU_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private PublicationMapper publicationMapper;

    @Autowired
    private PublicationService publicationService;

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

    private MockMvc restPublicationMockMvc;

    private Publication publication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PublicationResource publicationResource = new PublicationResource(publicationService);
        this.restPublicationMockMvc = MockMvcBuilders.standaloneSetup(publicationResource)
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
    public static Publication createEntity(EntityManager em) {
        Publication publication = new Publication()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .contenu(DEFAULT_CONTENU)
            .contenuContentType(DEFAULT_CONTENU_CONTENT_TYPE)
            .published(DEFAULT_PUBLISHED);
        // Add required entity
        TypePublication typePublication;
        if (TestUtil.findAll(em, TypePublication.class).isEmpty()) {
            typePublication = TypePublicationResourceIT.createEntity(em);
            em.persist(typePublication);
            em.flush();
        } else {
            typePublication = TestUtil.findAll(em, TypePublication.class).get(0);
        }
        publication.setTypePublication(typePublication);
        return publication;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publication createUpdatedEntity(EntityManager em) {
        Publication publication = new Publication()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .published(UPDATED_PUBLISHED);
        // Add required entity
        TypePublication typePublication;
        if (TestUtil.findAll(em, TypePublication.class).isEmpty()) {
            typePublication = TypePublicationResourceIT.createUpdatedEntity(em);
            em.persist(typePublication);
            em.flush();
        } else {
            typePublication = TestUtil.findAll(em, TypePublication.class).get(0);
        }
        publication.setTypePublication(typePublication);
        return publication;
    }

    @BeforeEach
    public void initTest() {
        publication = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublication() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);
        restPublicationMockMvc.perform(post("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isCreated());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeCreate + 1);
        Publication testPublication = publicationList.get(publicationList.size() - 1);
        assertThat(testPublication.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPublication.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPublication.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testPublication.getContenuContentType()).isEqualTo(DEFAULT_CONTENU_CONTENT_TYPE);
        assertThat(testPublication.isPublished()).isEqualTo(DEFAULT_PUBLISHED);
    }

    @Test
    @Transactional
    public void createPublicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication with an existing ID
        publication.setId(1L);
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicationMockMvc.perform(post("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPublications() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publicationList
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contenuContentType").value(hasItem(DEFAULT_CONTENU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENU))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", publication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(publication.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.contenuContentType").value(DEFAULT_CONTENU_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenu").value(Base64Utils.encodeToString(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPublication() throws Exception {
        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Update the publication
        Publication updatedPublication = publicationRepository.findById(publication.getId()).get();
        // Disconnect from session so that the updates on updatedPublication are not directly saved in db
        em.detach(updatedPublication);
        updatedPublication
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .published(UPDATED_PUBLISHED);
        PublicationDTO publicationDTO = publicationMapper.toDto(updatedPublication);

        restPublicationMockMvc.perform(put("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isOk());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeUpdate);
        Publication testPublication = publicationList.get(publicationList.size() - 1);
        assertThat(testPublication.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPublication.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPublication.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testPublication.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
        assertThat(testPublication.isPublished()).isEqualTo(UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    public void updateNonExistingPublication() throws Exception {
        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Create the Publication
        PublicationDTO publicationDTO = publicationMapper.toDto(publication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublicationMockMvc.perform(put("/api/publications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(publicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publication in the database
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        int databaseSizeBeforeDelete = publicationRepository.findAll().size();

        // Delete the publication
        restPublicationMockMvc.perform(delete("/api/publications/{id}", publication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Publication> publicationList = publicationRepository.findAll();
        assertThat(publicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publication.class);
        Publication publication1 = new Publication();
        publication1.setId(1L);
        Publication publication2 = new Publication();
        publication2.setId(publication1.getId());
        assertThat(publication1).isEqualTo(publication2);
        publication2.setId(2L);
        assertThat(publication1).isNotEqualTo(publication2);
        publication1.setId(null);
        assertThat(publication1).isNotEqualTo(publication2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicationDTO.class);
        PublicationDTO publicationDTO1 = new PublicationDTO();
        publicationDTO1.setId(1L);
        PublicationDTO publicationDTO2 = new PublicationDTO();
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
        publicationDTO2.setId(publicationDTO1.getId());
        assertThat(publicationDTO1).isEqualTo(publicationDTO2);
        publicationDTO2.setId(2L);
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
        publicationDTO1.setId(null);
        assertThat(publicationDTO1).isNotEqualTo(publicationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(publicationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(publicationMapper.fromId(null)).isNull();
    }
}
