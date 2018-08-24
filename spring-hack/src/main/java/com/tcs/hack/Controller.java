package com.tcs.hack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.hack.dto.BookingDTO;
import com.tcs.hack.dto.ReservationsDTO;
import com.tcs.hack.model.Booking;
import com.tcs.hack.model.Resource;
import com.tcs.hack.repository.BookingRepository;
import com.tcs.hack.repository.ResourceRepository;

@RestController
@RequestMapping("/tcs/hack/v1")
public class Controller {
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private BookingRepository bookingRepository;

	@GetMapping("/resources")
	public @ResponseBody List<Resource> retrieveAllResources() {
		return (List<Resource>) resourceRepository.findAll();
	}

	@GetMapping("/resources/{id}")
	public @ResponseBody Resource retrieveResource(@PathVariable int id) {
		Resource resource = resourceRepository.findOne(id);
		return resource;
	}

	@PostMapping("/resources")
	public @ResponseBody ResponseEntity<Resource> createResource(@RequestBody Resource resource) {
		Resource savedResource = resourceRepository.save(resource);
		return new ResponseEntity<Resource>(savedResource, HttpStatus.CREATED);
	}

	@DeleteMapping("/resources")
	public void deleteResource(@RequestBody Resource resource) {
		resourceRepository.delete(resource.getResourceId());
	}

	@GetMapping("/reservations")
	public @ResponseBody List<ReservationsDTO> retrieveAllBookings() {
		List<Booking> bookings = (List<Booking>) bookingRepository.findAll();
		ReservationsDTO reservationsDTO = new ReservationsDTO();
		List<ReservationsDTO> list = new ArrayList<ReservationsDTO>();
		for (Booking booking : bookings) {
			reservationsDTO.setBookingDate(booking.getBookingDate().toString());
			reservationsDTO.setBookingSlot(booking.getBookingSlot());
			reservationsDTO.setResourceName(booking.getResource().getResourceName());
			list.add(reservationsDTO);
		}
		return list;
	}

	@PostMapping("/reservations")
	public @ResponseBody ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO)
			throws ParseException {
		java.sql.Date date = new java.sql.Date(
				new SimpleDateFormat("dd-MM-yyyy").parse(bookingDTO.getBookingDate()).getTime());
		int count = bookingRepository.findAvailability(bookingDTO.getResourceId(), date, bookingDTO.getBookingSlot());
		if (count == 0) {
			Resource resource = new Resource();
			resource.setResourceId(bookingDTO.getResourceId());
			Booking booking = new Booking();
			booking.setBookingDate(date);
			booking.setBookingSlot(bookingDTO.getBookingSlot());
			booking.setResource(resource);
			Booking savedBooking = bookingRepository.save(booking);
			return new ResponseEntity<Booking>(savedBooking, HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Resource not available", HttpStatus.OK);
		
	}

}
