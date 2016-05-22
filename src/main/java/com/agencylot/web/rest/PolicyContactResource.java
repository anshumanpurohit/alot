package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.PolicyContact;
import com.agencylot.repository.PolicyContactRepository;
import com.agencylot.repository.search.PolicyContactSearchRepository;
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
 * REST controller for managing PolicyContact.
 */
@RestController
@RequestMapping("/api")
public class PolicyContactResource {

    private final Logger log = LoggerFactory.getLogger(PolicyContactResource.class);
        
    @Inject
    private PolicyContactRepository policyContactRepository;
    
    @Inject
    private PolicyContactSearchRepository policyContactSearchRepository;
    
    /**
     * POST  /policy-contacts : Create a new policyContact.
     *
     * @param policyContact the policyContact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new policyContact, or with status 400 (Bad Request) if the policyContact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policy-contacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyContact> createPolicyContact(@RequestBody PolicyContact policyContact) throws URISyntaxException {
        log.debug("REST request to save PolicyContact : {}", policyContact);
        if (policyContact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("policyContact", "idexists", "A new policyContact cannot already have an ID")).body(null);
        }
        PolicyContact result = policyContactRepository.save(policyContact);
        policyContactSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/policy-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("policyContact", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /policy-contacts : Updates an existing policyContact.
     *
     * @param policyContact the policyContact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated policyContact,
     * or with status 400 (Bad Request) if the policyContact is not valid,
     * or with status 500 (Internal Server Error) if the policyContact couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/policy-contacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyContact> updatePolicyContact(@RequestBody PolicyContact policyContact) throws URISyntaxException {
        log.debug("REST request to update PolicyContact : {}", policyContact);
        if (policyContact.getId() == null) {
            return createPolicyContact(policyContact);
        }
        PolicyContact result = policyContactRepository.save(policyContact);
        policyContactSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("policyContact", policyContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /policy-contacts : get all the policyContacts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of policyContacts in body
     */
    @RequestMapping(value = "/policy-contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolicyContact> getAllPolicyContacts() {
        log.debug("REST request to get all PolicyContacts");
        List<PolicyContact> policyContacts = policyContactRepository.findAll();
        return policyContacts;
    }

    /**
     * GET  /policy-contacts/:id : get the "id" policyContact.
     *
     * @param id the id of the policyContact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the policyContact, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/policy-contacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PolicyContact> getPolicyContact(@PathVariable Long id) {
        log.debug("REST request to get PolicyContact : {}", id);
        PolicyContact policyContact = policyContactRepository.findOne(id);
        return Optional.ofNullable(policyContact)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /policy-contacts/:id : delete the "id" policyContact.
     *
     * @param id the id of the policyContact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/policy-contacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePolicyContact(@PathVariable Long id) {
        log.debug("REST request to delete PolicyContact : {}", id);
        policyContactRepository.delete(id);
        policyContactSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("policyContact", id.toString())).build();
    }

    /**
     * SEARCH  /_search/policy-contacts?query=:query : search for the policyContact corresponding
     * to the query.
     *
     * @param query the query of the policyContact search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/policy-contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PolicyContact> searchPolicyContacts(@RequestParam String query) {
        log.debug("REST request to search PolicyContacts for query {}", query);
        return StreamSupport
            .stream(policyContactSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
