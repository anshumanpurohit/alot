package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.CoverageTermOptionDef;
import com.agencylot.repository.CoverageTermOptionDefRepository;
import com.agencylot.repository.search.CoverageTermOptionDefSearchRepository;

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
 * Test class for the CoverageTermOptionDefResource REST controller.
 *
 * @see CoverageTermOptionDefResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageTermOptionDefResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_BEGIN_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    @Inject
    private CoverageTermOptionDefRepository coverageTermOptionDefRepository;

    @Inject
    private CoverageTermOptionDefSearchRepository coverageTermOptionDefSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageTermOptionDefMockMvc;

    private CoverageTermOptionDef coverageTermOptionDef;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageTermOptionDefResource coverageTermOptionDefResource = new CoverageTermOptionDefResource();
        ReflectionTestUtils.setField(coverageTermOptionDefResource, "coverageTermOptionDefSearchRepository", coverageTermOptionDefSearchRepository);
        ReflectionTestUtils.setField(coverageTermOptionDefResource, "coverageTermOptionDefRepository", coverageTermOptionDefRepository);
        this.restCoverageTermOptionDefMockMvc = MockMvcBuilders.standaloneSetup(coverageTermOptionDefResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageTermOptionDefSearchRepository.deleteAll();
        coverageTermOptionDef = new CoverageTermOptionDef();
        coverageTermOptionDef.setDescription(DEFAULT_DESCRIPTION);
        coverageTermOptionDef.setBeginEffectiveDate(DEFAULT_BEGIN_EFFECTIVE_DATE);
        coverageTermOptionDef.setEndEffectiveDate(DEFAULT_END_EFFECTIVE_DATE);
        coverageTermOptionDef.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createCoverageTermOptionDef() throws Exception {
        int databaseSizeBeforeCreate = coverageTermOptionDefRepository.findAll().size();

        // Create the CoverageTermOptionDef

        restCoverageTermOptionDefMockMvc.perform(post("/api/coverage-term-option-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverageTermOptionDef)))
                .andExpect(status().isCreated());

        // Validate the CoverageTermOptionDef in the database
        List<CoverageTermOptionDef> coverageTermOptionDefs = coverageTermOptionDefRepository.findAll();
        assertThat(coverageTermOptionDefs).hasSize(databaseSizeBeforeCreate + 1);
        CoverageTermOptionDef testCoverageTermOptionDef = coverageTermOptionDefs.get(coverageTermOptionDefs.size() - 1);
        assertThat(testCoverageTermOptionDef.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCoverageTermOptionDef.getBeginEffectiveDate()).isEqualTo(DEFAULT_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageTermOptionDef.getEndEffectiveDate()).isEqualTo(DEFAULT_END_EFFECTIVE_DATE);
        assertThat(testCoverageTermOptionDef.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the CoverageTermOptionDef in ElasticSearch
        CoverageTermOptionDef coverageTermOptionDefEs = coverageTermOptionDefSearchRepository.findOne(testCoverageTermOptionDef.getId());
        assertThat(coverageTermOptionDefEs).isEqualToComparingFieldByField(testCoverageTermOptionDef);
    }

    @Test
    @Transactional
    public void getAllCoverageTermOptionDefs() throws Exception {
        // Initialize the database
        coverageTermOptionDefRepository.saveAndFlush(coverageTermOptionDef);

        // Get all the coverageTermOptionDefs
        restCoverageTermOptionDefMockMvc.perform(get("/api/coverage-term-option-defs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermOptionDef.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getCoverageTermOptionDef() throws Exception {
        // Initialize the database
        coverageTermOptionDefRepository.saveAndFlush(coverageTermOptionDef);

        // Get the coverageTermOptionDef
        restCoverageTermOptionDefMockMvc.perform(get("/api/coverage-term-option-defs/{id}", coverageTermOptionDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverageTermOptionDef.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.beginEffectiveDate").value(DEFAULT_BEGIN_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.endEffectiveDate").value(DEFAULT_END_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverageTermOptionDef() throws Exception {
        // Get the coverageTermOptionDef
        restCoverageTermOptionDefMockMvc.perform(get("/api/coverage-term-option-defs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverageTermOptionDef() throws Exception {
        // Initialize the database
        coverageTermOptionDefRepository.saveAndFlush(coverageTermOptionDef);
        coverageTermOptionDefSearchRepository.save(coverageTermOptionDef);
        int databaseSizeBeforeUpdate = coverageTermOptionDefRepository.findAll().size();

        // Update the coverageTermOptionDef
        CoverageTermOptionDef updatedCoverageTermOptionDef = new CoverageTermOptionDef();
        updatedCoverageTermOptionDef.setId(coverageTermOptionDef.getId());
        updatedCoverageTermOptionDef.setDescription(UPDATED_DESCRIPTION);
        updatedCoverageTermOptionDef.setBeginEffectiveDate(UPDATED_BEGIN_EFFECTIVE_DATE);
        updatedCoverageTermOptionDef.setEndEffectiveDate(UPDATED_END_EFFECTIVE_DATE);
        updatedCoverageTermOptionDef.setState(UPDATED_STATE);

        restCoverageTermOptionDefMockMvc.perform(put("/api/coverage-term-option-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverageTermOptionDef)))
                .andExpect(status().isOk());

        // Validate the CoverageTermOptionDef in the database
        List<CoverageTermOptionDef> coverageTermOptionDefs = coverageTermOptionDefRepository.findAll();
        assertThat(coverageTermOptionDefs).hasSize(databaseSizeBeforeUpdate);
        CoverageTermOptionDef testCoverageTermOptionDef = coverageTermOptionDefs.get(coverageTermOptionDefs.size() - 1);
        assertThat(testCoverageTermOptionDef.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCoverageTermOptionDef.getBeginEffectiveDate()).isEqualTo(UPDATED_BEGIN_EFFECTIVE_DATE);
        assertThat(testCoverageTermOptionDef.getEndEffectiveDate()).isEqualTo(UPDATED_END_EFFECTIVE_DATE);
        assertThat(testCoverageTermOptionDef.getState()).isEqualTo(UPDATED_STATE);

        // Validate the CoverageTermOptionDef in ElasticSearch
        CoverageTermOptionDef coverageTermOptionDefEs = coverageTermOptionDefSearchRepository.findOne(testCoverageTermOptionDef.getId());
        assertThat(coverageTermOptionDefEs).isEqualToComparingFieldByField(testCoverageTermOptionDef);
    }

    @Test
    @Transactional
    public void deleteCoverageTermOptionDef() throws Exception {
        // Initialize the database
        coverageTermOptionDefRepository.saveAndFlush(coverageTermOptionDef);
        coverageTermOptionDefSearchRepository.save(coverageTermOptionDef);
        int databaseSizeBeforeDelete = coverageTermOptionDefRepository.findAll().size();

        // Get the coverageTermOptionDef
        restCoverageTermOptionDefMockMvc.perform(delete("/api/coverage-term-option-defs/{id}", coverageTermOptionDef.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageTermOptionDefExistsInEs = coverageTermOptionDefSearchRepository.exists(coverageTermOptionDef.getId());
        assertThat(coverageTermOptionDefExistsInEs).isFalse();

        // Validate the database is empty
        List<CoverageTermOptionDef> coverageTermOptionDefs = coverageTermOptionDefRepository.findAll();
        assertThat(coverageTermOptionDefs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverageTermOptionDef() throws Exception {
        // Initialize the database
        coverageTermOptionDefRepository.saveAndFlush(coverageTermOptionDef);
        coverageTermOptionDefSearchRepository.save(coverageTermOptionDef);

        // Search the coverageTermOptionDef
        restCoverageTermOptionDefMockMvc.perform(get("/api/_search/coverage-term-option-defs?query=id:" + coverageTermOptionDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermOptionDef.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
