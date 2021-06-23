package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.Ambulance;
import com.online.hospital.managment.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance, Integer> {

    //search
    @Query("SELECT a FROM Ambulance a WHERE a.name LIKE %?1%"
            +"OR a.zilla LIKE %?1%"
            +"OR a.phone LIKE %?1%"
            +"OR a.thana LIKE %?1%")
    public Page<Ambulance> findAll(String keyword, Pageable pageable);
}
