package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.Blog;
import com.online.hospital.managment.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    //search
    @Query("SELECT h FROM Hospital h WHERE h.name LIKE %?1%"
            +"OR h.email LIKE %?1%"
            +"OR h.phone LIKE %?1%")
    public Page<Hospital> findAll(String keyword, Pageable pageable);
    
    @Query("SELECT h FROM Hospital h WHERE h.division LIKE %?1%"
            +"OR h.zilla LIKE %?1%"
            +"OR h.thana LIKE %?1%")
    public List<Hospital> findByArea(String keyword);
    
    @Query("select u from Hospital u where u.id = :id")
    public Hospital getHospitalbyId(@Param("id") Integer id);
}
