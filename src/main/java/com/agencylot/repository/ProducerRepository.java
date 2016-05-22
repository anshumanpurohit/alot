package com.agencylot.repository;

import com.agencylot.domain.Producer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Producer entity.
 */
public interface ProducerRepository extends JpaRepository<Producer,Long> {

}
