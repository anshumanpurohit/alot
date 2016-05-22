package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.ProductLine;
import com.agencylot.repository.ProductLineRepository;
import com.agencylot.repository.search.ProductLineSearchRepository;

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
 * Test class for the ProductLineResource REST controller.
 *
 * @see ProductLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProductLineResourceIntTest {


    private static final LocalDate DEFAULT_START_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ProductLineRepository productLineRepository;

    @Inject
    private ProductLineSearchRepository productLineSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductLineMockMvc;

    private ProductLine productLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductLineResource productLineResource = new ProductLineResource();
        ReflectionTestUtils.setField(productLineResource, "productLineSearchRepository", productLineSearchRepository);
        ReflectionTestUtils.setField(productLineResource, "productLineRepository", productLineRepository);
        this.restProductLineMockMvc = MockMvcBuilders.standaloneSetup(productLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productLineSearchRepository.deleteAll();
        productLine = new ProductLine();
        productLine.setStartEffectiveDate(DEFAULT_START_EFFECTIVE_DATE);
        productLine.setEndEffectiveDate(DEFAULT_END_EFFECTIVE_DATE);
    }

    @Test
    @Transactional
    public void createProductLine() throws Exception {
        int databaseSizeBeforeCreate = productLineRepository.findAll().size();

        // Create the ProductLine

        restProductLineMockMvc.perform(post("/api/product-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productLine)))
                .andExpect(status().isCreated());

        // Validate the ProductLine in the database
        List<ProductLine> productLines = productLineRepository.findAll();
        assertThat(productLines).hasSize(databaseSizeBeforeCreate + 1);
        ProductLine testProductLine = productLines.get(productLines.size() - 1);
        assertThat(testProductLine.getStartEffectiveDate()).isEqualTo(DEFAULT_START_EFFECTIVE_DATE);
        assertThat(testProductLine.getEndEffectiveDate()).isEqualTo(DEFAULT_END_EFFECTIVE_DATE);

        // Validate the ProductLine in ElasticSearch
        ProductLine productLineEs = productLineSearchRepository.findOne(testProductLine.getId());
        assertThat(productLineEs).isEqualToComparingFieldByField(testProductLine);
    }

    @Test
    @Transactional
    public void getAllProductLines() throws Exception {
        // Initialize the database
        productLineRepository.saveAndFlush(productLine);

        // Get all the productLines
        restProductLineMockMvc.perform(get("/api/product-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].startEffectiveDate").value(hasItem(DEFAULT_START_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProductLine() throws Exception {
        // Initialize the database
        productLineRepository.saveAndFlush(productLine);

        // Get the productLine
        restProductLineMockMvc.perform(get("/api/product-lines/{id}", productLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productLine.getId().intValue()))
            .andExpect(jsonPath("$.startEffectiveDate").value(DEFAULT_START_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.endEffectiveDate").value(DEFAULT_END_EFFECTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductLine() throws Exception {
        // Get the productLine
        restProductLineMockMvc.perform(get("/api/product-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductLine() throws Exception {
        // Initialize the database
        productLineRepository.saveAndFlush(productLine);
        productLineSearchRepository.save(productLine);
        int databaseSizeBeforeUpdate = productLineRepository.findAll().size();

        // Update the productLine
        ProductLine updatedProductLine = new ProductLine();
        updatedProductLine.setId(productLine.getId());
        updatedProductLine.setStartEffectiveDate(UPDATED_START_EFFECTIVE_DATE);
        updatedProductLine.setEndEffectiveDate(UPDATED_END_EFFECTIVE_DATE);

        restProductLineMockMvc.perform(put("/api/product-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProductLine)))
                .andExpect(status().isOk());

        // Validate the ProductLine in the database
        List<ProductLine> productLines = productLineRepository.findAll();
        assertThat(productLines).hasSize(databaseSizeBeforeUpdate);
        ProductLine testProductLine = productLines.get(productLines.size() - 1);
        assertThat(testProductLine.getStartEffectiveDate()).isEqualTo(UPDATED_START_EFFECTIVE_DATE);
        assertThat(testProductLine.getEndEffectiveDate()).isEqualTo(UPDATED_END_EFFECTIVE_DATE);

        // Validate the ProductLine in ElasticSearch
        ProductLine productLineEs = productLineSearchRepository.findOne(testProductLine.getId());
        assertThat(productLineEs).isEqualToComparingFieldByField(testProductLine);
    }

    @Test
    @Transactional
    public void deleteProductLine() throws Exception {
        // Initialize the database
        productLineRepository.saveAndFlush(productLine);
        productLineSearchRepository.save(productLine);
        int databaseSizeBeforeDelete = productLineRepository.findAll().size();

        // Get the productLine
        restProductLineMockMvc.perform(delete("/api/product-lines/{id}", productLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean productLineExistsInEs = productLineSearchRepository.exists(productLine.getId());
        assertThat(productLineExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductLine> productLines = productLineRepository.findAll();
        assertThat(productLines).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductLine() throws Exception {
        // Initialize the database
        productLineRepository.saveAndFlush(productLine);
        productLineSearchRepository.save(productLine);

        // Search the productLine
        restProductLineMockMvc.perform(get("/api/_search/product-lines?query=id:" + productLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].startEffectiveDate").value(hasItem(DEFAULT_START_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())));
    }
}
