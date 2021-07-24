package com.breakbooking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.breakbooking.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	//SELECT * FROM customers WHERE email=?
	Optional<Customer> findCustomerByEmail(String email);
	Optional<Customer> findAllById(Long id);

}
