package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.PolicyContact;
import com.agencylot.repository.PolicyContactRepository;
import com.agencylot.repository.search.PolicyContactSearchRepository;

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

import com.agencylot.domain.enumeration.CommunicationPreference;

/**
 * Test class for the PolicyContactResource REST controller.
 *
 * @see PolicyContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class PolicyContactResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";
    private static final String DEFAULT_CONTACT_REFERENCE_ID = "AAAAA";
    private static final String UPDATED_CONTACT_REFERENCE_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_MIDDLE_INITIAL = "AAAAA";
    private static final String UPDATED_MIDDLE_INITIAL = "BBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_WORK_PHONE = "AAAAA";
    private static final String UPDATED_WORK_PHONE = "BBBBB";
    private static final String DEFAULT_HOME_PHONE = "AAAAA";
    private static final String UPDATED_HOME_PHONE = "BBBBB";
    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBB";

    private static final CommunicationPreference DEFAULT_COMMUNICATION_PREFERENCE = CommunicationPreference.PHONE;
    private static final CommunicationPreference UPDATED_COMMUNICATION_PREFERENCE = CommunicationPreference.EMAIL;
    private static final String DEFAULT_COMPANY_NAME = "AAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBB";
    private static final String DEFAULT_EXTERNAL_REFERENCE_ID = "AAAAA";
    private static final String UPDATED_EXTERNAL_REFERENCE_ID = "BBBBB";

    @Inject
    private PolicyContactRepository policyContactRepository;

    @Inject
    private PolicyContactSearchRepository policyContactSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPolicyContactMockMvc;

    private PolicyContact policyContact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PolicyContactResource policyContactResource = new PolicyContactResource();
        ReflectionTestUtils.setField(policyContactResource, "policyContactSearchRepository", policyContactSearchRepository);
        ReflectionTestUtils.setField(policyContactResource, "policyContactRepository", policyContactRepository);
        this.restPolicyContactMockMvc = MockMvcBuilders.standaloneSetup(policyContactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        policyContactSearchRepository.deleteAll();
        policyContact = new PolicyContact();
        policyContact.setFixedId(DEFAULT_FIXED_ID);
        policyContact.setContactReferenceId(DEFAULT_CONTACT_REFERENCE_ID);
        policyContact.setDeleted(DEFAULT_DELETED);
        policyContact.setFirstName(DEFAULT_FIRST_NAME);
        policyContact.setLastName(DEFAULT_LAST_NAME);
        policyContact.setMiddleInitial(DEFAULT_MIDDLE_INITIAL);
        policyContact.setDob(DEFAULT_DOB);
        policyContact.setWorkPhone(DEFAULT_WORK_PHONE);
        policyContact.setHomePhone(DEFAULT_HOME_PHONE);
        policyContact.setEmailAddress(DEFAULT_EMAIL_ADDRESS);
        policyContact.setCommunicationPreference(DEFAULT_COMMUNICATION_PREFERENCE);
        policyContact.setCompanyName(DEFAULT_COMPANY_NAME);
        policyContact.setExternalReferenceId(DEFAULT_EXTERNAL_REFERENCE_ID);
    }

    @Test
    @Transactional
    public void createPolicyContact() throws Exception {
        int databaseSizeBeforeCreate = policyContactRepository.findAll().size();

        // Create the PolicyContact

        restPolicyContactMockMvc.perform(post("/api/policy-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(policyContact)))
                .andExpect(status().isCreated());

        // Validate the PolicyContact in the database
        List<PolicyContact> policyContacts = policyContactRepository.findAll();
        assertThat(policyContacts).hasSize(databaseSizeBeforeCreate + 1);
        PolicyContact testPolicyContact = policyContacts.get(policyContacts.size() - 1);
        assertThat(testPolicyContact.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testPolicyContact.getContactReferenceId()).isEqualTo(DEFAULT_CONTACT_REFERENCE_ID);
        assertThat(testPolicyContact.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testPolicyContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPolicyContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPolicyContact.getMiddleInitial()).isEqualTo(DEFAULT_MIDDLE_INITIAL);
        assertThat(testPolicyContact.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testPolicyContact.getWorkPhone()).isEqualTo(DEFAULT_WORK_PHONE);
        assertThat(testPolicyContact.getHomePhone()).isEqualTo(DEFAULT_HOME_PHONE);
        assertThat(testPolicyContact.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testPolicyContact.getCommunicationPreference()).isEqualTo(DEFAULT_COMMUNICATION_PREFERENCE);
        assertThat(testPolicyContact.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testPolicyContact.getExternalReferenceId()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE_ID);

        // Validate the PolicyContact in ElasticSearch
        PolicyContact policyContactEs = policyContactSearchRepository.findOne(testPolicyContact.getId());
        assertThat(policyContactEs).isEqualToComparingFieldByField(testPolicyContact);
    }

    @Test
    @Transactional
    public void getAllPolicyContacts() throws Exception {
        // Initialize the database
        policyContactRepository.saveAndFlush(policyContact);

        // Get all the policyContacts
        restPolicyContactMockMvc.perform(get("/api/policy-contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(policyContact.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].contactReferenceId").value(hasItem(DEFAULT_CONTACT_REFERENCE_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].middleInitial").value(hasItem(DEFAULT_MIDDLE_INITIAL.toString())))
                .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
                .andExpect(jsonPath("$.[*].workPhone").value(hasItem(DEFAULT_WORK_PHONE.toString())))
                .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE.toString())))
                .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].communicationPreference").value(hasItem(DEFAULT_COMMUNICATION_PREFERENCE.toString())))
                .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
                .andExpect(jsonPath("$.[*].externalReferenceId").value(hasItem(DEFAULT_EXTERNAL_REFERENCE_ID.toString())));
    }

    @Test
    @Transactional
    public void getPolicyContact() throws Exception {
        // Initialize the database
        policyContactRepository.saveAndFlush(policyContact);

        // Get the policyContact
        restPolicyContactMockMvc.perform(get("/api/policy-contacts/{id}", policyContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(policyContact.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.contactReferenceId").value(DEFAULT_CONTACT_REFERENCE_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.middleInitial").value(DEFAULT_MIDDLE_INITIAL.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.workPhone").value(DEFAULT_WORK_PHONE.toString()))
            .andExpect(jsonPath("$.homePhone").value(DEFAULT_HOME_PHONE.toString()))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS.toString()))
            .andExpect(jsonPath("$.communicationPreference").value(DEFAULT_COMMUNICATION_PREFERENCE.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.externalReferenceId").value(DEFAULT_EXTERNAL_REFERENCE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolicyContact() throws Exception {
        // Get the policyContact
        restPolicyContactMockMvc.perform(get("/api/policy-contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolicyContact() throws Exception {
        // Initialize the database
        policyContactRepository.saveAndFlush(policyContact);
        policyContactSearchRepository.save(policyContact);
        int databaseSizeBeforeUpdate = policyContactRepository.findAll().size();

        // Update the policyContact
        PolicyContact updatedPolicyContact = new PolicyContact();
        updatedPolicyContact.setId(policyContact.getId());
        updatedPolicyContact.setFixedId(UPDATED_FIXED_ID);
        updatedPolicyContact.setContactReferenceId(UPDATED_CONTACT_REFERENCE_ID);
        updatedPolicyContact.setDeleted(UPDATED_DELETED);
        updatedPolicyContact.setFirstName(UPDATED_FIRST_NAME);
        updatedPolicyContact.setLastName(UPDATED_LAST_NAME);
        updatedPolicyContact.setMiddleInitial(UPDATED_MIDDLE_INITIAL);
        updatedPolicyContact.setDob(UPDATED_DOB);
        updatedPolicyContact.setWorkPhone(UPDATED_WORK_PHONE);
        updatedPolicyContact.setHomePhone(UPDATED_HOME_PHONE);
        updatedPolicyContact.setEmailAddress(UPDATED_EMAIL_ADDRESS);
        updatedPolicyContact.setCommunicationPreference(UPDATED_COMMUNICATION_PREFERENCE);
        updatedPolicyContact.setCompanyName(UPDATED_COMPANY_NAME);
        updatedPolicyContact.setExternalReferenceId(UPDATED_EXTERNAL_REFERENCE_ID);

        restPolicyContactMockMvc.perform(put("/api/policy-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPolicyContact)))
                .andExpect(status().isOk());

        // Validate the PolicyContact in the database
        List<PolicyContact> policyContacts = policyContactRepository.findAll();
        assertThat(policyContacts).hasSize(databaseSizeBeforeUpdate);
        PolicyContact testPolicyContact = policyContacts.get(policyContacts.size() - 1);
        assertThat(testPolicyContact.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testPolicyContact.getContactReferenceId()).isEqualTo(UPDATED_CONTACT_REFERENCE_ID);
        assertThat(testPolicyContact.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testPolicyContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPolicyContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPolicyContact.getMiddleInitial()).isEqualTo(UPDATED_MIDDLE_INITIAL);
        assertThat(testPolicyContact.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testPolicyContact.getWorkPhone()).isEqualTo(UPDATED_WORK_PHONE);
        assertThat(testPolicyContact.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testPolicyContact.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testPolicyContact.getCommunicationPreference()).isEqualTo(UPDATED_COMMUNICATION_PREFERENCE);
        assertThat(testPolicyContact.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testPolicyContact.getExternalReferenceId()).isEqualTo(UPDATED_EXTERNAL_REFERENCE_ID);

        // Validate the PolicyContact in ElasticSearch
        PolicyContact policyContactEs = policyContactSearchRepository.findOne(testPolicyContact.getId());
        assertThat(policyContactEs).isEqualToComparingFieldByField(testPolicyContact);
    }

    @Test
    @Transactional
    public void deletePolicyContact() throws Exception {
        // Initialize the database
        policyContactRepository.saveAndFlush(policyContact);
        policyContactSearchRepository.save(policyContact);
        int databaseSizeBeforeDelete = policyContactRepository.findAll().size();

        // Get the policyContact
        restPolicyContactMockMvc.perform(delete("/api/policy-contacts/{id}", policyContact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean policyContactExistsInEs = policyContactSearchRepository.exists(policyContact.getId());
        assertThat(policyContactExistsInEs).isFalse();

        // Validate the database is empty
        List<PolicyContact> policyContacts = policyContactRepository.findAll();
        assertThat(policyContacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPolicyContact() throws Exception {
        // Initialize the database
        policyContactRepository.saveAndFlush(policyContact);
        policyContactSearchRepository.save(policyContact);

        // Search the policyContact
        restPolicyContactMockMvc.perform(get("/api/_search/policy-contacts?query=id:" + policyContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].contactReferenceId").value(hasItem(DEFAULT_CONTACT_REFERENCE_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].middleInitial").value(hasItem(DEFAULT_MIDDLE_INITIAL.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].workPhone").value(hasItem(DEFAULT_WORK_PHONE.toString())))
            .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE.toString())))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].communicationPreference").value(hasItem(DEFAULT_COMMUNICATION_PREFERENCE.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].externalReferenceId").value(hasItem(DEFAULT_EXTERNAL_REFERENCE_ID.toString())));
    }
}
