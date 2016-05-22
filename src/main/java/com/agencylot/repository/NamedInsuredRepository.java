package com.agencylot.repository;

import com.agencylot.domain.NamedInsured;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NamedInsured entity.
 */
public interface NamedInsuredRepository extends JpaRepository<NamedInsured,Long> {

}
