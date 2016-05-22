package com.agencylot.repository;

import com.agencylot.domain.PolicyContact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PolicyContact entity.
 */
public interface PolicyContactRepository extends JpaRepository<PolicyContact,Long> {

}
