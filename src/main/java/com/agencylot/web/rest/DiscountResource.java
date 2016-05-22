package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.Discount;
import com.agencylot.repository.DiscountRepository;
import com.agencylot.repository.search.DiscountSearchRepository;
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
 * REST controller for managing Discount.
 */
@RestController
@RequestMapping("/api")
public class DiscountResource {

    private final Logger log = LoggerFactory.getLogger(DiscountResource.class);
        
    @Inject
    private DiscountRepository discountRepository;
    
    @Inject
    private DiscountSearchRepository discountSearchRepository;
    
    /**
     * POST  /discounts : Create a new discount.
     *
     * @param discount the discount to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discount, or with status 400 (Bad Request) if the discount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) throws URISyntaxException {
        log.debug("REST request to save Discount : {}", discount);
        if (discount.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discount", "idexists", "A new discount cannot already have an ID")).body(null);
        }
        Discount result = discountRepository.save(discount);
        discountSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discounts : Updates an existing discount.
     *
     * @param discount the discount to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discount,
     * or with status 400 (Bad Request) if the discount is not valid,
     * or with status 500 (Internal Server Error) if the discount couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> updateDiscount(@RequestBody Discount discount) throws URISyntaxException {
        log.debug("REST request to update Discount : {}", discount);
        if (discount.getId() == null) {
            return createDiscount(discount);
        }
        Discount result = discountRepository.save(discount);
        discountSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discount", discount.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discounts : get all the discounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of discounts in body
     */
    @RequestMapping(value = "/discounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Discount> getAllDiscounts() {
        log.debug("REST request to get all Discounts");
        List<Discount> discounts = discountRepository.findAll();
        return discounts;
    }

    /**
     * GET  /discounts/:id : get the "id" discount.
     *
     * @param id the id of the discount to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discount, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/discounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Discount> getDiscount(@PathVariable Long id) {
        log.debug("REST request to get Discount : {}", id);
        Discount discount = discountRepository.findOne(id);
        return Optional.ofNullable(discount)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discounts/:id : delete the "id" discount.
     *
     * @param id the id of the discount to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/discounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        log.debug("REST request to delete Discount : {}", id);
        discountRepository.delete(id);
        discountSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discount", id.toString())).build();
    }

    /**
     * SEARCH  /_search/discounts?query=:query : search for the discount corresponding
     * to the query.
     *
     * @param query the query of the discount search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/discounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Discount> searchDiscounts(@RequestParam String query) {
        log.debug("REST request to search Discounts for query {}", query);
        return StreamSupport
            .stream(discountSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
