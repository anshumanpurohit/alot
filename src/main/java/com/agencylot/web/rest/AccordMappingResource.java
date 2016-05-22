package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.AccordMapping;
import com.agencylot.repository.AccordMappingRepository;
import com.agencylot.repository.search.AccordMappingSearchRepository;
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
 * REST controller for managing AccordMapping.
 */
@RestController
@RequestMapping("/api")
public class AccordMappingResource {

    private final Logger log = LoggerFactory.getLogger(AccordMappingResource.class);
        
    @Inject
    private AccordMappingRepository accordMappingRepository;
    
    @Inject
    private AccordMappingSearchRepository accordMappingSearchRepository;
    
    /**
     * POST  /accord-mappings : Create a new accordMapping.
     *
     * @param accordMapping the accordMapping to create
     * @return the ResponseEntity with status 201 (Created) and with body the new accordMapping, or with status 400 (Bad Request) if the accordMapping has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/accord-mappings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccordMapping> createAccordMapping(@RequestBody AccordMapping accordMapping) throws URISyntaxException {
        log.debug("REST request to save AccordMapping : {}", accordMapping);
        if (accordMapping.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("accordMapping", "idexists", "A new accordMapping cannot already have an ID")).body(null);
        }
        AccordMapping result = accordMappingRepository.save(accordMapping);
        accordMappingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/accord-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("accordMapping", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /accord-mappings : Updates an existing accordMapping.
     *
     * @param accordMapping the accordMapping to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated accordMapping,
     * or with status 400 (Bad Request) if the accordMapping is not valid,
     * or with status 500 (Internal Server Error) if the accordMapping couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/accord-mappings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccordMapping> updateAccordMapping(@RequestBody AccordMapping accordMapping) throws URISyntaxException {
        log.debug("REST request to update AccordMapping : {}", accordMapping);
        if (accordMapping.getId() == null) {
            return createAccordMapping(accordMapping);
        }
        AccordMapping result = accordMappingRepository.save(accordMapping);
        accordMappingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("accordMapping", accordMapping.getId().toString()))
            .body(result);
    }

    /**
     * GET  /accord-mappings : get all the accordMappings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of accordMappings in body
     */
    @RequestMapping(value = "/accord-mappings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccordMapping> getAllAccordMappings() {
        log.debug("REST request to get all AccordMappings");
        List<AccordMapping> accordMappings = accordMappingRepository.findAll();
        return accordMappings;
    }

    /**
     * GET  /accord-mappings/:id : get the "id" accordMapping.
     *
     * @param id the id of the accordMapping to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the accordMapping, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/accord-mappings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccordMapping> getAccordMapping(@PathVariable Long id) {
        log.debug("REST request to get AccordMapping : {}", id);
        AccordMapping accordMapping = accordMappingRepository.findOne(id);
        return Optional.ofNullable(accordMapping)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /accord-mappings/:id : delete the "id" accordMapping.
     *
     * @param id the id of the accordMapping to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/accord-mappings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAccordMapping(@PathVariable Long id) {
        log.debug("REST request to delete AccordMapping : {}", id);
        accordMappingRepository.delete(id);
        accordMappingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("accordMapping", id.toString())).build();
    }

    /**
     * SEARCH  /_search/accord-mappings?query=:query : search for the accordMapping corresponding
     * to the query.
     *
     * @param query the query of the accordMapping search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/accord-mappings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AccordMapping> searchAccordMappings(@RequestParam String query) {
        log.debug("REST request to search AccordMappings for query {}", query);
        return StreamSupport
            .stream(accordMappingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
