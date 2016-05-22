package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Coverage;
import com.agencylot.repository.CoverageRepository;
import com.agencylot.repository.search.CoverageSearchRepository;
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
 * REST controller for managing Coverage.
 */
@RestController
@RequestMapping("/api")
public class CoverageResource {

    private final Logger log = LoggerFactory.getLogger(CoverageResource.class);
        
    @Inject
    private CoverageRepository coverageRepository;
    
    @Inject
    private CoverageSearchRepository coverageSearchRepository;
    
    /**
     * POST  /coverages : Create a new coverage.
     *
     * @param coverage the coverage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverage, or with status 400 (Bad Request) if the coverage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coverage> createCoverage(@RequestBody Coverage coverage) throws URISyntaxException {
        log.debug("REST request to save Coverage : {}", coverage);
        if (coverage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverage", "idexists", "A new coverage cannot already have an ID")).body(null);
        }
        Coverage result = coverageRepository.save(coverage);
        coverageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverages : Updates an existing coverage.
     *
     * @param coverage the coverage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverage,
     * or with status 400 (Bad Request) if the coverage is not valid,
     * or with status 500 (Internal Server Error) if the coverage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coverage> updateCoverage(@RequestBody Coverage coverage) throws URISyntaxException {
        log.debug("REST request to update Coverage : {}", coverage);
        if (coverage.getId() == null) {
            return createCoverage(coverage);
        }
        Coverage result = coverageRepository.save(coverage);
        coverageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverage", coverage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverages : get all the coverages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverages in body
     */
    @RequestMapping(value = "/coverages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Coverage> getAllCoverages() {
        log.debug("REST request to get all Coverages");
        List<Coverage> coverages = coverageRepository.findAll();
        return coverages;
    }

    /**
     * GET  /coverages/:id : get the "id" coverage.
     *
     * @param id the id of the coverage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Coverage> getCoverage(@PathVariable Long id) {
        log.debug("REST request to get Coverage : {}", id);
        Coverage coverage = coverageRepository.findOne(id);
        return Optional.ofNullable(coverage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverages/:id : delete the "id" coverage.
     *
     * @param id the id of the coverage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverage(@PathVariable Long id) {
        log.debug("REST request to delete Coverage : {}", id);
        coverageRepository.delete(id);
        coverageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverage", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverages?query=:query : search for the coverage corresponding
     * to the query.
     *
     * @param query the query of the coverage search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Coverage> searchCoverages(@RequestParam String query) {
        log.debug("REST request to search Coverages for query {}", query);
        return StreamSupport
            .stream(coverageSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
