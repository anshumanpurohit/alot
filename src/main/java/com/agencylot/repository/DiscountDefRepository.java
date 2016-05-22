package com.agencylot.repository;

import com.agencylot.domain.DiscountDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DiscountDef entity.
 */
public interface DiscountDefRepository extends JpaRepository<DiscountDef,Long> {

}
