package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Discount;
import com.agencylot.repository.DiscountRepository;
import com.agencylot.repository.search.DiscountSearchRepository;

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
 * Test class for the DiscountResource REST controller.
 *
 * @see DiscountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountResourceIntTest {


    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    @Inject
    private DiscountRepository discountRepository;

    @Inject
    private DiscountSearchRepository discountSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountMockMvc;

    private Discount discount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountResource discountResource = new DiscountResource();
        ReflectionTestUtils.setField(discountResource, "discountSearchRepository", discountSearchRepository);
        ReflectionTestUtils.setField(discountResource, "discountRepository", discountRepository);
        this.restDiscountMockMvc = MockMvcBuilders.standaloneSetup(discountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discountSearchRepository.deleteAll();
        discount = new Discount();
        discount.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createDiscount() throws Exception {
        int databaseSizeBeforeCreate = discountRepository.findAll().size();

        // Create the Discount

        restDiscountMockMvc.perform(post("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discount)))
                .andExpect(status().isCreated());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeCreate + 1);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the Discount in ElasticSearch
        Discount discountEs = discountSearchRepository.findOne(testDiscount.getId());
        assertThat(discountEs).isEqualToComparingFieldByField(testDiscount);
    }

    @Test
    @Transactional
    public void getAllDiscounts() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get all the discounts
        restDiscountMockMvc.perform(get("/api/discounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }

    @Test
    @Transactional
    public void getDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);

        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discount.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingDiscount() throws Exception {
        // Get the discount
        restDiscountMockMvc.perform(get("/api/discounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);
        discountSearchRepository.save(discount);
        int databaseSizeBeforeUpdate = discountRepository.findAll().size();

        // Update the discount
        Discount updatedDiscount = new Discount();
        updatedDiscount.setId(discount.getId());
        updatedDiscount.setAmount(UPDATED_AMOUNT);

        restDiscountMockMvc.perform(put("/api/discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiscount)))
                .andExpect(status().isOk());

        // Validate the Discount in the database
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeUpdate);
        Discount testDiscount = discounts.get(discounts.size() - 1);
        assertThat(testDiscount.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the Discount in ElasticSearch
        Discount discountEs = discountSearchRepository.findOne(testDiscount.getId());
        assertThat(discountEs).isEqualToComparingFieldByField(testDiscount);
    }

    @Test
    @Transactional
    public void deleteDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);
        discountSearchRepository.save(discount);
        int databaseSizeBeforeDelete = discountRepository.findAll().size();

        // Get the discount
        restDiscountMockMvc.perform(delete("/api/discounts/{id}", discount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean discountExistsInEs = discountSearchRepository.exists(discount.getId());
        assertThat(discountExistsInEs).isFalse();

        // Validate the database is empty
        List<Discount> discounts = discountRepository.findAll();
        assertThat(discounts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscount() throws Exception {
        // Initialize the database
        discountRepository.saveAndFlush(discount);
        discountSearchRepository.save(discount);

        // Search the discount
        restDiscountMockMvc.perform(get("/api/_search/discounts?query=id:" + discount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discount.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }
}
