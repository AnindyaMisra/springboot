package com.tcs.casestudy1.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tcs.casestudy1.entity.Customer;
import com.tcs.casestudy1.exception.CustomerNotFoundException;
import com.tcs.casestudy1.repository.CustomerRepository;

@RestController
public class CustomerResource<StudentRepository> {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/customer")
	public List<Customer> retrieveAllStudents() throws CustomerNotFoundException {
		List<Customer> customers = customerRepository.findAll();
		if (customers.isEmpty()) {
			throw new CustomerNotFoundException();
		}
		return customers;
	}

	@GetMapping("/customer/{id}")
	public Customer retrieveStudent(@PathVariable long id) throws CustomerNotFoundException {
		Optional<Customer> customer = customerRepository.findById(id);
		if (!customer.isPresent()) {
			throw new CustomerNotFoundException();
		}
		return customer.get();
	}

	@PostMapping("/customer")
	public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
		Customer savedCustomer = customerRepository.save(customer);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedCustomer.getCustomerId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/customer/{id}")
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (!customerOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		customer.setCustomerId(id);
		customerRepository.save(customer);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/customer/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (!customerOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		customerRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/customer")
	public ResponseEntity<Object> deleteAllCustomers() {
		customerRepository.deleteAll();
		return ResponseEntity.ok().build();
	}

}