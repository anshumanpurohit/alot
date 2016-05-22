package com.agencylot.repository.search;

import com.agencylot.domain.Loss;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Loss entity.
 */
public interface LossSearchRepository extends ElasticsearchRepository<Loss, Long> {
}
