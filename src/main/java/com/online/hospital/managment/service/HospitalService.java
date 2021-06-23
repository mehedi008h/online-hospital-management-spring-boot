package com.online.hospital.managment.service;

import com.online.hospital.managment.model.Hospital;
import com.online.hospital.managment.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Page<Hospital> listAll(String keyword, Pageable pageable)
    {
        if(keyword != null)
        {
            return hospitalRepository.findAll(keyword, pageable);
        }
        return hospitalRepository.findAll(pageable);
    }
}
