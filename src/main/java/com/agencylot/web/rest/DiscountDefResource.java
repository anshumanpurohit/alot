package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.DiscountDef;
import com.agencylot.repository.DiscountDefRepository;
import com.agencylot.repository.search.DiscountDefSearchRepository;
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
 * REST controller for managing DiscountDef.
 */
@RestController
@RequestMapping("/api")
public class DiscountDefResource {

    private final Logger log = LoggerFactory.getLogger(DiscountDefResource.class);
        
    @Inject
    private DiscountDefRepository discountDefRepository;
    
    @Inject
    private DiscountDefSearchRepository discountDefSearchRepository;
    
    /**
     * POST  /discount-defs : Create a new discountDef.
     *
     * @param discountDef the discountDef to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discountDef, or with status 400 (Bad Request) if the discountDef has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discount-defs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountDef> createDiscountDef(@RequestBody DiscountDef discountDef) throws URISyntaxException {
        log.debug("REST request to save DiscountDef : {}", discountDef);
        if (discountDef.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discountDef", "idexists", "A new discountDef cannot already have an ID")).body(null);
        }
        DiscountDef result = discountDefRepository.save(discountDef);
        discountDefSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/discount-defs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discountDef", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discount-defs : Updates an existing discountDef.
     *
     * @param discountDef the discountDef to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discountDef,
     * or with status 400 (Bad Request) if the discountDef is not valid,
     * or with status 500 (Internal Server Error) if the discountDef couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discount-defs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountDef> updateDiscountDef(@RequestBody DiscountDef discountDef) throws URISyntaxException {
        log.debug("REST request to update DiscountDef : {}", discountDef);
        if (discountDef.getId() == null) {
            return createDiscountDef(discountDef);
        }
        DiscountDef result = discountDefRepository.save(discountDef);
        discountDefSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discountDef", discountDef.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discount-defs : get all the discountDefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discountDefs in body
     */
    @RequestMapping(value = "/discount-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DiscountDef> getAllDiscountDefs() {
        log.debug("REST request to get all DiscountDefs");
        List<DiscountDef> discountDefs = discountDefRepository.findAll();
        return discountDefs;
    }

    /**
     * GET  /discount-defs/:id : get the "id" discountDef.
     *
     * @param id the id of the discountDef to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discountDef, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/discount-defs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountDef> getDiscountDef(@PathVariable Long id) {
        log.debug("REST request to get DiscountDef : {}", id);
        DiscountDef discountDef = discountDefRepository.findOne(id);
        return Optional.ofNullable(discountDef)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discount-defs/:id : delete the "id" discountDef.
     *
     * @param id the id of the discountDef to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/discount-defs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscountDef(@PathVariable Long id) {
        log.debug("REST request to delete DiscountDef : {}", id);
        discountDefRepository.delete(id);
        discountDefSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discountDef", id.toString())).build();
    }

    /**
     * SEARCH  /_search/discount-defs?query=:query : search for the discountDef corresponding
     * to the query.
     *
     * @param query the query of the discountDef search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/discount-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DiscountDef> searchDiscountDefs(@RequestParam String query) {
        log.debug("REST request to search DiscountDefs for query {}", query);
        return StreamSupport
            .stream(discountDefSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
