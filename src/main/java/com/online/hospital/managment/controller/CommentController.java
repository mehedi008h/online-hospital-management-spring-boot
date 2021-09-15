package com.online.hospital.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.hospital.managment.repository.comment.BlogCommentRepository;

@Controller
@RequestMapping("/user")
public class CommentController {
	
	@Autowired
	private BlogCommentRepository blogCommentRepository;
	
	

}
