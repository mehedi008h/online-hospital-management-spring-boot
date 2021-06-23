package com.online.hospital.managment.service;

import com.online.hospital.managment.model.Blog;
import com.online.hospital.managment.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Page<Blog> listAll(String keyword, Pageable pageable)
    {
        if(keyword != null)
        {
            return blogRepository.findAll(keyword,pageable);
        }
        return blogRepository.findAll(pageable);
    }
}
