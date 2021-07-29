package com.breakbooking.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.breakbooking.exception.DuplicateResourceException;
import com.breakbooking.exception.InvalidDateException;
import com.breakbooking.exception.ResourceNotFoundException;
import com.breakbooking.model.Customer;
import com.breakbooking.repo.CustomerRepo;


@Service
public class CustomerService {
	
	@Autowired
	private final CustomerRepo customerRepo ;
	
	@Autowired
	public CustomerService(CustomerRepo customerRepo) {
		this.customerRepo = customerRepo;
	}
	
	
	/* READ */
	
	
	public List<Customer> findAllCustomers(){
			return customerRepo.findAll();
	}
	
	
	/* READ BY ID*/
	
	
	public Customer findCustomerById(Long id) {
	
		return customerRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer by id "+ id +" was not found"));
	}
	
	
	/* CREATE */
	
	
	public Customer addCustomer(Customer customer) {
		
		if(customer.getId()!=null && customerRepo.existsById(customer.getId()))
			throw new EntityExistsException("There is an existing customer with id "+customer.getId()+" in the database ");
		
	/*	if(customerRepo.findCustomerByEmail(customer.getEmail()).isPresent()) {
			throw new DuplicateResourceException("Customer with email id "+customer.getEmail()+"already exists");
		}
	*/
		if(customerRepo.selectExistsEmail(customer.getEmail())) {
			throw new DuplicateResourceException("Customer with email id "+customer.getEmail()+"already exists");
		}
		
		
		if(customer.getDob().isAfter(LocalDate.now())) 
			throw new InvalidDateException("Please enter a valid date of birth");
		
		customer.setCustomerCode(UUID.randomUUID().toString());
		return customerRepo.save(customer);
	}
	
		
	/*UPDATE*/


	public Customer updateCustomer(Long id, Customer newCustomer) {	
		
		Customer oldCustomer = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
				"Customer with id "+id
				+ " does not exist"));
		
		String email=newCustomer.getEmail();
	
		if(email!=null && email.length()>0 && !Objects.equals(oldCustomer.getEmail(),email ))
		{
		/*	if(customerRepo.findCustomerByEmail(email).isPresent()) {
				throw new DuplicateResourceException("Customer with email id "+customer.getEmail()+"already exists");
			}
			
			*/
			
			if(customerRepo.selectExistsEmail(newCustomer.getEmail())) {
				throw new DuplicateResourceException("Customer with email id "+newCustomer.getEmail()+"already exists");
			}
		}
		
		if(newCustomer.getDob().isAfter(LocalDate.now())) 
			throw new InvalidDateException("Please enter a valid date of birth");
		
		
		newCustomer.setId(id);
		oldCustomer.setName(newCustomer.getName());
		oldCustomer.setEmail(newCustomer.getEmail());
		oldCustomer.setJobTitle(newCustomer.getJobTitle());
		oldCustomer.setPhone(newCustomer.getPhone());
		oldCustomer.setDob(newCustomer.getDob());
		oldCustomer.setDescription(newCustomer.getDescription());
		oldCustomer.setCustomerCode(newCustomer.getCustomerCode());
	
		customerRepo.save(newCustomer);
		return newCustomer;
	
	
	}	
	
	
	/* DELETE */
	
	public Map<String, Boolean> deleteCustomer(Long id) {
		
		if(!customerRepo.existsById(id)) {
			throw new ResourceNotFoundException("Customer with id "+id+" does not exist");
		}
	
		customerRepo.deleteById(id);
		Map<String, Boolean> response =new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
	
		return response;
		
	}
	
}
