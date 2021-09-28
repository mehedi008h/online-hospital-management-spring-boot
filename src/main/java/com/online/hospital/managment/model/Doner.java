package com.online.hospital.managment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Optional;

@Entity
public class Doner {
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long id;
	private String hospitalName;
	private String donateDate;
	private String lastBDDate;
	private String problem;
	private String location;
	private boolean apply = false;
	private boolean status = false;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	@ManyToOne
	@JsonIgnore
	private BloodPost bloodPost;

	public Doner() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getDonateDate() {
		return donateDate;
	}

	public void setDonateDate(String donateDate) {
		this.donateDate = donateDate;
	}

	public String getLastBDDate() {
		return lastBDDate;
	}

	public void setLastBDDate(String lastBDDate) {
		this.lastBDDate = lastBDDate;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isApply() {
		return apply;
	}

	public void setApply(boolean apply) {
		this.apply = apply;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BloodPost getBloodPost() {
		return bloodPost;
	}

	public void setBloodPost(BloodPost bloodPost) {
		this.bloodPost = bloodPost;
	}

	@Override
    public boolean equals(Object obj) {
        return this.id == ((Doner)obj).getId();
    }
	
}
