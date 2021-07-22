package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Poster;
import bf.gov.anptic.repository.PosterRepository;
import bf.gov.anptic.service.PosterService;
import bf.gov.anptic.service.dto.PosterDTO;
import bf.gov.anptic.service.mapper.PosterMapper;
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
 * Integration tests for the {@link PosterResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class PosterResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENU = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENU = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENU_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENU_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private PosterRepository posterRepository;

    @Autowired
    private PosterMapper posterMapper;

    @Autowired
    private PosterService posterService;

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

    private MockMvc restPosterMockMvc;

    private Poster poster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PosterResource posterResource = new PosterResource(posterService);
        this.restPosterMockMvc = MockMvcBuilders.standaloneSetup(posterResource)
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
    public static Poster createEntity(EntityManager em) {
        Poster poster = new Poster()
            .url(DEFAULT_URL)
            .contenu(DEFAULT_CONTENU)
            .contenuContentType(DEFAULT_CONTENU_CONTENT_TYPE)
            .deleted(DEFAULT_DELETED);
        return poster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Poster createUpdatedEntity(EntityManager em) {
        Poster poster = new Poster()
            .url(UPDATED_URL)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .deleted(UPDATED_DELETED);
        return poster;
    }

    @BeforeEach
    public void initTest() {
        poster = createEntity(em);
    }

    @Test
    @Transactional
    public void createPoster() throws Exception {
        int databaseSizeBeforeCreate = posterRepository.findAll().size();

        // Create the Poster
        PosterDTO posterDTO = posterMapper.toDto(poster);
        restPosterMockMvc.perform(post("/api/posters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posterDTO)))
            .andExpect(status().isCreated());

        // Validate the Poster in the database
        List<Poster> posterList = posterRepository.findAll();
        assertThat(posterList).hasSize(databaseSizeBeforeCreate + 1);
        Poster testPoster = posterList.get(posterList.size() - 1);
        assertThat(testPoster.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testPoster.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testPoster.getContenuContentType()).isEqualTo(DEFAULT_CONTENU_CONTENT_TYPE);
        assertThat(testPoster.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createPosterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = posterRepository.findAll().size();

        // Create the Poster with an existing ID
        poster.setId(1L);
        PosterDTO posterDTO = posterMapper.toDto(poster);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPosterMockMvc.perform(post("/api/posters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Poster in the database
        List<Poster> posterList = posterRepository.findAll();
        assertThat(posterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPosters() throws Exception {
        // Initialize the database
        posterRepository.saveAndFlush(poster);

        // Get all the posterList
        restPosterMockMvc.perform(get("/api/posters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poster.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].contenuContentType").value(hasItem(DEFAULT_CONTENU_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENU))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPoster() throws Exception {
        // Initialize the database
        posterRepository.saveAndFlush(poster);

        // Get the poster
        restPosterMockMvc.perform(get("/api/posters/{id}", poster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(poster.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.contenuContentType").value(DEFAULT_CONTENU_CONTENT_TYPE))
            .andExpect(jsonPath("$.contenu").value(Base64Utils.encodeToString(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPoster() throws Exception {
        // Get the poster
        restPosterMockMvc.perform(get("/api/posters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePoster() throws Exception {
        // Initialize the database
        posterRepository.saveAndFlush(poster);

        int databaseSizeBeforeUpdate = posterRepository.findAll().size();

        // Update the poster
        Poster updatedPoster = posterRepository.findById(poster.getId()).get();
        // Disconnect from session so that the updates on updatedPoster are not directly saved in db
        em.detach(updatedPoster);
        updatedPoster
            .url(UPDATED_URL)
            .contenu(UPDATED_CONTENU)
            .contenuContentType(UPDATED_CONTENU_CONTENT_TYPE)
            .deleted(UPDATED_DELETED);
        PosterDTO posterDTO = posterMapper.toDto(updatedPoster);

        restPosterMockMvc.perform(put("/api/posters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posterDTO)))
            .andExpect(status().isOk());

        // Validate the Poster in the database
        List<Poster> posterList = posterRepository.findAll();
        assertThat(posterList).hasSize(databaseSizeBeforeUpdate);
        Poster testPoster = posterList.get(posterList.size() - 1);
        assertThat(testPoster.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testPoster.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testPoster.getContenuContentType()).isEqualTo(UPDATED_CONTENU_CONTENT_TYPE);
        assertThat(testPoster.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingPoster() throws Exception {
        int databaseSizeBeforeUpdate = posterRepository.findAll().size();

        // Create the Poster
        PosterDTO posterDTO = posterMapper.toDto(poster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPosterMockMvc.perform(put("/api/posters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(posterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Poster in the database
        List<Poster> posterList = posterRepository.findAll();
        assertThat(posterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePoster() throws Exception {
        // Initialize the database
        posterRepository.saveAndFlush(poster);

        int databaseSizeBeforeDelete = posterRepository.findAll().size();

        // Delete the poster
        restPosterMockMvc.perform(delete("/api/posters/{id}", poster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Poster> posterList = posterRepository.findAll();
        assertThat(posterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poster.class);
        Poster poster1 = new Poster();
        poster1.setId(1L);
        Poster poster2 = new Poster();
        poster2.setId(poster1.getId());
        assertThat(poster1).isEqualTo(poster2);
        poster2.setId(2L);
        assertThat(poster1).isNotEqualTo(poster2);
        poster1.setId(null);
        assertThat(poster1).isNotEqualTo(poster2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PosterDTO.class);
        PosterDTO posterDTO1 = new PosterDTO();
        posterDTO1.setId(1L);
        PosterDTO posterDTO2 = new PosterDTO();
        assertThat(posterDTO1).isNotEqualTo(posterDTO2);
        posterDTO2.setId(posterDTO1.getId());
        assertThat(posterDTO1).isEqualTo(posterDTO2);
        posterDTO2.setId(2L);
        assertThat(posterDTO1).isNotEqualTo(posterDTO2);
        posterDTO1.setId(null);
        assertThat(posterDTO1).isNotEqualTo(posterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(posterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(posterMapper.fromId(null)).isNull();
    }
}
