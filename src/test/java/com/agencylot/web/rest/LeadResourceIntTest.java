package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Lead;
import com.agencylot.repository.LeadRepository;
import com.agencylot.repository.search.LeadSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LeadResource REST controller.
 *
 * @see LeadResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class LeadResourceIntTest {


    private static final LocalDate DEFAULT_REQUESTED_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUESTED_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_RESPONSE_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESPONSE_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_PREMIMUM_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREMIMUM_AMOUNT = new BigDecimal(2);

    @Inject
    private LeadRepository leadRepository;

    @Inject
    private LeadSearchRepository leadSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLeadMockMvc;

    private Lead lead;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeadResource leadResource = new LeadResource();
        ReflectionTestUtils.setField(leadResource, "leadSearchRepository", leadSearchRepository);
        ReflectionTestUtils.setField(leadResource, "leadRepository", leadRepository);
        this.restLeadMockMvc = MockMvcBuilders.standaloneSetup(leadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        leadSearchRepository.deleteAll();
        lead = new Lead();
        lead.setRequestedTime(DEFAULT_REQUESTED_TIME);
        lead.setResponseTime(DEFAULT_RESPONSE_TIME);
        lead.setPremimumAmount(DEFAULT_PREMIMUM_AMOUNT);
    }

    @Test
    @Transactional
    public void createLead() throws Exception {
        int databaseSizeBeforeCreate = leadRepository.findAll().size();

        // Create the Lead

        restLeadMockMvc.perform(post("/api/leads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lead)))
                .andExpect(status().isCreated());

        // Validate the Lead in the database
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeCreate + 1);
        Lead testLead = leads.get(leads.size() - 1);
        assertThat(testLead.getRequestedTime()).isEqualTo(DEFAULT_REQUESTED_TIME);
        assertThat(testLead.getResponseTime()).isEqualTo(DEFAULT_RESPONSE_TIME);
        assertThat(testLead.getPremimumAmount()).isEqualTo(DEFAULT_PREMIMUM_AMOUNT);

        // Validate the Lead in ElasticSearch
        Lead leadEs = leadSearchRepository.findOne(testLead.getId());
        assertThat(leadEs).isEqualToComparingFieldByField(testLead);
    }

    @Test
    @Transactional
    public void getAllLeads() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get all the leads
        restLeadMockMvc.perform(get("/api/leads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestedTime").value(hasItem(DEFAULT_REQUESTED_TIME.toString())))
                .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())))
                .andExpect(jsonPath("$.[*].premimumAmount").value(hasItem(DEFAULT_PREMIMUM_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);

        // Get the lead
        restLeadMockMvc.perform(get("/api/leads/{id}", lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lead.getId().intValue()))
            .andExpect(jsonPath("$.requestedTime").value(DEFAULT_REQUESTED_TIME.toString()))
            .andExpect(jsonPath("$.responseTime").value(DEFAULT_RESPONSE_TIME.toString()))
            .andExpect(jsonPath("$.premimumAmount").value(DEFAULT_PREMIMUM_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLead() throws Exception {
        // Get the lead
        restLeadMockMvc.perform(get("/api/leads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);
        int databaseSizeBeforeUpdate = leadRepository.findAll().size();

        // Update the lead
        Lead updatedLead = new Lead();
        updatedLead.setId(lead.getId());
        updatedLead.setRequestedTime(UPDATED_REQUESTED_TIME);
        updatedLead.setResponseTime(UPDATED_RESPONSE_TIME);
        updatedLead.setPremimumAmount(UPDATED_PREMIMUM_AMOUNT);

        restLeadMockMvc.perform(put("/api/leads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLead)))
                .andExpect(status().isOk());

        // Validate the Lead in the database
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeUpdate);
        Lead testLead = leads.get(leads.size() - 1);
        assertThat(testLead.getRequestedTime()).isEqualTo(UPDATED_REQUESTED_TIME);
        assertThat(testLead.getResponseTime()).isEqualTo(UPDATED_RESPONSE_TIME);
        assertThat(testLead.getPremimumAmount()).isEqualTo(UPDATED_PREMIMUM_AMOUNT);

        // Validate the Lead in ElasticSearch
        Lead leadEs = leadSearchRepository.findOne(testLead.getId());
        assertThat(leadEs).isEqualToComparingFieldByField(testLead);
    }

    @Test
    @Transactional
    public void deleteLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);
        int databaseSizeBeforeDelete = leadRepository.findAll().size();

        // Get the lead
        restLeadMockMvc.perform(delete("/api/leads/{id}", lead.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean leadExistsInEs = leadSearchRepository.exists(lead.getId());
        assertThat(leadExistsInEs).isFalse();

        // Validate the database is empty
        List<Lead> leads = leadRepository.findAll();
        assertThat(leads).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLead() throws Exception {
        // Initialize the database
        leadRepository.saveAndFlush(lead);
        leadSearchRepository.save(lead);

        // Search the lead
        restLeadMockMvc.perform(get("/api/_search/leads?query=id:" + lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedTime").value(hasItem(DEFAULT_REQUESTED_TIME.toString())))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].premimumAmount").value(hasItem(DEFAULT_PREMIMUM_AMOUNT.intValue())));
    }
}
