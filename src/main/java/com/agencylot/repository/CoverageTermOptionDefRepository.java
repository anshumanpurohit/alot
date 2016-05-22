package com.agencylot.repository;

import com.agencylot.domain.CoverageTermOptionDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoverageTermOptionDef entity.
 */
public interface CoverageTermOptionDefRepository extends JpaRepository<CoverageTermOptionDef,Long> {

}
