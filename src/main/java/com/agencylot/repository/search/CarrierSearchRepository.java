package com.agencylot.repository.search;

import com.agencylot.domain.Carrier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Carrier entity.
 */
public interface CarrierSearchRepository extends ElasticsearchRepository<Carrier, Long> {
}
