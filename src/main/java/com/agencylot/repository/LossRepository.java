package com.agencylot.repository;

import com.agencylot.domain.Loss;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Loss entity.
 */
public interface LossRepository extends JpaRepository<Loss,Long> {

}
