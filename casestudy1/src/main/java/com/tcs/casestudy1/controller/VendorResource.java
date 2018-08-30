package com.tcs.casestudy1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.casestudy1.entity.Vendor;
import com.tcs.casestudy1.exception.VendorNotFoundException;
import com.tcs.casestudy1.repository.VendorRespository;

@RestController
public class VendorResource {

	@Autowired
	private VendorRespository vendorRepository;

	@GetMapping("/vendor")
	public List<Vendor> retrieveAllVendors() throws VendorNotFoundException {
		List<Vendor> vendors = vendorRepository.findAll();
		if (vendors.isEmpty()) {
			throw new VendorNotFoundException();
		}
		return vendors;
	}

	@GetMapping("/vendor/{id}")
	public Vendor retrieveCustomer(@PathVariable long id) throws VendorNotFoundException {
		Optional<Vendor> vendor = vendorRepository.findById(id);
		if (!vendor.isPresent()) {
			throw new VendorNotFoundException();
		}
		return vendor.get();
	}

	@PostMapping("/vendor")
	public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
		Vendor savedVendor = vendorRepository.save(vendor);
		return new ResponseEntity<Vendor>(savedVendor, HttpStatus.CREATED);
	}

	@PutMapping("/vendor/{id}")
	public ResponseEntity<Vendor> updateVendor(@RequestBody Vendor vendor, @PathVariable long id) {
		Optional<Vendor> vendorOptional = vendorRepository.findById(id);
		if (!vendorOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		vendor.setVendorId(id);
		vendorRepository.save(vendor);
		return new ResponseEntity<Vendor>(vendor, HttpStatus.OK);
	}

	@DeleteMapping("/vendor/{id}")
	public ResponseEntity<Vendor> deleteVendor(@PathVariable long id) {
		Optional<Vendor> vendorOptional = vendorRepository.findById(id);
		if (!vendorOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		vendorRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/vendor")
	public ResponseEntity<Object> deleteAllVendors() {
		vendorRepository.deleteAll();
		return ResponseEntity.ok().build();
	}

}