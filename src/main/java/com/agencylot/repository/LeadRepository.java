package com.agencylot.repository;

import com.agencylot.domain.Lead;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lead entity.
 */
public interface LeadRepository extends JpaRepository<Lead,Long> {

}
