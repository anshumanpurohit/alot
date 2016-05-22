package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.NamedInsured;
import com.agencylot.repository.NamedInsuredRepository;
import com.agencylot.repository.search.NamedInsuredSearchRepository;

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
 * Test class for the NamedInsuredResource REST controller.
 *
 * @see NamedInsuredResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class NamedInsuredResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Inject
    private NamedInsuredRepository namedInsuredRepository;

    @Inject
    private NamedInsuredSearchRepository namedInsuredSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNamedInsuredMockMvc;

    private NamedInsured namedInsured;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NamedInsuredResource namedInsuredResource = new NamedInsuredResource();
        ReflectionTestUtils.setField(namedInsuredResource, "namedInsuredSearchRepository", namedInsuredSearchRepository);
        ReflectionTestUtils.setField(namedInsuredResource, "namedInsuredRepository", namedInsuredRepository);
        this.restNamedInsuredMockMvc = MockMvcBuilders.standaloneSetup(namedInsuredResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        namedInsuredSearchRepository.deleteAll();
        namedInsured = new NamedInsured();
        namedInsured.setFixedId(DEFAULT_FIXED_ID);
        namedInsured.setDeleted(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createNamedInsured() throws Exception {
        int databaseSizeBeforeCreate = namedInsuredRepository.findAll().size();

        // Create the NamedInsured

        restNamedInsuredMockMvc.perform(post("/api/named-insureds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(namedInsured)))
                .andExpect(status().isCreated());

        // Validate the NamedInsured in the database
        List<NamedInsured> namedInsureds = namedInsuredRepository.findAll();
        assertThat(namedInsureds).hasSize(databaseSizeBeforeCreate + 1);
        NamedInsured testNamedInsured = namedInsureds.get(namedInsureds.size() - 1);
        assertThat(testNamedInsured.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testNamedInsured.isDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the NamedInsured in ElasticSearch
        NamedInsured namedInsuredEs = namedInsuredSearchRepository.findOne(testNamedInsured.getId());
        assertThat(namedInsuredEs).isEqualToComparingFieldByField(testNamedInsured);
    }

    @Test
    @Transactional
    public void getAllNamedInsureds() throws Exception {
        // Initialize the database
        namedInsuredRepository.saveAndFlush(namedInsured);

        // Get all the namedInsureds
        restNamedInsuredMockMvc.perform(get("/api/named-insureds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(namedInsured.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getNamedInsured() throws Exception {
        // Initialize the database
        namedInsuredRepository.saveAndFlush(namedInsured);

        // Get the namedInsured
        restNamedInsuredMockMvc.perform(get("/api/named-insureds/{id}", namedInsured.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(namedInsured.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNamedInsured() throws Exception {
        // Get the namedInsured
        restNamedInsuredMockMvc.perform(get("/api/named-insureds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNamedInsured() throws Exception {
        // Initialize the database
        namedInsuredRepository.saveAndFlush(namedInsured);
        namedInsuredSearchRepository.save(namedInsured);
        int databaseSizeBeforeUpdate = namedInsuredRepository.findAll().size();

        // Update the namedInsured
        NamedInsured updatedNamedInsured = new NamedInsured();
        updatedNamedInsured.setId(namedInsured.getId());
        updatedNamedInsured.setFixedId(UPDATED_FIXED_ID);
        updatedNamedInsured.setDeleted(UPDATED_DELETED);

        restNamedInsuredMockMvc.perform(put("/api/named-insureds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNamedInsured)))
                .andExpect(status().isOk());

        // Validate the NamedInsured in the database
        List<NamedInsured> namedInsureds = namedInsuredRepository.findAll();
        assertThat(namedInsureds).hasSize(databaseSizeBeforeUpdate);
        NamedInsured testNamedInsured = namedInsureds.get(namedInsureds.size() - 1);
        assertThat(testNamedInsured.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testNamedInsured.isDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the NamedInsured in ElasticSearch
        NamedInsured namedInsuredEs = namedInsuredSearchRepository.findOne(testNamedInsured.getId());
        assertThat(namedInsuredEs).isEqualToComparingFieldByField(testNamedInsured);
    }

    @Test
    @Transactional
    public void deleteNamedInsured() throws Exception {
        // Initialize the database
        namedInsuredRepository.saveAndFlush(namedInsured);
        namedInsuredSearchRepository.save(namedInsured);
        int databaseSizeBeforeDelete = namedInsuredRepository.findAll().size();

        // Get the namedInsured
        restNamedInsuredMockMvc.perform(delete("/api/named-insureds/{id}", namedInsured.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean namedInsuredExistsInEs = namedInsuredSearchRepository.exists(namedInsured.getId());
        assertThat(namedInsuredExistsInEs).isFalse();

        // Validate the database is empty
        List<NamedInsured> namedInsureds = namedInsuredRepository.findAll();
        assertThat(namedInsureds).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNamedInsured() throws Exception {
        // Initialize the database
        namedInsuredRepository.saveAndFlush(namedInsured);
        namedInsuredSearchRepository.save(namedInsured);

        // Search the namedInsured
        restNamedInsuredMockMvc.perform(get("/api/_search/named-insureds?query=id:" + namedInsured.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(namedInsured.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
