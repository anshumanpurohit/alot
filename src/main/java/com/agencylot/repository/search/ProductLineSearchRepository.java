package com.agencylot.repository.search;

import com.agencylot.domain.ProductLine;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductLine entity.
 */
public interface ProductLineSearchRepository extends ElasticsearchRepository<ProductLine, Long> {
}
