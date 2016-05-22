package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.CoverageTermDef;
import com.agencylot.repository.CoverageTermDefRepository;
import com.agencylot.repository.search.CoverageTermDefSearchRepository;

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
 * Test class for the CoverageTermDefResource REST controller.
 *
 * @see CoverageTermDefResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageTermDefResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_BEGIN_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    @Inject
    private CoverageTermDefRepository coverageTermDefRepository;

    @Inject
    private CoverageTermDefSearchRepository coverageTermDefSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageTermDefMockMvc;

    private CoverageTermDef coverageTermDef;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageTermDefResource coverageTermDefResource = new CoverageTermDefResource();
        ReflectionTestUtils.setField(coverageTermDefResource, "coverageTermDefSearchRepository", coverageTermDefSearchRepository);
        ReflectionTestUtils.setField(coverageTermDefResource, "coverageTermDefRepository", coverageTermDefRepository);
        this.restCoverageTermDefMockMvc = MockMvcBuilders.standaloneSetup(coverageTermDefResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageTermDefSearchRepository.deleteAll();
        coverageTermDef = new CoverageTermDef();
        coverageTermDef.setDescription(DEFAULT_DESCRIPTION);
        coverageTermDef.setBeginEffectiveDate(DEFAULT_BEGIN_EFFECTIVE_DATE);
        coverageTermDef.setEndEffectiveDate(DEFAULT_END_EFFECTIVE_DATE);
        coverageTermDef.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createCoverageTermDef() throws Exception {
        int databaseSizeBeforeCreate = coverageTermDefRepository.findAll().size();

        // Create the CoverageTermDef

        restCoverageTermDefMockMvc.perform(post("/api/coverage-term-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverageTermDef)))
                .andExpect(status().isCreated());

        // Validate the CoverageTermDef in the database
        List<CoverageTermDef> coverageTermDefs = coverageTermDefRepository.findAll();
        assertThat(coverageTermDefs).hasSize(databaseSizeBeforeCreate + 1);
        CoverageTermDef testCoverageTermDef = coverageTermDefs.get(coverageTermDefs.size() - 1);
        assertThat(testCoverageTermDef.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCoverageTermDef.getBeginEffectiveDate()).isEqualTo(DEFAULT_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageTermDef.getEndEffectiveDate()).isEqualTo(DEFAULT_END_EFFECTIVE_DATE);
        assertThat(testCoverageTermDef.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the CoverageTermDef in ElasticSearch
        CoverageTermDef coverageTermDefEs = coverageTermDefSearchRepository.findOne(testCoverageTermDef.getId());
        assertThat(coverageTermDefEs).isEqualToComparingFieldByField(testCoverageTermDef);
    }

    @Test
    @Transactional
    public void getAllCoverageTermDefs() throws Exception {
        // Initialize the database
        coverageTermDefRepository.saveAndFlush(coverageTermDef);

        // Get all the coverageTermDefs
        restCoverageTermDefMockMvc.perform(get("/api/coverage-term-defs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermDef.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getCoverageTermDef() throws Exception {
        // Initialize the database
        coverageTermDefRepository.saveAndFlush(coverageTermDef);

        // Get the coverageTermDef
        restCoverageTermDefMockMvc.perform(get("/api/coverage-term-defs/{id}", coverageTermDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverageTermDef.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.beginEffectiveDate").value(DEFAULT_BEGIN_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.endEffectiveDate").value(DEFAULT_END_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverageTermDef() throws Exception {
        // Get the coverageTermDef
        restCoverageTermDefMockMvc.perform(get("/api/coverage-term-defs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverageTermDef() throws Exception {
        // Initialize the database
        coverageTermDefRepository.saveAndFlush(coverageTermDef);
        coverageTermDefSearchRepository.save(coverageTermDef);
        int databaseSizeBeforeUpdate = coverageTermDefRepository.findAll().size();

        // Update the coverageTermDef
        CoverageTermDef updatedCoverageTermDef = new CoverageTermDef();
        updatedCoverageTermDef.setId(coverageTermDef.getId());
        updatedCoverageTermDef.setDescription(UPDATED_DESCRIPTION);
        updatedCoverageTermDef.setBeginEffectiveDate(UPDATED_BEGIN_EFFECTIVE_DATE);
        updatedCoverageTermDef.setEndEffectiveDate(UPDATED_END_EFFECTIVE_DATE);
        updatedCoverageTermDef.setState(UPDATED_STATE);

        restCoverageTermDefMockMvc.perform(put("/api/coverage-term-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverageTermDef)))
                .andExpect(status().isOk());

        // Validate the CoverageTermDef in the database
        List<CoverageTermDef> coverageTermDefs = coverageTermDefRepository.findAll();
        assertThat(coverageTermDefs).hasSize(databaseSizeBeforeUpdate);
        CoverageTermDef testCoverageTermDef = coverageTermDefs.get(coverageTermDefs.size() - 1);
        assertThat(testCoverageTermDef.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCoverageTermDef.getBeginEffectiveDate()).isEqualTo(UPDATED_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageTermDef.getEndEffectiveDate()).isEqualTo(UPDATED_END_EFFECTIVE_DATE);
        assertThat(testCoverageTermDef.getState()).isEqualTo(UPDATED_STATE);

        // Validate the CoverageTermDef in ElasticSearch
        CoverageTermDef coverageTermDefEs = coverageTermDefSearchRepository.findOne(testCoverageTermDef.getId());
        assertThat(coverageTermDefEs).isEqualToComparingFieldByField(testCoverageTermDef);
    }

    @Test
    @Transactional
    public void deleteCoverageTermDef() throws Exception {
        // Initialize the database
        coverageTermDefRepository.saveAndFlush(coverageTermDef);
        coverageTermDefSearchRepository.save(coverageTermDef);
        int databaseSizeBeforeDelete = coverageTermDefRepository.findAll().size();

        // Get the coverageTermDef
        restCoverageTermDefMockMvc.perform(delete("/api/coverage-term-defs/{id}", coverageTermDef.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageTermDefExistsInEs = coverageTermDefSearchRepository.exists(coverageTermDef.getId());
        assertThat(coverageTermDefExistsInEs).isFalse();

        // Validate the database is empty
        List<CoverageTermDef> coverageTermDefs = coverageTermDefRepository.findAll();
        assertThat(coverageTermDefs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverageTermDef() throws Exception {
        // Initialize the database
        coverageTermDefRepository.saveAndFlush(coverageTermDef);
        coverageTermDefSearchRepository.save(coverageTermDef);

        // Search the coverageTermDef
        restCoverageTermDefMockMvc.perform(get("/api/_search/coverage-term-defs?query=id:" + coverageTermDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermDef.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
