package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.ActivityPattern;
import com.agencylot.repository.ActivityPatternRepository;
import com.agencylot.repository.search.ActivityPatternSearchRepository;
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
 * REST controller for managing ActivityPattern.
 */
@RestController
@RequestMapping("/api")
public class ActivityPatternResource {

    private final Logger log = LoggerFactory.getLogger(ActivityPatternResource.class);
        
    @Inject
    private ActivityPatternRepository activityPatternRepository;
    
    @Inject
    private ActivityPatternSearchRepository activityPatternSearchRepository;
    
    /**
     * POST  /activity-patterns : Create a new activityPattern.
     *
     * @param activityPattern the activityPattern to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activityPattern, or with status 400 (Bad Request) if the activityPattern has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activity-patterns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityPattern> createActivityPattern(@RequestBody ActivityPattern activityPattern) throws URISyntaxException {
        log.debug("REST request to save ActivityPattern : {}", activityPattern);
        if (activityPattern.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activityPattern", "idexists", "A new activityPattern cannot already have an ID")).body(null);
        }
        ActivityPattern result = activityPatternRepository.save(activityPattern);
        activityPatternSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/activity-patterns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activityPattern", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activity-patterns : Updates an existing activityPattern.
     *
     * @param activityPattern the activityPattern to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activityPattern,
     * or with status 400 (Bad Request) if the activityPattern is not valid,
     * or with status 500 (Internal Server Error) if the activityPattern couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activity-patterns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityPattern> updateActivityPattern(@RequestBody ActivityPattern activityPattern) throws URISyntaxException {
        log.debug("REST request to update ActivityPattern : {}", activityPattern);
        if (activityPattern.getId() == null) {
            return createActivityPattern(activityPattern);
        }
        ActivityPattern result = activityPatternRepository.save(activityPattern);
        activityPatternSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activityPattern", activityPattern.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activity-patterns : get all the activityPatterns.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of activityPatterns in body
     */
    @RequestMapping(value = "/activity-patterns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActivityPattern> getAllActivityPatterns() {
        log.debug("REST request to get all ActivityPatterns");
        List<ActivityPattern> activityPatterns = activityPatternRepository.findAll();
        return activityPatterns;
    }

    /**
     * GET  /activity-patterns/:id : get the "id" activityPattern.
     *
     * @param id the id of the activityPattern to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activityPattern, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/activity-patterns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityPattern> getActivityPattern(@PathVariable Long id) {
        log.debug("REST request to get ActivityPattern : {}", id);
        ActivityPattern activityPattern = activityPatternRepository.findOne(id);
        return Optional.ofNullable(activityPattern)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activity-patterns/:id : delete the "id" activityPattern.
     *
     * @param id the id of the activityPattern to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/activity-patterns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivityPattern(@PathVariable Long id) {
        log.debug("REST request to delete ActivityPattern : {}", id);
        activityPatternRepository.delete(id);
        activityPatternSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activityPattern", id.toString())).build();
    }

    /**
     * SEARCH  /_search/activity-patterns?query=:query : search for the activityPattern corresponding
     * to the query.
     *
     * @param query the query of the activityPattern search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/activity-patterns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActivityPattern> searchActivityPatterns(@RequestParam String query) {
        log.debug("REST request to search ActivityPatterns for query {}", query);
        return StreamSupport
            .stream(activityPatternSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
