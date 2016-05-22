package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.AccordMapping;
import com.agencylot.repository.AccordMappingRepository;
import com.agencylot.repository.search.AccordMappingSearchRepository;

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
 * Test class for the AccordMappingResource REST controller.
 *
 * @see AccordMappingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class AccordMappingResourceIntTest {

    private static final String DEFAULT_INPUT = "AAAAA";
    private static final String UPDATED_INPUT = "BBBBB";
    private static final String DEFAULT_OUTPUT = "AAAAA";
    private static final String UPDATED_OUTPUT = "BBBBB";
    private static final String DEFAULT_ACCROD_VERSION = "AAAAA";
    private static final String UPDATED_ACCROD_VERSION = "BBBBB";

    @Inject
    private AccordMappingRepository accordMappingRepository;

    @Inject
    private AccordMappingSearchRepository accordMappingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAccordMappingMockMvc;

    private AccordMapping accordMapping;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccordMappingResource accordMappingResource = new AccordMappingResource();
        ReflectionTestUtils.setField(accordMappingResource, "accordMappingSearchRepository", accordMappingSearchRepository);
        ReflectionTestUtils.setField(accordMappingResource, "accordMappingRepository", accordMappingRepository);
        this.restAccordMappingMockMvc = MockMvcBuilders.standaloneSetup(accordMappingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accordMappingSearchRepository.deleteAll();
        accordMapping = new AccordMapping();
        accordMapping.setInput(DEFAULT_INPUT);
        accordMapping.setOutput(DEFAULT_OUTPUT);
        accordMapping.setAccrodVersion(DEFAULT_ACCROD_VERSION);
    }

    @Test
    @Transactional
    public void createAccordMapping() throws Exception {
        int databaseSizeBeforeCreate = accordMappingRepository.findAll().size();

        // Create the AccordMapping

        restAccordMappingMockMvc.perform(post("/api/accord-mappings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(accordMapping)))
                .andExpect(status().isCreated());

        // Validate the AccordMapping in the database
        List<AccordMapping> accordMappings = accordMappingRepository.findAll();
        assertThat(accordMappings).hasSize(databaseSizeBeforeCreate + 1);
        AccordMapping testAccordMapping = accordMappings.get(accordMappings.size() - 1);
        assertThat(testAccordMapping.getInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testAccordMapping.getOutput()).isEqualTo(DEFAULT_OUTPUT);
        assertThat(testAccordMapping.getAccrodVersion()).isEqualTo(DEFAULT_ACCROD_VERSION);

        // Validate the AccordMapping in ElasticSearch
        AccordMapping accordMappingEs = accordMappingSearchRepository.findOne(testAccordMapping.getId());
        assertThat(accordMappingEs).isEqualToComparingFieldByField(testAccordMapping);
    }

    @Test
    @Transactional
    public void getAllAccordMappings() throws Exception {
        // Initialize the database
        accordMappingRepository.saveAndFlush(accordMapping);

        // Get all the accordMappings
        restAccordMappingMockMvc.perform(get("/api/accord-mappings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accordMapping.getId().intValue())))
                .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.toString())))
                .andExpect(jsonPath("$.[*].output").value(hasItem(DEFAULT_OUTPUT.toString())))
                .andExpect(jsonPath("$.[*].accrodVersion").value(hasItem(DEFAULT_ACCROD_VERSION.toString())));
    }

    @Test
    @Transactional
    public void getAccordMapping() throws Exception {
        // Initialize the database
        accordMappingRepository.saveAndFlush(accordMapping);

        // Get the accordMapping
        restAccordMappingMockMvc.perform(get("/api/accord-mappings/{id}", accordMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(accordMapping.getId().intValue()))
            .andExpect(jsonPath("$.input").value(DEFAULT_INPUT.toString()))
            .andExpect(jsonPath("$.output").value(DEFAULT_OUTPUT.toString()))
            .andExpect(jsonPath("$.accrodVersion").value(DEFAULT_ACCROD_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccordMapping() throws Exception {
        // Get the accordMapping
        restAccordMappingMockMvc.perform(get("/api/accord-mappings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccordMapping() throws Exception {
        // Initialize the database
        accordMappingRepository.saveAndFlush(accordMapping);
        accordMappingSearchRepository.save(accordMapping);
        int databaseSizeBeforeUpdate = accordMappingRepository.findAll().size();

        // Update the accordMapping
        AccordMapping updatedAccordMapping = new AccordMapping();
        updatedAccordMapping.setId(accordMapping.getId());
        updatedAccordMapping.setInput(UPDATED_INPUT);
        updatedAccordMapping.setOutput(UPDATED_OUTPUT);
        updatedAccordMapping.setAccrodVersion(UPDATED_ACCROD_VERSION);

        restAccordMappingMockMvc.perform(put("/api/accord-mappings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAccordMapping)))
                .andExpect(status().isOk());

        // Validate the AccordMapping in the database
        List<AccordMapping> accordMappings = accordMappingRepository.findAll();
        assertThat(accordMappings).hasSize(databaseSizeBeforeUpdate);
        AccordMapping testAccordMapping = accordMappings.get(accordMappings.size() - 1);
        assertThat(testAccordMapping.getInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testAccordMapping.getOutput()).isEqualTo(UPDATED_OUTPUT);
        assertThat(testAccordMapping.getAccrodVersion()).isEqualTo(UPDATED_ACCROD_VERSION);

        // Validate the AccordMapping in ElasticSearch
        AccordMapping accordMappingEs = accordMappingSearchRepository.findOne(testAccordMapping.getId());
        assertThat(accordMappingEs).isEqualToComparingFieldByField(testAccordMapping);
    }

    @Test
    @Transactional
    public void deleteAccordMapping() throws Exception {
        // Initialize the database
        accordMappingRepository.saveAndFlush(accordMapping);
        accordMappingSearchRepository.save(accordMapping);
        int databaseSizeBeforeDelete = accordMappingRepository.findAll().size();

        // Get the accordMapping
        restAccordMappingMockMvc.perform(delete("/api/accord-mappings/{id}", accordMapping.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean accordMappingExistsInEs = accordMappingSearchRepository.exists(accordMapping.getId());
        assertThat(accordMappingExistsInEs).isFalse();

        // Validate the database is empty
        List<AccordMapping> accordMappings = accordMappingRepository.findAll();
        assertThat(accordMappings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAccordMapping() throws Exception {
        // Initialize the database
        accordMappingRepository.saveAndFlush(accordMapping);
        accordMappingSearchRepository.save(accordMapping);

        // Search the accordMapping
        restAccordMappingMockMvc.perform(get("/api/_search/accord-mappings?query=id:" + accordMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accordMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.toString())))
            .andExpect(jsonPath("$.[*].output").value(hasItem(DEFAULT_OUTPUT.toString())))
            .andExpect(jsonPath("$.[*].accrodVersion").value(hasItem(DEFAULT_ACCROD_VERSION.toString())));
    }
}
