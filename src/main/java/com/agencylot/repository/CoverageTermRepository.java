package com.agencylot.repository;

import com.agencylot.domain.CoverageTerm;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoverageTerm entity.
 */
public interface CoverageTermRepository extends JpaRepository<CoverageTerm,Long> {

}
