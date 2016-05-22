package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.CoverageTermOptionDef;
import com.agencylot.repository.CoverageTermOptionDefRepository;
import com.agencylot.repository.search.CoverageTermOptionDefSearchRepository;
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
 * REST controller for managing CoverageTermOptionDef.
 */
@RestController
@RequestMapping("/api")
public class CoverageTermOptionDefResource {

    private final Logger log = LoggerFactory.getLogger(CoverageTermOptionDefResource.class);
        
    @Inject
    private CoverageTermOptionDefRepository coverageTermOptionDefRepository;
    
    @Inject
    private CoverageTermOptionDefSearchRepository coverageTermOptionDefSearchRepository;
    
    /**
     * POST  /coverage-term-option-defs : Create a new coverageTermOptionDef.
     *
     * @param coverageTermOptionDef the coverageTermOptionDef to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverageTermOptionDef, or with status 400 (Bad Request) if the coverageTermOptionDef has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-option-defs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOptionDef> createCoverageTermOptionDef(@RequestBody CoverageTermOptionDef coverageTermOptionDef) throws URISyntaxException {
        log.debug("REST request to save CoverageTermOptionDef : {}", coverageTermOptionDef);
        if (coverageTermOptionDef.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverageTermOptionDef", "idexists", "A new coverageTermOptionDef cannot already have an ID")).body(null);
        }
        CoverageTermOptionDef result = coverageTermOptionDefRepository.save(coverageTermOptionDef);
        coverageTermOptionDefSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverage-term-option-defs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverageTermOptionDef", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverage-term-option-defs : Updates an existing coverageTermOptionDef.
     *
     * @param coverageTermOptionDef the coverageTermOptionDef to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverageTermOptionDef,
     * or with status 400 (Bad Request) if the coverageTermOptionDef is not valid,
     * or with status 500 (Internal Server Error) if the coverageTermOptionDef couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-option-defs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOptionDef> updateCoverageTermOptionDef(@RequestBody CoverageTermOptionDef coverageTermOptionDef) throws URISyntaxException {
        log.debug("REST request to update CoverageTermOptionDef : {}", coverageTermOptionDef);
        if (coverageTermOptionDef.getId() == null) {
            return createCoverageTermOptionDef(coverageTermOptionDef);
        }
        CoverageTermOptionDef result = coverageTermOptionDefRepository.save(coverageTermOptionDef);
        coverageTermOptionDefSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverageTermOptionDef", coverageTermOptionDef.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverage-term-option-defs : get all the coverageTermOptionDefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverageTermOptionDefs in body
     */
    @RequestMapping(value = "/coverage-term-option-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermOptionDef> getAllCoverageTermOptionDefs() {
        log.debug("REST request to get all CoverageTermOptionDefs");
        List<CoverageTermOptionDef> coverageTermOptionDefs = coverageTermOptionDefRepository.findAll();
        return coverageTermOptionDefs;
    }

    /**
     * GET  /coverage-term-option-defs/:id : get the "id" coverageTermOptionDef.
     *
     * @param id the id of the coverageTermOptionDef to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverageTermOptionDef, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverage-term-option-defs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOptionDef> getCoverageTermOptionDef(@PathVariable Long id) {
        log.debug("REST request to get CoverageTermOptionDef : {}", id);
        CoverageTermOptionDef coverageTermOptionDef = coverageTermOptionDefRepository.findOne(id);
        return Optional.ofNullable(coverageTermOptionDef)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverage-term-option-defs/:id : delete the "id" coverageTermOptionDef.
     *
     * @param id the id of the coverageTermOptionDef to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverage-term-option-defs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverageTermOptionDef(@PathVariable Long id) {
        log.debug("REST request to delete CoverageTermOptionDef : {}", id);
        coverageTermOptionDefRepository.delete(id);
        coverageTermOptionDefSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverageTermOptionDef", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverage-term-option-defs?query=:query : search for the coverageTermOptionDef corresponding
     * to the query.
     *
     * @param query the query of the coverageTermOptionDef search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverage-term-option-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermOptionDef> searchCoverageTermOptionDefs(@RequestParam String query) {
        log.debug("REST request to search CoverageTermOptionDefs for query {}", query);
        return StreamSupport
            .stream(coverageTermOptionDefSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
