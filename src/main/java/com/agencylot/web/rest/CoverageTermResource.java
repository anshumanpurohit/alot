package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.CoverageTerm;
import com.agencylot.repository.CoverageTermRepository;
import com.agencylot.repository.search.CoverageTermSearchRepository;
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
 * REST controller for managing CoverageTerm.
 */
@RestController
@RequestMapping("/api")
public class CoverageTermResource {

    private final Logger log = LoggerFactory.getLogger(CoverageTermResource.class);
        
    @Inject
    private CoverageTermRepository coverageTermRepository;
    
    @Inject
    private CoverageTermSearchRepository coverageTermSearchRepository;
    
    /**
     * POST  /coverage-terms : Create a new coverageTerm.
     *
     * @param coverageTerm the coverageTerm to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverageTerm, or with status 400 (Bad Request) if the coverageTerm has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-terms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTerm> createCoverageTerm(@RequestBody CoverageTerm coverageTerm) throws URISyntaxException {
        log.debug("REST request to save CoverageTerm : {}", coverageTerm);
        if (coverageTerm.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverageTerm", "idexists", "A new coverageTerm cannot already have an ID")).body(null);
        }
        CoverageTerm result = coverageTermRepository.save(coverageTerm);
        coverageTermSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverage-terms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverageTerm", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverage-terms : Updates an existing coverageTerm.
     *
     * @param coverageTerm the coverageTerm to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverageTerm,
     * or with status 400 (Bad Request) if the coverageTerm is not valid,
     * or with status 500 (Internal Server Error) if the coverageTerm couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-terms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTerm> updateCoverageTerm(@RequestBody CoverageTerm coverageTerm) throws URISyntaxException {
        log.debug("REST request to update CoverageTerm : {}", coverageTerm);
        if (coverageTerm.getId() == null) {
            return createCoverageTerm(coverageTerm);
        }
        CoverageTerm result = coverageTermRepository.save(coverageTerm);
        coverageTermSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverageTerm", coverageTerm.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverage-terms : get all the coverageTerms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverageTerms in body
     */
    @RequestMapping(value = "/coverage-terms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTerm> getAllCoverageTerms() {
        log.debug("REST request to get all CoverageTerms");
        List<CoverageTerm> coverageTerms = coverageTermRepository.findAll();
        return coverageTerms;
    }

    /**
     * GET  /coverage-terms/:id : get the "id" coverageTerm.
     *
     * @param id the id of the coverageTerm to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverageTerm, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverage-terms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTerm> getCoverageTerm(@PathVariable Long id) {
        log.debug("REST request to get CoverageTerm : {}", id);
        CoverageTerm coverageTerm = coverageTermRepository.findOne(id);
        return Optional.ofNullable(coverageTerm)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverage-terms/:id : delete the "id" coverageTerm.
     *
     * @param id the id of the coverageTerm to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverage-terms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverageTerm(@PathVariable Long id) {
        log.debug("REST request to delete CoverageTerm : {}", id);
        coverageTermRepository.delete(id);
        coverageTermSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverageTerm", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverage-terms?query=:query : search for the coverageTerm corresponding
     * to the query.
     *
     * @param query the query of the coverageTerm search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverage-terms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTerm> searchCoverageTerms(@RequestParam String query) {
        log.debug("REST request to search CoverageTerms for query {}", query);
        return StreamSupport
            .stream(coverageTermSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
