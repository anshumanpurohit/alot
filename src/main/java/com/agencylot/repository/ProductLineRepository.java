package com.agencylot.repository;

import com.agencylot.domain.ProductLine;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductLine entity.
 */
public interface ProductLineRepository extends JpaRepository<ProductLine,Long> {

}
