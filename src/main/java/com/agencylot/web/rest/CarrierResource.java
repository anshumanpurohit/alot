package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Carrier;
import com.agencylot.repository.CarrierRepository;
import com.agencylot.repository.search.CarrierSearchRepository;
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
 * REST controller for managing Carrier.
 */
@RestController
@RequestMapping("/api")
public class CarrierResource {

    private final Logger log = LoggerFactory.getLogger(CarrierResource.class);
        
    @Inject
    private CarrierRepository carrierRepository;
    
    @Inject
    private CarrierSearchRepository carrierSearchRepository;
    
    /**
     * POST  /carriers : Create a new carrier.
     *
     * @param carrier the carrier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carrier, or with status 400 (Bad Request) if the carrier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/carriers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Carrier> createCarrier(@RequestBody Carrier carrier) throws URISyntaxException {
        log.debug("REST request to save Carrier : {}", carrier);
        if (carrier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carrier", "idexists", "A new carrier cannot already have an ID")).body(null);
        }
        Carrier result = carrierRepository.save(carrier);
        carrierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/carriers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carrier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /carriers : Updates an existing carrier.
     *
     * @param carrier the carrier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carrier,
     * or with status 400 (Bad Request) if the carrier is not valid,
     * or with status 500 (Internal Server Error) if the carrier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/carriers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Carrier> updateCarrier(@RequestBody Carrier carrier) throws URISyntaxException {
        log.debug("REST request to update Carrier : {}", carrier);
        if (carrier.getId() == null) {
            return createCarrier(carrier);
        }
        Carrier result = carrierRepository.save(carrier);
        carrierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carrier", carrier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /carriers : get all the carriers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of carriers in body
     */
    @RequestMapping(value = "/carriers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Carrier> getAllCarriers() {
        log.debug("REST request to get all Carriers");
        List<Carrier> carriers = carrierRepository.findAll();
        return carriers;
    }

    /**
     * GET  /carriers/:id : get the "id" carrier.
     *
     * @param id the id of the carrier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carrier, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/carriers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Carrier> getCarrier(@PathVariable Long id) {
        log.debug("REST request to get Carrier : {}", id);
        Carrier carrier = carrierRepository.findOne(id);
        return Optional.ofNullable(carrier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /carriers/:id : delete the "id" carrier.
     *
     * @param id the id of the carrier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/carriers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarrier(@PathVariable Long id) {
        log.debug("REST request to delete Carrier : {}", id);
        carrierRepository.delete(id);
        carrierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carrier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/carriers?query=:query : search for the carrier corresponding
     * to the query.
     *
     * @param query the query of the carrier search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/carriers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Carrier> searchCarriers(@RequestParam String query) {
        log.debug("REST request to search Carriers for query {}", query);
        return StreamSupport
            .stream(carrierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
