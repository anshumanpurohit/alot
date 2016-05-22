package com.agencylot.repository;

import com.agencylot.domain.AddressBook;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AddressBook entity.
 */
public interface AddressBookRepository extends JpaRepository<AddressBook,Long> {

}
