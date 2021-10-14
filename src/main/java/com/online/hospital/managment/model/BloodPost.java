package com.online.hospital.managment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.online.hospital.managment.model.comment.BlogComment;
import com.online.hospital.managment.model.comment.BloodPostComment;

import javax.persistence.*;
import javax.validation.constraints.Null;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "BLOOD")
public class BloodPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String phone;
    private String email;
    private String gender;
    private String zilla;
    private String thana;
    private String location;
    private String blood_group;
    @Column(length = 5000)
    private String description;
    private String time;
    private boolean status = false;

    @ManyToOne
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "bloodPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doner> doners = new ArrayList<>();
    
    @OneToMany(mappedBy = "bloodPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloodPostComment> blogComment = new ArrayList<>();
    
    public BloodPost() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getZilla() {
        return zilla;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
    
	public List<Doner> getDoners() {
		return doners;
	}

	public void setDoners(List<Doner> doners) {
		this.doners = doners;
	}
	

	public List<BloodPostComment> getBlogComment() {
		return blogComment;
	}

	public void setBlogComment(List<BloodPostComment> blogComment) {
		this.blogComment = blogComment;
	}

	@Override
    public boolean equals(Object obj) {
        return this.id == ((BloodPost)obj).getId();
    }

}
