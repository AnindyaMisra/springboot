package com.tcs.hack.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcs.hack.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Integer>{
	@Query("SELECT COUNT(b) FROM Booking b WHERE b.resource.resourceId=:resourceId AND bookingDate=:bookingDate AND bookingSlot=:bookingSlot")
	public int findAvailability(@Param("resourceId")int resourceId, @Param("bookingDate")java.sql.Date date, @Param("bookingSlot")String slot);
}
