package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.ProductLine;
import com.agencylot.repository.ProductLineRepository;
import com.agencylot.repository.search.ProductLineSearchRepository;
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
 * REST controller for managing ProductLine.
 */
@RestController
@RequestMapping("/api")
public class ProductLineResource {

    private final Logger log = LoggerFactory.getLogger(ProductLineResource.class);
        
    @Inject
    private ProductLineRepository productLineRepository;
    
    @Inject
    private ProductLineSearchRepository productLineSearchRepository;
    
    /**
     * POST  /product-lines : Create a new productLine.
     *
     * @param productLine the productLine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productLine, or with status 400 (Bad Request) if the productLine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-lines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLine> createProductLine(@RequestBody ProductLine productLine) throws URISyntaxException {
        log.debug("REST request to save ProductLine : {}", productLine);
        if (productLine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productLine", "idexists", "A new productLine cannot already have an ID")).body(null);
        }
        ProductLine result = productLineRepository.save(productLine);
        productLineSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/product-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productLine", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /product-lines : Updates an existing productLine.
     *
     * @param productLine the productLine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productLine,
     * or with status 400 (Bad Request) if the productLine is not valid,
     * or with status 500 (Internal Server Error) if the productLine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/product-lines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLine> updateProductLine(@RequestBody ProductLine productLine) throws URISyntaxException {
        log.debug("REST request to update ProductLine : {}", productLine);
        if (productLine.getId() == null) {
            return createProductLine(productLine);
        }
        ProductLine result = productLineRepository.save(productLine);
        productLineSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productLine", productLine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /product-lines : get all the productLines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productLines in body
     */
    @RequestMapping(value = "/product-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductLine> getAllProductLines() {
        log.debug("REST request to get all ProductLines");
        List<ProductLine> productLines = productLineRepository.findAll();
        return productLines;
    }

    /**
     * GET  /product-lines/:id : get the "id" productLine.
     *
     * @param id the id of the productLine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productLine, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/product-lines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductLine> getProductLine(@PathVariable Long id) {
        log.debug("REST request to get ProductLine : {}", id);
        ProductLine productLine = productLineRepository.findOne(id);
        return Optional.ofNullable(productLine)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /product-lines/:id : delete the "id" productLine.
     *
     * @param id the id of the productLine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/product-lines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductLine(@PathVariable Long id) {
        log.debug("REST request to delete ProductLine : {}", id);
        productLineRepository.delete(id);
        productLineSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productLine", id.toString())).build();
    }

    /**
     * SEARCH  /_search/product-lines?query=:query : search for the productLine corresponding
     * to the query.
     *
     * @param query the query of the productLine search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/product-lines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductLine> searchProductLines(@RequestParam String query) {
        log.debug("REST request to search ProductLines for query {}", query);
        return StreamSupport
            .stream(productLineSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
