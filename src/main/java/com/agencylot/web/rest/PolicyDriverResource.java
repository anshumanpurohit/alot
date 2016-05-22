package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.PolicyDriver;
import com.agencylot.repository.PolicyDriverRepository;
import com.agencylot.repository.search.PolicyDriverSearchRepository;
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
 * REST controller for managing PolicyDriver.
 */
@RestController
@RequestMapping("/api")
public class PolicyDriverResource {

    private final Logger log = LoggerFactory.getLogger(PolicyDriverResource.class);
        
    @Inject
    private PolicyDriverRepository policyDriverRepository;
    
    @Inject
    private PolicyDriverSearchRepository policyDriverSearchRepository;
    
    /**
     * POST  /policy-drivers : Create a new policyDriver.
     *
     * @param policyDriver the policyDriver to create
     * @return the ResponseEntity with status 201 (Created) and with body the new policyDriver, or with status 400 (Bad Request) if the policyDriver has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policy-drivers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyDriver> createPolicyDriver(@RequestBody PolicyDriver policyDriver) throws URISyntaxException {
        log.debug("REST request to save PolicyDriver : {}", policyDriver);
        if (policyDriver.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("policyDriver", "idexists", "A new policyDriver cannot already have an ID")).body(null);
        }
        PolicyDriver result = policyDriverRepository.save(policyDriver);
        policyDriverSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/policy-drivers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("policyDriver", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /policy-drivers : Updates an existing policyDriver.
     *
     * @param policyDriver the policyDriver to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated policyDriver,
     * or with status 400 (Bad Request) if the policyDriver is not valid,
     * or with status 500 (Internal Server Error) if the policyDriver couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policy-drivers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyDriver> updatePolicyDriver(@RequestBody PolicyDriver policyDriver) throws URISyntaxException {
        log.debug("REST request to update PolicyDriver : {}", policyDriver);
        if (policyDriver.getId() == null) {
            return createPolicyDriver(policyDriver);
        }
        PolicyDriver result = policyDriverRepository.save(policyDriver);
        policyDriverSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("policyDriver", policyDriver.getId().toString()))
            .body(result);
    }

    /**
     * GET  /policy-drivers : get all the policyDrivers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of policyDrivers in body
     */
    @RequestMapping(value = "/policy-drivers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolicyDriver> getAllPolicyDrivers() {
        log.debug("REST request to get all PolicyDrivers");
        List<PolicyDriver> policyDrivers = policyDriverRepository.findAll();
        return policyDrivers;
    }

    /**
     * GET  /policy-drivers/:id : get the "id" policyDriver.
     *
     * @param id the id of the policyDriver to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the policyDriver, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/policy-drivers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyDriver> getPolicyDriver(@PathVariable Long id) {
        log.debug("REST request to get PolicyDriver : {}", id);
        PolicyDriver policyDriver = policyDriverRepository.findOne(id);
        return Optional.ofNullable(policyDriver)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /policy-drivers/:id : delete the "id" policyDriver.
     *
     * @param id the id of the policyDriver to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/policy-drivers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolicyDriver(@PathVariable Long id) {
        log.debug("REST request to delete PolicyDriver : {}", id);
        policyDriverRepository.delete(id);
        policyDriverSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("policyDriver", id.toString())).build();
    }

    /**
     * SEARCH  /_search/policy-drivers?query=:query : search for the policyDriver corresponding
     * to the query.
     *
     * @param query the query of the policyDriver search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/policy-drivers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolicyDriver> searchPolicyDrivers(@RequestParam String query) {
        log.debug("REST request to search PolicyDrivers for query {}", query);
        return StreamSupport
            .stream(policyDriverSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
