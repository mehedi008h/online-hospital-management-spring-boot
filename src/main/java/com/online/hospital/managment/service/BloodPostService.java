package com.online.hospital.managment.service;

import com.online.hospital.managment.model.BloodPost;
import com.online.hospital.managment.repository.BloodPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodPostService {

    @Autowired
    private BloodPostRepository bloodPostRepository;

    public Page<BloodPost> listAll(String keyword, Pageable pageable)
    {
        if(keyword != null)
        {
            return bloodPostRepository.findAll(keyword, pageable);
        }
        return bloodPostRepository.findAll(pageable);
    }



}
