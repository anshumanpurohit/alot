package com.agencylot.repository.search;

import com.agencylot.domain.DiscountDef;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DiscountDef entity.
 */
public interface DiscountDefSearchRepository extends ElasticsearchRepository<DiscountDef, Long> {
}
