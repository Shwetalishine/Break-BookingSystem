package com.breakbooking.controller;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.breakbooking.exception.DuplicateResourceException;
import com.breakbooking.exception.InvalidDateException;
import com.breakbooking.exception.ResourceNotFoundException;
import com.breakbooking.model.Customer;
import com.breakbooking.service.CustomerService;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController{
	
	@Autowired
	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	
	/* GET ALL CUSTOMERS */
	
	
	@GetMapping("/all")
	@ResponseBody
	public ResponseEntity<List<Customer>> getAllCustomers(){
		List<Customer> customers = customerService.findAllCustomers();
		return new ResponseEntity<>(customers,HttpStatus.OK);
	}
	
	
	/* FIND CUSTOMER BY ID */
	
	
	@GetMapping(path="/find/{id}")
	@ResponseBody
	public ResponseEntity<?> getCustomerById(@PathVariable ("id") Long id) throws Exception{
		try {
			Customer customer = customerService.findCustomerById(id);
			return new ResponseEntity<>(customer,HttpStatus.OK);
		}catch (ResourceNotFoundException e) {
			return new ResponseEntity<>("Customer by id "+ id +" was not found",HttpStatus.NOT_FOUND);
			
		}
	}
	
	
	/* CREATE CUSTOMER */
	
	
	@PostMapping(path="/add")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) throws Exception {
		
		try {
			Customer newCustomer = customerService.addCustomer(customer);
			return new ResponseEntity<>(newCustomer,HttpStatus.CREATED);
			
		}catch (EntityExistsException e) {
			return new ResponseEntity<>("There is an existing customer with id "+customer.getId()+" in the database ",HttpStatus.CONFLICT);
		}
		catch (DuplicateResourceException e) {
			return new ResponseEntity<>("Customer with email id "+customer.getEmail()+" already exists in the database",HttpStatus.CONFLICT);
				
		}catch (InvalidDateException e) {
			return new ResponseEntity<>("Please enter a valid date of birth",HttpStatus.BAD_REQUEST);
		}
	
	}
	
	
	/* UPDATE CUSTOMER */
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable ("id") Long id, @RequestBody Customer customer) throws Exception{
		try {
			Customer updateCustomer = customerService.updateCustomer(id, customer);
			return new ResponseEntity<>(updateCustomer, HttpStatus.OK);
			
		}catch (ResourceNotFoundException e) {
			return new ResponseEntity<>("Customer with id "+id+" does not exist",HttpStatus.NOT_FOUND);
				
		}
		catch (DuplicateResourceException e) {
			return new ResponseEntity<>("Customer with email id "+customer.getEmail()+" already exists in the database",HttpStatus.CONFLICT);
				
		}
		catch (InvalidDateException e) {
			return new ResponseEntity<>("Please enter a valid date of birth",HttpStatus.BAD_REQUEST);
		}
		
	}
	
		
	/* DELETE CUSTOMER */
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable ("id") Long id) throws ResourceNotFoundException{
		
		try {
			return new ResponseEntity<>(customerService.deleteCustomer(id),HttpStatus.OK);
		}catch (Exception e) {
		  return new ResponseEntity<>("Customer with id "+id+" does not exist",HttpStatus.NOT_FOUND);
	
		}
	
	}
}
