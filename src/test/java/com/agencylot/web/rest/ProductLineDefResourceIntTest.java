package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.ProductLineDef;
import com.agencylot.repository.ProductLineDefRepository;
import com.agencylot.repository.search.ProductLineDefSearchRepository;

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

import com.agencylot.domain.enumeration.ProductLineType;

/**
 * Test class for the ProductLineDefResource REST controller.
 *
 * @see ProductLineDefResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProductLineDefResourceIntTest {


    private static final ProductLineType DEFAULT_PRODUCT_LINE_TYPE = ProductLineType.HOME;
    private static final ProductLineType UPDATED_PRODUCT_LINE_TYPE = ProductLineType.PERSONALAUTO;

    @Inject
    private ProductLineDefRepository productLineDefRepository;

    @Inject
    private ProductLineDefSearchRepository productLineDefSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductLineDefMockMvc;

    private ProductLineDef productLineDef;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductLineDefResource productLineDefResource = new ProductLineDefResource();
        ReflectionTestUtils.setField(productLineDefResource, "productLineDefSearchRepository", productLineDefSearchRepository);
        ReflectionTestUtils.setField(productLineDefResource, "productLineDefRepository", productLineDefRepository);
        this.restProductLineDefMockMvc = MockMvcBuilders.standaloneSetup(productLineDefResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productLineDefSearchRepository.deleteAll();
        productLineDef = new ProductLineDef();
        productLineDef.setProductLineType(DEFAULT_PRODUCT_LINE_TYPE);
    }

    @Test
    @Transactional
    public void createProductLineDef() throws Exception {
        int databaseSizeBeforeCreate = productLineDefRepository.findAll().size();

        // Create the ProductLineDef

        restProductLineDefMockMvc.perform(post("/api/product-line-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productLineDef)))
                .andExpect(status().isCreated());

        // Validate the ProductLineDef in the database
        List<ProductLineDef> productLineDefs = productLineDefRepository.findAll();
        assertThat(productLineDefs).hasSize(databaseSizeBeforeCreate + 1);
        ProductLineDef testProductLineDef = productLineDefs.get(productLineDefs.size() - 1);
        assertThat(testProductLineDef.getProductLineType()).isEqualTo(DEFAULT_PRODUCT_LINE_TYPE);

        // Validate the ProductLineDef in ElasticSearch
        ProductLineDef productLineDefEs = productLineDefSearchRepository.findOne(testProductLineDef.getId());
        assertThat(productLineDefEs).isEqualToComparingFieldByField(testProductLineDef);
    }

    @Test
    @Transactional
    public void getAllProductLineDefs() throws Exception {
        // Initialize the database
        productLineDefRepository.saveAndFlush(productLineDef);

        // Get all the productLineDefs
        restProductLineDefMockMvc.perform(get("/api/product-line-defs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productLineDef.getId().intValue())))
                .andExpect(jsonPath("$.[*].productLineType").value(hasItem(DEFAULT_PRODUCT_LINE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getProductLineDef() throws Exception {
        // Initialize the database
        productLineDefRepository.saveAndFlush(productLineDef);

        // Get the productLineDef
        restProductLineDefMockMvc.perform(get("/api/product-line-defs/{id}", productLineDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productLineDef.getId().intValue()))
            .andExpect(jsonPath("$.productLineType").value(DEFAULT_PRODUCT_LINE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductLineDef() throws Exception {
        // Get the productLineDef
        restProductLineDefMockMvc.perform(get("/api/product-line-defs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductLineDef() throws Exception {
        // Initialize the database
        productLineDefRepository.saveAndFlush(productLineDef);
        productLineDefSearchRepository.save(productLineDef);
        int databaseSizeBeforeUpdate = productLineDefRepository.findAll().size();

        // Update the productLineDef
        ProductLineDef updatedProductLineDef = new ProductLineDef();
        updatedProductLineDef.setId(productLineDef.getId());
        updatedProductLineDef.setProductLineType(UPDATED_PRODUCT_LINE_TYPE);

        restProductLineDefMockMvc.perform(put("/api/product-line-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProductLineDef)))
                .andExpect(status().isOk());

        // Validate the ProductLineDef in the database
        List<ProductLineDef> productLineDefs = productLineDefRepository.findAll();
        assertThat(productLineDefs).hasSize(databaseSizeBeforeUpdate);
        ProductLineDef testProductLineDef = productLineDefs.get(productLineDefs.size() - 1);
        assertThat(testProductLineDef.getProductLineType()).isEqualTo(UPDATED_PRODUCT_LINE_TYPE);

        // Validate the ProductLineDef in ElasticSearch
        ProductLineDef productLineDefEs = productLineDefSearchRepository.findOne(testProductLineDef.getId());
        assertThat(productLineDefEs).isEqualToComparingFieldByField(testProductLineDef);
    }

    @Test
    @Transactional
    public void deleteProductLineDef() throws Exception {
        // Initialize the database
        productLineDefRepository.saveAndFlush(productLineDef);
        productLineDefSearchRepository.save(productLineDef);
        int databaseSizeBeforeDelete = productLineDefRepository.findAll().size();

        // Get the productLineDef
        restProductLineDefMockMvc.perform(delete("/api/product-line-defs/{id}", productLineDef.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean productLineDefExistsInEs = productLineDefSearchRepository.exists(productLineDef.getId());
        assertThat(productLineDefExistsInEs).isFalse();

        // Validate the database is empty
        List<ProductLineDef> productLineDefs = productLineDefRepository.findAll();
        assertThat(productLineDefs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProductLineDef() throws Exception {
        // Initialize the database
        productLineDefRepository.saveAndFlush(productLineDef);
        productLineDefSearchRepository.save(productLineDef);

        // Search the productLineDef
        restProductLineDefMockMvc.perform(get("/api/_search/product-line-defs?query=id:" + productLineDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productLineDef.getId().intValue())))
            .andExpect(jsonPath("$.[*].productLineType").value(hasItem(DEFAULT_PRODUCT_LINE_TYPE.toString())));
    }
}
