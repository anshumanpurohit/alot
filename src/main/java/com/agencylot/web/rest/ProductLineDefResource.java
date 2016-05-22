package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.ProductLineDef;
import com.agencylot.repository.ProductLineDefRepository;
import com.agencylot.repository.search.ProductLineDefSearchRepository;
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
 * REST controller for managing ProductLineDef.
 */
@RestController
@RequestMapping("/api")
public class ProductLineDefResource {

    private final Logger log = LoggerFactory.getLogger(ProductLineDefResource.class);
        
    @Inject
    private ProductLineDefRepository productLineDefRepository;
    
    @Inject
    private ProductLineDefSearchRepository productLineDefSearchRepository;
    
    /**
     * POST  /product-line-defs : Create a new productLineDef.
     *
     * @param productLineDef the productLineDef to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productLineDef, or with status 400 (Bad Request) if the productLineDef has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-line-defs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLineDef> createProductLineDef(@RequestBody ProductLineDef productLineDef) throws URISyntaxException {
        log.debug("REST request to save ProductLineDef : {}", productLineDef);
        if (productLineDef.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productLineDef", "idexists", "A new productLineDef cannot already have an ID")).body(null);
        }
        ProductLineDef result = productLineDefRepository.save(productLineDef);
        productLineDefSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-line-defs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productLineDef", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-line-defs : Updates an existing productLineDef.
     *
     * @param productLineDef the productLineDef to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productLineDef,
     * or with status 400 (Bad Request) if the productLineDef is not valid,
     * or with status 500 (Internal Server Error) if the productLineDef couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-line-defs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLineDef> updateProductLineDef(@RequestBody ProductLineDef productLineDef) throws URISyntaxException {
        log.debug("REST request to update ProductLineDef : {}", productLineDef);
        if (productLineDef.getId() == null) {
            return createProductLineDef(productLineDef);
        }
        ProductLineDef result = productLineDefRepository.save(productLineDef);
        productLineDefSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productLineDef", productLineDef.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-line-defs : get all the productLineDefs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productLineDefs in body
     */
    @RequestMapping(value = "/product-line-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductLineDef> getAllProductLineDefs() {
        log.debug("REST request to get all ProductLineDefs");
        List<ProductLineDef> productLineDefs = productLineDefRepository.findAll();
        return productLineDefs;
    }

    /**
     * GET  /product-line-defs/:id : get the "id" productLineDef.
     *
     * @param id the id of the productLineDef to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productLineDef, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/product-line-defs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLineDef> getProductLineDef(@PathVariable Long id) {
        log.debug("REST request to get ProductLineDef : {}", id);
        ProductLineDef productLineDef = productLineDefRepository.findOne(id);
        return Optional.ofNullable(productLineDef)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /product-line-defs/:id : delete the "id" productLineDef.
     *
     * @param id the id of the productLineDef to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/product-line-defs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductLineDef(@PathVariable Long id) {
        log.debug("REST request to delete ProductLineDef : {}", id);
        productLineDefRepository.delete(id);
        productLineDefSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productLineDef", id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-line-defs?query=:query : search for the productLineDef corresponding
     * to the query.
     *
     * @param query the query of the productLineDef search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/product-line-defs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductLineDef> searchProductLineDefs(@RequestParam String query) {
        log.debug("REST request to search ProductLineDefs for query {}", query);
        return StreamSupport
            .stream(productLineDefSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
