package com.agencylot.repository.search;

import com.agencylot.domain.Producer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Producer entity.
 */
public interface ProducerSearchRepository extends ElasticsearchRepository<Producer, Long> {
}
