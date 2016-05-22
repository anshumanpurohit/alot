package com.agencylot.repository.search;

import com.agencylot.domain.Violation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Violation entity.
 */
public interface ViolationSearchRepository extends ElasticsearchRepository<Violation, Long> {
}
