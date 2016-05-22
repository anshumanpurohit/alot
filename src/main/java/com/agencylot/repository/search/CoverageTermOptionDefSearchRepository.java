package com.agencylot.repository.search;

import com.agencylot.domain.CoverageTermOptionDef;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CoverageTermOptionDef entity.
 */
public interface CoverageTermOptionDefSearchRepository extends ElasticsearchRepository<CoverageTermOptionDef, Long> {
}
