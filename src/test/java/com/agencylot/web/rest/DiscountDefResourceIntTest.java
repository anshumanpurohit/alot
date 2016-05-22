package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.DiscountDef;
import com.agencylot.repository.DiscountDefRepository;
import com.agencylot.repository.search.DiscountDefSearchRepository;

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
 * Test class for the DiscountDefResource REST controller.
 *
 * @see DiscountDefResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountDefResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_BEGIN_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    @Inject
    private DiscountDefRepository discountDefRepository;

    @Inject
    private DiscountDefSearchRepository discountDefSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountDefMockMvc;

    private DiscountDef discountDef;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountDefResource discountDefResource = new DiscountDefResource();
        ReflectionTestUtils.setField(discountDefResource, "discountDefSearchRepository", discountDefSearchRepository);
        ReflectionTestUtils.setField(discountDefResource, "discountDefRepository", discountDefRepository);
        this.restDiscountDefMockMvc = MockMvcBuilders.standaloneSetup(discountDefResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discountDefSearchRepository.deleteAll();
        discountDef = new DiscountDef();
        discountDef.setDescription(DEFAULT_DESCRIPTION);
        discountDef.setBeginEffectiveDate(DEFAULT_BEGIN_EFFECTIVE_DATE);
        discountDef.setEndEffectiveDate(DEFAULT_END_EFFECTIVE_DATE);
        discountDef.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createDiscountDef() throws Exception {
        int databaseSizeBeforeCreate = discountDefRepository.findAll().size();

        // Create the DiscountDef

        restDiscountDefMockMvc.perform(post("/api/discount-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountDef)))
                .andExpect(status().isCreated());

        // Validate the DiscountDef in the database
        List<DiscountDef> discountDefs = discountDefRepository.findAll();
        assertThat(discountDefs).hasSize(databaseSizeBeforeCreate + 1);
        DiscountDef testDiscountDef = discountDefs.get(discountDefs.size() - 1);
        assertThat(testDiscountDef.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDiscountDef.getBeginEffectiveDate()).isEqualTo(DEFAULT_BEGIN_EFFECTIVE_DATE);
        assertThat(testDiscountDef.getEndEffectiveDate()).isEqualTo(DEFAULT_END_EFFECTIVE_DATE);
        assertThat(testDiscountDef.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the DiscountDef in ElasticSearch
        DiscountDef discountDefEs = discountDefSearchRepository.findOne(testDiscountDef.getId());
        assertThat(discountDefEs).isEqualToComparingFieldByField(testDiscountDef);
    }

    @Test
    @Transactional
    public void getAllDiscountDefs() throws Exception {
        // Initialize the database
        discountDefRepository.saveAndFlush(discountDef);

        // Get all the discountDefs
        restDiscountDefMockMvc.perform(get("/api/discount-defs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discountDef.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getDiscountDef() throws Exception {
        // Initialize the database
        discountDefRepository.saveAndFlush(discountDef);

        // Get the discountDef
        restDiscountDefMockMvc.perform(get("/api/discount-defs/{id}", discountDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discountDef.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.beginEffectiveDate").value(DEFAULT_BEGIN_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.endEffectiveDate").value(DEFAULT_END_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountDef() throws Exception {
        // Get the discountDef
        restDiscountDefMockMvc.perform(get("/api/discount-defs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountDef() throws Exception {
        // Initialize the database
        discountDefRepository.saveAndFlush(discountDef);
        discountDefSearchRepository.save(discountDef);
        int databaseSizeBeforeUpdate = discountDefRepository.findAll().size();

        // Update the discountDef
        DiscountDef updatedDiscountDef = new DiscountDef();
        updatedDiscountDef.setId(discountDef.getId());
        updatedDiscountDef.setDescription(UPDATED_DESCRIPTION);
        updatedDiscountDef.setBeginEffectiveDate(UPDATED_BEGIN_EFFECTIVE_DATE);
        updatedDiscountDef.setEndEffectiveDate(UPDATED_END_EFFECTIVE_DATE);
        updatedDiscountDef.setState(UPDATED_STATE);

        restDiscountDefMockMvc.perform(put("/api/discount-defs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiscountDef)))
                .andExpect(status().isOk());

        // Validate the DiscountDef in the database
        List<DiscountDef> discountDefs = discountDefRepository.findAll();
        assertThat(discountDefs).hasSize(databaseSizeBeforeUpdate);
        DiscountDef testDiscountDef = discountDefs.get(discountDefs.size() - 1);
        assertThat(testDiscountDef.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDiscountDef.getBeginEffectiveDate()).isEqualTo(UPDATED_BEGIN_EFFECTIVE_DATE);
        assertThat(testDiscountDef.getEndEffectiveDate()).isEqualTo(UPDATED_END_EFFECTIVE_DATE);
        assertThat(testDiscountDef.getState()).isEqualTo(UPDATED_STATE);

        // Validate the DiscountDef in ElasticSearch
        DiscountDef discountDefEs = discountDefSearchRepository.findOne(testDiscountDef.getId());
        assertThat(discountDefEs).isEqualToComparingFieldByField(testDiscountDef);
    }

    @Test
    @Transactional
    public void deleteDiscountDef() throws Exception {
        // Initialize the database
        discountDefRepository.saveAndFlush(discountDef);
        discountDefSearchRepository.save(discountDef);
        int databaseSizeBeforeDelete = discountDefRepository.findAll().size();

        // Get the discountDef
        restDiscountDefMockMvc.perform(delete("/api/discount-defs/{id}", discountDef.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean discountDefExistsInEs = discountDefSearchRepository.exists(discountDef.getId());
        assertThat(discountDefExistsInEs).isFalse();

        // Validate the database is empty
        List<DiscountDef> discountDefs = discountDefRepository.findAll();
        assertThat(discountDefs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscountDef() throws Exception {
        // Initialize the database
        discountDefRepository.saveAndFlush(discountDef);
        discountDefSearchRepository.save(discountDef);

        // Search the discountDef
        restDiscountDefMockMvc.perform(get("/api/_search/discount-defs?query=id:" + discountDef.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountDef.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].beginEffectiveDate").value(hasItem(DEFAULT_BEGIN_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].endEffectiveDate").value(hasItem(DEFAULT_END_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
