package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.CoverageTermOption;
import com.agencylot.repository.CoverageTermOptionRepository;
import com.agencylot.repository.search.CoverageTermOptionSearchRepository;
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
 * REST controller for managing CoverageTermOption.
 */
@RestController
@RequestMapping("/api")
public class CoverageTermOptionResource {

    private final Logger log = LoggerFactory.getLogger(CoverageTermOptionResource.class);
        
    @Inject
    private CoverageTermOptionRepository coverageTermOptionRepository;
    
    @Inject
    private CoverageTermOptionSearchRepository coverageTermOptionSearchRepository;
    
    /**
     * POST  /coverage-term-options : Create a new coverageTermOption.
     *
     * @param coverageTermOption the coverageTermOption to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverageTermOption, or with status 400 (Bad Request) if the coverageTermOption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-options",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOption> createCoverageTermOption(@RequestBody CoverageTermOption coverageTermOption) throws URISyntaxException {
        log.debug("REST request to save CoverageTermOption : {}", coverageTermOption);
        if (coverageTermOption.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("coverageTermOption", "idexists", "A new coverageTermOption cannot already have an ID")).body(null);
        }
        CoverageTermOption result = coverageTermOptionRepository.save(coverageTermOption);
        coverageTermOptionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/coverage-term-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("coverageTermOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coverage-term-options : Updates an existing coverageTermOption.
     *
     * @param coverageTermOption the coverageTermOption to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverageTermOption,
     * or with status 400 (Bad Request) if the coverageTermOption is not valid,
     * or with status 500 (Internal Server Error) if the coverageTermOption couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/coverage-term-options",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOption> updateCoverageTermOption(@RequestBody CoverageTermOption coverageTermOption) throws URISyntaxException {
        log.debug("REST request to update CoverageTermOption : {}", coverageTermOption);
        if (coverageTermOption.getId() == null) {
            return createCoverageTermOption(coverageTermOption);
        }
        CoverageTermOption result = coverageTermOptionRepository.save(coverageTermOption);
        coverageTermOptionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("coverageTermOption", coverageTermOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coverage-term-options : get all the coverageTermOptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of coverageTermOptions in body
     */
    @RequestMapping(value = "/coverage-term-options",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermOption> getAllCoverageTermOptions() {
        log.debug("REST request to get all CoverageTermOptions");
        List<CoverageTermOption> coverageTermOptions = coverageTermOptionRepository.findAll();
        return coverageTermOptions;
    }

    /**
     * GET  /coverage-term-options/:id : get the "id" coverageTermOption.
     *
     * @param id the id of the coverageTermOption to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverageTermOption, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/coverage-term-options/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CoverageTermOption> getCoverageTermOption(@PathVariable Long id) {
        log.debug("REST request to get CoverageTermOption : {}", id);
        CoverageTermOption coverageTermOption = coverageTermOptionRepository.findOne(id);
        return Optional.ofNullable(coverageTermOption)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /coverage-term-options/:id : delete the "id" coverageTermOption.
     *
     * @param id the id of the coverageTermOption to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/coverage-term-options/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCoverageTermOption(@PathVariable Long id) {
        log.debug("REST request to delete CoverageTermOption : {}", id);
        coverageTermOptionRepository.delete(id);
        coverageTermOptionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("coverageTermOption", id.toString())).build();
    }

    /**
     * SEARCH  /_search/coverage-term-options?query=:query : search for the coverageTermOption corresponding
     * to the query.
     *
     * @param query the query of the coverageTermOption search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/coverage-term-options",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CoverageTermOption> searchCoverageTermOptions(@RequestParam String query) {
        log.debug("REST request to search CoverageTermOptions for query {}", query);
        return StreamSupport
            .stream(coverageTermOptionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
