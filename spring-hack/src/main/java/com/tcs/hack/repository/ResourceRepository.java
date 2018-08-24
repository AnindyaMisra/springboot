package com.tcs.hack.repository;

import org.springframework.data.repository.CrudRepository;

import com.tcs.hack.model.Resource;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {

}
