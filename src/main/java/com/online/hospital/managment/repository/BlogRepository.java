package com.online.hospital.managment.repository;

import com.online.hospital.managment.model.Blog;
import com.online.hospital.managment.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Integer> {
    @Query("select u from Blog u where u.id = :id")
    public Blog getBlogByBlogId(@Param("id") String id);

    //search
    @Query("SELECT b FROM Blog b WHERE b.title LIKE %?1%"
            +"OR b.type LIKE %?1%"
            +"OR b.symptoms LIKE %?1%")
    public Page<Blog> findAll(String keyword, Pageable pageable);
}
