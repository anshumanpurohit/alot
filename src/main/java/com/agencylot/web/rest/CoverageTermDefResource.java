package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.CoverageTermDef;
import com.agencylot.repository.CoverageTermDefRepository;
import com.agencylot.repository.search.CoverageTermDefSearchRepository;
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
 * REST controller for managing CoverageTermDef.
 */
@RestController
@RequestMapping("/api")
public class CoverageTermDefResource {

    private final Logger log = LoggerFactory.getLogger(CoverageTermDefResource.class);
        
    @Inject
    private CoverageTermDefRepository coverageTermDefRepository;
    
    @Inject
    private CoverageTermDefSearchRepository coverageTermDefSearchRepository;
    
    /**
     * POST  /coverage-term-defs : Create a new coverageTermDef.
     *
     * @param coverageTermDef the coverageTermDef to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverageTermDef, or with status 400 (Bad Request) if the coverageTermDef has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-defs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermDef> createCoverageTermDef(@RequestBody CoverageTermDef coverageTermDef) throws URISyntaxException {
        log.debug("REST request to save CoverageTermDef : {}", coverageTermDef);
        if (coverageTermDef.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverageTermDef", "idexists", "A new coverageTermDef cannot already have an ID")).body(null);
        }
        CoverageTermDef result = coverageTermDefRepository.save(coverageTermDef);
        coverageTermDefSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverage-term-defs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverageTermDef", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverage-term-defs : Updates an existing coverageTermDef.
     *
     * @param coverageTermDef the coverageTermDef to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverageTermDef,
     * or with status 400 (Bad Request) if the coverageTermDef is not valid,
     * or with status 500 (Internal Server Error) if the coverageTermDef couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-defs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermDef> updateCoverageTermDef(@RequestBody CoverageTermDef coverageTermDef) throws URISyntaxException {
        log.debug("REST request to update CoverageTermDef : {}", coverageTermDef);
        if (coverageTermDef.getId() == null) {
            return createCoverageTermDef(coverageTermDef);
        }
        CoverageTermDef result = coverageTermDefRepository.save(coverageTermDef);
        coverageTermDefSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverageTermDef", coverageTermDef.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverage-term-defs : get all the coverageTermDefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverageTermDefs in body
     */
    @RequestMapping(value = "/coverage-term-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermDef> getAllCoverageTermDefs() {
        log.debug("REST request to get all CoverageTermDefs");
        List<CoverageTermDef> coverageTermDefs = coverageTermDefRepository.findAll();
        return coverageTermDefs;
    }

    /**
     * GET  /coverage-term-defs/:id : get the "id" coverageTermDef.
     *
     * @param id the id of the coverageTermDef to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverageTermDef, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverage-term-defs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermDef> getCoverageTermDef(@PathVariable Long id) {
        log.debug("REST request to get CoverageTermDef : {}", id);
        CoverageTermDef coverageTermDef = coverageTermDefRepository.findOne(id);
        return Optional.ofNullable(coverageTermDef)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverage-term-defs/:id : delete the "id" coverageTermDef.
     *
     * @param id the id of the coverageTermDef to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverage-term-defs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverageTermDef(@PathVariable Long id) {
        log.debug("REST request to delete CoverageTermDef : {}", id);
        coverageTermDefRepository.delete(id);
        coverageTermDefSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverageTermDef", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverage-term-defs?query=:query : search for the coverageTermDef corresponding
     * to the query.
     *
     * @param query the query of the coverageTermDef search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverage-term-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermDef> searchCoverageTermDefs(@RequestParam String query) {
        log.debug("REST request to search CoverageTermDefs for query {}", query);
        return StreamSupport
            .stream(coverageTermDefSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
