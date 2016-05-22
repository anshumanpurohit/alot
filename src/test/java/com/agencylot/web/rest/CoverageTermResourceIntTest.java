package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.CoverageTerm;
import com.agencylot.repository.CoverageTermRepository;
import com.agencylot.repository.search.CoverageTermSearchRepository;

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
 * Test class for the CoverageTermResource REST controller.
 *
 * @see CoverageTermResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageTermResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    @Inject
    private CoverageTermRepository coverageTermRepository;

    @Inject
    private CoverageTermSearchRepository coverageTermSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageTermMockMvc;

    private CoverageTerm coverageTerm;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageTermResource coverageTermResource = new CoverageTermResource();
        ReflectionTestUtils.setField(coverageTermResource, "coverageTermSearchRepository", coverageTermSearchRepository);
        ReflectionTestUtils.setField(coverageTermResource, "coverageTermRepository", coverageTermRepository);
        this.restCoverageTermMockMvc = MockMvcBuilders.standaloneSetup(coverageTermResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageTermSearchRepository.deleteAll();
        coverageTerm = new CoverageTerm();
        coverageTerm.setFixedId(DEFAULT_FIXED_ID);
    }

    @Test
    @Transactional
    public void createCoverageTerm() throws Exception {
        int databaseSizeBeforeCreate = coverageTermRepository.findAll().size();

        // Create the CoverageTerm

        restCoverageTermMockMvc.perform(post("/api/coverage-terms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverageTerm)))
                .andExpect(status().isCreated());

        // Validate the CoverageTerm in the database
        List<CoverageTerm> coverageTerms = coverageTermRepository.findAll();
        assertThat(coverageTerms).hasSize(databaseSizeBeforeCreate + 1);
        CoverageTerm testCoverageTerm = coverageTerms.get(coverageTerms.size() - 1);
        assertThat(testCoverageTerm.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);

        // Validate the CoverageTerm in ElasticSearch
        CoverageTerm coverageTermEs = coverageTermSearchRepository.findOne(testCoverageTerm.getId());
        assertThat(coverageTermEs).isEqualToComparingFieldByField(testCoverageTerm);
    }

    @Test
    @Transactional
    public void getAllCoverageTerms() throws Exception {
        // Initialize the database
        coverageTermRepository.saveAndFlush(coverageTerm);

        // Get all the coverageTerms
        restCoverageTermMockMvc.perform(get("/api/coverage-terms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTerm.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }

    @Test
    @Transactional
    public void getCoverageTerm() throws Exception {
        // Initialize the database
        coverageTermRepository.saveAndFlush(coverageTerm);

        // Get the coverageTerm
        restCoverageTermMockMvc.perform(get("/api/coverage-terms/{id}", coverageTerm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverageTerm.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverageTerm() throws Exception {
        // Get the coverageTerm
        restCoverageTermMockMvc.perform(get("/api/coverage-terms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverageTerm() throws Exception {
        // Initialize the database
        coverageTermRepository.saveAndFlush(coverageTerm);
        coverageTermSearchRepository.save(coverageTerm);
        int databaseSizeBeforeUpdate = coverageTermRepository.findAll().size();

        // Update the coverageTerm
        CoverageTerm updatedCoverageTerm = new CoverageTerm();
        updatedCoverageTerm.setId(coverageTerm.getId());
        updatedCoverageTerm.setFixedId(UPDATED_FIXED_ID);

        restCoverageTermMockMvc.perform(put("/api/coverage-terms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverageTerm)))
                .andExpect(status().isOk());

        // Validate the CoverageTerm in the database
        List<CoverageTerm> coverageTerms = coverageTermRepository.findAll();
        assertThat(coverageTerms).hasSize(databaseSizeBeforeUpdate);
        CoverageTerm testCoverageTerm = coverageTerms.get(coverageTerms.size() - 1);
        assertThat(testCoverageTerm.getFixedId()).isEqualTo(UPDATED_FIXED_ID);

        // Validate the CoverageTerm in ElasticSearch
        CoverageTerm coverageTermEs = coverageTermSearchRepository.findOne(testCoverageTerm.getId());
        assertThat(coverageTermEs).isEqualToComparingFieldByField(testCoverageTerm);
    }

    @Test
    @Transactional
    public void deleteCoverageTerm() throws Exception {
        // Initialize the database
        coverageTermRepository.saveAndFlush(coverageTerm);
        coverageTermSearchRepository.save(coverageTerm);
        int databaseSizeBeforeDelete = coverageTermRepository.findAll().size();

        // Get the coverageTerm
        restCoverageTermMockMvc.perform(delete("/api/coverage-terms/{id}", coverageTerm.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageTermExistsInEs = coverageTermSearchRepository.exists(coverageTerm.getId());
        assertThat(coverageTermExistsInEs).isFalse();

        // Validate the database is empty
        List<CoverageTerm> coverageTerms = coverageTermRepository.findAll();
        assertThat(coverageTerms).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverageTerm() throws Exception {
        // Initialize the database
        coverageTermRepository.saveAndFlush(coverageTerm);
        coverageTermSearchRepository.save(coverageTerm);

        // Search the coverageTerm
        restCoverageTermMockMvc.perform(get("/api/_search/coverage-terms?query=id:" + coverageTerm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTerm.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }
}
