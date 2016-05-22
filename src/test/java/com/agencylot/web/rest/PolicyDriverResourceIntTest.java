package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.PolicyDriver;
import com.agencylot.repository.PolicyDriverRepository;
import com.agencylot.repository.search.PolicyDriverSearchRepository;

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
 * Test class for the PolicyDriverResource REST controller.
 *
 * @see PolicyDriverResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class PolicyDriverResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    @Inject
    private PolicyDriverRepository policyDriverRepository;

    @Inject
    private PolicyDriverSearchRepository policyDriverSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolicyDriverMockMvc;

    private PolicyDriver policyDriver;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolicyDriverResource policyDriverResource = new PolicyDriverResource();
        ReflectionTestUtils.setField(policyDriverResource, "policyDriverSearchRepository", policyDriverSearchRepository);
        ReflectionTestUtils.setField(policyDriverResource, "policyDriverRepository", policyDriverRepository);
        this.restPolicyDriverMockMvc = MockMvcBuilders.standaloneSetup(policyDriverResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        policyDriverSearchRepository.deleteAll();
        policyDriver = new PolicyDriver();
        policyDriver.setFixedId(DEFAULT_FIXED_ID);
    }

    @Test
    @Transactional
    public void createPolicyDriver() throws Exception {
        int databaseSizeBeforeCreate = policyDriverRepository.findAll().size();

        // Create the PolicyDriver

        restPolicyDriverMockMvc.perform(post("/api/policy-drivers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(policyDriver)))
                .andExpect(status().isCreated());

        // Validate the PolicyDriver in the database
        List<PolicyDriver> policyDrivers = policyDriverRepository.findAll();
        assertThat(policyDrivers).hasSize(databaseSizeBeforeCreate + 1);
        PolicyDriver testPolicyDriver = policyDrivers.get(policyDrivers.size() - 1);
        assertThat(testPolicyDriver.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);

        // Validate the PolicyDriver in ElasticSearch
        PolicyDriver policyDriverEs = policyDriverSearchRepository.findOne(testPolicyDriver.getId());
        assertThat(policyDriverEs).isEqualToComparingFieldByField(testPolicyDriver);
    }

    @Test
    @Transactional
    public void getAllPolicyDrivers() throws Exception {
        // Initialize the database
        policyDriverRepository.saveAndFlush(policyDriver);

        // Get all the policyDrivers
        restPolicyDriverMockMvc.perform(get("/api/policy-drivers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(policyDriver.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }

    @Test
    @Transactional
    public void getPolicyDriver() throws Exception {
        // Initialize the database
        policyDriverRepository.saveAndFlush(policyDriver);

        // Get the policyDriver
        restPolicyDriverMockMvc.perform(get("/api/policy-drivers/{id}", policyDriver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(policyDriver.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicyDriver() throws Exception {
        // Get the policyDriver
        restPolicyDriverMockMvc.perform(get("/api/policy-drivers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicyDriver() throws Exception {
        // Initialize the database
        policyDriverRepository.saveAndFlush(policyDriver);
        policyDriverSearchRepository.save(policyDriver);
        int databaseSizeBeforeUpdate = policyDriverRepository.findAll().size();

        // Update the policyDriver
        PolicyDriver updatedPolicyDriver = new PolicyDriver();
        updatedPolicyDriver.setId(policyDriver.getId());
        updatedPolicyDriver.setFixedId(UPDATED_FIXED_ID);

        restPolicyDriverMockMvc.perform(put("/api/policy-drivers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPolicyDriver)))
                .andExpect(status().isOk());

        // Validate the PolicyDriver in the database
        List<PolicyDriver> policyDrivers = policyDriverRepository.findAll();
        assertThat(policyDrivers).hasSize(databaseSizeBeforeUpdate);
        PolicyDriver testPolicyDriver = policyDrivers.get(policyDrivers.size() - 1);
        assertThat(testPolicyDriver.getFixedId()).isEqualTo(UPDATED_FIXED_ID);

        // Validate the PolicyDriver in ElasticSearch
        PolicyDriver policyDriverEs = policyDriverSearchRepository.findOne(testPolicyDriver.getId());
        assertThat(policyDriverEs).isEqualToComparingFieldByField(testPolicyDriver);
    }

    @Test
    @Transactional
    public void deletePolicyDriver() throws Exception {
        // Initialize the database
        policyDriverRepository.saveAndFlush(policyDriver);
        policyDriverSearchRepository.save(policyDriver);
        int databaseSizeBeforeDelete = policyDriverRepository.findAll().size();

        // Get the policyDriver
        restPolicyDriverMockMvc.perform(delete("/api/policy-drivers/{id}", policyDriver.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean policyDriverExistsInEs = policyDriverSearchRepository.exists(policyDriver.getId());
        assertThat(policyDriverExistsInEs).isFalse();

        // Validate the database is empty
        List<PolicyDriver> policyDrivers = policyDriverRepository.findAll();
        assertThat(policyDrivers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPolicyDriver() throws Exception {
        // Initialize the database
        policyDriverRepository.saveAndFlush(policyDriver);
        policyDriverSearchRepository.save(policyDriver);

        // Search the policyDriver
        restPolicyDriverMockMvc.perform(get("/api/_search/policy-drivers?query=id:" + policyDriver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyDriver.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())));
    }
}
