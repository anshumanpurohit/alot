package com.agencylot.repository.search;

import com.agencylot.domain.ActivityPattern;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ActivityPattern entity.
 */
public interface ActivityPatternSearchRepository extends ElasticsearchRepository<ActivityPattern, Long> {
}
