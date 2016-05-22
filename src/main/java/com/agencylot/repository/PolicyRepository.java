package com.agencylot.repository;

import com.agencylot.domain.Policy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Policy entity.
 */
public interface PolicyRepository extends JpaRepository<Policy,Long> {

}
