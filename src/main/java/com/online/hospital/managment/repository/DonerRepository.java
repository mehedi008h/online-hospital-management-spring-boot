package com.online.hospital.managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online.hospital.managment.model.Doner;

public interface DonerRepository extends JpaRepository<Doner, Long> {

}
