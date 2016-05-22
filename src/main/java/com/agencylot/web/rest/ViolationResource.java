package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Violation;
import com.agencylot.repository.ViolationRepository;
import com.agencylot.repository.search.ViolationSearchRepository;
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
 * REST controller for managing Violation.
 */
@RestController
@RequestMapping("/api")
public class ViolationResource {

    private final Logger log = LoggerFactory.getLogger(ViolationResource.class);
        
    @Inject
    private ViolationRepository violationRepository;
    
    @Inject
    private ViolationSearchRepository violationSearchRepository;
    
    /**
     * POST  /violations : Create a new violation.
     *
     * @param violation the violation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new violation, or with status 400 (Bad Request) if the violation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/violations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Violation> createViolation(@RequestBody Violation violation) throws URISyntaxException {
        log.debug("REST request to save Violation : {}", violation);
        if (violation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("violation", "idexists", "A new violation cannot already have an ID")).body(null);
        }
        Violation result = violationRepository.save(violation);
        violationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/violations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("violation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /violations : Updates an existing violation.
     *
     * @param violation the violation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated violation,
     * or with status 400 (Bad Request) if the violation is not valid,
     * or with status 500 (Internal Server Error) if the violation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/violations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Violation> updateViolation(@RequestBody Violation violation) throws URISyntaxException {
        log.debug("REST request to update Violation : {}", violation);
        if (violation.getId() == null) {
            return createViolation(violation);
        }
        Violation result = violationRepository.save(violation);
        violationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("violation", violation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /violations : get all the violations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of violations in body
     */
    @RequestMapping(value = "/violations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Violation> getAllViolations() {
        log.debug("REST request to get all Violations");
        List<Violation> violations = violationRepository.findAll();
        return violations;
    }

    /**
     * GET  /violations/:id : get the "id" violation.
     *
     * @param id the id of the violation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the violation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/violations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Violation> getViolation(@PathVariable Long id) {
        log.debug("REST request to get Violation : {}", id);
        Violation violation = violationRepository.findOne(id);
        return Optional.ofNullable(violation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /violations/:id : delete the "id" violation.
     *
     * @param id the id of the violation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/violations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteViolation(@PathVariable Long id) {
        log.debug("REST request to delete Violation : {}", id);
        violationRepository.delete(id);
        violationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("violation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/violations?query=:query : search for the violation corresponding
     * to the query.
     *
     * @param query the query of the violation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/violations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Violation> searchViolations(@RequestParam String query) {
        log.debug("REST request to search Violations for query {}", query);
        return StreamSupport
            .stream(violationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
