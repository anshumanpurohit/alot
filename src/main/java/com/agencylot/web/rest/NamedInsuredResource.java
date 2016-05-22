package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.NamedInsured;
import com.agencylot.repository.NamedInsuredRepository;
import com.agencylot.repository.search.NamedInsuredSearchRepository;
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
 * REST controller for managing NamedInsured.
 */
@RestController
@RequestMapping("/api")
public class NamedInsuredResource {

    private final Logger log = LoggerFactory.getLogger(NamedInsuredResource.class);
        
    @Inject
    private NamedInsuredRepository namedInsuredRepository;
    
    @Inject
    private NamedInsuredSearchRepository namedInsuredSearchRepository;
    
    /**
     * POST  /named-insureds : Create a new namedInsured.
     *
     * @param namedInsured the namedInsured to create
     * @return the ResponseEntity with status 201 (Created) and with body the new namedInsured, or with status 400 (Bad Request) if the namedInsured has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/named-insureds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NamedInsured> createNamedInsured(@RequestBody NamedInsured namedInsured) throws URISyntaxException {
        log.debug("REST request to save NamedInsured : {}", namedInsured);
        if (namedInsured.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("namedInsured", "idexists", "A new namedInsured cannot already have an ID")).body(null);
        }
        NamedInsured result = namedInsuredRepository.save(namedInsured);
        namedInsuredSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/named-insureds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("namedInsured", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /named-insureds : Updates an existing namedInsured.
     *
     * @param namedInsured the namedInsured to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated namedInsured,
     * or with status 400 (Bad Request) if the namedInsured is not valid,
     * or with status 500 (Internal Server Error) if the namedInsured couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/named-insureds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NamedInsured> updateNamedInsured(@RequestBody NamedInsured namedInsured) throws URISyntaxException {
        log.debug("REST request to update NamedInsured : {}", namedInsured);
        if (namedInsured.getId() == null) {
            return createNamedInsured(namedInsured);
        }
        NamedInsured result = namedInsuredRepository.save(namedInsured);
        namedInsuredSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("namedInsured", namedInsured.getId().toString()))
            .body(result);
    }

    /**
     * GET  /named-insureds : get all the namedInsureds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of namedInsureds in body
     */
    @RequestMapping(value = "/named-insureds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NamedInsured> getAllNamedInsureds() {
        log.debug("REST request to get all NamedInsureds");
        List<NamedInsured> namedInsureds = namedInsuredRepository.findAll();
        return namedInsureds;
    }

    /**
     * GET  /named-insureds/:id : get the "id" namedInsured.
     *
     * @param id the id of the namedInsured to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the namedInsured, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/named-insureds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NamedInsured> getNamedInsured(@PathVariable Long id) {
        log.debug("REST request to get NamedInsured : {}", id);
        NamedInsured namedInsured = namedInsuredRepository.findOne(id);
        return Optional.ofNullable(namedInsured)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /named-insureds/:id : delete the "id" namedInsured.
     *
     * @param id the id of the namedInsured to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/named-insureds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNamedInsured(@PathVariable Long id) {
        log.debug("REST request to delete NamedInsured : {}", id);
        namedInsuredRepository.delete(id);
        namedInsuredSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("namedInsured", id.toString())).build();
    }

    /**
     * SEARCH  /_search/named-insureds?query=:query : search for the namedInsured corresponding
     * to the query.
     *
     * @param query the query of the namedInsured search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/named-insureds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NamedInsured> searchNamedInsureds(@RequestParam String query) {
        log.debug("REST request to search NamedInsureds for query {}", query);
        return StreamSupport
            .stream(namedInsuredSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
