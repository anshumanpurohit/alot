package com.agencylot.repository.search;

import com.agencylot.domain.CoverageDef;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CoverageDef entity.
 */
public interface CoverageDefSearchRepository extends ElasticsearchRepository<CoverageDef, Long> {
}
