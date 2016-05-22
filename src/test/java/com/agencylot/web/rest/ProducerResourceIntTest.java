package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Producer;
import com.agencylot.repository.ProducerRepository;
import com.agencylot.repository.search.ProducerSearchRepository;

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
 * Test class for the ProducerResource REST controller.
 *
 * @see ProducerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProducerResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_EXTERNAL_REFERENCE_ID = "AAAAA";
    private static final String UPDATED_EXTERNAL_REFERENCE_ID = "BBBBB";

    @Inject
    private ProducerRepository producerRepository;

    @Inject
    private ProducerSearchRepository producerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProducerMockMvc;

    private Producer producer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProducerResource producerResource = new ProducerResource();
        ReflectionTestUtils.setField(producerResource, "producerSearchRepository", producerSearchRepository);
        ReflectionTestUtils.setField(producerResource, "producerRepository", producerRepository);
        this.restProducerMockMvc = MockMvcBuilders.standaloneSetup(producerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        producerSearchRepository.deleteAll();
        producer = new Producer();
        producer.setFixedId(DEFAULT_FIXED_ID);
        producer.setDeleted(DEFAULT_DELETED);
        producer.setExternalReferenceId(DEFAULT_EXTERNAL_REFERENCE_ID);
    }

    @Test
    @Transactional
    public void createProducer() throws Exception {
        int databaseSizeBeforeCreate = producerRepository.findAll().size();

        // Create the Producer

        restProducerMockMvc.perform(post("/api/producers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(producer)))
                .andExpect(status().isCreated());

        // Validate the Producer in the database
        List<Producer> producers = producerRepository.findAll();
        assertThat(producers).hasSize(databaseSizeBeforeCreate + 1);
        Producer testProducer = producers.get(producers.size() - 1);
        assertThat(testProducer.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testProducer.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testProducer.getExternalReferenceId()).isEqualTo(DEFAULT_EXTERNAL_REFERENCE_ID);

        // Validate the Producer in ElasticSearch
        Producer producerEs = producerSearchRepository.findOne(testProducer.getId());
        assertThat(producerEs).isEqualToComparingFieldByField(testProducer);
    }

    @Test
    @Transactional
    public void getAllProducers() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producers
        restProducerMockMvc.perform(get("/api/producers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].externalReferenceId").value(hasItem(DEFAULT_EXTERNAL_REFERENCE_ID.toString())));
    }

    @Test
    @Transactional
    public void getProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get the producer
        restProducerMockMvc.perform(get("/api/producers/{id}", producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(producer.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.externalReferenceId").value(DEFAULT_EXTERNAL_REFERENCE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProducer() throws Exception {
        // Get the producer
        restProducerMockMvc.perform(get("/api/producers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);
        producerSearchRepository.save(producer);
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Update the producer
        Producer updatedProducer = new Producer();
        updatedProducer.setId(producer.getId());
        updatedProducer.setFixedId(UPDATED_FIXED_ID);
        updatedProducer.setDeleted(UPDATED_DELETED);
        updatedProducer.setExternalReferenceId(UPDATED_EXTERNAL_REFERENCE_ID);

        restProducerMockMvc.perform(put("/api/producers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProducer)))
                .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producers = producerRepository.findAll();
        assertThat(producers).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producers.get(producers.size() - 1);
        assertThat(testProducer.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testProducer.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testProducer.getExternalReferenceId()).isEqualTo(UPDATED_EXTERNAL_REFERENCE_ID);

        // Validate the Producer in ElasticSearch
        Producer producerEs = producerSearchRepository.findOne(testProducer.getId());
        assertThat(producerEs).isEqualToComparingFieldByField(testProducer);
    }

    @Test
    @Transactional
    public void deleteProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);
        producerSearchRepository.save(producer);
        int databaseSizeBeforeDelete = producerRepository.findAll().size();

        // Get the producer
        restProducerMockMvc.perform(delete("/api/producers/{id}", producer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean producerExistsInEs = producerSearchRepository.exists(producer.getId());
        assertThat(producerExistsInEs).isFalse();

        // Validate the database is empty
        List<Producer> producers = producerRepository.findAll();
        assertThat(producers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);
        producerSearchRepository.save(producer);

        // Search the producer
        restProducerMockMvc.perform(get("/api/_search/producers?query=id:" + producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].externalReferenceId").value(hasItem(DEFAULT_EXTERNAL_REFERENCE_ID.toString())));
    }
}
