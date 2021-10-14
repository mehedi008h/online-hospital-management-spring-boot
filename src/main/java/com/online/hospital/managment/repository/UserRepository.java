package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.BloodPost;
import com.online.hospital.managment.model.Hospital;
import com.online.hospital.managment.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("select u from User u where u.email = :email")
    public User getUserByUserName(@Param("email") String email);

    //search
    @Query("SELECT u FROM User u WHERE u.name LIKE %?1%"
            +"OR u.address LIKE %?1%"
            +"OR u.blood_group LIKE %?1%"
            +"OR u.email LIKE %?1%"
            +"OR u.phone LIKE %?1%")
    public Page<User> findAll(String keyword,Pageable pageable);

    @Query("select u from User u where u.id = :id")
    public User getUserByUserId(@Param("id") Integer id);
    
    @Query("SELECT u FROM User u WHERE u.address LIKE %?1%")
    public List<User> findByAddress(String address);
}
