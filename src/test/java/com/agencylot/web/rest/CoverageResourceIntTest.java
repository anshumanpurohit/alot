package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Coverage;
import com.agencylot.repository.CoverageRepository;
import com.agencylot.repository.search.CoverageSearchRepository;

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
 * Test class for the CoverageResource REST controller.
 *
 * @see CoverageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    @Inject
    private CoverageRepository coverageRepository;

    @Inject
    private CoverageSearchRepository coverageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageMockMvc;

    private Coverage coverage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageResource coverageResource = new CoverageResource();
        ReflectionTestUtils.setField(coverageResource, "coverageSearchRepository", coverageSearchRepository);
        ReflectionTestUtils.setField(coverageResource, "coverageRepository", coverageRepository);
        this.restCoverageMockMvc = MockMvcBuilders.standaloneSetup(coverageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageSearchRepository.deleteAll();
        coverage = new Coverage();
        coverage.setFixedId(DEFAULT_FIXED_ID);
    }

    @Test
    @Transactional
    public void createCoverage() throws Exception {
        int databaseSizeBeforeCreate = coverageRepository.findAll().size();

        // Create the Coverage

        restCoverageMockMvc.perform(post("/api/coverages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverage)))
                .andExpect(status().isCreated());

        // Validate the Coverage in the database
        List<Coverage> coverages = coverageRepository.findAll();
        assertThat(coverages).hasSize(databaseSizeBeforeCreate + 1);
        Coverage testCoverage = coverages.get(coverages.size() - 1);
        assertThat(testCoverage.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);

        // Validate the Coverage in ElasticSearch
        Coverage coverageEs = coverageSearchRepository.findOne(testCoverage.getId());
        assertThat(coverageEs).isEqualToComparingFieldByField(testCoverage);
    }

    @Test
    @Transactional
    public void getAllCoverages() throws Exception {
        // Initialize the database
        coverageRepository.saveAndFlush(coverage);

        // Get all the coverages
        restCoverageMockMvc.perform(get("/api/coverages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverage.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }

    @Test
    @Transactional
    public void getCoverage() throws Exception {
        // Initialize the database
        coverageRepository.saveAndFlush(coverage);

        // Get the coverage
        restCoverageMockMvc.perform(get("/api/coverages/{id}", coverage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverage.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverage() throws Exception {
        // Get the coverage
        restCoverageMockMvc.perform(get("/api/coverages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverage() throws Exception {
        // Initialize the database
        coverageRepository.saveAndFlush(coverage);
        coverageSearchRepository.save(coverage);
        int databaseSizeBeforeUpdate = coverageRepository.findAll().size();

        // Update the coverage
        Coverage updatedCoverage = new Coverage();
        updatedCoverage.setId(coverage.getId());
        updatedCoverage.setFixedId(UPDATED_FIXED_ID);

        restCoverageMockMvc.perform(put("/api/coverages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverage)))
                .andExpect(status().isOk());

        // Validate the Coverage in the database
        List<Coverage> coverages = coverageRepository.findAll();
        assertThat(coverages).hasSize(databaseSizeBeforeUpdate);
        Coverage testCoverage = coverages.get(coverages.size() - 1);
        assertThat(testCoverage.getFixedId()).isEqualTo(UPDATED_FIXED_ID);

        // Validate the Coverage in ElasticSearch
        Coverage coverageEs = coverageSearchRepository.findOne(testCoverage.getId());
        assertThat(coverageEs).isEqualToComparingFieldByField(testCoverage);
    }

    @Test
    @Transactional
    public void deleteCoverage() throws Exception {
        // Initialize the database
        coverageRepository.saveAndFlush(coverage);
        coverageSearchRepository.save(coverage);
        int databaseSizeBeforeDelete = coverageRepository.findAll().size();

        // Get the coverage
        restCoverageMockMvc.perform(delete("/api/coverages/{id}", coverage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageExistsInEs = coverageSearchRepository.exists(coverage.getId());
        assertThat(coverageExistsInEs).isFalse();

        // Validate the database is empty
        List<Coverage> coverages = coverageRepository.findAll();
        assertThat(coverages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverage() throws Exception {
        // Initialize the database
        coverageRepository.saveAndFlush(coverage);
        coverageSearchRepository.save(coverage);

        // Search the coverage
        restCoverageMockMvc.perform(get("/api/_search/coverages?query=id:" + coverage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverage.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }
}
