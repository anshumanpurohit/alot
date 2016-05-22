package com.agencylot.repository;

import com.agencylot.domain.CoverageDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoverageDef entity.
 */
public interface CoverageDefRepository extends JpaRepository<CoverageDef,Long> {

}
