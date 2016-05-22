package com.agencylot.web.rest;

import com.agencylot.AlotApp;
import com.agencylot.domain.Activity;
import com.agencylot.repository.ActivityRepository;
import com.agencylot.repository.search.ActivitySearchRepository;

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
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AlotApp.class)
@WebAppConfiguration
@IntegrationTest
public class ActivityResourceIntTest {

    private static final String DEFAULT_FIXED_ID = "AAAAA";
    private static final String UPDATED_FIXED_ID = "BBBBB";

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ActivitySearchRepository activitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityResource activityResource = new ActivityResource();
        ReflectionTestUtils.setField(activityResource, "activitySearchRepository", activitySearchRepository);
        ReflectionTestUtils.setField(activityResource, "activityRepository", activityRepository);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        activitySearchRepository.deleteAll();
        activity = new Activity();
        activity.setFixedId(DEFAULT_FIXED_ID);
        activity.setDeleted(DEFAULT_DELETED);
        activity.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity

        restActivityMockMvc.perform(post("/api/activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activities.get(activities.size() - 1);
        assertThat(testActivity.getFixedId()).isEqualTo(DEFAULT_FIXED_ID);
        assertThat(testActivity.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Activity in ElasticSearch
        Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
        assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
    }

    @Test
    @Transactional
    public void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activities
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.fixedId").value(DEFAULT_FIXED_ID.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        Activity updatedActivity = new Activity();
        updatedActivity.setId(activity.getId());
        updatedActivity.setFixedId(UPDATED_FIXED_ID);
        updatedActivity.setDeleted(UPDATED_DELETED);
        updatedActivity.setDescription(UPDATED_DESCRIPTION);

        restActivityMockMvc.perform(put("/api/activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivity)))
                .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activities.get(activities.size() - 1);
        assertThat(testActivity.getFixedId()).isEqualTo(UPDATED_FIXED_ID);
        assertThat(testActivity.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Activity in ElasticSearch
        Activity activityEs = activitySearchRepository.findOne(testActivity.getId());
        assertThat(activityEs).isEqualToComparingFieldByField(testActivity);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);
        int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean activityExistsInEs = activitySearchRepository.exists(activity.getId());
        assertThat(activityExistsInEs).isFalse();

        // Validate the database is empty
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        activitySearchRepository.save(activity);

        // Search the activity
        restActivityMockMvc.perform(get("/api/_search/activities?query=id:" + activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedId").value(hasItem(DEFAULT_FIXED_ID.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
