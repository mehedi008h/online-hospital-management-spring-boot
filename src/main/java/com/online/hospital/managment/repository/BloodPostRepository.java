package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.BloodPost;
import com.online.hospital.managment.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodPostRepository extends JpaRepository<BloodPost, Integer> {

    @Query("from BloodPost as c where c.user.id =:userId")
    public List<BloodPost> findBloodPostByUser(@Param("userId") int userId);

    //search
    @Query("SELECT b FROM BloodPost b WHERE b.title LIKE %?1%"
            +"OR b.blood_group LIKE %?1%"
            +"OR b.location LIKE %?1%"
            +"OR b.email LIKE %?1%"
            +"OR b.phone LIKE %?1%")
    public Page<BloodPost> findAll(String keyword, Pageable pageable);
}
