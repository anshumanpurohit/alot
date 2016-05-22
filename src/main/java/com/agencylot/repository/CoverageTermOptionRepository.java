package com.agencylot.repository;

import com.agencylot.domain.CoverageTermOption;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CoverageTermOption entity.
 */
public interface CoverageTermOptionRepository extends JpaRepository<CoverageTermOption,Long> {

}
