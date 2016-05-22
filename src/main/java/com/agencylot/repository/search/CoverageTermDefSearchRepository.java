package com.agencylot.repository.search;

import com.agencylot.domain.CoverageTermDef;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CoverageTermDef entity.
 */
public interface CoverageTermDefSearchRepository extends ElasticsearchRepository<CoverageTermDef, Long> {
}
