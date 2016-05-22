package com.agencylot.repository;

import com.agencylot.domain.PolicyDriver;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PolicyDriver entity.
 */
public interface PolicyDriverRepository extends JpaRepository<PolicyDriver,Long> {

}
