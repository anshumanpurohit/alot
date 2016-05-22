package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Address;
import com.agencylot.repository.AddressRepository;
import com.agencylot.repository.search.AddressSearchRepository;

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
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class AddressResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBB";
    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_ZIP_CODE = "AAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBB";
    private static final String DEFAULT_ZIP_4_CODE = "AAAAA";
    private static final String UPDATED_ZIP_4_CODE = "BBBBB";

    private static final Boolean DEFAULT_STANDARDIZED = false;
    private static final Boolean UPDATED_STANDARDIZED = true;
    private static final String DEFAULT_LATITUDE = "AAAAA";
    private static final String UPDATED_LATITUDE = "BBBBB";
    private static final String DEFAULT_LONGITUDE = "AAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBB";

    @Inject
    private AddressRepository addressRepository;

    @Inject
    private AddressSearchRepository addressSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressSearchRepository", addressSearchRepository);
        ReflectionTestUtils.setField(addressResource, "addressRepository", addressRepository);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        addressSearchRepository.deleteAll();
        address = new Address();
        address.setFixedId(DEFAULT_FIXED_ID);
        address.setDeleted(DEFAULT_DELETED);
        address.setAddressLine1(DEFAULT_ADDRESS_LINE_1);
        address.setAddressLine2(DEFAULT_ADDRESS_LINE_2);
        address.setCity(DEFAULT_CITY);
        address.setZipCode(DEFAULT_ZIP_CODE);
        address.setZip4Code(DEFAULT_ZIP_4_CODE);
        address.setStandardized(DEFAULT_STANDARDIZED);
        address.setLatitude(DEFAULT_LATITUDE);
        address.setLongitude(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testAddress.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testAddress.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testAddress.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAddress.getZip4Code()).isEqualTo(DEFAULT_ZIP_4_CODE);
        assertThat(testAddress.isStandardized()).isEqualTo(DEFAULT_STANDARDIZED);
        assertThat(testAddress.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testAddress.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);

        // Validate the Address in ElasticSearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addresses
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
                .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].zip4Code").value(hasItem(DEFAULT_ZIP_4_CODE.toString())))
                .andExpect(jsonPath("$.[*].standardized").value(hasItem(DEFAULT_STANDARDIZED.booleanValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1.toString()))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.zip4Code").value(DEFAULT_ZIP_4_CODE.toString()))
            .andExpect(jsonPath("$.standardized").value(DEFAULT_STANDARDIZED.booleanValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = new Address();
        updatedAddress.setId(address.getId());
        updatedAddress.setFixedId(UPDATED_FIXED_ID);
        updatedAddress.setDeleted(UPDATED_DELETED);
        updatedAddress.setAddressLine1(UPDATED_ADDRESS_LINE_1);
        updatedAddress.setAddressLine2(UPDATED_ADDRESS_LINE_2);
        updatedAddress.setCity(UPDATED_CITY);
        updatedAddress.setZipCode(UPDATED_ZIP_CODE);
        updatedAddress.setZip4Code(UPDATED_ZIP_4_CODE);
        updatedAddress.setStandardized(UPDATED_STANDARDIZED);
        updatedAddress.setLatitude(UPDATED_LATITUDE);
        updatedAddress.setLongitude(UPDATED_LONGITUDE);

        restAddressMockMvc.perform(put("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAddress)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testAddress.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testAddress.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testAddress.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddress.getZip4Code()).isEqualTo(UPDATED_ZIP_4_CODE);
        assertThat(testAddress.isStandardized()).isEqualTo(UPDATED_STANDARDIZED);
        assertThat(testAddress.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testAddress.getLongitude()).isEqualTo(UPDATED_LONGITUDE);

        // Validate the Address in ElasticSearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean addressExistsInEs = addressSearchRepository.exists(address.getId());
        assertThat(addressExistsInEs).isFalse();

        // Validate the database is empty
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);

        // Search the address
        restAddressMockMvc.perform(get("/api/_search/addresses?query=id:" + address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].zip4Code").value(hasItem(DEFAULT_ZIP_4_CODE.toString())))
            .andExpect(jsonPath("$.[*].standardized").value(hasItem(DEFAULT_STANDARDIZED.booleanValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())));
    }
}
