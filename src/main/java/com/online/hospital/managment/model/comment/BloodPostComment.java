package com.online.hospital.managment.model.comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.hospital.managment.model.BloodPost;
import com.online.hospital.managment.model.User;

@Entity
public class BloodPostComment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String text;
	
	@ManyToOne
	@JsonIgnore
	private BloodPost bloodPost;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public BloodPostComment() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public BloodPost getBloodPost() {
		return bloodPost;
	}

	public void setBloodPost(BloodPost bloodPost) {
		this.bloodPost = bloodPost;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
