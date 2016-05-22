package com.agencylot.repository.search;

import com.agencylot.domain.AddressBook;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AddressBook entity.
 */
public interface AddressBookSearchRepository extends ElasticsearchRepository<AddressBook, Long> {
}
