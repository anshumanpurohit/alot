package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Policy;
import com.agencylot.repository.PolicyRepository;
import com.agencylot.repository.search.PolicySearchRepository;
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
 * REST controller for managing Policy.
 */
@RestController
@RequestMapping("/api")
public class PolicyResource {

    private final Logger log = LoggerFactory.getLogger(PolicyResource.class);
        
    @Inject
    private PolicyRepository policyRepository;
    
    @Inject
    private PolicySearchRepository policySearchRepository;
    
    /**
     * POST  /policies : Create a new policy.
     *
     * @param policy the policy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new policy, or with status 400 (Bad Request) if the policy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy) throws URISyntaxException {
        log.debug("REST request to save Policy : {}", policy);
        if (policy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("policy", "idexists", "A new policy cannot already have an ID")).body(null);
        }
        Policy result = policyRepository.save(policy);
        policySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/policies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("policy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /policies : Updates an existing policy.
     *
     * @param policy the policy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated policy,
     * or with status 400 (Bad Request) if the policy is not valid,
     * or with status 500 (Internal Server Error) if the policy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Policy> updatePolicy(@RequestBody Policy policy) throws URISyntaxException {
        log.debug("REST request to update Policy : {}", policy);
        if (policy.getId() == null) {
            return createPolicy(policy);
        }
        Policy result = policyRepository.save(policy);
        policySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("policy", policy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /policies : get all the policies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of policies in body
     */
    @RequestMapping(value = "/policies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Policy> getAllPolicies() {
        log.debug("REST request to get all Policies");
        List<Policy> policies = policyRepository.findAll();
        return policies;
    }

    /**
     * GET  /policies/:id : get the "id" policy.
     *
     * @param id the id of the policy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the policy, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/policies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Policy> getPolicy(@PathVariable Long id) {
        log.debug("REST request to get Policy : {}", id);
        Policy policy = policyRepository.findOne(id);
        return Optional.ofNullable(policy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /policies/:id : delete the "id" policy.
     *
     * @param id the id of the policy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/policies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        log.debug("REST request to delete Policy : {}", id);
        policyRepository.delete(id);
        policySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("policy", id.toString())).build();
    }

    /**
     * SEARCH  /_search/policies?query=:query : search for the policy corresponding
     * to the query.
     *
     * @param query the query of the policy search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/policies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Policy> searchPolicies(@RequestParam String query) {
        log.debug("REST request to search Policies for query {}", query);
        return StreamSupport
            .stream(policySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
