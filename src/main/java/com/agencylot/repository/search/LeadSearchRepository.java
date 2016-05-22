package com.agencylot.repository.search;

import com.agencylot.domain.Lead;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Lead entity.
 */
public interface LeadSearchRepository extends ElasticsearchRepository<Lead, Long> {
}
