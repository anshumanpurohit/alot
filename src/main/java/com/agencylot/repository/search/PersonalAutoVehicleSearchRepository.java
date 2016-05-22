package com.agencylot.repository.search;

import com.agencylot.domain.PersonalAutoVehicle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PersonalAutoVehicle entity.
 */
public interface PersonalAutoVehicleSearchRepository extends ElasticsearchRepository<PersonalAutoVehicle, Long> {
}
