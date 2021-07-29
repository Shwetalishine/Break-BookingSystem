package com.breakbooking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.breakbooking.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	/* CUSTOM METHODS */
	
	/* SEARCH CUSTOMER BY EMAIL ID*/

	
	@Query("SELECT s FROM Customer s WHERE s.email=?1")
	Optional<Customer> findCustomerByEmail(String email);
	
	/* CHECK IF EMAIL IS EXISTS IN THE DATABASE */
	

	@Query("SELECT CASE WHEN COUNT(s) >0 THEN 'true' ELSE 'false' END FROM Customer s WHERE s.email=?1")
	Boolean selectExistsEmail(String email);
	
	

}
