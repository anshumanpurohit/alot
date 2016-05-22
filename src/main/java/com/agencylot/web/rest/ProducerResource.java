package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Producer;
import com.agencylot.repository.ProducerRepository;
import com.agencylot.repository.search.ProducerSearchRepository;
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
 * REST controller for managing Producer.
 */
@RestController
@RequestMapping("/api")
public class ProducerResource {

    private final Logger log = LoggerFactory.getLogger(ProducerResource.class);
        
    @Inject
    private ProducerRepository producerRepository;
    
    @Inject
    private ProducerSearchRepository producerSearchRepository;
    
    /**
     * POST  /producers : Create a new producer.
     *
     * @param producer the producer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new producer, or with status 400 (Bad Request) if the producer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/producers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Producer> createProducer(@RequestBody Producer producer) throws URISyntaxException {
        log.debug("REST request to save Producer : {}", producer);
        if (producer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("producer", "idexists", "A new producer cannot already have an ID")).body(null);
        }
        Producer result = producerRepository.save(producer);
        producerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/producers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("producer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /producers : Updates an existing producer.
     *
     * @param producer the producer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated producer,
     * or with status 400 (Bad Request) if the producer is not valid,
     * or with status 500 (Internal Server Error) if the producer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/producers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Producer> updateProducer(@RequestBody Producer producer) throws URISyntaxException {
        log.debug("REST request to update Producer : {}", producer);
        if (producer.getId() == null) {
            return createProducer(producer);
        }
        Producer result = producerRepository.save(producer);
        producerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("producer", producer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /producers : get all the producers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of producers in body
     */
    @RequestMapping(value = "/producers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Producer> getAllProducers() {
        log.debug("REST request to get all Producers");
        List<Producer> producers = producerRepository.findAll();
        return producers;
    }

    /**
     * GET  /producers/:id : get the "id" producer.
     *
     * @param id the id of the producer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the producer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/producers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Producer> getProducer(@PathVariable Long id) {
        log.debug("REST request to get Producer : {}", id);
        Producer producer = producerRepository.findOne(id);
        return Optional.ofNullable(producer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /producers/:id : delete the "id" producer.
     *
     * @param id the id of the producer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/producers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProducer(@PathVariable Long id) {
        log.debug("REST request to delete Producer : {}", id);
        producerRepository.delete(id);
        producerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("producer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/producers?query=:query : search for the producer corresponding
     * to the query.
     *
     * @param query the query of the producer search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/producers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Producer> searchProducers(@RequestParam String query) {
        log.debug("REST request to search Producers for query {}", query);
        return StreamSupport
            .stream(producerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
