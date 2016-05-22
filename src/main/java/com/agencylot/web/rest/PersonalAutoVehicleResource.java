package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.PersonalAutoVehicle;
import com.agencylot.repository.PersonalAutoVehicleRepository;
import com.agencylot.repository.search.PersonalAutoVehicleSearchRepository;
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
 * REST controller for managing PersonalAutoVehicle.
 */
@RestController
@RequestMapping("/api")
public class PersonalAutoVehicleResource {

    private final Logger log = LoggerFactory.getLogger(PersonalAutoVehicleResource.class);
        
    @Inject
    private PersonalAutoVehicleRepository personalAutoVehicleRepository;
    
    @Inject
    private PersonalAutoVehicleSearchRepository personalAutoVehicleSearchRepository;
    
    /**
     * POST  /personal-auto-vehicles : Create a new personalAutoVehicle.
     *
     * @param personalAutoVehicle the personalAutoVehicle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalAutoVehicle, or with status 400 (Bad Request) if the personalAutoVehicle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personal-auto-vehicles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonalAutoVehicle> createPersonalAutoVehicle(@RequestBody PersonalAutoVehicle personalAutoVehicle) throws URISyntaxException {
        log.debug("REST request to save PersonalAutoVehicle : {}", personalAutoVehicle);
        if (personalAutoVehicle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personalAutoVehicle", "idexists", "A new personalAutoVehicle cannot already have an ID")).body(null);
        }
        PersonalAutoVehicle result = personalAutoVehicleRepository.save(personalAutoVehicle);
        personalAutoVehicleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personal-auto-vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personalAutoVehicle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personal-auto-vehicles : Updates an existing personalAutoVehicle.
     *
     * @param personalAutoVehicle the personalAutoVehicle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalAutoVehicle,
     * or with status 400 (Bad Request) if the personalAutoVehicle is not valid,
     * or with status 500 (Internal Server Error) if the personalAutoVehicle couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personal-auto-vehicles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonalAutoVehicle> updatePersonalAutoVehicle(@RequestBody PersonalAutoVehicle personalAutoVehicle) throws URISyntaxException {
        log.debug("REST request to update PersonalAutoVehicle : {}", personalAutoVehicle);
        if (personalAutoVehicle.getId() == null) {
            return createPersonalAutoVehicle(personalAutoVehicle);
        }
        PersonalAutoVehicle result = personalAutoVehicleRepository.save(personalAutoVehicle);
        personalAutoVehicleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personalAutoVehicle", personalAutoVehicle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personal-auto-vehicles : get all the personalAutoVehicles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personalAutoVehicles in body
     */
    @RequestMapping(value = "/personal-auto-vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PersonalAutoVehicle> getAllPersonalAutoVehicles() {
        log.debug("REST request to get all PersonalAutoVehicles");
        List<PersonalAutoVehicle> personalAutoVehicles = personalAutoVehicleRepository.findAll();
        return personalAutoVehicles;
    }

    /**
     * GET  /personal-auto-vehicles/:id : get the "id" personalAutoVehicle.
     *
     * @param id the id of the personalAutoVehicle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalAutoVehicle, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/personal-auto-vehicles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonalAutoVehicle> getPersonalAutoVehicle(@PathVariable Long id) {
        log.debug("REST request to get PersonalAutoVehicle : {}", id);
        PersonalAutoVehicle personalAutoVehicle = personalAutoVehicleRepository.findOne(id);
        return Optional.ofNullable(personalAutoVehicle)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personal-auto-vehicles/:id : delete the "id" personalAutoVehicle.
     *
     * @param id the id of the personalAutoVehicle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/personal-auto-vehicles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonalAutoVehicle(@PathVariable Long id) {
        log.debug("REST request to delete PersonalAutoVehicle : {}", id);
        personalAutoVehicleRepository.delete(id);
        personalAutoVehicleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personalAutoVehicle", id.toString())).build();
    }

    /**
     * SEARCH  /_search/personal-auto-vehicles?query=:query : search for the personalAutoVehicle corresponding
     * to the query.
     *
     * @param query the query of the personalAutoVehicle search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/personal-auto-vehicles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PersonalAutoVehicle> searchPersonalAutoVehicles(@RequestParam String query) {
        log.debug("REST request to search PersonalAutoVehicles for query {}", query);
        return StreamSupport
            .stream(personalAutoVehicleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
