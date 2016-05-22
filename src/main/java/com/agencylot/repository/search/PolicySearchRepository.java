package com.agencylot.repository.search;

import com.agencylot.domain.Policy;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Policy entity.
 */
public interface PolicySearchRepository extends ElasticsearchRepository<Policy, Long> {
}
