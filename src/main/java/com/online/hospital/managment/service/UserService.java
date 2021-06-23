package com.online.hospital.managment.service;

import com.online.hospital.managment.model.User;
import com.online.hospital.managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> listAll(String keyword, Pageable pageable)
    {
        if(keyword != null)
        {
            return userRepository.findAll(keyword,pageable);
        }
        return userRepository.findAll(pageable);
    }
}
