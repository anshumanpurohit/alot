package com.agencylot.repository;

import com.agencylot.domain.Carrier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Carrier entity.
 */
public interface CarrierRepository extends JpaRepository<Carrier,Long> {

}
