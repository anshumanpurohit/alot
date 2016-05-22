package com.agencylot.repository;

import com.agencylot.domain.ProductLineDef;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductLineDef entity.
 */
public interface ProductLineDefRepository extends JpaRepository<ProductLineDef,Long> {

}
