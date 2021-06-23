package com.online.hospital.managment.service;

import com.online.hospital.managment.model.Ambulance;
import com.online.hospital.managment.repository.AmbulanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmbulanceService {

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    public Page<Ambulance> listAll(String keyword, Pageable pageable)
    {
        if(keyword != null)
        {
            return ambulanceRepository.findAll(keyword, pageable);
        }
        return ambulanceRepository.findAll(pageable);
    }
}
