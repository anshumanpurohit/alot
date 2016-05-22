package com.agencylot.repository;

import com.agencylot.domain.AccordMapping;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccordMapping entity.
 */
public interface AccordMappingRepository extends JpaRepository<AccordMapping,Long> {

}
