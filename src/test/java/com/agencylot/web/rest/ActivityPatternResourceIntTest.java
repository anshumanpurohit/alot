package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.ActivityPattern;
import com.agencylot.repository.ActivityPatternRepository;
import com.agencylot.repository.search.ActivityPatternSearchRepository;

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
 * Test class for the ActivityPatternResource REST controller.
 *
 * @see ActivityPatternResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ActivityPatternResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_ESCALATION_DAYS = 1;
    private static final Integer UPDATED_ESCALATION_DAYS = 2;
    private static final String DEFAULT_CREATE_ACTIVITY_FOR = "AAAAA";
    private static final String UPDATED_CREATE_ACTIVITY_FOR = "BBBBB";

    @Inject
    private ActivityPatternRepository activityPatternRepository;

    @Inject
    private ActivityPatternSearchRepository activityPatternSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActivityPatternMockMvc;

    private ActivityPattern activityPattern;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityPatternResource activityPatternResource = new ActivityPatternResource();
        ReflectionTestUtils.setField(activityPatternResource, "activityPatternSearchRepository", activityPatternSearchRepository);
        ReflectionTestUtils.setField(activityPatternResource, "activityPatternRepository", activityPatternRepository);
        this.restActivityPatternMockMvc = MockMvcBuilders.standaloneSetup(activityPatternResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        activityPatternSearchRepository.deleteAll();
        activityPattern = new ActivityPattern();
        activityPattern.setFixedId(DEFAULT_FIXED_ID);
        activityPattern.setDeleted(DEFAULT_DELETED);
        activityPattern.setDescription(DEFAULT_DESCRIPTION);
        activityPattern.setEscalationDays(DEFAULT_ESCALATION_DAYS);
        activityPattern.setCreateActivityFor(DEFAULT_CREATE_ACTIVITY_FOR);
    }

    @Test
    @Transactional
    public void createActivityPattern() throws Exception {
        int databaseSizeBeforeCreate = activityPatternRepository.findAll().size();

        // Create the ActivityPattern

        restActivityPatternMockMvc.perform(post("/api/activity-patterns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityPattern)))
                .andExpect(status().isCreated());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatterns = activityPatternRepository.findAll();
        assertThat(activityPatterns).hasSize(databaseSizeBeforeCreate + 1);
        ActivityPattern testActivityPattern = activityPatterns.get(activityPatterns.size() - 1);
        assertThat(testActivityPattern.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testActivityPattern.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testActivityPattern.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testActivityPattern.getEscalationDays()).isEqualTo(DEFAULT_ESCALATION_DAYS);
        assertThat(testActivityPattern.getCreateActivityFor()).isEqualTo(DEFAULT_CREATE_ACTIVITY_FOR);

        // Validate the ActivityPattern in ElasticSearch
        ActivityPattern activityPatternEs = activityPatternSearchRepository.findOne(testActivityPattern.getId());
        assertThat(activityPatternEs).isEqualToComparingFieldByField(testActivityPattern);
    }

    @Test
    @Transactional
    public void getAllActivityPatterns() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get all the activityPatterns
        restActivityPatternMockMvc.perform(get("/api/activity-patterns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activityPattern.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].escalationDays").value(hasItem(DEFAULT_ESCALATION_DAYS)))
                .andExpect(jsonPath("$.[*].createActivityFor").value(hasItem(DEFAULT_CREATE_ACTIVITY_FOR.toString())));
    }

    @Test
    @Transactional
    public void getActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);

        // Get the activityPattern
        restActivityPatternMockMvc.perform(get("/api/activity-patterns/{id}", activityPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activityPattern.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.escalationDays").value(DEFAULT_ESCALATION_DAYS))
            .andExpect(jsonPath("$.createActivityFor").value(DEFAULT_CREATE_ACTIVITY_FOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityPattern() throws Exception {
        // Get the activityPattern
        restActivityPatternMockMvc.perform(get("/api/activity-patterns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);
        activityPatternSearchRepository.save(activityPattern);
        int databaseSizeBeforeUpdate = activityPatternRepository.findAll().size();

        // Update the activityPattern
        ActivityPattern updatedActivityPattern = new ActivityPattern();
        updatedActivityPattern.setId(activityPattern.getId());
        updatedActivityPattern.setFixedId(UPDATED_FIXED_ID);
        updatedActivityPattern.setDeleted(UPDATED_DELETED);
        updatedActivityPattern.setDescription(UPDATED_DESCRIPTION);
        updatedActivityPattern.setEscalationDays(UPDATED_ESCALATION_DAYS);
        updatedActivityPattern.setCreateActivityFor(UPDATED_CREATE_ACTIVITY_FOR);

        restActivityPatternMockMvc.perform(put("/api/activity-patterns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivityPattern)))
                .andExpect(status().isOk());

        // Validate the ActivityPattern in the database
        List<ActivityPattern> activityPatterns = activityPatternRepository.findAll();
        assertThat(activityPatterns).hasSize(databaseSizeBeforeUpdate);
        ActivityPattern testActivityPattern = activityPatterns.get(activityPatterns.size() - 1);
        assertThat(testActivityPattern.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testActivityPattern.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testActivityPattern.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testActivityPattern.getEscalationDays()).isEqualTo(UPDATED_ESCALATION_DAYS);
        assertThat(testActivityPattern.getCreateActivityFor()).isEqualTo(UPDATED_CREATE_ACTIVITY_FOR);

        // Validate the ActivityPattern in ElasticSearch
        ActivityPattern activityPatternEs = activityPatternSearchRepository.findOne(testActivityPattern.getId());
        assertThat(activityPatternEs).isEqualToComparingFieldByField(testActivityPattern);
    }

    @Test
    @Transactional
    public void deleteActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);
        activityPatternSearchRepository.save(activityPattern);
        int databaseSizeBeforeDelete = activityPatternRepository.findAll().size();

        // Get the activityPattern
        restActivityPatternMockMvc.perform(delete("/api/activity-patterns/{id}", activityPattern.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean activityPatternExistsInEs = activityPatternSearchRepository.exists(activityPattern.getId());
        assertThat(activityPatternExistsInEs).isFalse();

        // Validate the database is empty
        List<ActivityPattern> activityPatterns = activityPatternRepository.findAll();
        assertThat(activityPatterns).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchActivityPattern() throws Exception {
        // Initialize the database
        activityPatternRepository.saveAndFlush(activityPattern);
        activityPatternSearchRepository.save(activityPattern);

        // Search the activityPattern
        restActivityPatternMockMvc.perform(get("/api/_search/activity-patterns?query=id:" + activityPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].escalationDays").value(hasItem(DEFAULT_ESCALATION_DAYS)))
            .andExpect(jsonPath("$.[*].createActivityFor").value(hasItem(DEFAULT_CREATE_ACTIVITY_FOR.toString())));
    }
}
