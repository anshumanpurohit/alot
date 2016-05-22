package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Activity;
import com.agencylot.repository.ActivityRepository;
import com.agencylot.repository.search.ActivitySearchRepository;
import com.agencylot.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);
        
    @Inject
    private ActivityRepository activityRepository;
    
    @Inject
    private ActivitySearchRepository activitySearchRepository;
    
    /**
     * POST  /activities : Create a new activity.
     *
     * @param activity the activity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activity, or with status 400 (Bad Request) if the activity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activity", "idexists", "A new activity cannot already have an ID")).body(null);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activities : Updates an existing activity.
     *
     * @param activity the activity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activity,
     * or with status 400 (Bad Request) if the activity is not valid,
     * or with status 500 (Internal Server Error) if the activity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return createActivity(activity);
        }
        Activity result = activityRepository.save(activity);
        activitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activity", activity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activities : get all the activities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of activities in body
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Activity> getAllActivities() {
        log.debug("REST request to get all Activities");
        List<Activity> activities = activityRepository.findAll();
        return activities;
    }

    /**
     * GET  /activities/:id : get the "id" activity.
     *
     * @param id the id of the activity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        Activity activity = activityRepository.findOne(id);
        return Optional.ofNullable(activity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activities/:id : delete the "id" activity.
     *
     * @param id the id of the activity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.delete(id);
        activitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/activities?query=:query : search for the activity corresponding
     * to the query.
     *
     * @param query the query of the activity search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Activity> searchActivities(@RequestParam String query) {
        log.debug("REST request to search Activities for query {}", query);
        return StreamSupport
            .stream(activitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
