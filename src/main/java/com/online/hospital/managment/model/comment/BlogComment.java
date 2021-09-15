package com.online.hospital.managment.model.comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.hospital.managment.model.Blog;
import com.online.hospital.managment.model.User;

@Entity
public class BlogComment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String text;
	
	@ManyToOne
	@JsonIgnore
	private Blog blog;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	public BlogComment() {
		
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

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
    public boolean equals(Object obj) {
        return this.id == ((BlogComment)obj).getId();
    }
	
	

}
