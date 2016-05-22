package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.AddressBook;
import com.agencylot.repository.AddressBookRepository;
import com.agencylot.repository.search.AddressBookSearchRepository;

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
 * Test class for the AddressBookResource REST controller.
 *
 * @see AddressBookResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class AddressBookResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AddressBookRepository addressBookRepository;

    @Inject
    private AddressBookSearchRepository addressBookSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAddressBookMockMvc;

    private AddressBook addressBook;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressBookResource addressBookResource = new AddressBookResource();
        ReflectionTestUtils.setField(addressBookResource, "addressBookSearchRepository", addressBookSearchRepository);
        ReflectionTestUtils.setField(addressBookResource, "addressBookRepository", addressBookRepository);
        this.restAddressBookMockMvc = MockMvcBuilders.standaloneSetup(addressBookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        addressBookSearchRepository.deleteAll();
        addressBook = new AddressBook();
        addressBook.setFixedId(DEFAULT_FIXED_ID);
        addressBook.setDeleted(DEFAULT_DELETED);
        addressBook.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAddressBook() throws Exception {
        int databaseSizeBeforeCreate = addressBookRepository.findAll().size();

        // Create the AddressBook

        restAddressBookMockMvc.perform(post("/api/address-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(addressBook)))
                .andExpect(status().isCreated());

        // Validate the AddressBook in the database
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeCreate + 1);
        AddressBook testAddressBook = addressBooks.get(addressBooks.size() - 1);
        assertThat(testAddressBook.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testAddressBook.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testAddressBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the AddressBook in ElasticSearch
        AddressBook addressBookEs = addressBookSearchRepository.findOne(testAddressBook.getId());
        assertThat(addressBookEs).isEqualToComparingFieldByField(testAddressBook);
    }

    @Test
    @Transactional
    public void getAllAddressBooks() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get all the addressBooks
        restAddressBookMockMvc.perform(get("/api/address-books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(addressBook.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);

        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", addressBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(addressBook.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddressBook() throws Exception {
        // Get the addressBook
        restAddressBookMockMvc.perform(get("/api/address-books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);
        addressBookSearchRepository.save(addressBook);
        int databaseSizeBeforeUpdate = addressBookRepository.findAll().size();

        // Update the addressBook
        AddressBook updatedAddressBook = new AddressBook();
        updatedAddressBook.setId(addressBook.getId());
        updatedAddressBook.setFixedId(UPDATED_FIXED_ID);
        updatedAddressBook.setDeleted(UPDATED_DELETED);
        updatedAddressBook.setDescription(UPDATED_DESCRIPTION);

        restAddressBookMockMvc.perform(put("/api/address-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAddressBook)))
                .andExpect(status().isOk());

        // Validate the AddressBook in the database
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeUpdate);
        AddressBook testAddressBook = addressBooks.get(addressBooks.size() - 1);
        assertThat(testAddressBook.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testAddressBook.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testAddressBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the AddressBook in ElasticSearch
        AddressBook addressBookEs = addressBookSearchRepository.findOne(testAddressBook.getId());
        assertThat(addressBookEs).isEqualToComparingFieldByField(testAddressBook);
    }

    @Test
    @Transactional
    public void deleteAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);
        addressBookSearchRepository.save(addressBook);
        int databaseSizeBeforeDelete = addressBookRepository.findAll().size();

        // Get the addressBook
        restAddressBookMockMvc.perform(delete("/api/address-books/{id}", addressBook.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean addressBookExistsInEs = addressBookSearchRepository.exists(addressBook.getId());
        assertThat(addressBookExistsInEs).isFalse();

        // Validate the database is empty
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        assertThat(addressBooks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAddressBook() throws Exception {
        // Initialize the database
        addressBookRepository.saveAndFlush(addressBook);
        addressBookSearchRepository.save(addressBook);

        // Search the addressBook
        restAddressBookMockMvc.perform(get("/api/_search/address-books?query=id:" + addressBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
