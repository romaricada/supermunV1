package bf.gov.anptic.web.rest;

import bf.gov.anptic.SupermunApp;
import bf.gov.anptic.domain.Information;
import bf.gov.anptic.repository.InformationRepository;
import bf.gov.anptic.service.InformationService;
import bf.gov.anptic.service.dto.InformationDTO;
import bf.gov.anptic.service.mapper.InformationMapper;
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
 * Integration tests for the {@link InformationResource} REST controller.
 */
@SpringBootTest(classes = SupermunApp.class)
public class InformationResourceIT {

    private static final String DEFAULT_PRESENTATION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private InformationRepository informationRepository;

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private InformationService informationService;

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

    private MockMvc restInformationMockMvc;

    private Information information;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InformationResource informationResource = new InformationResource(informationService);
        this.restInformationMockMvc = MockMvcBuilders.standaloneSetup(informationResource)
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
    public static Information createEntity(EntityManager em) {
        Information information = new Information()
            .presentation(DEFAULT_PRESENTATION)
            .contact(DEFAULT_CONTACT)
            .deleted(DEFAULT_DELETED);
        return information;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createUpdatedEntity(EntityManager em) {
        Information information = new Information()
            .presentation(UPDATED_PRESENTATION)
            .contact(UPDATED_CONTACT)
            .deleted(UPDATED_DELETED);
        return information;
    }

    @BeforeEach
    public void initTest() {
        information = createEntity(em);
    }

    @Test
    @Transactional
    public void createInformation() throws Exception {
        int databaseSizeBeforeCreate = informationRepository.findAll().size();

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);
        restInformationMockMvc.perform(post("/api/information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isCreated());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeCreate + 1);
        Information testInformation = informationList.get(informationList.size() - 1);
        assertThat(testInformation.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
        assertThat(testInformation.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testInformation.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = informationRepository.findAll().size();

        // Create the Information with an existing ID
        information.setId(1L);
        InformationDTO informationDTO = informationMapper.toDto(information);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationMockMvc.perform(post("/api/information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        // Get all the informationList
        restInformationMockMvc.perform(get("/api/information?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(information.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        // Get the information
        restInformationMockMvc.perform(get("/api/information/{id}", information.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(information.getId().intValue()))
            .andExpect(jsonPath("$.presentation").value(DEFAULT_PRESENTATION))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInformation() throws Exception {
        // Get the information
        restInformationMockMvc.perform(get("/api/information/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        int databaseSizeBeforeUpdate = informationRepository.findAll().size();

        // Update the information
        Information updatedInformation = informationRepository.findById(information.getId()).get();
        // Disconnect from session so that the updates on updatedInformation are not directly saved in db
        em.detach(updatedInformation);
        updatedInformation
            .presentation(UPDATED_PRESENTATION)
            .contact(UPDATED_CONTACT)
            .deleted(UPDATED_DELETED);
        InformationDTO informationDTO = informationMapper.toDto(updatedInformation);

        restInformationMockMvc.perform(put("/api/information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isOk());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeUpdate);
        Information testInformation = informationList.get(informationList.size() - 1);
        assertThat(testInformation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
        assertThat(testInformation.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testInformation.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingInformation() throws Exception {
        int databaseSizeBeforeUpdate = informationRepository.findAll().size();

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc.perform(put("/api/information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInformation() throws Exception {
        // Initialize the database
        informationRepository.saveAndFlush(information);

        int databaseSizeBeforeDelete = informationRepository.findAll().size();

        // Delete the information
        restInformationMockMvc.perform(delete("/api/information/{id}", information.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Information> informationList = informationRepository.findAll();
        assertThat(informationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Information.class);
        Information information1 = new Information();
        information1.setId(1L);
        Information information2 = new Information();
        information2.setId(information1.getId());
        assertThat(information1).isEqualTo(information2);
        information2.setId(2L);
        assertThat(information1).isNotEqualTo(information2);
        information1.setId(null);
        assertThat(information1).isNotEqualTo(information2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformationDTO.class);
        InformationDTO informationDTO1 = new InformationDTO();
        informationDTO1.setId(1L);
        InformationDTO informationDTO2 = new InformationDTO();
        assertThat(informationDTO1).isNotEqualTo(informationDTO2);
        informationDTO2.setId(informationDTO1.getId());
        assertThat(informationDTO1).isEqualTo(informationDTO2);
        informationDTO2.setId(2L);
        assertThat(informationDTO1).isNotEqualTo(informationDTO2);
        informationDTO1.setId(null);
        assertThat(informationDTO1).isNotEqualTo(informationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(informationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(informationMapper.fromId(null)).isNull();
    }
}
