package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Loss;
import com.agencylot.repository.LossRepository;
import com.agencylot.repository.search.LossSearchRepository;
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
 * REST controller for managing Loss.
 */
@RestController
@RequestMapping("/api")
public class LossResource {

    private final Logger log = LoggerFactory.getLogger(LossResource.class);
        
    @Inject
    private LossRepository lossRepository;
    
    @Inject
    private LossSearchRepository lossSearchRepository;
    
    /**
     * POST  /losses : Create a new loss.
     *
     * @param loss the loss to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loss, or with status 400 (Bad Request) if the loss has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/losses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Loss> createLoss(@RequestBody Loss loss) throws URISyntaxException {
        log.debug("REST request to save Loss : {}", loss);
        if (loss.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("loss", "idexists", "A new loss cannot already have an ID")).body(null);
        }
        Loss result = lossRepository.save(loss);
        lossSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/losses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("loss", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /losses : Updates an existing loss.
     *
     * @param loss the loss to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loss,
     * or with status 400 (Bad Request) if the loss is not valid,
     * or with status 500 (Internal Server Error) if the loss couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/losses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Loss> updateLoss(@RequestBody Loss loss) throws URISyntaxException {
        log.debug("REST request to update Loss : {}", loss);
        if (loss.getId() == null) {
            return createLoss(loss);
        }
        Loss result = lossRepository.save(loss);
        lossSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("loss", loss.getId().toString()))
            .body(result);
    }

    /**
     * GET  /losses : get all the losses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of losses in body
     */
    @RequestMapping(value = "/losses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Loss> getAllLosses() {
        log.debug("REST request to get all Losses");
        List<Loss> losses = lossRepository.findAll();
        return losses;
    }

    /**
     * GET  /losses/:id : get the "id" loss.
     *
     * @param id the id of the loss to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loss, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/losses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Loss> getLoss(@PathVariable Long id) {
        log.debug("REST request to get Loss : {}", id);
        Loss loss = lossRepository.findOne(id);
        return Optional.ofNullable(loss)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /losses/:id : delete the "id" loss.
     *
     * @param id the id of the loss to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/losses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLoss(@PathVariable Long id) {
        log.debug("REST request to delete Loss : {}", id);
        lossRepository.delete(id);
        lossSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("loss", id.toString())).build();
    }

    /**
     * SEARCH  /_search/losses?query=:query : search for the loss corresponding
     * to the query.
     *
     * @param query the query of the loss search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/losses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Loss> searchLosses(@RequestParam String query) {
        log.debug("REST request to search Losses for query {}", query);
        return StreamSupport
            .stream(lossSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
