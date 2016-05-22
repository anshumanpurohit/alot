package com.agencylot.repository.search;

import com.agencylot.domain.Contact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Contact entity.
 */
public interface ContactSearchRepository extends ElasticsearchRepository<Contact, Long> {
}
