package com.agencylot.repository;

import com.agencylot.domain.Coverage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Coverage entity.
 */
public interface CoverageRepository extends JpaRepository<Coverage,Long> {

}
