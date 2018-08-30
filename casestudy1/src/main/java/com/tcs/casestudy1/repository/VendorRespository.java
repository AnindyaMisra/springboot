package com.tcs.casestudy1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.casestudy1.entity.Vendor;

@Repository
public interface VendorRespository extends JpaRepository<Vendor, Long> {

}