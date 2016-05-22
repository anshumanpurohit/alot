package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.CoverageTermOption;
import com.agencylot.repository.CoverageTermOptionRepository;
import com.agencylot.repository.search.CoverageTermOptionSearchRepository;

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
 * Test class for the CoverageTermOptionResource REST controller.
 *
 * @see CoverageTermOptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoverageTermOptionResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    @Inject
    private CoverageTermOptionRepository coverageTermOptionRepository;

    @Inject
    private CoverageTermOptionSearchRepository coverageTermOptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoverageTermOptionMockMvc;

    private CoverageTermOption coverageTermOption;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverageTermOptionResource coverageTermOptionResource = new CoverageTermOptionResource();
        ReflectionTestUtils.setField(coverageTermOptionResource, "coverageTermOptionSearchRepository", coverageTermOptionSearchRepository);
        ReflectionTestUtils.setField(coverageTermOptionResource, "coverageTermOptionRepository", coverageTermOptionRepository);
        this.restCoverageTermOptionMockMvc = MockMvcBuilders.standaloneSetup(coverageTermOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coverageTermOptionSearchRepository.deleteAll();
        coverageTermOption = new CoverageTermOption();
        coverageTermOption.setFixedId(DEFAULT_FIXED_ID);
    }

    @Test
    @Transactional
    public void createCoverageTermOption() throws Exception {
        int databaseSizeBeforeCreate = coverageTermOptionRepository.findAll().size();

        // Create the CoverageTermOption

        restCoverageTermOptionMockMvc.perform(post("/api/coverage-term-options")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(coverageTermOption)))
                .andExpect(status().isCreated());

        // Validate the CoverageTermOption in the database
        List<CoverageTermOption> coverageTermOptions = coverageTermOptionRepository.findAll();
        assertThat(coverageTermOptions).hasSize(databaseSizeBeforeCreate + 1);
        CoverageTermOption testCoverageTermOption = coverageTermOptions.get(coverageTermOptions.size() - 1);
        assertThat(testCoverageTermOption.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);

        // Validate the CoverageTermOption in ElasticSearch
        CoverageTermOption coverageTermOptionEs = coverageTermOptionSearchRepository.findOne(testCoverageTermOption.getId());
        assertThat(coverageTermOptionEs).isEqualToComparingFieldByField(testCoverageTermOption);
    }

    @Test
    @Transactional
    public void getAllCoverageTermOptions() throws Exception {
        // Initialize the database
        coverageTermOptionRepository.saveAndFlush(coverageTermOption);

        // Get all the coverageTermOptions
        restCoverageTermOptionMockMvc.perform(get("/api/coverage-term-options?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermOption.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }

    @Test
    @Transactional
    public void getCoverageTermOption() throws Exception {
        // Initialize the database
        coverageTermOptionRepository.saveAndFlush(coverageTermOption);

        // Get the coverageTermOption
        restCoverageTermOptionMockMvc.perform(get("/api/coverage-term-options/{id}", coverageTermOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(coverageTermOption.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoverageTermOption() throws Exception {
        // Get the coverageTermOption
        restCoverageTermOptionMockMvc.perform(get("/api/coverage-term-options/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoverageTermOption() throws Exception {
        // Initialize the database
        coverageTermOptionRepository.saveAndFlush(coverageTermOption);
        coverageTermOptionSearchRepository.save(coverageTermOption);
        int databaseSizeBeforeUpdate = coverageTermOptionRepository.findAll().size();

        // Update the coverageTermOption
        CoverageTermOption updatedCoverageTermOption = new CoverageTermOption();
        updatedCoverageTermOption.setId(coverageTermOption.getId());
        updatedCoverageTermOption.setFixedId(UPDATED_FIXED_ID);

        restCoverageTermOptionMockMvc.perform(put("/api/coverage-term-options")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCoverageTermOption)))
                .andExpect(status().isOk());

        // Validate the CoverageTermOption in the database
        List<CoverageTermOption> coverageTermOptions = coverageTermOptionRepository.findAll();
        assertThat(coverageTermOptions).hasSize(databaseSizeBeforeUpdate);
        CoverageTermOption testCoverageTermOption = coverageTermOptions.get(coverageTermOptions.size() - 1);
        assertThat(testCoverageTermOption.getFixedId()).isEqualTo(UPDATED_FIXED_ID);

        // Validate the CoverageTermOption in ElasticSearch
        CoverageTermOption coverageTermOptionEs = coverageTermOptionSearchRepository.findOne(testCoverageTermOption.getId());
        assertThat(coverageTermOptionEs).isEqualToComparingFieldByField(testCoverageTermOption);
    }

    @Test
    @Transactional
    public void deleteCoverageTermOption() throws Exception {
        // Initialize the database
        coverageTermOptionRepository.saveAndFlush(coverageTermOption);
        coverageTermOptionSearchRepository.save(coverageTermOption);
        int databaseSizeBeforeDelete = coverageTermOptionRepository.findAll().size();

        // Get the coverageTermOption
        restCoverageTermOptionMockMvc.perform(delete("/api/coverage-term-options/{id}", coverageTermOption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coverageTermOptionExistsInEs = coverageTermOptionSearchRepository.exists(coverageTermOption.getId());
        assertThat(coverageTermOptionExistsInEs).isFalse();

        // Validate the database is empty
        List<CoverageTermOption> coverageTermOptions = coverageTermOptionRepository.findAll();
        assertThat(coverageTermOptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCoverageTermOption() throws Exception {
        // Initialize the database
        coverageTermOptionRepository.saveAndFlush(coverageTermOption);
        coverageTermOptionSearchRepository.save(coverageTermOption);

        // Search the coverageTermOption
        restCoverageTermOptionMockMvc.perform(get("/api/_search/coverage-term-options?query=id:" + coverageTermOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverageTermOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }
}
