package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Violation;
import com.agencylot.repository.ViolationRepository;
import com.agencylot.repository.search.ViolationSearchRepository;

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
 * Test class for the ViolationResource REST controller.
 *
 * @see ViolationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ViolationResourceIntTest {

    private static final String DEFAULT_EXTERNAL_VIOLATION_CODE = "AAAAA";
    private static final String UPDATED_EXTERNAL_VIOLATION_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_VIOLATION_OCCURRED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VIOLATION_OCCURRED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ADDITIONAL_DETAILS = "AAAAA";
    private static final String UPDATED_ADDITIONAL_DETAILS = "BBBBB";

    @Inject
    private ViolationRepository violationRepository;

    @Inject
    private ViolationSearchRepository violationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restViolationMockMvc;

    private Violation violation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ViolationResource violationResource = new ViolationResource();
        ReflectionTestUtils.setField(violationResource, "violationSearchRepository", violationSearchRepository);
        ReflectionTestUtils.setField(violationResource, "violationRepository", violationRepository);
        this.restViolationMockMvc = MockMvcBuilders.standaloneSetup(violationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        violationSearchRepository.deleteAll();
        violation = new Violation();
        violation.setExternalViolationCode(DEFAULT_EXTERNAL_VIOLATION_CODE);
        violation.setDescription(DEFAULT_DESCRIPTION);
        violation.setViolationOccurredDate(DEFAULT_VIOLATION_OCCURRED_DATE);
        violation.setAdditionalDetails(DEFAULT_ADDITIONAL_DETAILS);
    }

    @Test
    @Transactional
    public void createViolation() throws Exception {
        int databaseSizeBeforeCreate = violationRepository.findAll().size();

        // Create the Violation

        restViolationMockMvc.perform(post("/api/violations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(violation)))
                .andExpect(status().isCreated());

        // Validate the Violation in the database
        List<Violation> violations = violationRepository.findAll();
        assertThat(violations).hasSize(databaseSizeBeforeCreate + 1);
        Violation testViolation = violations.get(violations.size() - 1);
        assertThat(testViolation.getExternalViolationCode()).isEqualTo(DEFAULT_EXTERNAL_VIOLATION_CODE);
        assertThat(testViolation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testViolation.getViolationOccurredDate()).isEqualTo(DEFAULT_VIOLATION_OCCURRED_DATE);
        assertThat(testViolation.getAdditionalDetails()).isEqualTo(DEFAULT_ADDITIONAL_DETAILS);

        // Validate the Violation in ElasticSearch
        Violation violationEs = violationSearchRepository.findOne(testViolation.getId());
        assertThat(violationEs).isEqualToComparingFieldByField(testViolation);
    }

    @Test
    @Transactional
    public void getAllViolations() throws Exception {
        // Initialize the database
        violationRepository.saveAndFlush(violation);

        // Get all the violations
        restViolationMockMvc.perform(get("/api/violations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(violation.getId().intValue())))
                .andExpect(jsonPath("$.[*].externalViolationCode").value(hasItem(DEFAULT_EXTERNAL_VIOLATION_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].violationOccurredDate").value(hasItem(DEFAULT_VIOLATION_OCCURRED_DATE.toString())))
                .andExpect(jsonPath("$.[*].additionalDetails").value(hasItem(DEFAULT_ADDITIONAL_DETAILS.toString())));
    }

    @Test
    @Transactional
    public void getViolation() throws Exception {
        // Initialize the database
        violationRepository.saveAndFlush(violation);

        // Get the violation
        restViolationMockMvc.perform(get("/api/violations/{id}", violation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(violation.getId().intValue()))
            .andExpect(jsonPath("$.externalViolationCode").value(DEFAULT_EXTERNAL_VIOLATION_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.violationOccurredDate").value(DEFAULT_VIOLATION_OCCURRED_DATE.toString()))
            .andExpect(jsonPath("$.additionalDetails").value(DEFAULT_ADDITIONAL_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingViolation() throws Exception {
        // Get the violation
        restViolationMockMvc.perform(get("/api/violations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateViolation() throws Exception {
        // Initialize the database
        violationRepository.saveAndFlush(violation);
        violationSearchRepository.save(violation);
        int databaseSizeBeforeUpdate = violationRepository.findAll().size();

        // Update the violation
        Violation updatedViolation = new Violation();
        updatedViolation.setId(violation.getId());
        updatedViolation.setExternalViolationCode(UPDATED_EXTERNAL_VIOLATION_CODE);
        updatedViolation.setDescription(UPDATED_DESCRIPTION);
        updatedViolation.setViolationOccurredDate(UPDATED_VIOLATION_OCCURRED_DATE);
        updatedViolation.setAdditionalDetails(UPDATED_ADDITIONAL_DETAILS);

        restViolationMockMvc.perform(put("/api/violations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedViolation)))
                .andExpect(status().isOk());

        // Validate the Violation in the database
        List<Violation> violations = violationRepository.findAll();
        assertThat(violations).hasSize(databaseSizeBeforeUpdate);
        Violation testViolation = violations.get(violations.size() - 1);
        assertThat(testViolation.getExternalViolationCode()).isEqualTo(UPDATED_EXTERNAL_VIOLATION_CODE);
        assertThat(testViolation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testViolation.getViolationOccurredDate()).isEqualTo(UPDATED_VIOLATION_OCCURRED_DATE);
        assertThat(testViolation.getAdditionalDetails()).isEqualTo(UPDATED_ADDITIONAL_DETAILS);

        // Validate the Violation in ElasticSearch
        Violation violationEs = violationSearchRepository.findOne(testViolation.getId());
        assertThat(violationEs).isEqualToComparingFieldByField(testViolation);
    }

    @Test
    @Transactional
    public void deleteViolation() throws Exception {
        // Initialize the database
        violationRepository.saveAndFlush(violation);
        violationSearchRepository.save(violation);
        int databaseSizeBeforeDelete = violationRepository.findAll().size();

        // Get the violation
        restViolationMockMvc.perform(delete("/api/violations/{id}", violation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean violationExistsInEs = violationSearchRepository.exists(violation.getId());
        assertThat(violationExistsInEs).isFalse();

        // Validate the database is empty
        List<Violation> violations = violationRepository.findAll();
        assertThat(violations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchViolation() throws Exception {
        // Initialize the database
        violationRepository.saveAndFlush(violation);
        violationSearchRepository.save(violation);

        // Search the violation
        restViolationMockMvc.perform(get("/api/_search/violations?query=id:" + violation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(violation.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalViolationCode").value(hasItem(DEFAULT_EXTERNAL_VIOLATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].violationOccurredDate").value(hasItem(DEFAULT_VIOLATION_OCCURRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalDetails").value(hasItem(DEFAULT_ADDITIONAL_DETAILS.toString())));
    }
}
