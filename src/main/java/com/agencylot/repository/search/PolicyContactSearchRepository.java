package com.agencylot.repository.search;

import com.agencylot.domain.PolicyContact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PolicyContact entity.
 */
public interface PolicyContactSearchRepository extends ElasticsearchRepository<PolicyContact, Long> {
}
