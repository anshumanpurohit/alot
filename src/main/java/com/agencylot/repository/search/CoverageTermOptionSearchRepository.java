package com.agencylot.repository.search;

import com.agencylot.domain.CoverageTermOption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CoverageTermOption entity.
 */
public interface CoverageTermOptionSearchRepository extends ElasticsearchRepository<CoverageTermOption, Long> {
}
