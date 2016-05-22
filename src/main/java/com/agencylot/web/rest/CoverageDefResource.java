package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.CoverageDef;
import com.agencylot.repository.CoverageDefRepository;
import com.agencylot.repository.search.CoverageDefSearchRepository;
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
 * REST controller for managing CoverageDef.
 */
@RestController
@RequestMapping("/api")
public class CoverageDefResource {

    private final Logger log = LoggerFactory.getLogger(CoverageDefResource.class);
        
    @Inject
    private CoverageDefRepository coverageDefRepository;
    
    @Inject
    private CoverageDefSearchRepository coverageDefSearchRepository;
    
    /**
     * POST  /coverage-defs : Create a new coverageDef.
     *
     * @param coverageDef the coverageDef to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverageDef, or with status 400 (Bad Request) if the coverageDef has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-defs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageDef> createCoverageDef(@RequestBody CoverageDef coverageDef) throws URISyntaxException {
        log.debug("REST request to save CoverageDef : {}", coverageDef);
        if (coverageDef.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverageDef", "idexists", "A new coverageDef cannot already have an ID")).body(null);
        }
        CoverageDef result = coverageDefRepository.save(coverageDef);
        coverageDefSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverage-defs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverageDef", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverage-defs : Updates an existing coverageDef.
     *
     * @param coverageDef the coverageDef to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverageDef,
     * or with status 400 (Bad Request) if the coverageDef is not valid,
     * or with status 500 (Internal Server Error) if the coverageDef couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-defs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageDef> updateCoverageDef(@RequestBody CoverageDef coverageDef) throws URISyntaxException {
        log.debug("REST request to update CoverageDef : {}", coverageDef);
        if (coverageDef.getId() == null) {
            return createCoverageDef(coverageDef);
        }
        CoverageDef result = coverageDefRepository.save(coverageDef);
        coverageDefSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverageDef", coverageDef.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverage-defs : get all the coverageDefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverageDefs in body
     */
    @RequestMapping(value = "/coverage-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageDef> getAllCoverageDefs() {
        log.debug("REST request to get all CoverageDefs");
        List<CoverageDef> coverageDefs = coverageDefRepository.findAll();
        return coverageDefs;
    }

    /**
     * GET  /coverage-defs/:id : get the "id" coverageDef.
     *
     * @param id the id of the coverageDef to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverageDef, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverage-defs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageDef> getCoverageDef(@PathVariable Long id) {
        log.debug("REST request to get CoverageDef : {}", id);
        CoverageDef coverageDef = coverageDefRepository.findOne(id);
        return Optional.ofNullable(coverageDef)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverage-defs/:id : delete the "id" coverageDef.
     *
     * @param id the id of the coverageDef to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverage-defs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverageDef(@PathVariable Long id) {
        log.debug("REST request to delete CoverageDef : {}", id);
        coverageDefRepository.delete(id);
        coverageDefSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverageDef", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverage-defs?query=:query : search for the coverageDef corresponding
     * to the query.
     *
     * @param query the query of the coverageDef search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverage-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageDef> searchCoverageDefs(@RequestParam String query) {
        log.debug("REST request to search CoverageDefs for query {}", query);
        return StreamSupport
            .stream(coverageDefSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
