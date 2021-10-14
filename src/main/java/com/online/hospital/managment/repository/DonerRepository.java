package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.BloodPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.online.hospital.managment.model.Doner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonerRepository extends JpaRepository<Doner, Long> {

    @Query("from Doner as c where c.bloodPost.id =:userId")
    public List<Doner> findDonerByBloodPost(@Param("userId") int userId);

    @Query("select u from Doner u where u.id = :id")
    public BloodPost getBloodPostByBloodPostId(@Param("id") Integer id);
    
}
