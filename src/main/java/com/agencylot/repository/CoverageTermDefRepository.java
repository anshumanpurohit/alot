package com.agencylot.repository;

import com.agencylot.domain.CoverageTermDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoverageTermDef entity.
 */
public interface CoverageTermDefRepository extends JpaRepository<CoverageTermDef,Long> {

}
