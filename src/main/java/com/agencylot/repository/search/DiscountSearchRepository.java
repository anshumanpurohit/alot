package com.agencylot.repository.search;

import com.agencylot.domain.Discount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Discount entity.
 */
public interface DiscountSearchRepository extends ElasticsearchRepository<Discount, Long> {
}
