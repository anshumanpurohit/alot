package com.agencylot.repository;

import com.agencylot.domain.Violation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Violation entity.
 */
public interface ViolationRepository extends JpaRepository<Violation,Long> {

}
