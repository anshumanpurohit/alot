package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Loss;
import com.agencylot.repository.LossRepository;
import com.agencylot.repository.search.LossSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LossResource REST controller.
 *
 * @see LossResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class LossResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";
    private static final String DEFAULT_EXTERNAL_REF_ID = "AAAAA";
    private static final String UPDATED_EXTERNAL_REF_ID = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_LOSS_OCCURRED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LOSS_OCCURRED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_CLAIM_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CLAIM_AMOUNT = new BigDecimal(2);

    @Inject
    private LossRepository lossRepository;

    @Inject
    private LossSearchRepository lossSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLossMockMvc;

    private Loss loss;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LossResource lossResource = new LossResource();
        ReflectionTestUtils.setField(lossResource, "lossSearchRepository", lossSearchRepository);
        ReflectionTestUtils.setField(lossResource, "lossRepository", lossRepository);
        this.restLossMockMvc = MockMvcBuilders.standaloneSetup(lossResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lossSearchRepository.deleteAll();
        loss = new Loss();
        loss.setFixedId(DEFAULT_FIXED_ID);
        loss.setExternalRefId(DEFAULT_EXTERNAL_REF_ID);
        loss.setDescription(DEFAULT_DESCRIPTION);
        loss.setLossOccurredDate(DEFAULT_LOSS_OCCURRED_DATE);
        loss.setClaimAmount(DEFAULT_CLAIM_AMOUNT);
    }

    @Test
    @Transactional
    public void createLoss() throws Exception {
        int databaseSizeBeforeCreate = lossRepository.findAll().size();

        // Create the Loss

        restLossMockMvc.perform(post("/api/losses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loss)))
                .andExpect(status().isCreated());

        // Validate the Loss in the database
        List<Loss> losses = lossRepository.findAll();
        assertThat(losses).hasSize(databaseSizeBeforeCreate + 1);
        Loss testLoss = losses.get(losses.size() - 1);
        assertThat(testLoss.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testLoss.getExternalRefId()).isEqualTo(DEFAULT_EXTERNAL_REF_ID);
        assertThat(testLoss.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLoss.getLossOccurredDate()).isEqualTo(DEFAULT_LOSS_OCCURRED_DATE);
        assertThat(testLoss.getClaimAmount()).isEqualTo(DEFAULT_CLAIM_AMOUNT);

        // Validate the Loss in ElasticSearch
        Loss lossEs = lossSearchRepository.findOne(testLoss.getId());
        assertThat(lossEs).isEqualToComparingFieldByField(testLoss);
    }

    @Test
    @Transactional
    public void getAllLosses() throws Exception {
        // Initialize the database
        lossRepository.saveAndFlush(loss);

        // Get all the losses
        restLossMockMvc.perform(get("/api/losses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(loss.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].externalRefId").value(hasItem(DEFAULT_EXTERNAL_REF_ID.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].lossOccurredDate").value(hasItem(DEFAULT_LOSS_OCCURRED_DATE.toString())))
                .andExpect(jsonPath("$.[*].claimAmount").value(hasItem(DEFAULT_CLAIM_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getLoss() throws Exception {
        // Initialize the database
        lossRepository.saveAndFlush(loss);

        // Get the loss
        restLossMockMvc.perform(get("/api/losses/{id}", loss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(loss.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.externalRefId").value(DEFAULT_EXTERNAL_REF_ID.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.lossOccurredDate").value(DEFAULT_LOSS_OCCURRED_DATE.toString()))
            .andExpect(jsonPath("$.claimAmount").value(DEFAULT_CLAIM_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLoss() throws Exception {
        // Get the loss
        restLossMockMvc.perform(get("/api/losses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoss() throws Exception {
        // Initialize the database
        lossRepository.saveAndFlush(loss);
        lossSearchRepository.save(loss);
        int databaseSizeBeforeUpdate = lossRepository.findAll().size();

        // Update the loss
        Loss updatedLoss = new Loss();
        updatedLoss.setId(loss.getId());
        updatedLoss.setFixedId(UPDATED_FIXED_ID);
        updatedLoss.setExternalRefId(UPDATED_EXTERNAL_REF_ID);
        updatedLoss.setDescription(UPDATED_DESCRIPTION);
        updatedLoss.setLossOccurredDate(UPDATED_LOSS_OCCURRED_DATE);
        updatedLoss.setClaimAmount(UPDATED_CLAIM_AMOUNT);

        restLossMockMvc.perform(put("/api/losses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLoss)))
                .andExpect(status().isOk());

        // Validate the Loss in the database
        List<Loss> losses = lossRepository.findAll();
        assertThat(losses).hasSize(databaseSizeBeforeUpdate);
        Loss testLoss = losses.get(losses.size() - 1);
        assertThat(testLoss.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testLoss.getExternalRefId()).isEqualTo(UPDATED_EXTERNAL_REF_ID);
        assertThat(testLoss.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLoss.getLossOccurredDate()).isEqualTo(UPDATED_LOSS_OCCURRED_DATE);
        assertThat(testLoss.getClaimAmount()).isEqualTo(UPDATED_CLAIM_AMOUNT);

        // Validate the Loss in ElasticSearch
        Loss lossEs = lossSearchRepository.findOne(testLoss.getId());
        assertThat(lossEs).isEqualToComparingFieldByField(testLoss);
    }

    @Test
    @Transactional
    public void deleteLoss() throws Exception {
        // Initialize the database
        lossRepository.saveAndFlush(loss);
        lossSearchRepository.save(loss);
        int databaseSizeBeforeDelete = lossRepository.findAll().size();

        // Get the loss
        restLossMockMvc.perform(delete("/api/losses/{id}", loss.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lossExistsInEs = lossSearchRepository.exists(loss.getId());
        assertThat(lossExistsInEs).isFalse();

        // Validate the database is empty
        List<Loss> losses = lossRepository.findAll();
        assertThat(losses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLoss() throws Exception {
        // Initialize the database
        lossRepository.saveAndFlush(loss);
        lossSearchRepository.save(loss);

        // Search the loss
        restLossMockMvc.perform(get("/api/_search/losses?query=id:" + loss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loss.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].externalRefId").value(hasItem(DEFAULT_EXTERNAL_REF_ID.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lossOccurredDate").value(hasItem(DEFAULT_LOSS_OCCURRED_DATE.toString())))
            .andExpect(jsonPath("$.[*].claimAmount").value(hasItem(DEFAULT_CLAIM_AMOUNT.intValue())));
    }
}
