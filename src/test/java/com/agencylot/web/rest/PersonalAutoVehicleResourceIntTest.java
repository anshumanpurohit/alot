package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.PersonalAutoVehicle;
import com.agencylot.repository.PersonalAutoVehicleRepository;
import com.agencylot.repository.search.PersonalAutoVehicleSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PersonalAutoVehicleResource REST controller.
 *
 * @see PersonalAutoVehicleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonalAutoVehicleResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final String DEFAULT_MAKE = "AAAAA";
    private static final String UPDATED_MAKE = "BBBBB";
    private static final String DEFAULT_MODEL = "AAAAA";
    private static final String UPDATED_MODEL = "BBBBB";
    private static final String DEFAULT_BODY_STYLE = "AAAAA";
    private static final String UPDATED_BODY_STYLE = "BBBBB";
    private static final String DEFAULT_SYMBOLS = "AAAAA";
    private static final String UPDATED_SYMBOLS = "BBBBB";

    @Inject
    private PersonalAutoVehicleRepository personalAutoVehicleRepository;

    @Inject
    private PersonalAutoVehicleSearchRepository personalAutoVehicleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonalAutoVehicleMockMvc;

    private PersonalAutoVehicle personalAutoVehicle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonalAutoVehicleResource personalAutoVehicleResource = new PersonalAutoVehicleResource();
        ReflectionTestUtils.setField(personalAutoVehicleResource, "personalAutoVehicleSearchRepository", personalAutoVehicleSearchRepository);
        ReflectionTestUtils.setField(personalAutoVehicleResource, "personalAutoVehicleRepository", personalAutoVehicleRepository);
        this.restPersonalAutoVehicleMockMvc = MockMvcBuilders.standaloneSetup(personalAutoVehicleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personalAutoVehicleSearchRepository.deleteAll();
        personalAutoVehicle = new PersonalAutoVehicle();
        personalAutoVehicle.setFixedId(DEFAULT_FIXED_ID);
        personalAutoVehicle.setYear(DEFAULT_YEAR);
        personalAutoVehicle.setMake(DEFAULT_MAKE);
        personalAutoVehicle.setModel(DEFAULT_MODEL);
        personalAutoVehicle.setBodyStyle(DEFAULT_BODY_STYLE);
        personalAutoVehicle.setSymbols(DEFAULT_SYMBOLS);
    }

    @Test
    @Transactional
    public void createPersonalAutoVehicle() throws Exception {
        int databaseSizeBeforeCreate = personalAutoVehicleRepository.findAll().size();

        // Create the PersonalAutoVehicle

        restPersonalAutoVehicleMockMvc.perform(post("/api/personal-auto-vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personalAutoVehicle)))
                .andExpect(status().isCreated());

        // Validate the PersonalAutoVehicle in the database
        List<PersonalAutoVehicle> personalAutoVehicles = personalAutoVehicleRepository.findAll();
        assertThat(personalAutoVehicles).hasSize(databaseSizeBeforeCreate + 1);
        PersonalAutoVehicle testPersonalAutoVehicle = personalAutoVehicles.get(personalAutoVehicles.size() - 1);
        assertThat(testPersonalAutoVehicle.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testPersonalAutoVehicle.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPersonalAutoVehicle.getMake()).isEqualTo(DEFAULT_MAKE);
        assertThat(testPersonalAutoVehicle.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testPersonalAutoVehicle.getBodyStyle()).isEqualTo(DEFAULT_BODY_STYLE);
        assertThat(testPersonalAutoVehicle.getSymbols()).isEqualTo(DEFAULT_SYMBOLS);

        // Validate the PersonalAutoVehicle in ElasticSearch
        PersonalAutoVehicle personalAutoVehicleEs = personalAutoVehicleSearchRepository.findOne(testPersonalAutoVehicle.getId());
        assertThat(personalAutoVehicleEs).isEqualToComparingFieldByField(testPersonalAutoVehicle);
    }

    @Test
    @Transactional
    public void getAllPersonalAutoVehicles() throws Exception {
        // Initialize the database
        personalAutoVehicleRepository.saveAndFlush(personalAutoVehicle);

        // Get all the personalAutoVehicles
        restPersonalAutoVehicleMockMvc.perform(get("/api/personal-auto-vehicles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personalAutoVehicle.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
                .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE.toString())))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
                .andExpect(jsonPath("$.[*].bodyStyle").value(hasItem(DEFAULT_BODY_STYLE.toString())))
                .andExpect(jsonPath("$.[*].symbols").value(hasItem(DEFAULT_SYMBOLS.toString())));
    }

    @Test
    @Transactional
    public void getPersonalAutoVehicle() throws Exception {
        // Initialize the database
        personalAutoVehicleRepository.saveAndFlush(personalAutoVehicle);

        // Get the personalAutoVehicle
        restPersonalAutoVehicleMockMvc.perform(get("/api/personal-auto-vehicles/{id}", personalAutoVehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personalAutoVehicle.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.make").value(DEFAULT_MAKE.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.bodyStyle").value(DEFAULT_BODY_STYLE.toString()))
            .andExpect(jsonPath("$.symbols").value(DEFAULT_SYMBOLS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonalAutoVehicle() throws Exception {
        // Get the personalAutoVehicle
        restPersonalAutoVehicleMockMvc.perform(get("/api/personal-auto-vehicles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonalAutoVehicle() throws Exception {
        // Initialize the database
        personalAutoVehicleRepository.saveAndFlush(personalAutoVehicle);
        personalAutoVehicleSearchRepository.save(personalAutoVehicle);
        int databaseSizeBeforeUpdate = personalAutoVehicleRepository.findAll().size();

        // Update the personalAutoVehicle
        PersonalAutoVehicle updatedPersonalAutoVehicle = new PersonalAutoVehicle();
        updatedPersonalAutoVehicle.setId(personalAutoVehicle.getId());
        updatedPersonalAutoVehicle.setFixedId(UPDATED_FIXED_ID);
        updatedPersonalAutoVehicle.setYear(UPDATED_YEAR);
        updatedPersonalAutoVehicle.setMake(UPDATED_MAKE);
        updatedPersonalAutoVehicle.setModel(UPDATED_MODEL);
        updatedPersonalAutoVehicle.setBodyStyle(UPDATED_BODY_STYLE);
        updatedPersonalAutoVehicle.setSymbols(UPDATED_SYMBOLS);

        restPersonalAutoVehicleMockMvc.perform(put("/api/personal-auto-vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPersonalAutoVehicle)))
                .andExpect(status().isOk());

        // Validate the PersonalAutoVehicle in the database
        List<PersonalAutoVehicle> personalAutoVehicles = personalAutoVehicleRepository.findAll();
        assertThat(personalAutoVehicles).hasSize(databaseSizeBeforeUpdate);
        PersonalAutoVehicle testPersonalAutoVehicle = personalAutoVehicles.get(personalAutoVehicles.size() - 1);
        assertThat(testPersonalAutoVehicle.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testPersonalAutoVehicle.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPersonalAutoVehicle.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testPersonalAutoVehicle.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testPersonalAutoVehicle.getBodyStyle()).isEqualTo(UPDATED_BODY_STYLE);
        assertThat(testPersonalAutoVehicle.getSymbols()).isEqualTo(UPDATED_SYMBOLS);

        // Validate the PersonalAutoVehicle in ElasticSearch
        PersonalAutoVehicle personalAutoVehicleEs = personalAutoVehicleSearchRepository.findOne(testPersonalAutoVehicle.getId());
        assertThat(personalAutoVehicleEs).isEqualToComparingFieldByField(testPersonalAutoVehicle);
    }

    @Test
    @Transactional
    public void deletePersonalAutoVehicle() throws Exception {
        // Initialize the database
        personalAutoVehicleRepository.saveAndFlush(personalAutoVehicle);
        personalAutoVehicleSearchRepository.save(personalAutoVehicle);
        int databaseSizeBeforeDelete = personalAutoVehicleRepository.findAll().size();

        // Get the personalAutoVehicle
        restPersonalAutoVehicleMockMvc.perform(delete("/api/personal-auto-vehicles/{id}", personalAutoVehicle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean personalAutoVehicleExistsInEs = personalAutoVehicleSearchRepository.exists(personalAutoVehicle.getId());
        assertThat(personalAutoVehicleExistsInEs).isFalse();

        // Validate the database is empty
        List<PersonalAutoVehicle> personalAutoVehicles = personalAutoVehicleRepository.findAll();
        assertThat(personalAutoVehicles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonalAutoVehicle() throws Exception {
        // Initialize the database
        personalAutoVehicleRepository.saveAndFlush(personalAutoVehicle);
        personalAutoVehicleSearchRepository.save(personalAutoVehicle);

        // Search the personalAutoVehicle
        restPersonalAutoVehicleMockMvc.perform(get("/api/_search/personal-auto-vehicles?query=id:" + personalAutoVehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalAutoVehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].bodyStyle").value(hasItem(DEFAULT_BODY_STYLE.toString())))
            .andExpect(jsonPath("$.[*].symbols").value(hasItem(DEFAULT_SYMBOLS.toString())));
    }
}
