package com.agencylot.repository;

import com.agencylot.domain.PersonalAutoVehicle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonalAutoVehicle entity.
 */
public interface PersonalAutoVehicleRepository extends JpaRepository<PersonalAutoVehicle,Long> {

}
