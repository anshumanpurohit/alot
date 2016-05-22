package com.agencylot.repository.search;

import com.agencylot.domain.Activity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Activity entity.
 */
public interface ActivitySearchRepository extends ElasticsearchRepository<Activity, Long> {
}
