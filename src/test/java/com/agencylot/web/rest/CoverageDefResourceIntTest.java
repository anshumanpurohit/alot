package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.CoverageDef;
import com.agencylot.repository.CoverageDefRepository;
import com.agencylot.repository.search.CoverageDefSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CoverageDefResource REST controller.
 *
 * @see CoverageDefResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageDefResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_BEGIN_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    @Inject
    private CoverageDefRepository coverageDefRepository;

    @Inject
    private CoverageDefSearchRepository coverageDefSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageDefMockMvc;

    private CoverageDef coverageDef;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageDefResource coverageDefResource = new CoverageDefResource();
        ReflectionTestUtils.setField(coverageDefResource, "coverageDefSearchRepository", coverageDefSearchRepository);
        ReflectionTestUtils.setField(coverageDefResource, "coverageDefRepository", coverageDefRepository);
        this.restCoverageDefMockMvc = MockMvcBuilders.standaloneSetup(coverageDefResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageDefSearchRepository.deleteAll();
        coverageDef = new CoverageDef();
        coverageDef.setDescription(DEFAULT_DESCRIPTION);
        coverageDef.setBeginEffectiveDate(DEFAULT_BEGIN_EFFECTIVE_DATE);
        coverageDef.setEndEffectiveDate(DEFAULT_END_EFFECTIVE_DATE);
        coverageDef.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createCoverageDef() throws Exception {
        int databaseSizeBeforeCreate = coverageDefRepository.findAll().size();

        // Create the CoverageDef

        restCoverageDefMockMvc.perform(post("/api/coverage-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverageDef)))
                .andExpect(status().isCreated());

        // Validate the CoverageDef in the database
        List<CoverageDef> coverageDefs = coverageDefRepository.findAll();
        assertThat(coverageDefs).hasSize(databaseSizeBeforeCreate + 1);
        CoverageDef testCoverageDef = coverageDefs.get(coverageDefs.size() - 1);
        assertThat(testCoverageDef.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCoverageDef.getBeginEffectiveDate()).isEqualTo(DEFAULT_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageDef.getEndEffectiveDate()).isEqualTo(DEFAULT_END_EFFECTIVE_DATE);
        assertThat(testCoverageDef.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the CoverageDef in ElasticSearch
        CoverageDef coverageDefEs = coverageDefSearchRepository.findOne(testCoverageDef.getId());
        assertThat(coverageDefEs).isEqualToComparingFieldByField(testCoverageDef);
    }

    @Test
    @Transactional
    public void getAllCoverageDefs() throws Exception {
        // Initialize the database
        coverageDefRepository.saveAndFlush(coverageDef);

        // Get all the coverageDefs
        restCoverageDefMockMvc.perform(get("/api/coverage-defs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverageDef.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getCoverageDef() throws Exception {
        // Initialize the database
        coverageDefRepository.saveAndFlush(coverageDef);

        // Get the coverageDef
        restCoverageDefMockMvc.perform(get("/api/coverage-defs/{id}", coverageDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverageDef.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.beginEffectiveDate").value(DEFAULT_BEGIN_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.endEffectiveDate").value(DEFAULT_END_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverageDef() throws Exception {
        // Get the coverageDef
        restCoverageDefMockMvc.perform(get("/api/coverage-defs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverageDef() throws Exception {
        // Initialize the database
        coverageDefRepository.saveAndFlush(coverageDef);
        coverageDefSearchRepository.save(coverageDef);
        int databaseSizeBeforeUpdate = coverageDefRepository.findAll().size();

        // Update the coverageDef
        CoverageDef updatedCoverageDef = new CoverageDef();
        updatedCoverageDef.setId(coverageDef.getId());
        updatedCoverageDef.setDescription(UPDATED_DESCRIPTION);
        updatedCoverageDef.setBeginEffectiveDate(UPDATED_BEGIN_EFFECTIVE_DATE);
        updatedCoverageDef.setEndEffectiveDate(UPDATED_END_EFFECTIVE_DATE);
        updatedCoverageDef.setState(UPDATED_STATE);

        restCoverageDefMockMvc.perform(put("/api/coverage-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverageDef)))
                .andExpect(status().isOk());

        // Validate the CoverageDef in the database
        List<CoverageDef> coverageDefs = coverageDefRepository.findAll();
        assertThat(coverageDefs).hasSize(databaseSizeBeforeUpdate);
        CoverageDef testCoverageDef = coverageDefs.get(coverageDefs.size() - 1);
        assertThat(testCoverageDef.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCoverageDef.getBeginEffectiveDate()).isEqualTo(UPDATED_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageDef.getEndEffectiveDate()).isEqualTo(UPDATED_END_EFFECTIVE_DATE);
        assertThat(testCoverageDef.getState()).isEqualTo(UPDATED_STATE);

        // Validate the CoverageDef in ElasticSearch
        CoverageDef coverageDefEs = coverageDefSearchRepository.findOne(testCoverageDef.getId());
        assertThat(coverageDefEs).isEqualToComparingFieldByField(testCoverageDef);
    }

    @Test
    @Transactional
    public void deleteCoverageDef() throws Exception {
        // Initialize the database
        coverageDefRepository.saveAndFlush(coverageDef);
        coverageDefSearchRepository.save(coverageDef);
        int databaseSizeBeforeDelete = coverageDefRepository.findAll().size();

        // Get the coverageDef
        restCoverageDefMockMvc.perform(delete("/api/coverage-defs/{id}", coverageDef.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageDefExistsInEs = coverageDefSearchRepository.exists(coverageDef.getId());
        assertThat(coverageDefExistsInEs).isFalse();

        // Validate the database is empty
        List<CoverageDef> coverageDefs = coverageDefRepository.findAll();
        assertThat(coverageDefs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverageDef() throws Exception {
        // Initialize the database
        coverageDefRepository.saveAndFlush(coverageDef);
        coverageDefSearchRepository.save(coverageDef);

        // Search the coverageDef
        restCoverageDefMockMvc.perform(get("/api/_search/coverage-defs?query=id:" + coverageDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverageDef.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
