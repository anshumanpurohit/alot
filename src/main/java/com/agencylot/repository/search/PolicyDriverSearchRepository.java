package com.agencylot.repository.search;

import com.agencylot.domain.PolicyDriver;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PolicyDriver entity.
 */
public interface PolicyDriverSearchRepository extends ElasticsearchRepository<PolicyDriver, Long> {
}
