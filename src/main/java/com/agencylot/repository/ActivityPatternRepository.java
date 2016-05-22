package com.agencylot.repository;

import com.agencylot.domain.ActivityPattern;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActivityPattern entity.
 */
public interface ActivityPatternRepository extends JpaRepository<ActivityPattern,Long> {

}
