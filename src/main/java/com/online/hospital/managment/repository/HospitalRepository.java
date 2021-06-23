package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    //search
    @Query("SELECT h FROM Hospital h WHERE h.name LIKE %?1%"
            +"OR h.email LIKE %?1%"
            +"OR h.phone LIKE %?1%")
    public Page<Hospital> findAll(String keyword, Pageable pageable);
}
