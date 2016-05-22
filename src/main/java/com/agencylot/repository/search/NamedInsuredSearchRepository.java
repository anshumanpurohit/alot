package com.agencylot.repository.search;

import com.agencylot.domain.NamedInsured;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NamedInsured entity.
 */
public interface NamedInsuredSearchRepository extends ElasticsearchRepository<NamedInsured, Long> {
}
