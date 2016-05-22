package com.agencylot.repository.search;

import com.agencylot.domain.CoverageTerm;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CoverageTerm entity.
 */
public interface CoverageTermSearchRepository extends ElasticsearchRepository<CoverageTerm, Long> {
}
