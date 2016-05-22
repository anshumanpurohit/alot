package com.agencylot.repository.search;

import com.agencylot.domain.AccordMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AccordMapping entity.
 */
public interface AccordMappingSearchRepository extends ElasticsearchRepository<AccordMapping, Long> {
}
