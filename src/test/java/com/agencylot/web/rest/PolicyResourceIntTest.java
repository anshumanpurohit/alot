package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Policy;
import com.agencylot.repository.PolicyRepository;
import com.agencylot.repository.search.PolicySearchRepository;

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
 * Test class for the PolicyResource REST controller.
 *
 * @see PolicyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class PolicyResourceIntTest {

    private static final String DEFAULT_POLICY_NUMBER = "AAAAA";
    private static final String UPDATED_POLICY_NUMBER = "BBBBB";
    private static final String DEFAULT_BASE_STATE = "AAAAA";
    private static final String UPDATED_BASE_STATE = "BBBBB";

    @Inject
    private PolicyRepository policyRepository;

    @Inject
    private PolicySearchRepository policySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolicyMockMvc;

    private Policy policy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolicyResource policyResource = new PolicyResource();
        ReflectionTestUtils.setField(policyResource, "policySearchRepository", policySearchRepository);
        ReflectionTestUtils.setField(policyResource, "policyRepository", policyRepository);
        this.restPolicyMockMvc = MockMvcBuilders.standaloneSetup(policyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        policySearchRepository.deleteAll();
        policy = new Policy();
        policy.setPolicyNumber(DEFAULT_POLICY_NUMBER);
        policy.setBaseState(DEFAULT_BASE_STATE);
    }

    @Test
    @Transactional
    public void createPolicy() throws Exception {
        int databaseSizeBeforeCreate = policyRepository.findAll().size();

        // Create the Policy

        restPolicyMockMvc.perform(post("/api/policies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(policy)))
                .andExpect(status().isCreated());

        // Validate the Policy in the database
        List<Policy> policies = policyRepository.findAll();
        assertThat(policies).hasSize(databaseSizeBeforeCreate + 1);
        Policy testPolicy = policies.get(policies.size() - 1);
        assertThat(testPolicy.getPolicyNumber()).isEqualTo(DEFAULT_POLICY_NUMBER);
        assertThat(testPolicy.getBaseState()).isEqualTo(DEFAULT_BASE_STATE);

        // Validate the Policy in ElasticSearch
        Policy policyEs = policySearchRepository.findOne(testPolicy.getId());
        assertThat(policyEs).isEqualToComparingFieldByField(testPolicy);
    }

    @Test
    @Transactional
    public void getAllPolicies() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get all the policies
        restPolicyMockMvc.perform(get("/api/policies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(policy.getId().intValue())))
                .andExpect(jsonPath("$.[*].policyNumber").value(hasItem(DEFAULT_POLICY_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].baseState").value(hasItem(DEFAULT_BASE_STATE.toString())));
    }

    @Test
    @Transactional
    public void getPolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);

        // Get the policy
        restPolicyMockMvc.perform(get("/api/policies/{id}", policy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(policy.getId().intValue()))
            .andExpect(jsonPath("$.policyNumber").value(DEFAULT_POLICY_NUMBER.toString()))
            .andExpect(jsonPath("$.baseState").value(DEFAULT_BASE_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicy() throws Exception {
        // Get the policy
        restPolicyMockMvc.perform(get("/api/policies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);
        policySearchRepository.save(policy);
        int databaseSizeBeforeUpdate = policyRepository.findAll().size();

        // Update the policy
        Policy updatedPolicy = new Policy();
        updatedPolicy.setId(policy.getId());
        updatedPolicy.setPolicyNumber(UPDATED_POLICY_NUMBER);
        updatedPolicy.setBaseState(UPDATED_BASE_STATE);

        restPolicyMockMvc.perform(put("/api/policies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPolicy)))
                .andExpect(status().isOk());

        // Validate the Policy in the database
        List<Policy> policies = policyRepository.findAll();
        assertThat(policies).hasSize(databaseSizeBeforeUpdate);
        Policy testPolicy = policies.get(policies.size() - 1);
        assertThat(testPolicy.getPolicyNumber()).isEqualTo(UPDATED_POLICY_NUMBER);
        assertThat(testPolicy.getBaseState()).isEqualTo(UPDATED_BASE_STATE);

        // Validate the Policy in ElasticSearch
        Policy policyEs = policySearchRepository.findOne(testPolicy.getId());
        assertThat(policyEs).isEqualToComparingFieldByField(testPolicy);
    }

    @Test
    @Transactional
    public void deletePolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);
        policySearchRepository.save(policy);
        int databaseSizeBeforeDelete = policyRepository.findAll().size();

        // Get the policy
        restPolicyMockMvc.perform(delete("/api/policies/{id}", policy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean policyExistsInEs = policySearchRepository.exists(policy.getId());
        assertThat(policyExistsInEs).isFalse();

        // Validate the database is empty
        List<Policy> policies = policyRepository.findAll();
        assertThat(policies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPolicy() throws Exception {
        // Initialize the database
        policyRepository.saveAndFlush(policy);
        policySearchRepository.save(policy);

        // Search the policy
        restPolicyMockMvc.perform(get("/api/_search/policies?query=id:" + policy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policy.getId().intValue())))
            .andExpect(jsonPath("$.[*].policyNumber").value(hasItem(DEFAULT_POLICY_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].baseState").value(hasItem(DEFAULT_BASE_STATE.toString())));
    }
}
