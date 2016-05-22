package com.agencylot.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agencylot.domain.AddressBook;
import com.agencylot.repository.AddressBookRepository;
import com.agencylot.repository.search.AddressBookSearchRepository;
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
 * REST controller for managing AddressBook.
 */
@RestController
@RequestMapping("/api")
public class AddressBookResource {

    private final Logger log = LoggerFactory.getLogger(AddressBookResource.class);
        
    @Inject
    private AddressBookRepository addressBookRepository;
    
    @Inject
    private AddressBookSearchRepository addressBookSearchRepository;
    
    /**
     * POST  /address-books : Create a new addressBook.
     *
     * @param addressBook the addressBook to create
     * @return the ResponseEntity with status 201 (Created) and with body the new addressBook, or with status 400 (Bad Request) if the addressBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/address-books",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AddressBook> createAddressBook(@RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to save AddressBook : {}", addressBook);
        if (addressBook.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("addressBook", "idexists", "A new addressBook cannot already have an ID")).body(null);
        }
        AddressBook result = addressBookRepository.save(addressBook);
        addressBookSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/address-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("addressBook", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /address-books : Updates an existing addressBook.
     *
     * @param addressBook the addressBook to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated addressBook,
     * or with status 400 (Bad Request) if the addressBook is not valid,
     * or with status 500 (Internal Server Error) if the addressBook couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/address-books",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AddressBook> updateAddressBook(@RequestBody AddressBook addressBook) throws URISyntaxException {
        log.debug("REST request to update AddressBook : {}", addressBook);
        if (addressBook.getId() == null) {
            return createAddressBook(addressBook);
        }
        AddressBook result = addressBookRepository.save(addressBook);
        addressBookSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("addressBook", addressBook.getId().toString()))
            .body(result);
    }

    /**
     * GET  /address-books : get all the addressBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addressBooks in body
     */
    @RequestMapping(value = "/address-books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AddressBook> getAllAddressBooks() {
        log.debug("REST request to get all AddressBooks");
        List<AddressBook> addressBooks = addressBookRepository.findAll();
        return addressBooks;
    }

    /**
     * GET  /address-books/:id : get the "id" addressBook.
     *
     * @param id the id of the addressBook to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the addressBook, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/address-books/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AddressBook> getAddressBook(@PathVariable Long id) {
        log.debug("REST request to get AddressBook : {}", id);
        AddressBook addressBook = addressBookRepository.findOne(id);
        return Optional.ofNullable(addressBook)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /address-books/:id : delete the "id" addressBook.
     *
     * @param id the id of the addressBook to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/address-books/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAddressBook(@PathVariable Long id) {
        log.debug("REST request to delete AddressBook : {}", id);
        addressBookRepository.delete(id);
        addressBookSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("addressBook", id.toString())).build();
    }

    /**
     * SEARCH  /_search/address-books?query=:query : search for the addressBook corresponding
     * to the query.
     *
     * @param query the query of the addressBook search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/address-books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AddressBook> searchAddressBooks(@RequestParam String query) {
        log.debug("REST request to search AddressBooks for query {}", query);
        return StreamSupport
            .stream(addressBookSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
