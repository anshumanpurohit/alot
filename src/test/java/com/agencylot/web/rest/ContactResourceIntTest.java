package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Contact;
import com.agencylot.repository.ContactRepository;
import com.agencylot.repository.search.ContactSearchRepository;

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
 * Test class for the ContactResource REST controller.
 *
 * @see ContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContactResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

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
    private ContactRepository contactRepository;

    @Inject
    private ContactSearchRepository contactSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContactMockMvc;

    private Contact contact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContactResource contactResource = new ContactResource();
        ReflectionTestUtils.setField(contactResource, "contactSearchRepository", contactSearchRepository);
        ReflectionTestUtils.setField(contactResource, "contactRepository", contactRepository);
        this.restContactMockMvc = MockMvcBuilders.standaloneSetup(contactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contactSearchRepository.deleteAll();
        contact = new Contact();
        contact.setFixedId(DEFAULT_FIXED_ID);
        contact.setDeleted(DEFAULT_DELETED);
        contact.setFirstName(DEFAULT_FIRST_NAME);
        contact.setLastName(DEFAULT_LAST_NAME);
        contact.setMiddleInitial(DEFAULT_MIDDLE_INITIAL);
        contact.setDob(DEFAULT_DOB);
        contact.setWorkPhone(DEFAULT_WORK_PHONE);
        contact.setHomePhone(DEFAULT_HOME_PHONE);
        contact.setEmailAddress(DEFAULT_EMAIL_ADDRESS);
        contact.setCommunicationPreference(DEFAULT_COMMUNICATION_PREFERENCE);
        contact.setCompanyName(DEFAULT_COMPANY_NAME);
        contact.setExternalReferenceId(DEFAULT_EXTERNAL_REFERENCE_ID);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact

        restContactMockMvc.perform(post("/api/contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contact)))
                .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contacts.get(contacts.size() - 1);
        assertThat(testContact.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testContact.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContact.getMiddleInitial()).isEqualTo(DEFAULT_MIDDLE_INITIAL);
        assertThat(testContact.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testContact.getWorkPhone()).isEqualTo(DEFAULT_WORK_PHONE);
        assertThat(testContact.getHomePhone()).isEqualTo(DEFAULT_HOME_PHONE);
        assertThat(testContact.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testContact.getCommunicationPreference()).isEqualTo(DEFAULT_COMMUNICATION_PREFERENCE);
        assertThat(testContact.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testContact.getExternalReferenceId()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE_ID);

        // Validate the Contact in ElasticSearch
        Contact contactEs = contactSearchRepository.findOne(testContact.getId());
        assertThat(contactEs).isEqualToComparingFieldByField(testContact);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contacts
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
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
    public void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
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
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        contactSearchRepository.save(contact);
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = new Contact();
        updatedContact.setId(contact.getId());
        updatedContact.setFixedId(UPDATED_FIXED_ID);
        updatedContact.setDeleted(UPDATED_DELETED);
        updatedContact.setFirstName(UPDATED_FIRST_NAME);
        updatedContact.setLastName(UPDATED_LAST_NAME);
        updatedContact.setMiddleInitial(UPDATED_MIDDLE_INITIAL);
        updatedContact.setDob(UPDATED_DOB);
        updatedContact.setWorkPhone(UPDATED_WORK_PHONE);
        updatedContact.setHomePhone(UPDATED_HOME_PHONE);
        updatedContact.setEmailAddress(UPDATED_EMAIL_ADDRESS);
        updatedContact.setCommunicationPreference(UPDATED_COMMUNICATION_PREFERENCE);
        updatedContact.setCompanyName(UPDATED_COMPANY_NAME);
        updatedContact.setExternalReferenceId(UPDATED_EXTERNAL_REFERENCE_ID);

        restContactMockMvc.perform(put("/api/contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContact)))
                .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contacts.get(contacts.size() - 1);
        assertThat(testContact.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testContact.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContact.getMiddleInitial()).isEqualTo(UPDATED_MIDDLE_INITIAL);
        assertThat(testContact.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testContact.getWorkPhone()).isEqualTo(UPDATED_WORK_PHONE);
        assertThat(testContact.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testContact.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testContact.getCommunicationPreference()).isEqualTo(UPDATED_COMMUNICATION_PREFERENCE);
        assertThat(testContact.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testContact.getExternalReferenceId()).isEqualTo(UPDATED_EXTERNAL_REFERENCE_ID);

        // Validate the Contact in ElasticSearch
        Contact contactEs = contactSearchRepository.findOne(testContact.getId());
        assertThat(contactEs).isEqualToComparingFieldByField(testContact);
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        contactSearchRepository.save(contact);
        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Get the contact
        restContactMockMvc.perform(delete("/api/contacts/{id}", contact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contactExistsInEs = contactSearchRepository.exists(contact.getId());
        assertThat(contactExistsInEs).isFalse();

        // Validate the database is empty
        List<Contact> contacts = contactRepository.findAll();
        assertThat(contacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        contactSearchRepository.save(contact);

        // Search the contact
        restContactMockMvc.perform(get("/api/_search/contacts?query=id:" + contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
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
