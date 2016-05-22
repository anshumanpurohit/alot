package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Carrier;
import com.agencylot.repository.CarrierRepository;
import com.agencylot.repository.search.CarrierSearchRepository;

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
 * Test class for the CarrierResource REST controller.
 *
 * @see CarrierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class CarrierResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CarrierRepository carrierRepository;

    @Inject
    private CarrierSearchRepository carrierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCarrierMockMvc;

    private Carrier carrier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarrierResource carrierResource = new CarrierResource();
        ReflectionTestUtils.setField(carrierResource, "carrierSearchRepository", carrierSearchRepository);
        ReflectionTestUtils.setField(carrierResource, "carrierRepository", carrierRepository);
        this.restCarrierMockMvc = MockMvcBuilders.standaloneSetup(carrierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        carrierSearchRepository.deleteAll();
        carrier = new Carrier();
        carrier.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCarrier() throws Exception {
        int databaseSizeBeforeCreate = carrierRepository.findAll().size();

        // Create the Carrier

        restCarrierMockMvc.perform(post("/api/carriers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carrier)))
                .andExpect(status().isCreated());

        // Validate the Carrier in the database
        List<Carrier> carriers = carrierRepository.findAll();
        assertThat(carriers).hasSize(databaseSizeBeforeCreate + 1);
        Carrier testCarrier = carriers.get(carriers.size() - 1);
        assertThat(testCarrier.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Carrier in ElasticSearch
        Carrier carrierEs = carrierSearchRepository.findOne(testCarrier.getId());
        assertThat(carrierEs).isEqualToComparingFieldByField(testCarrier);
    }

    @Test
    @Transactional
    public void getAllCarriers() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get all the carriers
        restCarrierMockMvc.perform(get("/api/carriers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carrier.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", carrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(carrier.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarrier() throws Exception {
        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);
        carrierSearchRepository.save(carrier);
        int databaseSizeBeforeUpdate = carrierRepository.findAll().size();

        // Update the carrier
        Carrier updatedCarrier = new Carrier();
        updatedCarrier.setId(carrier.getId());
        updatedCarrier.setDescription(UPDATED_DESCRIPTION);

        restCarrierMockMvc.perform(put("/api/carriers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCarrier)))
                .andExpect(status().isOk());

        // Validate the Carrier in the database
        List<Carrier> carriers = carrierRepository.findAll();
        assertThat(carriers).hasSize(databaseSizeBeforeUpdate);
        Carrier testCarrier = carriers.get(carriers.size() - 1);
        assertThat(testCarrier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Carrier in ElasticSearch
        Carrier carrierEs = carrierSearchRepository.findOne(testCarrier.getId());
        assertThat(carrierEs).isEqualToComparingFieldByField(testCarrier);
    }

    @Test
    @Transactional
    public void deleteCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);
        carrierSearchRepository.save(carrier);
        int databaseSizeBeforeDelete = carrierRepository.findAll().size();

        // Get the carrier
        restCarrierMockMvc.perform(delete("/api/carriers/{id}", carrier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean carrierExistsInEs = carrierSearchRepository.exists(carrier.getId());
        assertThat(carrierExistsInEs).isFalse();

        // Validate the database is empty
        List<Carrier> carriers = carrierRepository.findAll();
        assertThat(carriers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);
        carrierSearchRepository.save(carrier);

        // Search the carrier
        restCarrierMockMvc.perform(get("/api/_search/carriers?query=id:" + carrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrier.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
