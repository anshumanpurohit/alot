package com.agencylot.repository.search;

import com.agencylot.domain.ProductLineDef;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductLineDef entity.
 */
public interface ProductLineDefSearchRepository extends ElasticsearchRepository<ProductLineDef, Long> {
}
