package com.agencylot.repository.search;

import com.agencylot.domain.Coverage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Coverage entity.
 */
public interface CoverageSearchRepository extends ElasticsearchRepository<Coverage, Long> {
}
